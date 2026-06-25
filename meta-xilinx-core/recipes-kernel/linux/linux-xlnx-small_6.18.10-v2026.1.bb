require linux-xlnx_6.18.10-v2026.1.bb

SUMMARY = "AMD Xilinx Linux kernel (linux-xlnx) with a reduced \
defconfig for image-recovery use."
DESCRIPTION = "Xilinx Kernel - Alternative smaller configuration"

# Eventually we may have more smaller configuration, set a variable
# to use with a SoC or machine override to specify this.
LOCAL_DEFCONFIG = ""
LOCAL_DEFCONFIG:aarch64 = "file://amd_aarch64_mini_defconfig"

SRC_URI += " ${LOCAL_DEFCONFIG}"

# Unsetting the KBUILD_DEFCONFIG so that kernel-yocto.bbclass will use the
# defconfig from SRC_URI for imgrcvry distro
KBUILD_DEFCONFIG:forcevariable = ""

# Only aarch64 is currently supported
COMPATIBLE_MACHINE:aarch64 = ".*"

COMPATIBLE_MACHINE:microblaze = "^$"
COMPATIBLE_MACHINE:riscv32 = "^$"
COMPATIBLE_MACHINE:riscv64 = "^$"
COMPATIBLE_MACHINE:zynq = "^$"
