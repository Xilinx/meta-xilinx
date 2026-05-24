SUMMARY = "VEK386 Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK386 Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05240622/external/fwapp/vek386-pl-bram-gpio-fw_2026.1_0524_1_05240622.tar.gz"

SRC_URI[sha256sum] = "c45e816de651a1a0a668ec9de3c5918d17928f63e79ceae8cb4b1a7e8c712fc2"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-mali-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm-vek386-sdt-seg = "${MACHINE}"

