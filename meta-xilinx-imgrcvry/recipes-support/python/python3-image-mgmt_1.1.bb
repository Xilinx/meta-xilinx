SUMMARY = "AMD System Boot Image Management Tool"
DESCRIPTION = "Python helper utility that orchestrates AMD System Boot \
Image (BOOT.BIN) selection and fall-back across the primary and \
recovery boot partitions."

require recipes-bsp/image-recovery-linux/image-recovery-linux-source-${PV}.inc

inherit setuptools3

S = "${WORKDIR}/git"

# Python dependencies
RDEPENDS:${PN} = " \
    python3-core \
"

# Host utilities called from Python scripts
RDEPENDS:${PN} += " \
    binutils \
    mtd-utils \
    freeipmi \
    ufs-utils \
"
