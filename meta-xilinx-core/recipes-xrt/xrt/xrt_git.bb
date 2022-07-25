SUMMARY  = "Xilinx Runtime(XRT) libraries"
DESCRIPTION = "Xilinx Runtime User Space Libraries and headers"

require xrt.inc

LICENSE = "GPLv2 & Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=da5408f748bce8a9851dac18e66f4bcf \
                    file://runtime_src/core/edge/drm/zocl/LICENSE;md5=7d040f51aae6ac6208de74e88a3795f8 \
                    file://runtime_src/core/pcie/driver/linux/xocl/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://runtime_src/core/pcie/linux/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
                    file://runtime_src/core/pcie/tools/xbutil/LICENSE;md5=d273d63619c9aeaf15cdaf76422c4f87 \
                    file://runtime_src/core/edge/tools/xbutil/LICENSE;md5=d273d63619c9aeaf15cdaf76422c4f87 "

S = "${WORKDIR}/git/src"

inherit cmake

BBCLASSEXTEND = "native nativesdk"

# util-linux is for libuuid-dev.
DEPENDS = "libdrm opencl-headers ocl-icd opencl-clhpp boost util-linux git-replacement-native protobuf-native protobuf elfutils libffi rapidjson"
RDEPENDS:${PN} = "bash ocl-icd boost-system boost-filesystem zocl"

EXTRA_OECMAKE += " \
		-DCMAKE_BUILD_TYPE=Release \
		-DCMAKE_EXPORT_COMPILE_COMANDS=ON \
		"
PACKAGE_ARCH:versal-ai-core = "${SOC_VARIANT_ARCH}"
EXTRA_OECMAKE:append:versal-ai-core = " -DXRT_AIE_BUILD=true"
TARGET_CXXFLAGS:append:versal-ai-core = " -DXRT_ENABLE_AIE"
DEPENDS:append:versal-ai-core = " libmetal libxaiengine aiefal"
RDEPENDS:${PN}:append:versal-ai-core = " libxaiengine aiefal"
EXTRA_OECMAKE:append:versal = " -DXRT_LIBDFX=true"
EXTRA_OECMAKE:append:zynqmp = " -DXRT_LIBDFX=true"
DEPENDS:append:versal = " libdfx"
DEPENDS:append:zynqmp = " libdfx"


FILES_SOLIBSDEV = ""
FILES:${PN} += "\
    ${libdir}/lib*.so \
    ${libdir}/lib*.so.* \
    ${libdir}/ps_kernels_lib \
    /lib/*.so* "
INSANE_SKIP:${PN} += "dev-so"

pkg_postinst_ontarget:${PN}() {
  #!/bin/sh
  if [ ! -e /etc/OpenCL/vendors/xilinx.icd ]; then
	echo "INFO: Creating ICD entry for Xilinx Platform"
	mkdir -p /etc/OpenCL/vendors
	echo "libxilinxopencl.so" > /etc/OpenCL/vendors/xilinx.icd
	chmod -R 755 /etc/OpenCL
  fi
}
