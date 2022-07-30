inherit devicetree

DEPENDS = "dtc-native bootgen-native"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:zynq = ".*"

PROVIDES = ""

do_fetch[cleandirs] = "${B}"

DT_PADDING_SIZE = "0x1000"
BOOTGEN_FLAGS ?= " -arch ${SOC_FAMILY} ${@bb.utils.contains('SOC_FAMILY','zynqmp','-w','-process_bitstream bin',d)}"

S ?= "${WORKDIR}"
FW_DIR ?= ""
DTSI_PATH ?= ""
DT_FILES_PATH = "${S}/${DTSI_PATH}"

python (){

    if "git://" in d.getVar("SRC_URI") or "https://" in d.getVar("SRC_URI"):
        d.setVar("S",'${WORKDIR}/git/'+d.getVar("FW_DIR"))
    else:
        if d.getVar("SRC_URI").count(".dtsi") != 1 or d.getVar("SRC_URI").count(".bit") != 1 \
            or d.getVar("SRC_URI").count("shell.json") != 1:
            raise bb.parse.SkipRecipe("Need one '.dtsi', one '.bit' and one 'shell.json' file added to SRC_URI")

        d.setVar("DTSI_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.dtsi' in a][0].lstrip('file://')))
        d.setVar("BIT_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.bit' in a][0].lstrip('file://')))
        d.setVar("JSON_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if 'shell.json' in a][0].lstrip('file://')))

        #optional input
        if '.xclbin' in d.getVar("SRC_URI"):
            d.setVar("XCL_PATH",os.path.dirname([a for a in d.getVar('SRC_URI').split() if '.xclbin' in a][0].lstrip('file://')))
}
python do_configure() {
    import glob, re, shutil

    if bb.utils.contains('MACHINE_FEATURES', 'fpga-overlay', False, True, d):
        bb.warn("Using fpga-manager.bbclass requires fpga-overlay MACHINE_FEATURE to be enabled")

    #renaming firmware-name using $PN as bitstream will be renamed using $PN when generating the bin file
    orig_dtsi = glob.glob(d.getVar('S')+ (d.getVar('DTSI_PATH') or '') + '/*.dtsi')[0]
    new_dtsi = d.getVar('S') + '/pl.dtsi_firmwarename'
    with open(new_dtsi, 'w') as newdtsi:
        with open(orig_dtsi) as olddtsi:
            for line in olddtsi:
                newdtsi.write(re.sub('firmware-name.*\".*\"','firmware-name = \"'+d.getVar('PN')+'.bit.bin\"',line))
    shutil.move(new_dtsi,orig_dtsi)
}

python devicetree_do_compile:append() {
    import glob, subprocess
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
    install -Dm 0644 *.dtbo ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.dtbo
    install -Dm 0644 ${PN}.bit.bin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.bit.bin
    if ls ${S}/${XCL_PATH}/*.xclbin >/dev/null 2>&1; then
        install -Dm 0644 ${S}/${XCL_PATH}/*.xclbin ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/${PN}.xclbin
    fi
    install -Dm 0644 ${S}/${JSON_PATH}/shell.json ${D}/${nonarch_base_libdir}/firmware/xilinx/${PN}/shell.json
}

do_deploy[noexec] = "1"

FILES:${PN} += "${nonarch_base_libdir}/firmware/xilinx/${PN}"
