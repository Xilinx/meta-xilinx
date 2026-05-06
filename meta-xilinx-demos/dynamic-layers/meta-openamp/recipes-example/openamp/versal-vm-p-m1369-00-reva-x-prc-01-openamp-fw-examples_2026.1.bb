SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05060546/external/packagegroup-openamp-fw-examples/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples_05060546.tar.gz"
SRC_URI[sha256sum] = "2ef0f55b3e7781431a065fcfa6943cda88a9143bf128ec5ec412764419dcea63"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "versal-vm-p-m1369-00-reva-x-prc-01"
