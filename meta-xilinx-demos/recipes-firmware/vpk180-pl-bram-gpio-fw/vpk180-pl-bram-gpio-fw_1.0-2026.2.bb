SUMMARY = "VPK180 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VPK180 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07251404/external/fwapp/vpk180-pl-bram-gpio-fw_2026.2_0725_1_07251404.tar.gz"

SRC_URI[sha256sum] = "33acde5ca4daa3156529f8f27a00ed632c287ea5775d8a05dd1e9eaf5472b9e7"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vpk180-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk180-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk180/pl/${FW_DIR}"

