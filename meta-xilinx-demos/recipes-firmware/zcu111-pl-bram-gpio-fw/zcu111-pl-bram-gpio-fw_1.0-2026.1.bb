SUMMARY = "ZCU111 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU111 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05190927/external/fwapp/zcu111-pl-bram-gpio-fw_2026.1_0519_1_05190927.tar.gz"

SRC_URI[sha256sum] = "f9e24cf55581c1d95cc8a6fb6a451cf9eab39573ad0dcbe0ac799f80e067d85f"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu111-sdt-full = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu111-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu111/pl/${FW_DIR}"
