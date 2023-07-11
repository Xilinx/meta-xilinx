# This bbclass is inherited by flat, DFx Static and DFx RP firmware recipes.
# dfx_user_dts.bbclass expects user to generate pl dtsi for flat, DFx Static
# and DFx RP xsa outside of yocto.

inherit devicetree

DEPENDS = "dtc-native bootgen-native"

# recipes that inherit from this class need to use an appropriate machine
# override for COMPATIBLE_MACHINE to build successfully; don't allow building
# for microblaze MACHINE
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:microblaze = "^$"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PROVIDES = ""

do_fetch[cleandirs] = "${B}"

DT_PADDING_SIZE = "0x1000"
BOOTGEN_FLAGS ?= " -arch ${SOC_FAMILY} -w ${@bb.utils.contains('SOC_FAMILY','zynqmp','','-process_bitstream bin',d)}"

S ?= "${WORKDIR}"
FW_DIR ?= ""
DTSI_PATH ?= ""
DTBO_PATH ?= ""
DT_FILES_PATH = "${S}/${DTSI_PATH}"
FIRMWARE_NAME_DT_FILE ?= ""
USER_DTS_FILE ?= ""

FIRMWARE_NAME_DT_FILE[doc] = "DT file which has firmware-name device-tree property"
USER_DTS_FILE[doc] = "Final DTSI or DTS file which is used for packaging final DT overlay"

