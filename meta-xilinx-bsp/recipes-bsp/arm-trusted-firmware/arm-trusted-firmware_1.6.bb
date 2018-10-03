ATF_VERSION = "2.0"
XILINX_RELEASE_VERSION = "versal"
BRANCH ?= "xilinx/versal"
SRCREV ?= "c11ab5af5a7fa38c57f18677f47d9963be73c313"

include arm-trusted-firmware.inc
PLATFORM_virt-versal = "versal"
ATF_CONSOLE_virt-versal ?= "pl011"

EXTRA_OEMAKE_append_virt-versal = "${@' VERSAL_CONSOLE=${ATF_CONSOLE}' if d.getVar('ATF_CONSOLE', True) != '' else ''}"

COMPATIBLE_MACHINE_virt-versal = "virt-versal"

LIC_FILES_CHKSUM = "file://license.rst;md5=e927e02bca647e14efd87e9e914b2443"
