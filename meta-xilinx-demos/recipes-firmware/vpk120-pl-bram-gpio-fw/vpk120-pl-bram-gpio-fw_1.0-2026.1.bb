SUMMARY = "VPK120 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VPK120 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05120610/external/fwapp/vpk120-pl-bram-gpio-fw_2026.1_0512_1_05120610.tar.gz"

SRC_URI[sha256sum] = "e2949fb5b824134d59aab4ab7d6ee879f208dc3905a1714a68bbc013614f1a08"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vpk120-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk120-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk120/pl/${FW_DIR}"