python() {
    import re
    soc_family = d.getVar("SOC_FAMILY")
    if "git://" in d.getVar("SRC_URI") or "https://" in d.getVar("SRC_URI"):
        d.setVar("S",'${WORKDIR}/git/'+d.getVar("FW_DIR"))
    else:
        dtsi_found = False
        dtbo_found = False
        bit_found = False
        pdi_found = False

        # Required Inputs
        if '.dtsi' in d.getVar("SRC_URI") or '.dts' in d.getVar("SRC_URI"):
            dtsi_found = True
            d.setVar("DTSI_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.dtsi' in a or '.dts' in a][0].lstrip('file://')))

        if '.dtbo' in d.getVar("SRC_URI"):
            dtbo_found = True
            d.setVar("DTBO_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.dtbo' in a][0].lstrip('file://')))

        if '.bit' in d.getVar("SRC_URI") and soc_family != "versal":
            bit_found = True
            d.setVar("BIT_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.bit' in a][0].lstrip('file://')))

        if '.pdi' in d.getVar("SRC_URI") and soc_family == "versal":
            pdi_found = True
            d.setVar("PDI_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.pdi' in a][0].lstrip('file://')))

        # Check for valid combination of input files in SRC_URI
        if dtsi_found or dtbo_found:
            bb.debug(2, "dtsi or dtbo found in SRC_URI")
            if bit_found or pdi_found:
                bb.debug(2, "bitstream or pdi found in SRC_URI")
            else:
                raise bb.parse.SkipRecipe("Need one '.bit' or one '.pdi' file added to SRC_URI ")
        else:
            raise bb.parse.SkipRecipe("Need one '.dtsi' or one '.dtbo' file added to SRC_URI ")

        # Check for valid combination of dtsi and dts files in SRC_URI
        # Following file combinations are not supported use case.
        # 1. More than one '.dtsi' and zero '.dts' file.
        # 2. More than one '.dts' and zero or more than one '.dtsi'file .
        pattern_dts = re.compile(r'.dts\b')
        pattern_dtsi = re.compile(r'.dtsi\b')
        dts_count = len([*re.finditer(pattern_dts, d.getVar('SRC_URI'))])
        dtsi_count = len([*re.finditer(pattern_dtsi, d.getVar("SRC_URI"))])

        if dtsi_count > 1 and dts_count == 0:
            raise bb.parse.SkipRecipe("Recipe has more than one '.dtsi' and zero '.dts' found, this is an unsupported use case")
        elif dts_count > 1:
            raise bb.parse.SkipRecipe("Recipe has more than one '.dts' and zero or more than one '.dtsi' found, this is an unsupported use case")

        # Optional input
        if '.json' in d.getVar("SRC_URI"):
            d.setVar("JSON_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.json' in a][0].lstrip('file://')))

        if '.xclbin' in d.getVar("SRC_URI"):
            d.setVar("XCL_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.xclbin' in a][0].lstrip('file://')))
}

# Function to get dts or dtsi file count.
def get_dt_count(d, dt_ext):
    import glob
    dt_count = sum(1 for f in glob.iglob((d.getVar('S') + (d.getVar('DTSI_PATH')) + '/*.' + dt_ext),recursive=True) if os.path.isfile(f))
    return dt_count

# Function to search for dt firmware-name property in dts or dtsi file.
python find_firmware_file() {
    import glob
    pattern_fw = 'firmware-name'
    search_count = 0
    for dt_files in glob.iglob((d.getVar('S') + (d.getVar('DTSI_PATH')) + '/*.dts*'),recursive=True):
        with open(dt_files, "r") as f:
            current_fd = f.read()
            if pattern_fw in current_fd:
                search_count += 1
                if search_count > 1:
                    bb.error("firmware-name dt property found in more than one dt files! Please fix the dts or dtsi file.")
                    break
                else:
                    d.setVar('FIRMWARE_NAME_DT_FILE', os.path.basename(dt_files))
}

do_configure[prefuncs] += "find_firmware_file"

python do_configure() {
    import glob, re, shutil
    soc_family = d.getVar("SOC_FAMILY")

    if bb.utils.contains('MACHINE_FEATURES', 'fpga-overlay', False, True, d):
        bb.warn("Using fpga-manager.bbclass requires fpga-overlay MACHINE_FEATURE to be enabled")

    # Renaming firmware-name using $PN as bitstream/PDI will be renamed using
    # $PN when generating the bin/pdi file.
    if os.path.isfile(d.getVar('S') + (d.getVar('DTSI_PATH') or '') + '/' + d.getVar('FIRMWARE_NAME_DT_FILE')):
        orig_dtsi = glob.glob(d.getVar('S')+ (d.getVar('DTSI_PATH') or '') + '/' + d.getVar('FIRMWARE_NAME_DT_FILE'))[0]
        new_dtsi = d.getVar('S') + '/pl.dtsi_firmwarename'
        with open(new_dtsi, 'w') as newdtsi:
            with open(orig_dtsi) as olddtsi:
                for line in olddtsi:
                    if soc_family == 'versal':
                        newdtsi.write(re.sub('firmware-name.*\".*\"','firmware-name = \"'+d.getVar('PN')+'.pdi\"',line))
                    else:
                        newdtsi.write(re.sub('firmware-name.*\".*\"','firmware-name = \"'+d.getVar('PN')+'.bit.bin\"',line))
        shutil.move(new_dtsi,orig_dtsi)
}

do_compile[prefuncs] += "find_firmware_file"

python devicetree_do_compile:append() {
    import glob, subprocess, shutil
    soc_family = d.getVar("SOC_FAMILY")

    # Convert .bit to bit.bin format only if dtsi is input.
    # In case of dtbo as input, bbclass doesn't know if firmware-name is .bit or
    # .bit.bin format and corresponding file name. Hence we are not doing
    # bit.bin conversion.
    if soc_family != 'versal' and glob.glob(d.getVar('S') + '/' + d.getVar('FIRMWARE_NAME_DT_FILE')):
        pn = d.getVar('PN')
        biffile = pn + '.bif'

        with open(biffile, 'w') as f:
            f.write('all:\n{\n\t' + glob.glob(d.getVar('S')+(d.getVar('BIT_PATH') or '') + '/*.bit')[0] + '\n}')

        bootgenargs = ["bootgen"] + (d.getVar("BOOTGEN_FLAGS") or "").split()
        bootgenargs += ["-image", biffile, "-o", pn + ".bit.bin"]
        subprocess.run(bootgenargs, check = True)

        # In Zynq7k using both "-process_bitstream bin" and "-o" in bootgen flag,
        # to convert bit file to bin format, "-o" option will not be effective
        # and generated output file name is ${S}+${BIT_PATH}/<bit_file_name>.bit.bin
        # file, Hence we need to rename this file from <bit_file_name>.bit.bin to
        # ${PN}.bit.bin which matches the firmware name in dtbo and move
        # ${PN}.bit.bin to ${B} directory.
        if soc_family == 'zynq':
            src_bitbin_file = glob.glob(d.getVar('S') + (d.getVar('BIT_PATH') or '') + '/*.bit.bin')[0]
            dst_bitbin_file = d.getVar('B') + '/' + pn + '.bit.bin'
            shutil.move(src_bitbin_file, dst_bitbin_file)

        if not os.path.isfile(pn + ".bit.bin"):
            bb.fatal("Couldn't find %s file, Enable '-log trace' in BOOTGEN_FLAGS" \
                "and check bootgen_log.txt" % (d.getVar('B') + '/' + pn + '.bit.bin'))
}

# If user inputs both dtsi and dts files then device-tree will generate dtbo
# files for each dt file, Hence to package the firmware pick the right user dt
# overlay file.
python find_user_dts_overlay_file() {
    import glob
    dtbo_count = sum(1 for f in glob.iglob((d.getVar('S') + '/*.dtbo'),recursive=True) if os.path.isfile(f))
    # Skip if input file is dtbo in SRC_URI
    if not dtbo_count:
        dts_count = get_dt_count(d, 'dts')
        dtsi_count = get_dt_count(d, 'dtsi')
        if dtsi_count == 1 and dts_count == 0:
            dts_file =glob.glob(d.getVar('S')+ (d.getVar('DTSI_PATH') or '') + '/*.dtsi')[0]
        elif dtsi_count > 1 and dts_count == 1:
            dts_file = glob.glob(d.getVar('S')+ (d.getVar('DTSI_PATH') or '') + '/*.dts')[0]

        d.setVar('USER_DTS_FILE', os.path.splitext(os.path.basename(dts_file))[0])
}

do_install[prefuncs] += "find_user_dts_overlay_file"

do_install() {
    install -d ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/

    # In case of dtbo as input, dtbo will be copied from directly from ${S}
    # In case of dtsi as input, dtbo will be copied from directly from ${B}
    if [ `ls ${S}/*.dtbo | wc -l` -eq 1 ]; then
        install -Dm 0644 ${S}/*.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
    elif [ `ls ${S}/*.dtbo | wc -l` -gt 1 ]; then
        bbfatal "Multiple DTBO found, use the right DTBO in SRC_URI from the following:\n$(basename -a ${S}/*.dtbo)"
    elif [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
        install -Dm 0644 ${B}/${USER_DTS_FILE}.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.dtbo
    else
        bbfatal "A dtbo ending '.dtbo' expected but not found"
    fi

    if [ "${SOC_FAMILY}" == "versal" ]; then
        # In case of dtbo as input, pdi will be copied from directly from ${S}
        # without renaming the pdi name to ${PN}.pdi
        if [ `ls ${S}/*.pdi | wc -l` -eq 1 ] && [ `ls ${S}/*.dtbo | wc -l` -eq 1 ]; then
            install -Dm 0644 ${S}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/*.pdi | wc -l` -gt 1 ]; then
            bbfatal "Multiple PDI found, use the right PDI in SRC_URI from the following:\n$(basename -a ${S}/*.pdi)"
        elif [ `ls ${S}/*.pdi | wc -l` -eq 1 ] && [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.pdi
        else
            bbfatal "A PDI file with '.pdi' expected but not found"
        fi
    else
        # In case of dtbo as input, .bit or .bit.bin will be copied from directly
        # from ${S} without renaming the .bit name to ${PN}.bit.bin
        if [ `ls ${S}/*.bit* | wc -l` -eq 1 ] && [ `ls ${S}/*.dtbo | wc -l` -eq 1 ]; then
            install -Dm 0644 ${S}/*.bit* ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/*.bit* | wc -l` -gt 1 ]; then
            bbfatal "Multiple bit/bit.bin found, use the right bit/bit.bin in SRC_URI from the following:\n$(basename -a ${S}/*.bit*)"
        elif [ -f ${B}/${PN}.bit.bin ] && [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${B}/${PN}.bit.bin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.bit.bin
        else
            bbfatal "A bitstream file with '.bit' or '.bit.bin' expected but not found"
        fi
    fi

    if ls ${S}/${XCL_PATH}/*.xclbin >/dev/null 2>&1; then
        install -Dm 0644 ${S}/${XCL_PATH}/*.xclbin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.xclbin
    fi

    if [ -f ${S}/${JSON_PATH}/shell.json ] || [ -f ${S}/${JSON_PATH}/accel.json ]; then
        install -Dm 0644 ${S}/${JSON_PATH}/*.json ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
    fi
}

do_deploy[noexec] = "1"

FILES:${PN} += "${nonarch_base_libdir}/firmware/xilinx/${PN}"
