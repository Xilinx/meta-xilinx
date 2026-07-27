SUMMARY = "ZC702 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZC702 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07270924/external/fwapp/zc702-pl-bram-gpio-fw_2026.2_0727_1_07270924.tar.gz"

SRC_URI[sha256sum] = "8675d6352816513a5ba91a5029a515b0d0731a2bd7cb52159e32e8e3d87df922"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa9thf-neon-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynq-zc702-sdt-full = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zc702-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zc702/pl/${FW_DIR}"
