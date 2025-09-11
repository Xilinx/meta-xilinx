SUMMARY = "VRK160 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VRK160 Segemented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://petalinux.xilinx.com/sswreleases/rel-v2025.1/sdt/2025.1.1/2025.1.1_0910_1_09110322/external/vrk160-pl-bram-gpio-fw/vrk160-pl-bram-gpio-fw_2025.1.1_0910_1_09110322.tar.gz"

SRC_URI[sha256sum] = "9965fdcaea9591efb1e36ceda8b16735dafa07b1fa15ed467905af7673011ed3"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa72-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vrk160-sdt-seg = "${MACHINE}"

# When do_upack is exectuted it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vrk160-pl-bram-gpio-fw"
