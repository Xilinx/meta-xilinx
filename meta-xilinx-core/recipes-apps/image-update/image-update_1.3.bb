DESCRIPTION = "Image update is used to update alternate images on compatible firmware."
SUMMARY = "Image update is used to update alternate image on compatible firmware. \
	If the current image is ImageA, ImageB will get updated and vice versa. \
	Usage: image_update <Input Image File>"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSES/MIT;md5=2ac09a7a37dd6ee0ba23ce497d57d09b"

BRANCH = "master"
SRC_URI = "git://github.com/Xilinx/linux-image_update.git;branch=${BRANCH};protocol=https"
SRCREV = "1bd7d7405b484d808176c6e711691a846c18b4f0"

RDEPENDS:${PN} += "freeipmi"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-net = "versal-net"

PACKAGE_ARCH:zynqmp = "${MACHINE_ARCH}"

# Force the make system to use the flags we want!
EXTRA_OEMAKE = 'CC="${CC} ${TARGET_CFLAGS} ${TARGET_LDFLAGS}" all'

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/image_update ${D}${bindir}/
}
