DESCRIPTION = "libGLES for ZynqMP with Mali 400"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://README.md;md5=d5750ae6496dd931669b454b5aaae2cd"

inherit distro_features_check
ANY_OF_DISTRO_FEATURES = "fbdev x11"

PROVIDES += "virtual/libgles1 virtual/libgles2 virtual/egl"
RPROVIDES_${PN} += "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2"

FILESEXTRAPATHS_append := " \
                ${THISDIR}/files: \
                ${THISDIR}/r7p0-00rel0: "

REPO ?= "git://gitenterprise.xilinx.com/Graphics/mali400-xlnx-userspace.git;protocol=https"
BRANCH ?= "master"
SRCREV ?= "dce796615dd346926e09af37f66123c03903b6e9"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
PV = "r7p0-00rel0"
SRC_URI = " \
    ${REPO};${BRANCHARG} \
    file://egl.pc \
    file://glesv1_cm.pc \
    file://glesv1.pc \
    file://glesv2.pc \
    "

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"

S = "${WORKDIR}/git"

X11RDEPENDS = "libxdamage libxext libx11 libdrm libxau libxcb libxdmcp libxfixes"
RDEPENDS_${PN} = " \
	kernel-module-mali \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${X11RDEPENDS}', '', d)} \
	"

EGL_TYPE = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11',  \
               bb.utils.contains('DISTRO_FEATURES', 'fbdev',  'fbdev', '', d), d)}"

do_install() {
    #Identify the ARCH type
    ${TARGET_PREFIX}gcc --version > ARCH_PLATFORM
    if grep -q aarch64 "ARCH_PLATFORM"; then
	ARCH_PLATFORM_DIR=aarch64-linux-gnu
    else
	ARCH_PLATFORM_DIR=arm-linux-gnueabihf
    fi

    # install headers
    install -d -m 0655 ${D}${includedir}/EGL
    install -m 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/${EGL_TYPE}/usr/include/EGL/*.h ${D}${includedir}/EGL/
    install -d -m 0655 ${D}${includedir}/GLES
    install -m 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/${EGL_TYPE}/usr/include/GLES/*.h ${D}${includedir}/GLES/
    install -d -m 0655 ${D}${includedir}/GLES2
    install -m 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/${EGL_TYPE}/usr/include/GLES2/*.h ${D}${includedir}/GLES2/
    install -d -m 0655 ${D}${includedir}/KHR
    install -m 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/${EGL_TYPE}/usr/include/KHR/*.h ${D}${includedir}/KHR/

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${WORKDIR}/egl.pc ${D}${libdir}/pkgconfig/egl.pc
    install -m 0644 ${WORKDIR}/glesv2.pc ${D}${libdir}/pkgconfig/glesv2.pc
    install -m 0644 ${WORKDIR}/glesv1.pc ${D}${libdir}/pkgconfig/glesv1.pc
    install -m 0644 ${WORKDIR}/glesv1_cm.pc ${D}${libdir}/pkgconfig/glesv1_cm.pc

    install -d ${D}${libdir}
    cp -a --no-preserve=ownership ${S}/${PV}/${ARCH_PLATFORM_DIR}/${EGL_TYPE}/usr/lib/*.so* ${D}${libdir}

    if ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'false', 'true', d)}; then
        sed -i -e 's/^#if defined(MESA_EGL_NO_X11_HEADERS)$/#if (1)/' ${D}${includedir}/EGL/eglplatform.h
    fi
}


# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"
