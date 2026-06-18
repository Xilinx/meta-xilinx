SUMMARY = "VPK180 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VPK180 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/03090000/external/fwapp/vpk180-pl-bram-gpio-fw_2026.1_0308_1_03090000.tar.gz"

SRC_URI[sha256sum] = "2c9f9e4de4bf9d647123c08cacf7756f5589121136ef7d3868ec9cedfd48885c"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vpk180-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vpk180-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vpk180/pl/${FW_DIR}"

