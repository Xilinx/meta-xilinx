#
# Copyright (C) 2023-2024, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
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
BIT_PATH ?= ""
BIN_PATH ?= ""
PDI_PATH ?= ""
JSON_PATH ?= ""
XCl_PATH ?= ""
DT_FILES_PATH = "${S}/${DTSI_PATH}"
FIRMWARE_NAME_DT_FILE ?= ""
USER_DTS_FILE ?= ""

FIRMWARE_NAME_DT_FILE[doc] = "DT file which has firmware-name device-tree property"
USER_DTS_FILE[doc] = "Final DTSI or DTS file which is used for packaging final DT overlay"
DTSI_PATH[doc] = "Absolute '.dtsi' or ''.dts' file path as input to SRC_URI"
DTBO_PATH[doc] = "Absolute '.dtbo' file path as input to SRC_URI"
BIT_PATH[doc] = "Absolute '.bit' file path as input to SRC_URI"
BIN_PATH[doc] = "Absolute '.bin' file path as input to SRC_URI"
JSON_PATH[doc] = "Absolute '.json' file path as input to SRC_URI"
XCL_PATH[doc] = "Absolute '.xclbin' file path as input to SRC_URI"

python() {
    import re
    soc_family = d.getVar("SOC_FAMILY")
    if "git://" in d.getVar("SRC_URI") or "https://" in d.getVar("SRC_URI"):
        d.setVar("S",'${WORKDIR}/git/'+d.getVar("FW_DIR"))
    else:
        dtsi_found = False
        dtbo_found = False
        bit_found = False
        bin_found = False
        pdi_found = False

        # Required Inputs
        for s in d.getVar("SRC_URI").split():
            if s.endswith(('.dtsi', '.dts')):
                dtsi_found = True
                d.setVar("DTSI_PATH",os.path.dirname(s.lstrip('file://')))
            if s.endswith('.dtbo'):
                if dtbo_found:
                    bb.warn("More then one '.dtbo' file specified in SRC_URI.")
                dtbo_found = True
                d.setVar("DTBO_PATH",os.path.dirname(s.lstrip('file://')))
            if soc_family == "zynq" or soc_family == "zynqmp":
                if s.endswith('.bit'):
                    if bit_found:
                        bb.warn("More then one '.bit' file specified in SRC_URI.")
                    bit_found = True
                    d.setVar("BIT_PATH",os.path.dirname(s.lstrip('file://')))
                if s.endswith('.bin'):
                    if bin_found:
                        bb.warn("More then one '.bin' file specified in SRC_URI.")
                    bin_found = True
                    d.setVar("BIN_PATH",os.path.dirname(s.lstrip('file://')))
            else:
                if s.endswith('.pdi'):
                    if pdi_found:
                        bb.warn("More then one '.pdi' file specified in SRC_URI.")
                    pdi_found = True
                    d.setVar("PDI_PATH",os.path.dirname(s.lstrip('file://')))

            # Optional input
            if s.endswith('.json'):
                d.setVar("JSON_PATH",os.path.dirname(s.lstrip('file://')))

            if s.endswith('.xclbin'):
                d.setVar("XCL_PATH",os.path.dirname(s.lstrip('file://')))

        # Check for valid combination of input files in SRC_URI
        # Skip recipe if any of the below conditions are not satisfied.
        # 1. At least one bit or bin or pdi should exists.
        # 2. More than one dtbo.
        # 3. More than one bit or bin or pdi.
        # 4. More than one dts and zero dtsi.
        # 5. More than one dtsi and zero dts
        # 6. Both bit and bin exists.
        # 7. Both bit or bin and pdi exits.
        # 8. Both dts or dtsi and dtbo exists.
        if bit_found or bin_found or pdi_found:
            bb.debug(2, "dtsi or dtbo or bitstream or pdi found in SRC_URI")
            if bit_found and pdi_found :
                raise bb.parse.SkipRecipe("Both '.bit' and '.pdi' file found in SRC_URI, this is invalid use case.")

            if bin_found and pdi_found :
                raise bb.parse.SkipRecipe("Both '.bin' and '.pdi' file found in SRC_URI, this is invalid use case.")

            if bit_found and bin_found:
                raise bb.parse.SkipRecipe("Both '.bit' and '.bin' file found in SRC_URI, either .bit or .bin file is supported but not both.")

            if dtsi_found and dtbo_found:
                raise bb.parse.SkipRecipe("Both '.dts or dtsi' and '.dtbo' file found in SRC_URI, either .dts/dtsi or .dtbo file is supported but not both.")
        else:
            raise bb.parse.SkipRecipe("Need one '.bit' or '.bin' or '.pdi' file added to SRC_URI.")

        # Check for valid combination of dtsi and dts files in SRC_URI
        # Following file combinations are not supported use case.
        # 1. More than one '.dtsi' and zero '.dts' file.
        # 2. More than one '.dts' and zero or more than one '.dtsi'file .
        pattern_dts = re.compile(r'[.]+dts\b')
        pattern_dtsi = re.compile(r'[.]+dtsi\b')
        dts_count = len([*re.finditer(pattern_dts, d.getVar('SRC_URI'))])
        dtsi_count = len([*re.finditer(pattern_dtsi, d.getVar("SRC_URI"))])

        if dtsi_count > 1 and dts_count == 0:
            raise bb.parse.SkipRecipe("Recipe has more than one '.dtsi' and zero '.dts' found, this is an unsupported use case")
        elif dts_count > 1:
            raise bb.parse.SkipRecipe("Recipe has more than one '.dts' and zero or more than one '.dtsi' found, this is an unsupported use case")
}

