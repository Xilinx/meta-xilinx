SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04230645/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04230645.tar.gz"
SRC_URI[sha256sum] = "5a38aebf0e692c22c1f474064b65a0ad0cf74175f59049b05b9acdbbdd3963fe"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
