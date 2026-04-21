SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04210604/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04210604.tar.gz"
SRC_URI[sha256sum] = "3b18a3cb18bbec0eac277991e2d8be30b539a0a76a4308f7ac942af3cebbf323"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
