SUMMARY = "VPK120 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VPK120 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08020753/external/fwapp/vpk120-pl-bram-gpio-fw_2026.2_0802_1_08020753.tar.gz"

SRC_URI[sha256sum] = "173cdcfa67b7d19c6fd6067f4ccd3daef65a53b67d368c1c0bd70c9c49c87f38"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vpk120-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk120-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk120/pl/${FW_DIR}"
