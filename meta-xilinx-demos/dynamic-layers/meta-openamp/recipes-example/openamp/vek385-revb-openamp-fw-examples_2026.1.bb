SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04220616/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04220616.tar.gz"
SRC_URI[sha256sum] = "86eec38a73b573ce8767f7a19a5b3fd07125ec70ded0ba0976288c9aeab4e3bf"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
