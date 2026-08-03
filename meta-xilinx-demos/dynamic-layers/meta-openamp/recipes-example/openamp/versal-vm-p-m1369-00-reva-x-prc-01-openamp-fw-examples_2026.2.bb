SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VM-P-M1369-00 Rev A SOM on the X-PRC-01 \
carrier."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VM-P-M1369-00 Rev A \
SOM mounted on the X-PRC-01 carrier."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08030529/external/packagegroup-openamp-fw-examples/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples_08030529.tar.gz"
SRC_URI[sha256sum] = "2d37249507ee4d8d8fb61c86949d5f4d4591d1fc679e1cc254da3a6318176221"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "versal-vm-p-m1369-00-reva-x-prc-01"
