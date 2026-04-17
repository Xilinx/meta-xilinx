SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04171450/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04171450.tar.gz"
SRC_URI[sha256sum] = "05265b1782daced80d27ee724a8a32d42cbac632b66ec25388013c1bf54ff2f3"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
