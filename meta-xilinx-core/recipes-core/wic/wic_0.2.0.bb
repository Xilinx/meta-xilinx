DESCRIPTION = "wic Image Manipulator"
SECTION = "console/utils"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4f0802e55821766fdb26000965ec2bdf"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=https;branch=no-create-var-sector"
SRCREV = "a5fec5ef73a34266003e48257666f3fa47d9e4c2"
S = "${WORKDIR}/git"

inherit python_poetry_core

# wic is not generally a tool that would be installed on a target
# usually it would be run on the build host
# the wic non-create commands (ls, cp) need the following
RDEPENDS:${PN} = " \
    dosfstools \
    e2fsprogs \
    e2fsprogs-resize2fs \
    gptfdisk \
    mtools \
    parted \
    util-linux \
"

BBCLASSEXTEND = "native nativesdk"
