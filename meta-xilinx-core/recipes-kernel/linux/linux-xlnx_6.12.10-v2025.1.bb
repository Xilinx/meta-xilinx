LINUX_VERSION = "6.12.10"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.12;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.12_LTS_2025.1_update"
SRCREV = "a3c19209094a80f89f5c381f40a72cfb2739a486"
SRCREV_meta = "5d9c6c5b0531161f9e8e9d108740ebcec9177398"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
