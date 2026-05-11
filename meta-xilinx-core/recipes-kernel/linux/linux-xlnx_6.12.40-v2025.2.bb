LINUX_VERSION = "6.12.40"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.12;destsuffix=yocto-kmeta"
KBRANCH="xlnx_rebase_v6.12_LTS"
SRCREV = "31626ef92ff11f9d3dee01926720572666fcc65a"
SRCREV_meta = "5d9c6c5b0531161f9e8e9d108740ebcec9177398"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion-linux-xlnx.inc
include recipes-kernel/linux/cve-exclusion-linux-xlnx_${LINUX_VERSION}.inc

# Disamiguiate 6.12.10-v2025.1 and 6.12.10-v2025.2
PV .= "+v2025.2"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
