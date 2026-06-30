SUMMARY = "VEK386 Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK386 Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/06291953/external/fwapp/vek386-pl-bram-gpio-fw_2026.1.1_0629_1_06291953.tar.gz"

SRC_URI[sha256sum] = "86fcd54a0f483bb1943ceef09c902f189653d7d79208fab413d5e63b0e44120f"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-mali-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm-vek386-sdt-seg = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vek386-pl-bram-gpio-fw"
FW_INSTALL_DIR = "vek386/pl/${FW_DIR}"
