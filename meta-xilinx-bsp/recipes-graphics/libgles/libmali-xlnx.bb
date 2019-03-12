DESCRIPTION = "libGLES for ZynqMP with Mali 400"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://EULA;md5=82e466d0ed92c5a15f568dbe6b31089c"

inherit distro_features_check update-alternatives

REQUIRED_DISTRO_FEATURES = "x11 fbdev wayland"

PROVIDES += "virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm"

FILESEXTRAPATHS_append := " \
                ${THISDIR}/files: \
                ${THISDIR}/r8p0-00rel0: "


REPO ?= "git://github.com/Xilinx/mali-userspace-binaries.git;protocol=https"
BRANCH ?= "rel-v2019.2"
SRCREV ?= "90ea8555cddaa7979019fd7eeaeb01a9f1b26ac7"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"

PV = "r8p0-01rel0"
SRC_URI = " \
    ${REPO};${BRANCHARG} \
    file://egl.pc \
    file://glesv1_cm.pc \
    file://glesv1.pc \
    file://glesv2.pc \
    file://gbm.pc \
    "

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmpeg = "zynqmpeg"
COMPATIBLE_MACHINE_zynqmpev = "zynqmpev"

PACKAGE_ARCH = "${SOC_FAMILY}"


S = "${WORKDIR}/git"

# If were switching at runtime, we need all RDEPENDS needed for all backends available
RDEPENDS_${PN} = " kernel-module-mali libxdamage libxext libx11 libdrm libxfixes"

# We dont build anything but we want to avoid QA warning build-deps
DEPENDS = "wayland"

# x11 is default, set to "fbdev" , "wayland", or "headless" if required
MALI_BACKEND_DEFAULT ?= "x11"

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
    install -m 0644 ${S}/${PV}/glesHeaders/EGL/*.h ${D}${includedir}/EGL/
    install -d -m 0655 ${D}${includedir}/GLES
    install -m 0644 ${S}/${PV}/glesHeaders/GLES/*.h ${D}${includedir}/GLES/
    install -d -m 0655 ${D}${includedir}/GLES2
    install -m 0644 ${S}/${PV}/glesHeaders/GLES2/*.h ${D}${includedir}/GLES2/
    install -d -m 0655 ${D}${includedir}/KHR
    install -m 0644 ${S}/${PV}/glesHeaders/KHR/*.h ${D}${includedir}/KHR/

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${WORKDIR}/egl.pc ${D}${libdir}/pkgconfig/egl.pc
    install -m 0644 ${WORKDIR}/glesv2.pc ${D}${libdir}/pkgconfig/glesv2.pc
    install -m 0644 ${WORKDIR}/glesv1.pc ${D}${libdir}/pkgconfig/glesv1.pc
    install -m 0644 ${WORKDIR}/glesv1_cm.pc ${D}${libdir}/pkgconfig/glesv1_cm.pc

    install -d ${D}${libdir}
    install -d ${D}${includedir}

    cp -a --no-preserve=ownership ${S}/${PV}/${ARCH_PLATFORM_DIR}/common/*.so* ${D}${libdir}


    install -m 0644 ${S}/${PV}/glesHeaders/GBM/gbm.h ${D}${includedir}/
    install -m 0644 ${WORKDIR}/gbm.pc ${D}${libdir}/pkgconfig/gbm.pc
    install -Dm 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/wayland/libMali.so.8.0 ${D}${libdir}/wayland/libMali.so.8.0
    install -Dm 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/x11/libMali.so.8.0 ${D}${libdir}/x11/libMali.so.8.0
    install -Dm 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/fbdev/libMali.so.8.0 ${D}${libdir}/fbdev/libMali.so.8.0
    install -Dm 0644 ${S}/${PV}/${ARCH_PLATFORM_DIR}/headless/libMali.so.8.0 ${D}${libdir}/headless/libMali.so.8.0

    # We need to have one of the libraries available at build time for the linker
    ln -snf x11/libMali.so.8.0 ${D}${libdir}/libMali.so.8.0
}


# We need separate packages to provide multiple alternatives, at this point we install
# everything on the default one but that can be split if necessary
PACKAGES += "${PN}-x11 ${PN}-fbdev ${PN}-wayland ${PN}-headless"

# This is default/common for all alternatives
ALTERNATIVE_LINK_NAME[libmali-xlnx] = "${libdir}/libMali.so.8.0"


# Declare alternatives and corresponding library location
ALTERNATIVE_${PN}-x11 = "libmali-xlnx"
ALTERNATIVE_TARGET_libmali-xlnx-x11[libmali-xlnx] = "${libdir}/x11/libMali.so.8.0"

ALTERNATIVE_${PN}-fbdev = "libmali-xlnx"
ALTERNATIVE_TARGET_libmali-xlnx-fbdev[libmali-xlnx] = "${libdir}/fbdev/libMali.so.8.0"

ALTERNATIVE_${PN}-wayland = "libmali-xlnx"
ALTERNATIVE_TARGET_libmali-xlnx-wayland[libmali-xlnx] = "${libdir}/wayland/libMali.so.8.0"

ALTERNATIVE_${PN}-headless = "libmali-xlnx"
ALTERNATIVE_TARGET_libmali-xlnx-headless[libmali-xlnx] = "${libdir}/headless/libMali.so.8.0"

# Set priorities according to what we prveiously defined
ALTERNATIVE_PRIORITY_libmali-xlnx-x11[libmali-xlnx] = "${@bb.utils.contains("MALI_BACKEND_DEFAULT", "x11", "20", "10", d)}"
ALTERNATIVE_PRIORITY_libmali-xlnx-fbdev[libmali-xlnx] = "${@bb.utils.contains("MALI_BACKEND_DEFAULT", "fbdev", "20", "10", d)}"
ALTERNATIVE_PRIORITY_libmali-xlnx-wayland[libmali-xlnx] = "${@bb.utils.contains("MALI_BACKEND_DEFAULT", "wayland", "20", "10", d)}"
ALTERNATIVE_PRIORITY_libmali-xlnx-headless[libmali-xlnx] = "${@bb.utils.contains("MALI_BACKEND_DEFAULT", "headless", "20", "10", d)}"


# Package gets renamed on the debian class, but we want to keep -xlnx
DEBIAN_NOAUTONAME_libmali-xlnx = "1"

# Update alternatives will actually have separate postinst scripts (one for each package)
# This wont work for us, so we create a common postinst script and we pass that as the general
# libmali-xlnx postinst script, but we defer execution to run on first boot (pkg_postinst_ontarget).
# This will avoid ldconfig removing the symbolic links when creating the root filesystem.
python populate_packages_updatealternatives_append () {
    # We need to remove the 'fake' libmali-xlnx before creating any links
    libdir = d.getVar('libdir')
    common_postinst = "#!/bin/sh\nrm " + libdir + "/libMali.so.8.0\n"
    for pkg in (d.getVar('PACKAGES') or "").split():
        # Not all packages provide an alternative (e.g. ${PN}-lic)
        postinst = d.getVar('pkg_postinst_%s' % pkg)
        if postinst:
            old_postinst = postinst
            new_postinst = postinst.replace('#!/bin/sh','')
            common_postinst += new_postinst
    d.setVar('pkg_postinst_ontarget_%s' % 'libmali-xlnx', common_postinst)
}


# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

RREPLACES_${PN} = "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libgbm"
RPROVIDES_${PN} = "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libgbm"
RCONFLICTS_${PN} = "libegl libgles1 libglesv1-cm1 libgles2 libglesv2-2 libgbm"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"
FILES_${PN} += "${libdir}/*"
