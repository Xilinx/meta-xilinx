LINUX_VERSION = "6.1.30"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.1;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.1_LTS"
SRCREV = "bd2c73dd60da25dae9f78894f9d96688543abccf"
SRCREV_meta = "185bcfcbe480c742247d9117011794c69682914f"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"