SUMMARY = "VPK120 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VPK120 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08060629/external/fwapp/vpk120-pl-bram-gpio-fw_2026.2_0806_1_08060629.tar.gz"

SRC_URI[sha256sum] = "1432bb3064aa832c84cf517537b9106ba1036e2908f3a4fb9a9566c60402bc4c"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vpk120-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk120-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk120/pl/${FW_DIR}"
