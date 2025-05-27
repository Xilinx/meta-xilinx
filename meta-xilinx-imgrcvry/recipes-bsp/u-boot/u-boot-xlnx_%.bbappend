FILESEXTRAPATHS:prepend:imgrcvry := "${THISDIR}/${PN}:"

PREBOOT_CFG_FILE ?= "file://preboot_cmd.cfg"
PREBOOT_CFG_FILE:versal-2ve-2vm ?= "file://preboot_cmd_versal_2ve_2vm.cfg"
SRC_URI:append:imgrcvry = " ${PREBOOT_CFG_FILE}"
