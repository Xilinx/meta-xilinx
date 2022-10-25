#specify BIF partition attributes required for BOOT.bin
BIF_FSBL_ATTR       ??= "fsbl"
BIF_BITSTREAM_ATTR  ??= ""
BIF_DEVICETREE_ATTR ??= "device-tree"
BIF_SSBL_ATTR       ??= "u-boot-xlnx"
BIF_PARTITION_ATTR  ??= "${BIF_FSBL_ATTR} ${BIF_BITSTREAM_ATTR} ${BIF_SSBL_ATTR} ${BIF_DEVICETREE_ATTR}"

#specify BIF partition attributes for FSBL
#bootloader is FSBL. Location where FSBL binary is present and dependency to build FSBL
BIF_PARTITION_ATTR[fsbl] ?= "bootloader"
BIF_PARTITION_IMAGE[fsbl] ?= "${RECIPE_SYSROOT}/boot/fsbl.elf"

#specify BIF partition attributes for u-boot
#Location where u-boot binary is present
BIF_PARTITION_IMAGE[u-boot-xlnx] ?= "${RECIPE_SYSROOT}/boot/u-boot.elf"

# specify BIF partition attributes for dtb
BIF_PARTITION_ATTR[device-tree] ?= "load=0x100000"
BIF_PARTITION_IMAGE[device-tree] ?= "${RECIPE_SYSROOT}/boot/devicetree/system-top.dtb"

# enable bitstream-Note this is not enabled by default (missing in BIF_PARTITION_ATTR)
BIF_PARTITION_IMAGE[bitstream] ?= "${RECIPE_SYSROOT}/boot/bitstream/download-${MACHINE}.bit"
