DESCRIPTION = "Video Codec Unit (VCU) for ZynqMP binaries"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README.md;md5=a2db2eb4ce2c1eb3423750a50733acbf"

PROVIDES += "al5d.fw al5d_b.fw al5e.fw al5e_b.fw AL_Decoder AL_Encoder"

PV = "alpha0"
SRCREV = "2016.3_alpha0"
SRC_URI = " \
    git://git@github.com/Xilinx/vcu-binaries.git;protocol=ssh;nobranch=1 \
    "
S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_zynqmp = "(.*)"

RDEPENDS_${PN} = " \
    vcu-modules \
    "

do_install() {
    install -d ${D}/usr/local/bin
    install -d ${D}/lib/firmware

    cp -a ${S}/${PV}/usr/local/bin/* ${D}/usr/local/bin
    cp -a ${S}/${PV}/lib/firmware/* ${D}/lib/firmware
}

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
FILES_${PN} = "/lib/firmware/* /usr/local/bin/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"
