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
        return "console=%s,%s earlyprintk" % (console, speed)
    return ""

def qemu_target_binary(d):
    ta = d.getVar("TARGET_ARCH", True)
    if ta == "microblazeeb":
        ta = "microblaze"
    elif ta == "arm":
        ta = "aarch64"
    return "qemu-system-%s" % ta

# For runqemu, default setup across all machines in meta-xilinx
IMAGE_CLASSES += "qemuboot"
QB_SYSTEM_NAME ?= "${@qemu_target_binary(d)}"
QB_DEFAULT_FSTYPE ?= "cpio"
QB_DTB ?= "${@qemu_default_dtb(d)}"
QB_KERNEL_CMDLINE_APPEND ?= "${@qemu_default_serial(d)}"

