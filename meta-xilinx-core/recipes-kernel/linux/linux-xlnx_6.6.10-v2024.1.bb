LINUX_VERSION = "6.6.10"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.6_LTS"
SRCREV = "3af4295e00efdced3e8c6973606a7de55f6bf7dc"
SRCREV_meta = "5d0809d0d939c7738cb6e5391126c73fd0e4e865"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
