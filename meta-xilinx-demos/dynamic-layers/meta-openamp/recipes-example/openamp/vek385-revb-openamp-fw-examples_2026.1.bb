SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04200422/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04200422.tar.gz"
SRC_URI[sha256sum] = "8092c4af28f5c2733d2d3f04ac7135fb82a59e740efbc590001f3cc3734f8903"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
