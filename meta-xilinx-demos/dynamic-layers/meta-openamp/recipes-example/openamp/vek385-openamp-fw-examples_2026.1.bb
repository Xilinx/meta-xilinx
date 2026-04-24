SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04240558/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04240558.tar.gz"
SRC_URI[sha256sum] = "bc4de9810adcd661e398fc47627fb0815d47ffcae19e5631365062568563009a"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
