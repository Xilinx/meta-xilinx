SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04161528/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04161528.tar.gz"
SRC_URI[sha256sum] = "f91d0eb3ca1086e34ee9a4fbe03be0ef19746e393546518fa8e8cf1afc016f11"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
