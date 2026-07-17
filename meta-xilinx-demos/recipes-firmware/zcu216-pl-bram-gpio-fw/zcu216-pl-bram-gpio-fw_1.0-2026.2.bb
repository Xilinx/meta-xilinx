SUMMARY = "ZCU216 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU216 full  PL BRAM AND GPIO firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07170702/external/fwapp/zcu216-pl-bram-gpio-fw_2026.2_0717_1_07170702.tar.gz"

SRC_URI[sha256sum] = "02892d0730bd50cef52ebe3fef5eb6d394c8aad6aea0a1d87a846febb267ef87"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa53-common = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-zcu216-sdt-full = "${MACHINE}"

# When do_unpack is executed it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu216-pl-bram-gpio-fw"
FW_INSTALL_DIR = "zcu216/pl/${FW_DIR}"
