LINUX_VERSION = "6.6.40"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.6_LTS"
SRCREV = "e1ac97a52259dbbc864de032205a1d5d7b9316ce"
SRCREV_meta = "5d0809d0d939c7738cb6e5391126c73fd0e4e865"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
