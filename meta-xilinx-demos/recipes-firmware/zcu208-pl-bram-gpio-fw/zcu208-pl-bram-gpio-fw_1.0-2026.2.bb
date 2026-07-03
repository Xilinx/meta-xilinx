SUMMARY = "ZCU208 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU208 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07022126/external/fwapp/zcu208-pl-bram-gpio-fw_2026.2_0702_1_07022126.tar.gz"

SRC_URI[sha256sum] = "d316cf909074e18011ea3c89b8187ff170b213e55747128d43d1ac73b6732807"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu208-sdt-full = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu208-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu208/pl/${FW_DIR}"
