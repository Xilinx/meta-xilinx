SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04191450/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04191450.tar.gz"
SRC_URI[sha256sum] = "5809b4dc8ef41fbabe794c4e94c38c20fd3d4df8bc79240e04ae87ae1488cfae"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
