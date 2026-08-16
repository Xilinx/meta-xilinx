SUMMARY = "ZCU216 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU216 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08160737/external/fwapp/zcu216-pl-bram-gpio-fw_2026.2_0816_1_08160737.tar.gz"

SRC_URI[sha256sum] = "a5150ff1b112373a236f69855a5861c093f1be74e9e6be8d44b4d39d9ef0689b"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu216-sdt-full = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu216-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu216/pl/${FW_DIR}"
