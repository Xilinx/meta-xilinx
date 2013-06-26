inherit kernel

# This version extension should match CONFIG_LOCALVERSION in defconfig
LINUX_VERSION_EXTENSION ?= "-xilinx"

require recipes-kernel/linux/linux-yocto.inc

# MicroBlaze is a uImage target, but its not called 'uImage'
DEPENDS_append_microblaze += "u-boot-mkimage-native"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"
SRC_URI = "git://github.com/Xilinx/linux-xlnx;protocol=git;nocheckout=1"

PR = "r1"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCREV}"

# Device Tree and Kernel Configuration

# Common Device Tree Includes
# (put them in ${WORKDIR} to avoid using dtc with include paths)
FILESEXTRAPATHS_append := "${XILINX_LAYERDIR}/conf/machine/boards/common:"
SRC_URI_append += "file://zynq-7-base.dtsi"

# Override COMPATIBLE_MACHINE to include your machine in a bbappend file.
COMPATIBLE_MACHINE = "qemumicroblaze|qemuzynq|zynq7-zedboard"

# Scan all files in MACHINE_DEVICETREE and MACHINE_KCONFIG and populate SRC_URI,
# FILESEXTRAPATHS and KERNEL_DEVICETREE.
python __anonymous () {
    workdir = d.getVar("WORKDIR", True)
    machine_devicetree = d.getVar("MACHINE_DEVICETREE", True) or ''
    machine_kconfigs = d.getVar("MACHINE_KCONFIG", True) or ''
    extrapaths = set()
    sources = set()
    if machine_devicetree:
        files=set()
        for path in machine_devicetree.split():
            sources.add("file://" + os.path.basename(path))
            extrapaths.add(os.path.dirname(path))
            if os.path.splitext(os.path.basename(path))[1] == '.dts':
                files.add(os.path.join(workdir, os.path.basename(path)))
        d.setVar("KERNEL_DEVICETREE", ' '.join(files))
    if machine_kconfigs:
        for path in machine_kconfigs.split():
            files.add(os.path.join(workdir, os.path.basename(path)))
            sources.add("file://" + os.path.basename(path))
            extrapaths.add(os.path.dirname(path))

    if len(extrapaths) != 0:
        d.setVar("FILESEXTRAPATHS_append", ":".join(extrapaths) + ":")
    if len(sources) != 0:
        d.setVar("SRC_URI_append", " %s " % " ".join(sources))
}
