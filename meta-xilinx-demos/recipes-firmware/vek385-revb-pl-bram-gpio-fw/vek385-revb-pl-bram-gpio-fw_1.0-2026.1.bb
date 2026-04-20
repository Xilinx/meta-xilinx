SUMMARY = "VEK385-revB Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK385-revB Segemented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04200422/external/fwapp/vek385-revb-pl-bram-gpio-fw_2026.1_0420_1_04200422.tar.gz"

SRC_URI[sha256sum] = "7bf70f07268d62bcaae80eb4dd3ae63697e80bf7473616c9fa25818754337410"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-mali-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm-vek385-revb-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vek385-revb-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vek385-revb/pl/${FW_DIR}"
