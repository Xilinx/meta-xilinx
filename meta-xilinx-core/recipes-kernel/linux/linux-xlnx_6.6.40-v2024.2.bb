LINUX_VERSION = "6.6.40"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.6_LTS"
SRCREV = "0dfaecf365d9f9c89f3776a4091387893057dcd4"
SRCREV_meta = "5d0809d0d939c7738cb6e5391126c73fd0e4e865"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
