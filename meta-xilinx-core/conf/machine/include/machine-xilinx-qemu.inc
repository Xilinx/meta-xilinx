# This include is used to setup default QEMU and qemuboot config for meta-xilinx
# machines.

# Use the xilinx specific version for these users
IMAGE_CLASSES += "qemuboot-xilinx"

# depend on qemu-helper-native, which will depend on QEMU
EXTRA_IMAGEDEPENDS += "qemu-helper-native:do_addto_recipe_sysroot"

PREFERRED_PROVIDER_qemu-helper-native = "qemu-xilinx-helper-native"
PREFERRED_PROVIDER_qemu = "qemu-xilinx"
PREFERRED_PROVIDER_qemu-native = "qemu-xilinx-native"
PREFERRED_PROVIDER_nativesdk-qemu = "nativesdk-qemu-xilinx"

def qemu_add_extra_args(d):
    initramfs_image = d.getVar('INITRAMFS_IMAGE') or ""
    bundle_image = d.getVar('INITRAMFS_IMAGE_BUNDLE') or ""
    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE') or ""
    machine_name = d.getVar('MACHINE') or ""
    soc_family = d.getVar('SOC_FAMILY') or ""
    qb_extra_args = ''
    # Add kernel image and boot.scr to qemu boot command when initramfs_image supplied
    kernel_name = ''
    bootscr_image = '%s/boot.scr' % deploy_dir
    if soc_family in ('zynqmp', 'versal'):
        kernel_name = 'Image'
        bootscr_loadaddr = '0x20000000'
    if initramfs_image:
        kernel_image = '%s/%s' % (deploy_dir, kernel_name)
        if bundle_image == "1":
            kernel_image = '%s/%s-initramfs-%s.bin' % (deploy_dir, kernel_name, machine_name)
        kernel_loadaddr = '0x200000'
        if kernel_name:
            qb_extra_args = ' -device loader,file=%s,addr=%s,force-raw=on' % (kernel_image, kernel_loadaddr)
            qb_extra_args += ' -device loader,file=%s,addr=%s,force-raw=on' % (bootscr_image, bootscr_loadaddr)
        if soc_family == 'versal':
            qb_extra_args += ' -boot mode=5'
    else:
        if soc_family in ('zynqmp', 'versal'):
            qb_extra_args = ' -boot mode=5'
    return qb_extra_args

def qemu_rootfs_params(d,param):
    initramfs_image = d.getVar('INITRAMFS_IMAGE') or ""
    bundle_image = d.getVar('INITRAMFS_IMAGE_BUNDLE') or ""
    soc_family = d.getVar('SOC_FAMILY') or ""
    tune_features = (d.getVar('TUNE_FEATURES') or []).split()
    if param == 'rootfs':
        return 'none' if bundle_image == "1" else ''
    elif param == 'fstype':
        fstype_dict = {
            "microblaze": "cpio.gz",
            "zynq": "cpio.gz",
            "zynqmp": "cpio.gz.u-boot",
            "versal": "cpio.gz.u-boot.qemu-sd-fatimg"
        }
        if initramfs_image:
            if 'microblaze' in tune_features:
                return fstype_dict['microblaze']
            else:
                return fstype_dict[soc_family]
        else:
                return 'wic.qemu-sd'
    elif param == 'rootfs-opt':
        if not initramfs_image or soc_family == 'versal':
            sd_index = "1"
            if soc_family == 'zynq': sd_index = "0"
            return ' -drive if=sd,index=%s,file=@ROOTFS@,format=raw' % (sd_index)
        elif soc_family not in ('zynq') or 'microblaze' in tune_features:
            return ' -device loader,file=@ROOTFS@,addr=0x04000000,force-raw=on'

def qemu_default_dtb(d):
    if d.getVar("IMAGE_BOOT_FILES", True):
        dtbs = d.getVar("IMAGE_BOOT_FILES", True).split(" ")
        # IMAGE_BOOT_FILES has extra renaming info in the format '<source>;<target>'
        # Note: Wildcard sources work here only because runqemu expands them at run time
        dtbs = [f.split(";")[0] for f in dtbs]
        dtbs = [f for f in dtbs if f.endswith(".dtb")]
        if len(dtbs) != 0:
            return dtbs[0]
    return ""

def qemu_default_serial(d):
    if d.getVar("SERIAL_CONSOLES", True):
        first_console = d.getVar("SERIAL_CONSOLES", True).split(" ")[0]
        speed, console = first_console.split(";", 1)
        # zynqmp uses earlycon and stdout (in dtb)
        if "zynqmp" in d.getVar("MACHINEOVERRIDES", True).split(":"):
            return ""
        return "console=%s,%s earlyprintk" % (console, speed)
    return ""

def qemu_target_binary(d):
    ta = d.getVar("TARGET_ARCH", True)
    if ta == "microblazeeb":
        ta = "microblaze"
    elif ta == "arm":
        ta = "aarch64"
    return "qemu-system-%s" % ta

def qemu_zynqmp_unhalt(d, multiarch):
    if multiarch:
        return "-global xlnx,zynqmp-boot.cpu-num=0 -global xlnx,zynqmp-boot.use-pmufw=true"
    return "-device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4 -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4"

# For qemuboot, default setup across all machines in meta-xilinx
QB_SYSTEM_NAME:aarch64 ?= "${@qemu_target_binary(d)}-multiarch"
QB_SYSTEM_NAME ?= "${@qemu_target_binary(d)}"
QB_DEFAULT_FSTYPE ?= "cpio"
QB_DTB ?= "${@qemu_default_dtb(d)}"
