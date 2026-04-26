SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04260607/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04260607.tar.gz"
SRC_URI[sha256sum] = "8976ec06710973d3344c5afe4e9f25f4006210e563574ed916c4b9420641e3a9"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
