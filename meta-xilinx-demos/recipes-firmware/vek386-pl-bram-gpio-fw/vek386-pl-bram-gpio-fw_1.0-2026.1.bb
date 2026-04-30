SUMMARY = "VEK386 Segmented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK386 Segmented Configuration(DFx Full) PL AXI BRAM and AXI GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04300623/external/fwapp/vek386-pl-bram-gpio-fw_2026.1_0430_1_04300623.tar.gz"

SRC_URI[sha256sum] = "6b06a69a76c207bd9f6541f0f97812f2da5cbb684944525d7b342dc340f41ba4"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-mali-common = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm-vek386-sdt-seg = "${MACHINE}"

