SUMMARY = "VRK160 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK160 Segemented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07300857/external/fwapp/vrk160-pl-bram-gpio-fw_2026.2_0730_1_07300857.tar.gz"

SRC_URI[sha256sum] = "70b3254450002d4405f1cbfa12f429e7afb4092843aaec804629a5e2aff78e0c"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk160-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk160-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vrk160/pl/${FW_DIR}"
