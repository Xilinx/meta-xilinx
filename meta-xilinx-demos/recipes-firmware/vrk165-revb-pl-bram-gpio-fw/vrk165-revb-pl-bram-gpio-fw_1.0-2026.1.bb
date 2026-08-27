SUMMARY = "VRK165 RevB Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK165 RevB Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08262212/external/fwapp/vrk165-revb-pl-bram-gpio-fw_2026.1.1_0826_1_08262212.tar.gz"

SRC_URI[sha256sum] = "288e43cc451608f757a4ea587c7632add6fbe1f5d6f1f927d983615972525456"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk165-revb-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk165-revb-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk165-revb/pl/${FW_DIR}"

