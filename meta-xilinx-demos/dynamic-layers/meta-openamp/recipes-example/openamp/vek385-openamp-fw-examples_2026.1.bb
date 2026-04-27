SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04270913/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04270913.tar.gz"
SRC_URI[sha256sum] = "e29ffedc8ff072d2118e3ce8d23d09f6b44e222db70d72915dac403f34b676b9"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
