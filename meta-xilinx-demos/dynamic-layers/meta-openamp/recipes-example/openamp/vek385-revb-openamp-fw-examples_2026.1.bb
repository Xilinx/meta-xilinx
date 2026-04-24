SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04240558/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04240558.tar.gz"
SRC_URI[sha256sum] = "a0d10caceffb8b2046a9982fc4f95708ae9eb3853f583a4d90f029272c72c0c5"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
