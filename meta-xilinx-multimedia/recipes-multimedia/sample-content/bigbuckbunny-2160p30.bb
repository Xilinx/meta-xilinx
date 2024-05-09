SUMMARY = "Big Buck Bunny Movie - 3840*2160@30fps"
LICENSE = "CC-BY-3.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/CC-BY-3.0;md5=dfa02b5755629022e267f10b9c0a2ab7"

SRC_URI = "http://ftp.nluug.nl/pub/graphics/blender/demo/movies/BBB/bbb_sunflower_2160p_30fps_normal.mp4"
SRC_URI[md5sum] = "fca22c88d7cf72c02df69a6157d2185b"
SRC_URI[sha256sum] = "37f0ff251a606c2dcfa26c19fe6bf843234b4e7a8889cfab50bc26f644e55520"

inherit allarch

do_install() {
    install -d ${D}${datadir}/movies
    install -m 0644 ${WORKDIR}/bbb_sunflower_2160p_30fps_normal.mp4 ${D}${datadir}/movies/
}

FILES:${PN} += "${datadir}/movies"
