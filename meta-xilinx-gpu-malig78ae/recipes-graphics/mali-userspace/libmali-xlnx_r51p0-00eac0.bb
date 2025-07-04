DESCRIPTION = "libraries for Versal with Mali G78AE"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://EULA;md5=646a0c3fd8ffd4c63169926fbc5f62a8"

inherit features_check update-alternatives

REQUIRED_DISTRO_FEATURES = "wayland"
REQUIRED_MACHINE_FEATURES = "malig78ae"

REPO ?= "git://github.com/Xilinx/g78ae-userspace-binaries.git;protocol=https"
BRANCH ?= "r51p0-00eac0-1.rel03"
SRCREV ?= "3d52a4e78f1b2f5348df12ceaa7185e6519af34b"

SRC_URI = "${REPO};branch=${BRANCH}"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "libdrm wayland opencl-headers"

RDEPENDS:${PN} = " kernel-modules-g78ae"

do_compile[noexec] = "1"

PROVIDES += "virtual/libgles1 virtual/libgles2 virtual/libgles3 virtual/egl virtual/libgbm virtual/opencl-icd"
RREPLACES:${PN} = "libegl libglesv1-cm1 libgles2 libgles3 libglesv2-2 libgbm virtual-opencl-icd"
RPROVIDES:${PN} = "libegl libglesv1-cm1 libgles2 libgles3 libglesv2-2 libgbm virtual-opencl-icd"
RCONFLICTS:${PN} = "libegl libglesv1-cm1 libgles2 libgles3 libglesv2-2 libgbm virtual-opencl-icd"

do_install() {
    install_include_dir="${D}${includedir}"
    install_lib_dir="${D}${libdir}"
    install_pkgconfig_dir="${D}${libdir}/pkgconfig"
    install_vulkan_dir="${D}/${datadir}/vulkan"

    # libraries
    install -d ${install_lib_dir}
    cp -r ${S}/lib/* ${install_lib_dir}/.

    # headers
    install -d -m 0655 ${install_include_dir}/EGL
    install -m 0644 ${S}/include/EGL/*.h ${install_include_dir}/EGL/
    install -d -m 0655 ${install_include_dir}/GLES
    install -m 0644 ${S}/include/GLES/*.h ${install_include_dir}/GLES/
    install -d -m 0655 ${install_include_dir}/GLES2
    install -m 0644 ${S}/include/GLES2/*.h ${install_include_dir}/GLES2/
    install -d -m 0655 ${install_include_dir}/GLES3
    install -m 0644 ${S}/include/GLES3/*.h ${install_include_dir}/GLES3/
    install -d -m 0655 ${install_include_dir}/KHR
    install -m 0644 ${S}/include/KHR/*.h ${install_include_dir}/KHR/
    install -d -m 0655 ${install_include_dir}
    install -m 0644 ${S}/include/gbm.h ${install_include_dir}/

    # pkconfig
    install -d ${install_pkgconfig_dir}
    install -m 0644 ${S}/pkgconfig/egl.pc ${install_pkgconfig_dir}/egl.pc
    install -m 0644 ${S}/pkgconfig/glesv1_cm.pc ${install_pkgconfig_dir}/glesv1_cm.pc
    install -m 0644 ${S}/pkgconfig/glesv2.pc ${install_pkgconfig_dir}/glesv2.pc
    install -m 0644 ${S}/pkgconfig/gbm.pc ${install_pkgconfig_dir}/gbm.pc

    # vulkan icd and implicit_layer
    install -d ${install_vulkan_dir}/icd.d
    install -m 0644 ${S}/vulkan/icd.d/mali_icd.json ${install_vulkan_dir}/icd.d/mali_icd.json
    install -d ${install_vulkan_dir}/implicit_layer.d
    install -m 0644 ${S}/vulkan/implicit_layer.d/* ${install_vulkan_dir}/implicit_layer.d/
}

# Package gets renamed on the debian class, but we want to keep -xlnx
DEBIAN_NOAUTONAME:libmali-xlnx = "1"

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"

FILES:${PN}-dev = " \
	${includedir} \
	${libdir}/pkgconfig \
	${libdir}/libEGL.so \
	${libdir}/libgbm.so \
	${libdir}/libGLESv1_CM.so \
	${libdir}/libGLESv2.so \
	${libdir}/libmali.so \
	${libdir}/libOpenCL.so \
	"

FILES:${PN} += " \
	${datadir} \
	"

INSANE_SKIP:${PN} = "already-stripped dev-so"
