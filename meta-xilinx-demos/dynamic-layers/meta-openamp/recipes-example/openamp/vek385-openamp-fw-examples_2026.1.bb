SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04250544/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04250544.tar.gz"
SRC_URI[sha256sum] = "7e6744c542c27f811f24109d7d3fc487e8b86f3ad6836e942d7568837298c929"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