# Function to search for dt firmware-name property in dts or dtsi file.
python find_firmware_file() {
    import glob
    pattern_fw = 'firmware-name'
    search_count = 0
    for dt_files in glob.iglob((d.getVar('S') + '/' + (d.getVar('DTSI_PATH')) + '/*.dts*'),recursive=True):
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
        bb.warn("Using dfx_user_dts.bbclass requires fpga-overlay MACHINE_FEATURE to be enabled")

    # Renaming firmware-name using $PN as bitstream/PDI will be renamed using
    # $PN when generating the bin/pdi file.
    if os.path.isfile(d.getVar('S') + '/' + (d.getVar('DTSI_PATH') or '') + '/' + d.getVar('FIRMWARE_NAME_DT_FILE')):
        orig_dtsi = glob.glob(d.getVar('S') + '/' + (d.getVar('DTSI_PATH') or '') + '/' + d.getVar('FIRMWARE_NAME_DT_FILE'))[0]
        new_dtsi = d.getVar('S') + '/' + (d.getVar('DTSI_PATH') or '') + '/pl.dtsi_firmwarename'
        with open(new_dtsi, 'w') as newdtsi:
            with open(orig_dtsi) as olddtsi:
                for line in olddtsi:
                    if soc_family == 'zynq' or soc_family == 'zynqmp':
                        newdtsi.write(re.sub('firmware-name.*\".*\"','firmware-name = \"'+d.getVar('PN')+'.bin\"',line))
                    else:
                        newdtsi.write(re.sub('firmware-name.*\".*\"','firmware-name = \"'+d.getVar('PN')+'.pdi\"',line))
        shutil.move(new_dtsi,orig_dtsi)
}

do_compile[prefuncs] += "find_firmware_file"

python devicetree_do_compile:append() {
    import glob, subprocess, shutil
    soc_family = d.getVar("SOC_FAMILY")

    dtbo_count = sum(1 for f in glob.iglob((d.getVar('S') + '/' + (d.getVar('DTSI_PATH') or '') + '/*.dtbo'),recursive=True) if os.path.isfile(f))
    bin_count = sum(1 for f in glob.iglob((d.getVar('S') + '/' + (d.getVar('BIN_PATH') or '') + '/*.bin'),recursive=True) if os.path.isfile(f))
    bit_count = sum(1 for f in glob.iglob((d.getVar('S') + '/' + (d.getVar('BIT_PATH') or '') + '/*.bit'),recursive=True) if os.path.isfile(f))
    # Skip devicetree do_compile task if input file is dtbo or bin in SRC_URI
    if not dtbo_count and not bin_count and bit_count:
        # Convert .bit to .bin format only if dtsi is input.
        # In case of dtbo as input, bbclass doesn't know if firmware-name is .bit
        # or .bin format and corresponding file name. Hence we are not doing .bin
        # conversion.
        if soc_family == 'zynq' or soc_family == 'zynqmp' and glob.glob(d.getVar('S') + '/' +(d.getVar('DTSI_PATH') or '') + '/' + d.getVar('FIRMWARE_NAME_DT_FILE')):
            pn = d.getVar('PN')
            biffile = pn + '.bif'
            with open(biffile, 'w') as f:
                f.write('all:\n{\n\t' + glob.glob(d.getVar('S') + '/' + (d.getVar('BIT_PATH') or '') + '/*.bit')[0] + '\n}')

            bootgenargs = ["bootgen"] + (d.getVar("BOOTGEN_FLAGS") or "").split()
            bootgenargs += ["-image", biffile, "-o", pn + ".bin"]
            subprocess.run(bootgenargs, check = True)

            # In Zynq7k using both "-process_bitstream bin" and "-o" in bootgen flag,
            # to convert bit file to bin format, "-o" option will not be effective
            # and generated output file name is ${S}+${BIT_PATH}/<bit_file_name>.bin
            # file, Hence we need to rename this file from <bit_file_name>.bin to
            # ${PN}.bin which matches the firmware name in dtbo and move
            # ${PN}.bin to ${B} directory.
            if soc_family == 'zynq':
                src_bitbin_file = glob.glob(d.getVar('S') + '/' + (d.getVar('BIT_PATH') or '') + '/*.bin')[0]
                dst_bitbin_file = d.getVar('B') + '/' + pn + '.bin'
                shutil.move(src_bitbin_file, dst_bitbin_file)

            if not os.path.isfile(pn + ".bin"):
                bb.fatal("Couldn't find %s file, Enable '-log trace' in BOOTGEN_FLAGS" \
                    "and check bootgen_log.txt" % (d.getVar('B') + '/' + pn + '.bin'))
}

