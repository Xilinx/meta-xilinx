SUMMARY = "Vpk360 Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "Vpk360 Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08311448/external/fwapp/vpk360-pl-bram-gpio-fw_2026.1.1_0831_1_08311448.tar.gz"

SRC_URI[sha256sum] = "cd85294b87add239ded5ff65cbb9863f74c8984eaca425fdcb89f75d8e8c28f5"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2vp-vpk360-sdt-seg = "${MACHINE}"
# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk360-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk360/pl/${FW_DIR}"
