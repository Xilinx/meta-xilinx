SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04250544/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04250544.tar.gz"
SRC_URI[sha256sum] = "ffde07ba04f7ecca7257887df723aab5d7a7f06c9987adb4cec2d4ea95e0b853"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
