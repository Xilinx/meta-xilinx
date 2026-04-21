SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04210604/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04210604.tar.gz"
SRC_URI[sha256sum] = "1f826dd098d427acd0286a612ba65073e64a07951d371aa6d64e9b9011d009b9"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
