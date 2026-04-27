SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04270913/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04270913.tar.gz"
SRC_URI[sha256sum] = "bf0ec9106f89ae594ebb0f28c81c9063af532060ecc8e86f99e321e3035d3054"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
