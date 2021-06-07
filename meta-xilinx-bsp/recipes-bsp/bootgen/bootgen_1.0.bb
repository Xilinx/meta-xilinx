SUMMARY = "Building and installing bootgen"
DESCRIPTION = "Building and installing bootgen, a Xilinx tool that lets you stitch binary files together and generate device boot images"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d526b6d0807bf263b97da1da876f39b1"

S = "${WORKDIR}/git"

DEPENDS += "openssl"
RDEPENDS_${PN} += "openssl"

REPO ?= "git://github.com/Xilinx/bootgen.git;protocol=https"
BRANCH ?= "xlnx-rel-v2021.1"
SRCREV = "34c4313a09dd75cf6ff5b188136e1a077c5b0aa2"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

EXTRA_OEMAKE += 'CROSS_COMPILER="${CXX}" -C ${S}'
CXXFLAGS_append = " -std=c++0x"

TARGET_CC_ARCH += "${LDFLAGS}"

do_install() {
    install -d ${D}${bindir}
    install -Dm 0755 ${S}/bootgen ${D}${bindir}
}

FILES_${PN} = "${bindir}/bootgen"

BBCLASSEXTEND = "native nativesdk"
