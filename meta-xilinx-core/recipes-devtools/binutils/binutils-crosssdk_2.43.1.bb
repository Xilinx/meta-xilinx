SUMMARY = "GNU Binary Utilities (binutils) cross-SDK 2.43.1 \
(meta-xilinx backport)."
DESCRIPTION = "Cross-SDK build of GNU binutils 2.43.1 backported into \
meta-xilinx-core so the AMD Xilinx layers can be built against this \
toolchain version on Yocto releases that ship a different default."
require binutils-cross_${PV}.bb

inherit crosssdk

PN = "binutils-crosssdk-${SDK_SYS}"

SRC_URI += "file://0001-binutils-crosssdk-Generate-relocatable-SDKs.patch"

do_configure:prepend () {
	sed -i 's#/usr/local/lib /lib /usr/lib#${SDKPATHNATIVE}/lib ${SDKPATHNATIVE}/usr/lib /usr/local/lib /lib /usr/lib#' ${S}/ld/configure.tgt
}
