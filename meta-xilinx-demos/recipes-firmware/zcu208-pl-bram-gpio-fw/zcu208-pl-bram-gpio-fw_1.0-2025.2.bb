SUMMARY = "ZCU208 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU208 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/03180800/external/fwapp/zcu208-pl-bram-gpio-fw_2025.2.1_0317_1_03180800.tar.gz"

SRC_URI[sha256sum] = "892f6340f83aba397fb7de51601f7d655e0750a7d8fe26071802fed21e506b63"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu208-sdt-full = "${MACHINE}"

# When do_upack is exectuted it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu208-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu208/pl/${FW_DIR}"
