SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04280544/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04280544.tar.gz"
SRC_URI[sha256sum] = "17c9c5f51687654a007afbacd2b8577138733c96bfe78b01daf3bfa27a96377f"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
