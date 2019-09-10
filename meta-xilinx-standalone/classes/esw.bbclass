inherit pkgconfig cmake yocto-cmake-translation

LICENSE = "Proprietary"
LICFILENAME = "license.txt"
LIC_FILES_CHKSUM = "file://${ESWS}/${LICFILENAME};md5=c83c24ed6555ade24e37e6b74ade2629"

XILINX_RELEASE_VERSION = "v2019.2"

# We should move to an actual SRCREV eventually
SRCREV = "${AUTOREV}"
PV = "${XILINX_RELEASE_VERSION}+git${SRCPV}"
SRC_URI = "git://gitenterprise.xilinx.com/appanad/decoupling_embeddedsw.git;branch=master;protocol=https"

SRCREV_FORMAT = "src_decouple"

ESWS = "${WORKDIR}/git/"
S = "${ESWS}/${ESW_COMPONENT_SRC}"


# We need to put these per recipe probably, e.g. pmu on mb, fsbl on a53
COMPATIBLE_HOST_zynqmp-pmu = "microblaze.*-elf"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp-pmu = "zynqmp-pmu"

COMPATIBLE_HOST_cortexa53 = "aarch64.*-elf"
COMPATIBLE_MACHINE_cortexa53 = "cortexa53"

cmake_do_generate_toolchain_file_append() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
    include(CMakeForceCompiler)
    CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
    CMAKE_FORCE_CXX_COMPILER("${OECMAKE_CXX_COMPILER}" GNU)
EOF
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${B}/${ESW_COMPONENT_NAME} ${D}${libdir}
    install -m 0644  ${B}/include/*.h ${D}${includedir}
}


# Should these be LDFLAGS?
CFLAGS_append = " -Os -flto -ffat-lto-objects"


# We need to find the license file, which vaires depending on the component
# recurse a maximum of x times, could be fancier but it gets complicated since
# we dont know for certain we are running devtool or just externalsrc
python(){
    import os.path
    if bb.data.inherits_class('externalsrc', d) or d.getVar('EXTERNALSRC'):
        externalsrc = d.getVar('EXTERNALSRC')
        lic_file = d.getVar('LIC_FILES_CHKSUM', False)
        licpath=externalsrc
        for i in range(5):
            licpath=os.path.dirname(licpath)
            if os.path.isfile(licpath + '/' + d.getVar('LICFILENAME',True)):
                lic_file = lic_file.replace('${ESWS}',licpath)
                d.setVar('LIC_FILES_CHKSUM', lic_file)
                return
        bb.error("Couldn't find license file: %s, within directory %s or his parent directories" % (d.getVar('LICFILENAME',True), externalsrc))
}
