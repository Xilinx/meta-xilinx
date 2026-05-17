SUMMARY = "VRK165 Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK165 Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05170553/external/fwapp/vrk165-pl-bram-gpio-fw_2026.1_0517_1_05170553.tar.gz"

SRC_URI[sha256sum] = "0009f6fbf343e9da64ae6f8a20fc78153411c8c32f97801c7480841d1b8ff413"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk165-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk165-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk165/pl/${FW_DIR}"
