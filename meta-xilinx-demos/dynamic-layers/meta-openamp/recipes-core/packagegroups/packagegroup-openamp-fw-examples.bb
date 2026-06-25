SUMMARY = "Packagegroup pulling in the per-board OpenAMP RPU firmware \
demo payloads used by the AMD Xilinx OpenAMP example applications."
DESCRIPTION = "OpenAMP firmware examples"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

OPENAMP_PACKAGES = " \
    openamp-fw-echo-testd-freertos \
    openamp-fw-mat-muld-freertos \
    openamp-fw-rpc-demo-freertos \
    "

RDEPENDS:${PN} = "${OPENAMP_PACKAGES}"

INSANE_SKIP:${PN} += "arch"
