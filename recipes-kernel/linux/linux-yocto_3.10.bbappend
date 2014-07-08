
require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
SRC_URI_append_microblaze += "file://ec2eba55f0c0e74dd39aca14dcc597583cf1eb67.patch"
