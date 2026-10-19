SUMMARY = "Building and installing bootgen"
DESCRIPTION = "Building and installing bootgen, a Xilinx tool that lets you stitch binary files together and generate device boot images"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=04054e01a445f223956a47542277e6ad"

DEPENDS += "openssl"
RDEPENDS:${PN} += "openssl"

REPO ?= "git://github.com/Xilinx/bootgen.git;protocol=https"
BRANCH = "master"
SRCREV = "b0f60761ffce44d589229a70f1ad4c125fce2afe"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

do_install() {
    install -d ${D}${bindir}
    install -Dm 0755 ${S}/build/bin/bootgen ${D}${bindir}
}

FILES:${PN} = "${bindir}/bootgen"

BBCLASSEXTEND = "native nativesdk"
