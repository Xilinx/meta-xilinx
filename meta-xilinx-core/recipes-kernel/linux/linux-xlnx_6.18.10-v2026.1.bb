LINUX_VERSION = "6.18.10"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.18;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.18_LTS"
SRCREV = "9b4d47ea620961a458846aad28afb767b2573f07"
SRCREV_meta = "f202157d297182244bde71c8ed8b6d4946deec8f"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

PV .= "+v2026.1"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