# If user inputs both dtsi and dts files then device-tree will generate dtbo
# files for each dt file, Hence to package the firmware pick the right user dt
# overlay file.
python find_user_dts_overlay_file() {
    import glob
    dtbo_count = sum(1 for f in glob.iglob((d.getVar('S') + '/' + d.getVar('DTBO_PATH') + '/*.dtbo'),recursive=True) if os.path.isfile(f))
    dts_count = sum(1 for f in glob.iglob((d.getVar('S') + '/' + d.getVar('DTSI_PATH') + '/*.dts'),recursive=True) if os.path.isfile(f))
    dtsi_count = sum(1 for f in glob.iglob((d.getVar('S') + '/' + d.getVar('DTSI_PATH') + '/*.dtsi'),recursive=True) if os.path.isfile(f))
    # Set USER_DTS_FILE if input file is dts/dtsi in SRC_URI else skip operation.
    if not dtbo_count and dts_count or dtsi_count:
        if dtsi_count == 1 and dts_count == 0:
            dts_file = glob.glob(d.getVar('S') + '/' + (d.getVar('DTSI_PATH') or '') + '/*.dtsi')[0]
        elif dtsi_count >=0 and dts_count == 1:
            dts_file = glob.glob(d.getVar('S') + '/' + (d.getVar('DTSI_PATH') or '') + '/*.dts')[0]
        else:
            dts_file = ''

        d.setVar('USER_DTS_FILE', os.path.splitext(os.path.basename(dts_file))[0])
    elif dtbo_count:
        bb.debug(2, "Firmware recipe input file is dtbo in SRC_URI")
    else:
        bb.debug(2, "Firmware recipe input file is bit/bin/pdi in SRC_URI")
}

do_install[prefuncs] += "find_user_dts_overlay_file"

