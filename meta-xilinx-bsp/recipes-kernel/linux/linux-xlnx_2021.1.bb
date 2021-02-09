LINUX_VERSION = "5.10"
SRCREV ?= "62ea514294a0c9a80455e51f1f4de36e66e8c546"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
