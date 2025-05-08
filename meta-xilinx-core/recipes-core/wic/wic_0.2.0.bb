DESCRIPTION = "wic Image Manipulator"
SECTION = "console/utils"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4f0802e55821766fdb26000965ec2bdf"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=https;branch=no-create-var-sector"
SRCREV = "a5fec5ef73a34266003e48257666f3fa47d9e4c2"
S = "${WORKDIR}/git"

inherit python_poetry_core

BBCLASSEXTEND = "native nativesdk"
