SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04220616/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04220616.tar.gz"
SRC_URI[sha256sum] = "b634cbf8475295718d3e3462c53ea87f25c5587ef8ac6b0c80436f31d92770f4"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
