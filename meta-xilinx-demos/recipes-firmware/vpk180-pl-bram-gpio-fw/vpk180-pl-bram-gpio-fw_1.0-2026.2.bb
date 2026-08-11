SUMMARY = "VPK180 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VPK180 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08110739/external/fwapp/vpk180-pl-bram-gpio-fw_2026.2_0811_1_08110739.tar.gz"

SRC_URI[sha256sum] = "3773149094138d086521c0a02647f21bb0baa5bc7b09ef5ea2d47cf9ae14acca"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vpk180-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk180-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk180/pl/${FW_DIR}"

