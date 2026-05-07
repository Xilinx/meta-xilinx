SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05070618/external/packagegroup-openamp-fw-examples/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples_05070618.tar.gz"
SRC_URI[sha256sum] = "0510839feb3a5297b98ba3fbe2e2c8c45dce57f0005b12b66d6afb8d4510fb14"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "versal-vm-p-m1369-00-reva-x-prc-01"
