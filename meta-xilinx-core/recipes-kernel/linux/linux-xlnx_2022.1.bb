LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS_2022.1_update"
SRCREV = "04c8e16ec59f8653fb205e7c83e6c28d658e4b67"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
