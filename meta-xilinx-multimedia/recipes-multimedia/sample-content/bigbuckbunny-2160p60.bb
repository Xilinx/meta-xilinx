SUMMARY = "Big Buck Bunny Movie - 3840*2160@60fps"
LICENSE = "CC-BY-3.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/CC-BY-3.0;md5=dfa02b5755629022e267f10b9c0a2ab7"

SRC_URI = "http://ftp.nluug.nl/pub/graphics/blender/demo/movies/BBB/bbb_sunflower_2160p_60fps_normal.mp4"
SRC_URI[md5sum] = "fb20ba0a7f531eabb345a5abcce33d78"
SRC_URI[sha256sum] = "35db9a007021f1b0066993e1d2c4448c83a8b279f799c97d33cbba73980a8a36"

inherit allarch

do_install() {
    install -d ${D}${datadir}/movies
    install -m 0644 ${WORKDIR}/bbb_sunflower_2160p_60fps_normal.mp4 ${D}${datadir}/movies/
}

FILES:${PN} += "${datadir}/movies"
