SUMMARY = "ZCU208 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU208 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08140754/external/fwapp/zcu208-pl-bram-gpio-fw_2026.2_0814_1_08140754.tar.gz"

SRC_URI[sha256sum] = "a7b963f36fa7a978021ee565b2eca26370b2c86008179074e9ea6b73aad5cd3b"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu208-sdt-full = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu208-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu208/pl/${FW_DIR}"
