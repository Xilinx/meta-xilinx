FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/Xilinx/qemu-bootbin-helper;protocol=https;branch=main \
    file://0001-amd_boot_image_loader-Zynq-ZynqMP-Do-not-load-images.patch \
    file://0002-amd_boot_image_loader-versal2vp-Add-versal-2vp-suppo.patch \
    file://0003-amd_boot_image_loader-versal2vp-add-a-versal2vpp-loa.patch \
    file://0004-amd_boot_image_loader-versal_2vp-Auto-switch-formats.patch \
    file://0005-amd_boot_image_loader-Add-file_type-field.patch \
    file://0006-README-document-the-boot-header-addresses-in-PRAM.patch \
    file://0007-qemu-system-amd-fpga-multiarch-Separate-configure_ve.patch \
    file://0008-qemu-system-amd-fpga-multiarch-add-versal2vp-support.patch \
    "
SRCREV = "8fd93d4afd68b6e2a1c52639deb2986e38ac5612"
