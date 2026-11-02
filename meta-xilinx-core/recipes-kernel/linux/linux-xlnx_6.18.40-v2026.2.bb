LINUX_VERSION = "6.18.40"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.18;destsuffix=yocto-kmeta"
KBRANCH = "xlnx_rebase_v6.18_LTS"
SRCREV = "43281162e28293f31a71c70a1bf06b7e8e1c0ba7"
SRCREV_meta = "f202157d297182244bde71c8ed8b6d4946deec8f"

include linux-xlnx.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion-linux-xlnx.inc
include recipes-kernel/linux/cve-exclusion-linux-xlnx_${LINUX_VERSION}.inc

PV .= "+v2026.2"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
