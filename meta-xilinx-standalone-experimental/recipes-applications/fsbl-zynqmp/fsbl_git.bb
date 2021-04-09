inherit esw deploy

COMPATIBLE_HOST = "aarch64.*-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE_zynqmp = ".*"

ESW_COMPONENT_SRC = "/lib/sw_apps/zynqmp_fsbl/src"

DEPENDS += "xilstandalone xiltimer xilffs xilsecure xilpm"

do_copy_psu_init[depends] += "device-tree-lops:do_deploy"
python do_copy_psu_init() {
    import glob, subprocess, os

    system_dt = d.getVar('SYSTEM_DTFILE')
    src_dir = glob.glob(d.getVar('OECMAKE_SOURCEPATH'))
    psu_init_src = os.path.dirname(system_dt)
    src_file = psu_init_src + str("/psu_init.c")
    hdr_file = psu_init_src + str("/psu_init.h")
    if os.path.exists(src_file):
         command = ["install"] + ["-m"] + ["0755"] + [src_file] + [src_dir[0]]
         subprocess.run(command, check = True)
         command = ["install"] + ["-m"] + ["0755"] + [hdr_file] + [src_dir[0]]
         subprocess.run(command, check = True)
}
addtask do_copy_psu_init before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/zynqmp_fsbl* ${D}/${base_libdir}/firmware
}

ZYNQMP_FSBL_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
ZYNQMP_FSBL_BASE_NAME[vardepsexclude] = "DATETIME"

do_deploy() {

    # We need to deploy the stripped elf, hence why not doing it from ${D}
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.elf ${DEPLOYDIR}/${ZYNQMP_FSBL_BASE_NAME}.elf
    ln -sf ${ZYNQMP_FSBL_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.elf ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/zynqmp_fsbl.bin ${DEPLOYDIR}/${ZYNQMP_FSBL_BASE_NAME}.bin
    ln -sf ${ZYNQMP_FSBL_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

CFLAGS_append_aarch64 = " -DARMA53_64"
CFLAGS_append_armrm = " -DARMR5"

FILES_${PN} = "${base_libdir}/firmware/zynqmp_fsbl*"
