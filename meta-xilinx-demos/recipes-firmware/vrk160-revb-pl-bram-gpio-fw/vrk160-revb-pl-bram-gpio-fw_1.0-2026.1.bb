SUMMARY = "VRK160 REVB Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK160 REVB Segemented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08170047/external/fwapp/vrk160-revb-pl-bram-gpio-fw_2026.1.1_0816_1_08170047.tar.gz"
SRC_URI[sha256sum] = "0bda9ab1dc026b32a01c55f7bfd32e9969b2f708211fbe08ae554a9c132e6fd4"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk160-revb-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk160-revb-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk160-revb/pl/${FW_DIR}"

