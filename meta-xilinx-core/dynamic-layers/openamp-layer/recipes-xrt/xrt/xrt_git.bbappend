# Use libmetal for systems with AIE
# For vck190 kind of devices
PACKAGE_ARCH:versal-ai-core = "${SOC_VARIANT_ARCH}"
EXTRA_OECMAKE:append:versal-ai-core = " -DXRT_AIE_BUILD=true -DFAL_LINUX=on"
TARGET_CXXFLAGS:append:versal-ai-core = " -DXRT_ENABLE_AIE"
DEPENDS:append:versal-ai-core = " libxaiengine aiefal"
RDEPENDS:${PN}:append:versal-ai-core = " libxaiengine aiefal"

# For vek280 kind of devices
PACKAGE_ARCH:versal-ai-edge = "${SOC_VARIANT_ARCH}"
EXTRA_OECMAKE:append:versal-ai-edge = " -DXRT_AIE_BUILD=true -DFAL_LINUX=on"
TARGET_CXXFLAGS:append:versal-ai-edge = " -DXRT_ENABLE_AIE"
DEPENDS:append:versal-ai-edge = " libxaiengine aiefal"
RDEPENDS:${PN}:append:versal-ai-edge = " libxaiengine aiefal"
