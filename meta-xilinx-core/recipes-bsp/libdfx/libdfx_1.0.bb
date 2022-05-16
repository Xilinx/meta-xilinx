SUMMARY = "Xilinx libdfx library"
DESCRIPTION = "Xilinx libdfx Library and headers"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=94aba86aec117f003b958a52f019f1a7"

BRANCH ?= "master"
REPO ?= "git://github.com/Xilinx/libdfx.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"
SRCREV = "1a1454ed0c3a56912250182f591c051f355fbf47"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"

S = "${WORKDIR}/git"

inherit cmake

