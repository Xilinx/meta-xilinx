SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04260607/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04260607.tar.gz"
SRC_URI[sha256sum] = "88afc39ac1963c2f8906dc1692588fd648d32cb497b9c547ddb51ed0b10334db"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
