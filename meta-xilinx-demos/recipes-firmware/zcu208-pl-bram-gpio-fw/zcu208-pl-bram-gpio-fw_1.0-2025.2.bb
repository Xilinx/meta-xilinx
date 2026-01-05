SUMMARY = "ZCU208 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU208 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/01050000/external/fwapp/zcu208-pl-bram-gpio-fw_2025.2.1_0104_1_01050000.tar.gz"

SRC_URI[sha256sum] = "f92b3e7e8c4898a8a54e2b24115ffcad8824541364b299139b735a348a75d72d"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu208-sdt-full = "${MACHINE}"

# When do_upack is exectuted it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu208-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu208/pl/${FW_DIR}"