do_install() {
    install -d ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/

    # Install dtbo
    # In case of dtbo as input, dtbo will be copied from directly from ${S}
    # In case of dtsi as input, dtbo will be copied from directly from ${B}
    # If more than one dtbo file is found then fatal the task.
    # If no dtbo file is found add warning message as in some use case if IP
    # doesn't have any driver then user can load pdi/bit/bin file.
    if [ `ls ${S}/*.dtbo | wc -l` -eq 1 ]; then
        install -Dm 0644 ${S}/*.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
    elif [ `ls ${S}/${DTBO_PATH}/*.dtbo | wc -l` -eq 1 ]; then
        install -Dm 0644 ${S}/${DTBO_PATH}/*.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
    elif [ `ls ${S}/*.dtbo | wc -l` -gt 1 ]; then
        bbfatal "Multiple DTBO found, use the right DTBO in SRC_URI from the following:\n$(basename -a ${S}/*.dtbo)"
    elif [ `ls ${S}/${DTBO_PATH}/*.dtbo | wc -l` -gt 1 ]; then
        bbfatal "Multiple DTBO found, use the right DTBO in SRC_URI from the following:\n$(basename -a ${S}/${DTBO_PATH}/*.dtbo)"
    elif [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
        install -Dm 0644 ${B}/${USER_DTS_FILE}.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.dtbo
    else
        bbnote "A dtbo ending '.dtbo' expected but not found in ${S} or ${B}, This means firmware can be loaded without dtbo dependency."
    fi

    # Install bit or bin if soc family is zynq-7000 or zynqmp.
    # In case of dtbo as input or no dtbo exists in ${B}, then .bit or .bin will
    # be copied from directly from ${S} without renaming the .bit/.bin name to
    # ${PN}.bit/${PN}.bin.
    # if more than one .bit/.bin file is found then fatal the task.
    # if no .bit/.bin file is found then fatal the task.
    if [ "${SOC_FAMILY}" = "zynq" ] || [ "${SOC_FAMILY}" = "zynqmp" ]; then
        if [ `ls ${S}/*.bit | wc -l` -gt 1 ]; then
            bbfatal "Multiple .bit found, use the right .bit in SRC_URI from the following:\n$(basename -a ${S}/*.bit)"
        elif [ `ls ${S}/${BIT_PATH}/*.bit | wc -l` -gt 1 ]; then
            bbfatal "Multiple .bit found, use the right .bit in SRC_URI from the following:\n$(basename -a ${S}/${BIT_PATH}/*.bit)"
        elif [ `ls ${S}/*.bin | wc -l` -gt 1 ]; then
            bbfatal "Multiple .bin found, use the right .bin in SRC_URI from the following:\n$(basename -a ${S}/*.bin)"
        elif [ `ls ${S}/${BIN_PATH}/*.bin | wc -l` -gt 1 ]; then
            bbfatal "Multiple .bin found, use the right .bin in SRC_URI from the following:\n$(basename -a ${S}/${BIN_PATH}/*.bin)"
        elif [ `ls ${S}/*.bit | wc -l` -eq 1 ] && [ ! -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/*.bit ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/${BIT_PATH}/*.bit | wc -l` -eq 1 ] && [ ! -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/${BIT_PATH}/*.bit ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/*.bin | wc -l` -eq 1 ]; then
            install -Dm 0644 ${S}/*.bin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/${BIN_PATH}/*.bin | wc -l` -eq 1 ]; then
            install -Dm 0644 ${S}/${BIN_PATH}/*.bin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ -f ${B}/${PN}.bin ] && [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${B}/${PN}.bin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.bin
        else
            bbfatal "A bitstream file with '.bit' or '.bin' expected but not found"
        fi
    fi

    # Install pdi if soc family is versal or new silicon.
    # In case of dtbo as input or no dtbo exists in ${B}, then .pdi will be copied
    # from directly from ${S} without renaming the pdi name to ${PN}.pdi
    # if more than one .pdi file is found then fail the task.
    # In case of Versal DFx Static, only static dtbo can be loaded as BOOT.bin
    # already contains static pdi. bbclass is not smart enough to determine
    # whether it is static pdi or not, hence change fatal to warn if no PDI is found.
    if [ "${SOC_FAMILY}" != "zynq" ] && [ "${SOC_FAMILY}" != "zynqmp" ]; then
        if [ `ls ${S}/*.pdi | wc -l` -eq 1 ] && [ ! -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/${PDI_PATH}/*.pdi | wc -l` -eq 1 ] && [ ! -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/${PDI_PATH}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
        elif [ `ls ${S}/*.pdi | wc -l` -gt 1 ]; then
            bbfatal "Multiple PDI found, use the right PDI in SRC_URI from the following:\n$(basename -a ${S}/*.pdi)"
        elif [ `ls ${S}/${PDI_PATH}/*.pdi | wc -l` -gt 1 ]; then
            bbfatal "Multiple PDI found, use the right PDI in SRC_URI from the following:\n$(basename -a ${S}/${PDI_PATH}/*.pdi)"
        elif [ `ls ${S}/*.pdi | wc -l` -eq 1 ] && [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.pdi
        elif [ `ls ${S}/${PDI_PATH}/*.pdi | wc -l` -eq 1 ] && [ -f ${B}/${USER_DTS_FILE}.dtbo ]; then
            install -Dm 0644 ${S}/${PDI_PATH}/*.pdi ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.pdi
        else
            bbwarn "A PDI file with '.pdi' expected but not found"
        fi
    fi

    # Install xclbin
    if ls ${S}/${XCL_PATH}/*.xclbin >/dev/null 2>&1; then
        install -Dm 0644 ${S}/${XCL_PATH}/*.xclbin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.xclbin
    fi

    # Install shell.json or accel.json
    if [ -f ${S}/${JSON_PATH}/shell.json ] || [ -f ${S}/${JSON_PATH}/accel.json ]; then
        install -Dm 0644 ${S}/${JSON_PATH}/*.json ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/
    fi
}

do_deploy[noexec] = "1"

FILES:${PN} += "${nonarch_base_libdir}/firmware/xilinx/${PN}"
