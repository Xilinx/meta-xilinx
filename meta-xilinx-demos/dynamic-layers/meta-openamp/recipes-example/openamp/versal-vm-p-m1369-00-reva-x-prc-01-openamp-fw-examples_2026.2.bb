SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VM-P-M1369-00 Rev A SOM on the X-PRC-01 \
carrier."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VM-P-M1369-00 Rev A \
SOM mounted on the X-PRC-01 carrier."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07241050/external/packagegroup-openamp-fw-examples/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples_07241050.tar.gz"
SRC_URI[sha256sum] = "59c55ad1874fb21de08d6d26e2fa4a9ca57afd6d50a72e4a4131cf8ef56ef75b"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "versal-vm-p-m1369-00-reva-x-prc-01"
