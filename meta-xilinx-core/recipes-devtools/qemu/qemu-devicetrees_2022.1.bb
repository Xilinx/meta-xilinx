
require qemu-devicetrees.inc

BRANCH ?= "xlnx_rel_v2022.1"
SRCREV ?= "0499324af1178057c3730b0989c8fb5c5bbc4cf8"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://0001-Makefile-Use-python3-instead-of-python.patch"
