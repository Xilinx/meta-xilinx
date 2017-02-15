EXTRA_IMAGEDEPENDS += "qemu-native qemu-helper-native"

def qemu_default_dtb(d):
    if d.getVar("MACHINE_DEVICETREE", True):
        dtbs = d.getVar("MACHINE_DEVICETREE", True).split(" ")
        dtbs = [os.path.splitext(os.path.basename(d)) for d in dtbs]
        dtbs = ["%s.dtb" % name for name, ext in dtbs if ext == ".dts" or ext == ".dtb"]
        return dtbs[0] if dtbs else ""
    elif d.getVar("KERNEL_DEVICETREE", True):
        dtbs = d.getVar("KERNEL_DEVICETREE", True).split(" ")
        dtbs = [os.path.basename(d) for d in dtbs]
        return d.getVar("KERNEL_IMAGETYPE", True) + "-" + (dtbs[0] if dtbs else "")
    return ""

def qemu_default_serial(d):
    if d.getVar("SERIAL_CONSOLE", True):
        speed, console = d.getVar("SERIAL_CONSOLE", True).split(" ", 1)
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
    if qemuboot_xilinx_enabled(d):
        return "qemu-xilinx/qemu-system-%s" % ta
    return "qemu-system-%s" % ta

def qemuboot_xilinx_enabled(d):
    if bb.utils.contains("MACHINE_FEATURES", "qemu-system-xilinx", True, False, d):
        if d.getVar("XILINX_QEMUBOOT", True) == "1":
            return True
    return False

def qemuboot_xilinx_multi_enabled(d):
    if bb.utils.contains("MACHINE_FEATURES", "qemu-system-xilinx", True, False, d):
        if d.getVar("XILINX_QEMUBOOT_MULTI", True) == "1":
            return True
    return False

def qemuboot_xilinx_hw_devicetrees(d):
    if bb.utils.contains("MACHINE_FEATURES", "qemu-system-xilinx", True, False, d):
        if d.getVar("XILINX_QEMUBOOT_MULTI", True) == "1":
            return " -hw-dtb ${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees/MULTI_ARCH/"
    return " -hw-dtb ${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees/SINGLE_ARCH/"

def qemuboot_xilinx_unhalt(d):
    if bb.utils.contains("MACHINE_FEATURES", "qemu-system-xilinx", True, False, d):
        if d.getVar("XILINX_QEMUBOOT_MULTI", True) == "1":
            return " -global xlnx,zynqmp-boot.cpu-num=0 -global xlnx,zynqmp-boot.use-pmufw=true "
    return " -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4 "

# For runqemu, default setup across all machines in meta-xilinx
IMAGE_CLASSES += "qemuboot"
QB_SYSTEM_NAME ?= "${@qemu_target_binary(d)}"
QB_DEFAULT_FSTYPE ?= "cpio"

XILINX_QEMUBOOT[doc] = "Set this variable to '1' to enable the generation of qemuboot.conf which uses the Xilinx QEMU."
XILINX_QEMUBOOT ?= ""

# Dependencies for native Xilinx QEMU
# XILINX_QEMU_DEPENDS ?= "qemu-xilinx-native"
# If the machine supports the Xilinx QEMU, add the dependencies
EXTRA_IMAGEDEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'qemu-system-xilinx', '${XILINX_QEMU_DEPENDS}', '', d)}"

# Add an override for when booting with the Xilinx QEMU
MACHINEOVERRIDES .= "${@':qemuboot-xilinx' if qemuboot_xilinx_enabled(d) else ''}"
MACHINEOVERRIDES .= "${@':qemuboot-xilinx-multi' if qemuboot_xilinx_multi_enabled(d) else ''}"

# This is ignored if using Xilinx's QEMU fork
QB_DTB ?= "${@qemu_default_dtb(d)}"

# ZynqMP machines need special "-hw-dtb" device trees for the Xilinx QEMU
XILINX_QEMU_DEPENDS_append_zynqmp = " qemu-devicetrees"

QB_DEFAULT_KERNEL_qemuboot-xilinx = "none"
QB_KERNEL_CMDLINE_APPEND ?= "${@qemu_default_serial(d)}"

# Default machine targets for Xilinx QEMU (FDT Generic)
QB_MACHINE_qemuboot-xilinx_aarch64 = "-machine arm-generic-fdt"
QB_MACHINE_qemuboot-xilinx_arm = "-machine arm-generic-fdt-plnx"
QB_MACHINE_qemuboot-xilinx_microblaze = "-machine microblaze-generic-fdt"
