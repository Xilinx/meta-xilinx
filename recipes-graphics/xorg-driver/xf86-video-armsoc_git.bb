require recipes-graphics/xorg-driver/xorg-driver-video.inc

SUMMARY = "X.Org X server -- Xilinx ARM SOC display driver"
DESCRIPTION = "Xilinx ARM SOC display driver "

LICENSE = "MIT-X & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=10ce5de3b111315ea652a5f74ec0c602"

DEPENDS += "libx11 libdrm xf86driproto"
RDEPENDS_${PN} += "xserver-xorg-module-exa"

PV = "1.3.0+git${SRCPV}"

SRCREV_pn-${PN} = "8ca8513880697f9a34d4006c43342b830bdd1ff2"
SRC_URI = " \
	git://anongit.freedesktop.org/xorg/driver/xf86-video-armsoc \
        file://0001-src-drmmode_xilinx-Add-the-dumb-gem-support-for-Xili.patch \
        file://0002-enable-subdir-objects.patch \
	"

S = "${WORKDIR}/git"

EXTRA_OECONF = " --enable-maintainer-mode --with-drmmode=xilinx"
CFLAGS += " -I${STAGING_INCDIR}/xorg "
