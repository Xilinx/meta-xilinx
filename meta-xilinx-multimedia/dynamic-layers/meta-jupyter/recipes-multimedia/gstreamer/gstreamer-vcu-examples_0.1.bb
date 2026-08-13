SUMMARY = "GStreamer demo scripts exercising the H.264/H.265 Video \
Codec Unit (VCU) on Zynq UltraScale+ MPSoC EV devices."
DESCRIPTION = "Demo scripts to run common usecases involving VCU in \
ZynqMP"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=17e31b2e971eed6471a361c7dc4faa18"

require gstreamer-multimedia-notebooks_0.1.inc


inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"
PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} = "gstreamer1.0-omx gstreamer1.0-plugins-bad bash python3-pip alsa-utils"

EXTRA_OEMAKE = 'D=${D} bindir=${bindir} datadir=${datadir}'

do_install() {
        oe_runmake -C ${S}/vcu install_vcu_examples

        # The AVC and HEVC decode entries launch matchbox-terminal, which is
        # no longer shipped. The ReadMe entry does not use matchbox and stays.
        rm -f ${D}${datadir}/applications/4K_AVC_Decode.desktop
        rm -f ${D}${datadir}/applications/4K_HEVC_Decode.desktop
}

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.

EXCLUDE_FROM_WORLD = "1"
