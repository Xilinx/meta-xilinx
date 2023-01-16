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
BOOTGEN_FLAGS ?= " -arch ${SOC_FAMILY} ${@bb.utils.contains('SOC_FAMILY','zynqmp','-w','-process_bitstream bin',d)}"

S ?= "${WORKDIR}"
FW_DIR ?= ""
DTSI_PATH ?= ""
DTBO_PATH ?= ""
DT_FILES_PATH = "${S}/${DTSI_PATH}"

python() {
    soc_family = d.getVar("SOC_FAMILY")
    if "git://" in d.getVar("SRC_URI") or "https://" in d.getVar("SRC_URI"):
        d.setVar("S",'${WORKDIR}/git/'+d.getVar("FW_DIR"))
    else:
        dtsi_found = False
        dtbo_found = False
        bit_found = False
        pdi_found = False

        # Required Inputs
        if '.dtsi' in d.getVar("SRC_URI"):
            dtsi_found = True
            d.setVar("DTSI_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.dtsi' in a][0].lstrip('file://')))

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

        # Optional input
        if 'accel.json' in d.getVar("SRC_URI"):
            d.setVar("JSON_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if 'accel.json' in a][0].lstrip('file://')))

        if '.xclbin' in d.getVar("SRC_URI"):
            d.setVar("XCL_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.xclbin' in a][0].lstrip('file://')))
}
python do_configure() {
    import glob, re, shutil
    soc_family = d.getVar("SOC_FAMILY")

    if bb.utils.contains('MACHINE_FEATURES', 'fpga-overlay', False, True, d):
        bb.warn("Using fpga-manager.bbclass requires fpga-overlay MACHINE_FEATURE to be enabled")

    # Renaming firmware-name using $PN as bitstream/PDI will be renamed using
    # $PN when generating the bin/pdi file.
    if '.dtsi' in d.getVar("SRC_URI"):
        orig_dtsi = glob.glob(d.getVar('S')+ (d.getVar('DTSI_PATH') or '') + '/*.dtsi')[0]
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

python devicetree_do_compile:append() {
    import glob, subprocess
    soc_family = d.getVar("SOC_FAMILY")

    # Convert .bit to bit.bin format only if dtsi is input.
    # In case of dtbo as input, bbclass doesn't know if firmware-name is .bit or
    # .bit.bin format and corresponding file name. Hence we are not doing
    # bit.bin conversion.
    if soc_family != 'versal' and glob.glob(d.getVar('S') + '/*.dtsi'):
        pn = d.getVar('PN')
        biffile = pn + '.bif'

        with open(biffile, 'w') as f:
            f.write('all:\n{\n\t' + glob.glob(d.getVar('S')+(d.getVar('BIT_PATH') or '') + '/*.bit')[0] + '\n}')

        bootgenargs = ["bootgen"] + (d.getVar("BOOTGEN_FLAGS") or "").split()
        bootgenargs += ["-image", biffile, "-o", pn + ".bit.bin"]
        subprocess.run(bootgenargs, check = True)

        if not os.path.isfile(pn + ".bit.bin"):
            bb.fatal("bootgen failed. Enable -log debug with bootgen and check logs")
}

do_install() {
    install -d ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/

    # In case of dtbo as input, dtbo will be copied from directly from ${S}
    # In case of dtsi as input, dtbo will be copied from directly from ${B}
    if [ -f ${S}/*.dtbo ]; then
        install -Dm 0644 ${S}/*.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
    elif [ -f ${B}/*.dtbo ]; then
        install -Dm 0644 ${B}/*.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.dtbo
    else
        bbfatal "A dtbo ending '.dtbo' expected but not found"
    fi

    if [ "${SOC_FAMILY}" == "versal" ]; then
        # In case of dtbo as input, pdi will be copied from directly from ${S}
        # without renaming the pdi name to ${PN}.pdi
        if [ -f ${S}/*.pdi ] && [ -f ${S}/*.dtbo ]; then
            install -Dm 0644 ${S}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ -f ${S}/*.pdi ] && [ -f ${B}/*.dtbo ]; then
            install -Dm 0644 ${S}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.pdi
        else
            bbfatal "A PDI file with '.pdi' expected but not found"
        fi
    else
        # In case of dtbo as input, .bit or .bit.in will be copied from directly
        # from ${S} without renaming the .bit name to ${PN}.bit.bin
        if [ -f ${S}/*.bit* ] && [ -f ${S}/*.dtbo ]; then
            install -Dm 0644 ${S}/*.bit* ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ -f ${B}/${PN}.bit.bin ] && [ -f ${B}/*.dtbo ]; then
            install -Dm 0644 ${B}/${PN}.bit.bin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.bit.bin
        else
            bbfatal "A bitstream file with '.bit' or '.bit.bin' expected but not found"
        fi
    fi

    if ls ${S}/${XCL_PATH}/*.xclbin >/dev/null 2>&1; then
        install -Dm 0644 ${S}/${XCL_PATH}/*.xclbin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.xclbin
    fi

    if [ -f ${WORKDIR}/${JSON_PATH}/accel.json ]; then
        install -Dm 0644 ${S}/${JSON_PATH}/accel.json ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/accel.json
    fi
}

do_deploy[noexec] = "1"

FILES:${PN} += "${nonarch_base_libdir}/firmware/xilinx/${PN}"
