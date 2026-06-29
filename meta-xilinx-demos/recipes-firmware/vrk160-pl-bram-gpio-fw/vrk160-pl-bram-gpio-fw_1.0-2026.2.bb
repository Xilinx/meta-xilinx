SUMMARY = "VRK160 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK160 Segemented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/06290539/external/fwapp/vrk160-pl-bram-gpio-fw_2026.2_0629_1_06290539.tar.gz"

SRC_URI[sha256sum] = "92e12d47fb9e3f238536a560db3c7ffbe7cea2cc157d2e6ebf05c11fea1718ff"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk160-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk160-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk160/pl/${FW_DIR}"
