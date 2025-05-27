SUMMARY = "Image Recovery Live image init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS:${PN} = "busybox-mdev"
SRC_URI = "file://imgrcvry-init.sh"

S = "${WORKDIR}"

do_install() {
        install -m 0755 ${WORKDIR}/imgrcvry-init.sh ${D}/init
        install -d ${D}/dev
        mknod -m 622 ${D}/dev/console c 5 1
}

FILES:${PN} += " /init /dev "
