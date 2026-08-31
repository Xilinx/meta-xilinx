SUMMARY = "VRK165 RevB Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK165 RevB Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08311448/external/fwapp/vrk165-revb-pl-bram-gpio-fw_2026.1.1_0831_1_08311448.tar.gz"

SRC_URI[sha256sum] = "6773f6b28e16eb222697caa4b757cb53b61b59d1175e366593c390bc4ae79bc9"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk165-revb-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk165-revb-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk165-revb/pl/${FW_DIR}"

