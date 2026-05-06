SUMMARY = "VEK280 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK280 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05060546/external/fwapp/vek280-pl-bram-gpio-fw_2026.1_0506_1_05060546.tar.gz"

SRC_URI[sha256sum] = "f6b6e772c1e3c0e009c3e67e9c721a47a6e4185289d22d420cffdc04a282138d"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vek280-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vek280-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vek280/pl/${FW_DIR}"
