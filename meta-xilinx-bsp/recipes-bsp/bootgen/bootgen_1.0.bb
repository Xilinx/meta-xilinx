SUMMARY = "Building and installing bootgen"
DESCRIPTION = "Building and installing bootgen, a Xilinx tool that lets you stitch binary files together and generate device boot images"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0c64669e3cd5e9cea3ad864ed14b0cb4"

S = "${WORKDIR}/git"

DEPENDS += "openssl"
RDEPENDS_${PN} += "openssl"

REPO = "git://gitenterprise.xilinx.com/SDK/bootgen.git;protocol=https"
BRANCH = "master"
SRCREV = "${AUTOREV}"

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
