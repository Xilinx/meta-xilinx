SUMMARY = "OpenAMP firmware examples demo payload processing"
DESCRIPTION = "OpenAMP firmware examples demo payload processing"
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04191450/external/packagegroup-openamp-fw-examples/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples_04191450.tar.gz"
SRC_URI[sha256sum] = "596c1bc4fa4f9467f007157b3c0cff083d5a18830cb7c0a40414bb0af7edb251"

#OPENAMPFW_PKGDIR:versal = "versal-vm-p-m1369-00-reva-x-prc-01-reva-md_packagegroup-openamp-fw-examples"
require conf/includes/openamp-fw-example.inc
