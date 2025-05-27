DESCRIPTION = "OpenAMP supported packages"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# We don't support Zynq
COMPATIBLE_MACHINE:zynq = "$^"

inherit packagegroup features_check

REQUIRED_DISTRO_FEATURES = "openamp"

PACKAGES = "\
	packagegroup-openamp-echo-test \
	packagegroup-openamp-matrix-mul \
	packagegroup-openamp-rpc-demo \
	packagegroup-openamp \
	"

RDEPENDS:${PN}-echo-test = "rpmsg-echo-test"
RDEPENDS:${PN}-echo-test:append:zcu102-zynqmp = " openamp-fw-echo-testd"

RDEPENDS:${PN}-matrix-mul = "rpmsg-mat-mul"
RDEPENDS:${PN}-matrix-mul:append:zcu102-zynqmp = " openamp-fw-mat-muld"

RDEPENDS:${PN}-rpc-demo = "rpmsg-proxy-app"
RDEPENDS:${PN}-rpc-demo:append:zcu102-zynqmp = " openamp-fw-rpc-demo"

# Ensure that each of these kernel modules is present regardless of defconfig used
RDEPENDS:${PN}:append = " kernel-module-uio-pdrv-genirq kernel-module-rpmsg-char kernel-module-rpmsg-ctrl "

# ENABLE_OPENAMP_DTSI = 0 or empty: Build a dtbo
# ENABLE_OPENAMP_DTSI = 1: Bundle into the device-tree the openamp items
# ENABLE_OPENAMP_DTSI = 2 (!= 0, 1 or empty): Do nothing, assume openamp is already integrated into the device-tree
RDEPENDS:${PN}:append = " ${@'open-amp-device-tree' if not d.getVar('ENABLE_OPENAMP_DTSI') or d.getVar('ENABLE_OPENAMP_DTSI') == '0' else ''}"

RDEPENDS:${PN}:append = " \
	libmetal \
	libmetal-demos \
	open-amp \
	open-amp-demos \
	packagegroup-openamp-echo-test \
	packagegroup-openamp-matrix-mul \
	packagegroup-openamp-rpc-demo \
	"
