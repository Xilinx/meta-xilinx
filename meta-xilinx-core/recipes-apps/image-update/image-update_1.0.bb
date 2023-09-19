DESCRIPTION = "Image update is used to update alternate images on compatible firmware."
SUMMARY = "Image update is used to update alternate image on compatible firmware. \
	If the current image is ImageA, ImageB will get updated and vice versa. \
	Usage: image_update <Input Image File>"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSES/MIT;md5=2ac09a7a37dd6ee0ba23ce497d57d09b"

BRANCH = "xlnx_rel_v2023.2"
SRC_URI = "git://github.com/Xilinx/linux-image_update.git;branch=${BRANCH};protocol=https"
SRCREV = "c1117cad92d967d7adca7fd2ba655808f4687516"

RDEPENDS:${PN} += "freeipmi"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"

PACKAGE_ARCH:zynqmp = "${SOC_FAMILY_ARCH}"

# Force the make system to use the flags we want!
EXTRA_OEMAKE = 'CC="${CC} ${TARGET_CFLAGS} ${TARGET_LDFLAGS}" all'

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/image_update ${D}${bindir}/
}
