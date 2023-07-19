LINUX_VERSION = "6.1.30"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.1;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.1_LTS"
SRCREV = "65e83cb0575c6ac3d8faf68bc7de6cdcad76ddb2"
SRCREV_meta = "185bcfcbe480c742247d9117011794c69682914f"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
