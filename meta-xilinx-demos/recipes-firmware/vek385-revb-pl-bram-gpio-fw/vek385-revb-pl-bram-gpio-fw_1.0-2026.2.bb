SUMMARY = "VEK385-revB Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK385-revB Segemented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07210642/external/fwapp/vek385-revb-pl-bram-gpio-fw_2026.2_0721_1_07210642.tar.gz"

SRC_URI[sha256sum] = "55cf991936354e100dc9ce23344e06f0bce2a2ef305ba9c3a2d8d56785ec901c"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-mali-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm-vek385-revb-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vek385-revb-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vek385-revb/pl/${FW_DIR}"
