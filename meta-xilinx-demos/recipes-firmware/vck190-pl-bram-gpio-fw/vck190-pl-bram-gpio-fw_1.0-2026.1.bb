SUMMARY = "VCK190 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VCK190 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05290534/external/fwapp/vck190-pl-bram-gpio-fw_2026.1_0529_1_05290534.tar.gz"

SRC_URI[sha256sum] = "6c3bc47326c4f1e69ab6963b8196e7f1fffae0d0cd2eb2599612966826cab417"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vck190-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vck190-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vck190/pl/${FW_DIR}"
