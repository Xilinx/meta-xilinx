SUMMARY = "VEK280 Segemented Configuration(DFx Full) firmware using dfx_user_dts bbclass"
DESCRIPTION = "VEK280 Segemented Configuration(DFx Full) PL AXI BRAM, AXI GPIO and AXI UART firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = " \
    https://artifactory.xilinx.com/artifactory/petalinux-hwproj-dev/sdt/2024.2/2024.2_0910_1_09120003/external/vek280-pl-bram-gpio-fw/vek280-pl-bram-gpio-fw.tar.gz \
    "

SRC_URI[sha256sum] = "f5afe128ee7568fe08fd32ed5553c6f64d2a5544855642c6dcf97b3da7913b98"

COMPATIBLE_MACHINE:versal-vek280-sdt-seg = "${MACHINE}"
COMPATIBLE_MACHINE:versal-vek280-sdt-seg-ospi = "${MACHINE}"

# When do_upack is exectuted it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "vek280-pl-bram-gpio-fw"

# Workaround for CR-1205774
do_configure[prefuncs] += "copy_fw_files"
python copy_fw_files () {
    import shutil
    fw_file_src = d.getVar('WORKDIR') + '/' + d.getVar("FW_DIR")
    fw_file_dest = d.getVar('S')
    shutil.copytree(fw_file_src, fw_file_dest, dirs_exist_ok=True)
}
