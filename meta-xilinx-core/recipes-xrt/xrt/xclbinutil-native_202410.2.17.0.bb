SUMMARY  = "Xilinx Runtime(XRT) - minimal native build for xclbinutil"
DESCRIPTION = "Native build of xclbinutil using XRT codebase"

require xrt-${PV}.inc

FILESEXTRAPATHS:append := ":${THISDIR}/xrt"

LICENSE = "GPL-2.0-or-later & Apache-2.0 & MIT"
LIC_FILES_CHKSUM = " \
    file://../LICENSE;md5=de2c993ac479f02575bcbfb14ef9b485 \
    file://runtime_src/core/edge/drm/zocl/LICENSE;md5=7d040f51aae6ac6208de74e88a3795f8 \
    file://runtime_src/core/pcie/driver/linux/xocl/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://runtime_src/core/pcie/linux/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://runtime_src/core/tools/xbutil2/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://runtime_src/core/common/elf/LICENSE.txt;md5=b996e8b74af169e7e72e22d9e7d05b06 \
"

S = "${WORKDIR}/git/src"

inherit cmake pkgconfig native

DEPENDS = "libdrm-native ocl-icd-native boost-native rapidjson-native protobuf-native python3-pybind11-native systemtap-native"

EXTRA_OECMAKE += " -DCMAKE_BUILD_TYPE=Release -DCMAKE_EXPORT_COMPILE_COMANDS=ON"

do_install() {
    install -d ${D}${bindir}
    install -Dm 0755 ${WORKDIR}/build/runtime_src/tools/xclbinutil/xclbinutil ${D}${bindir}
}
