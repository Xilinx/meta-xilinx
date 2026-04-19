SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04181750/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04181750.tar.gz"
SRC_URI[sha256sum] = "f9a387e5925ac47a5e2ba40b2337d2f641ff372ac33f9b54b0970270bcc4ba08"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
