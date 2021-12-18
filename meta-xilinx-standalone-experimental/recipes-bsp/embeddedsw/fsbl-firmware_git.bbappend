# We WANT to default to this version when available
DEFAULT_PREFERENCE = "100"

# Reset this
SRC_URI = "${EMBEDDEDSW_SRCURI}"

inherit esw

# Not compatible with Zynq
COMPATIBLE_MACHINE:zynq = "none"

ESW_COMPONENT_SRC = "/lib/sw_apps/undefined/src"
ESW_COMPONENT_SRC:zynq = "/lib/sw_apps/zynq_fsbl/src"
ESW_COMPONENT_SRC:zynqmp = "/lib/sw_apps/zynqmp_fsbl/src"

DEPENDS += "xilstandalone xiltimer xilffs xilsecure xilpm"

do_copy_psu_init[depends] += "device-tree:do_deploy"
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
    :
}

addtask deploy before do_build after do_package

ESW_COMPONENT = "undefined"
ESW_COMPONENT:zynq = "zynq_fsbl.elf"
ESW_COMPONENT:zynqmp = "zynqmp_fsbl.elf"

CFLAGS:append:aarch64 = " -DARMA53_64"
CFLAGS:append:armv7r = " -DARMR5"
