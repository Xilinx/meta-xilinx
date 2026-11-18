
BASE_DTS ?= "${@os.path.splitext(os.path.basename(d.getVar('CONFIG_DTFILE') or ''))[0] or 'system-top'}"
DTB_PATH ?= "boot/devicetree/"
DTB_FILE_NAME ?= ""

# This vairable is used for U-boot user specified dts from u-boot repo path
# u-boot-xlnx/arch/arm/dts/versal-vck190-revA-x-ebm-01-revA.dts.
# Note: .dts extension is not required for this variable settings.
# Example: UBOOT_USER_SPECIFIED_DTS = "versal-vck190-revA-x-ebm-01-revA"
UBOOT_USER_SPECIFIED_DTS ?= ""

EXTRA_OEMAKE += "${@'EXT_DTB=${RECIPE_SYSROOT}/${DTB_PATH}/${DTB_FILE_NAME}' if (d.getVar('DTB_FILE_NAME') != '' and d.getVar('UBOOT_USER_SPECIFIED_DTS') == '') else '' }"
EXTRA_OEMAKE += "${@'DEVICE_TREE=${UBOOT_USER_SPECIFIED_DTS}' if (d.getVar('UBOOT_USER_SPECIFIED_DTS') != '') else '' }"
EXTRA_OEMAKE:append:riscv64 = " ${@'OPENSBI=${RECIPE_SYSROOT}/share/opensbi/lp64/generic/firmware/fw_dynamic.bin'}"

python __anonymous () {
    #check if there are any dtb providers
    providerdtb = d.getVar("PREFERRED_PROVIDER_virtual/dtb")
    if providerdtb:
       d.appendVarFlag('do_configure', 'depends', ' virtual/dtb:do_populate_sysroot')
       if d.getVar("DTB_FILE_NAME") is not None:
           d.setVar('DTB_FILE_NAME', d.getVar('BASE_DTS')+ '.dtb')
}

DEFAULT_ARCH_DTS_DIR = ""
DEFAULT_ARCH_DTS_DIR:arm = "arm"
DEFAULT_ARCH_DTS_DIR:aarch64 = "arm"
DEFAULT_ARCH_DTS_DIR:riscv64 = "riscv"
ARCH_DTS_DIR ?= "${DEFAULT_ARCH_DTS_DIR}"

do_configure:prepend () {
    if [ -n "${UBOOT_USER_SPECIFIED_DTS}" ] && [ ! -f "${S}/arch/${ARCH_DTS_DIR}/dts/${UBOOT_USER_SPECIFIED_DTS}.dts" ]; then
        bbfatal "Uboot user specified dts (${UBOOT_USER_SPECIFIED_DTS}.dts) is not found in \
the${S}/arch/${ARCH_DTS_DIR}/dts directory, you need to patch dts file to u-boot source and use this configuration. \
For more details refer https://u-boot.readthedocs.io/en/latest/develop/devicetree/control.html#configuration "
    fi
}
