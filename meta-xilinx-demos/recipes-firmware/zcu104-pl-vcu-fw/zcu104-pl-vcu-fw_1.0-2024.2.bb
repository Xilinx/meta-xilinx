SUMMARY = "ZCU104 full pl firmware using dfx_user_dts bbclass"
DESCRIPTION = "ZCU104 full  PL VCU firmware application"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "https://petalinux.xilinx.com/sswreleases/rel-v2024.2/sdt/2024.2/2024.2_1111_2_12062044/external/zcu104-pl-vcu-fw/zcu104-pl-vcu-fw_2024.2_1111_1.tar.gz"

SRC_URI[sha256sum] = "51fcec0e3d27cee434bed76bf99c9abbffcf8fc958fe624135cca61bc0c8ecbe"


COMPATIBLE_MACHINE:zynqmp-zcu104-sdt-full = "${MACHINE}"

# When do_upack is exectuted it will extract tar file with original directory
# name so set the FW_DIR pointing to pdi and dtsi files.
FW_DIR = "zcu104-pl-vcu-fw"

# fw files doesn't install on rootfs using dfx_user_dts bbclass using artifactory
# method. To workaround this issue we are using copy_fw_files pre-functions.
# copy_fw_files prefuncs needs to be called before find_firmware_file to update
# the firmware-name to ${PN}.
do_configure[prefuncs] =+ "copy_fw_files"
python copy_fw_files () {
    import shutil
    fw_file_src = d.getVar('WORKDIR') + '/' + d.getVar("FW_DIR")
    fw_file_dest = d.getVar('S')
    shutil.copytree(fw_file_src, fw_file_dest, dirs_exist_ok=True)
}
