SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04230645/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04230645.tar.gz"
SRC_URI[sha256sum] = "cff6b85e7d619a8560f24650eb8a582ee16f3a00f5d797361ccbd050334110bf"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
