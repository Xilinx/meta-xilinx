SUMMARY = "VRK160 REVB Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK160 REVB Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08170744/external/fwapp/vrk160-revb-pl-bram-gpio-fw_2026.2_0817_1_08170744.tar.gz"
SRC_URI[sha256sum] = "7c1148f5fbf8093158c221044e22cf79d112bea9d1ba32eef6a04e21347b9ecb"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk160-revb-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk160-revb-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk160-revb/pl/${FW_DIR}"

