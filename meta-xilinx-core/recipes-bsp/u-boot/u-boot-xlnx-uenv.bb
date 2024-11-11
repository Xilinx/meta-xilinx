SUMMARY = "U-Boot uEnv.txt SD boot environment generation for Zynq targets"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy image-wic-utils

def remove_task_from_depends(d):
    extra_imagedepends = d.getVar('EXTRA_IMAGEDEPENDS') or ''
    uenv_depends = ''
    for imagedepend in extra_imagedepends.split():
        if imagedepend == d.getVar("BPN") or '':
            continue
        elif ':' in imagedepend:
            uenv_depends += ' %s' % imagedepend.split(':')[0]
        else:
            uenv_depends += ' %s' % imagedepend
    return uenv_depends

def uboot_boot_cmd(d):
    if (d.getVar("KERNEL_IMAGETYPE") or '') in ["uImage", "fitImage"]:
        return "bootm"
    if (d.getVar("KERNEL_IMAGETYPE") or '') in ["zImage"]:
        return "bootz"
    if (d.getVar("KERNEL_IMAGETYPE") or '') in ["Image"]:
        return "booti"
    raise bb.parse.SkipRecipe("Unsupport kernel image type")

def get_sdbootdev(d):
    if (d.getVar("SOC_FAMILY") or '') in ["zynqmp"]:
        return "${devnum}"
    else:
        return "0"

def uenv_populate(d):
    # populate the environment values
    env = {}

    env["machine_name"] = d.getVar("MACHINE") or ''

    env["kernel_image"] = d.getVar("KERNEL_IMAGETYPE") or ''
    env["kernel_load_address"] = d.getVar("KERNEL_LOAD_ADDRESS") or ''

    env["devicetree_image"] = boot_files_dtb_filepath(d)
    env["devicetree_load_address"] = d.getVar("DEVICETREE_LOAD_ADDRESS") or ''
    env["devicetree_overlay_load_address" ] = d.getVar("DEVICETREE_OVERLAY_LOAD_ADDRESS") or ''

    env["bootargs"] = d.getVar("KERNEL_BOOTARGS") or ''

    env["loadkernel"] = "fatload mmc " + get_sdbootdev(d) + " ${kernel_load_address} ${kernel_image}"
    env["loaddtb"] = "fatload mmc  " + get_sdbootdev(d) + " ${devicetree_load_address} ${devicetree_image}"
    env["loaddtbo"] = "if test -e mmc " + get_sdbootdev(d) + " /devicetree/openamp.dtbo; then fatload mmc " + get_sdbootdev(d) + " ${devicetree_overlay_load_address} /devicetree/openamp.dtbo ; fdt addr ${devicetree_load_address} ; fdt resize 8192 ; fdt apply ${devicetree_overlay_load_address} ; fi"
    env["bootkernel"] = "run loadkernel && run loaddtb && run loaddtbo && " + uboot_boot_cmd(d) + " ${kernel_load_address} - ${devicetree_load_address}"

    if (d.getVar("SOC_FAMILY") or '') in ["zynqmp"]:
        env["bootkernel"] = "setenv bootargs " +  (d.getVar("KERNEL_BOOTARGS") or '') + " ; " + env["bootkernel"]

    # default uenvcmd does not load bitstream
    env["uenvcmd"] = "run bootkernel"

    bitstream, bitstreamtype = boot_files_bitstream(d)
    if bitstream:
        env["bitstream_image"] = bitstream
        env["bitstream_load_address"] = "0x100000"

        # if bitstream is "bit" format use loadb, otherwise use load
        env["bitstream_type"] = "loadb" if bitstreamtype else "load"

        # load bitstream first with loadfpa
        env["loadfpga"] = "fatload mmc " + get_sdbootdev(d) + " ${bitstream_load_address} ${bitstream_image} && fpga ${bitstream_type} 0 ${bitstream_load_address} ${filesize}"
        env["uenvcmd"] = "run loadfpga && run bootkernel"

    return env

DEPENDS:append := " virtual/kernel ${@remove_task_from_depends(d)}"

# bootargs, default to booting with the rootfs device being partition 2
KERNEL_BOOTARGS:zynq = "earlyprintk console=ttyPS0,115200 root=/dev/mmcblk0p2 rw rootwait"
KERNEL_BOOTARGS:zynqmp = "earlycon root=/dev/mmcblk${devnum}p2 rw rootwait"

KERNEL_LOAD_ADDRESS:zynq = "0x2080000"
KERNEL_LOAD_ADDRESS:zynqmp = "0x200000"
DEVICETREE_LOAD_ADDRESS:zynq = "0x2000000"
DEVICETREE_LOAD_ADDRESS:zynqmp = "0x4000000"
DEVICETREE_OVERLAY_LOAD_ADDRESS:zynq = "${@hex(int(d.getVar("DEVICETREE_LOAD_ADDRESS"),16) + 0xf00000)}"
DEVICETREE_OVERLAY_LOAD_ADDRESS:zynqmp = "${@hex(int(d.getVar("DEVICETREE_LOAD_ADDRESS"),16) + 0xf00000)}"

python do_compile() {
    env = uenv_populate(d)
    with open(d.expand("${WORKDIR}/uEnv.txt"), "w") as f:
        for k, v in env.items():
            f.write("{0}={1}\n".format(k, v))
}

FILES:${PN} += "/boot/uEnv.txt"

do_install() {
	install -Dm 0644 ${WORKDIR}/uEnv.txt ${D}/boot/uEnv.txt
}

do_deploy() {
	install -Dm 0644 ${WORKDIR}/uEnv.txt ${DEPLOYDIR}/uEnv.txt
}
addtask do_deploy after do_compile before do_build

