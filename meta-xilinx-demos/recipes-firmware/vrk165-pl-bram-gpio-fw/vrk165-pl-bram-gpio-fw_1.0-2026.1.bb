SUMMARY = "VRK165 Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK165 Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08031517/external/fwapp/vrk165-pl-bram-gpio-fw_2026.1.1_0803_1_08031517.tar.gz"

SRC_URI[sha256sum] = "24b2a2d60fc84dc7675517c74ffe4ca5b5ff68cc907120b783101a4ae80a20fd"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk165-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk165-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk165/pl/${FW_DIR}"
