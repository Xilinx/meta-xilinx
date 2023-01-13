LINUX_VERSION = "6.1.0"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.1;destsuffix=yocto-kmeta"
KBRANCH="master"
SRCREV = "72a24cae76209b1519e2bbe0923023919bc36be9"
SRCREV_meta = "185bcfcbe480c742247d9117011794c69682914f"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
