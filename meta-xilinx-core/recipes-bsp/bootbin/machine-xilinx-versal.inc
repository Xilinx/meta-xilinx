#Extra args for versal
BOOTGEN_EXTRA_ARGS += "-dump bh"

# specify BIF common attribute for FSBL
BIF_COMMON_ATTR ?= ""

# specify BIF optional attributes
BIF_OPTIONAL_DATA ?= ""

#specify BIF partition attributes required for BOOT.bin
BIF_FSBL_ATTR       ??= "base-pdi plmfw psmfw"
BIF_ATF_ATTR        ??= "arm-trusted-firmware"
BIF_DEVICETREE_ATTR ??= "device-tree"
BIF_SSBL_ATTR       ??= "u-boot-xlnx"
BIF_PARTITION_ATTR  ??= "${BIF_FSBL_ATTR} ${BIF_DEVICETREE_ATTR} ${BIF_ATF_ATTR} ${BIF_SSBL_ATTR}"

# specify BIF partition attributes for base-pdi
BIF_PARTITION_ATTR[base-pdi] ?= "type=bootimage"
BIF_PARTITION_IMAGE[base-pdi] ?= "${RECIPE_SYSROOT}/boot/base-design.pdi"

# specify BIF partition attributes for plmfw
BIF_PARTITION_ATTR[plmfw] ?= "type=bootloader"
BIF_PARTITION_IMAGE[plmfw] ?= "${RECIPE_SYSROOT}/boot/plmfw.elf"

# specify BIF partition attributes for psmfw
BIF_PARTITION_ATTR[psmfw] ?= "core=psm"
BIF_PARTITION_IMAGE[psmfw] ?= "${RECIPE_SYSROOT}/boot/psmfw.elf"

# specify BIF partition attributes for device-tree
BIF_PARTITION_ATTR[device-tree] ?= "type=raw, load=0x1000"
BIF_PARTITION_IMAGE[device-tree] ?= "${RECIPE_SYSROOT}/boot/devicetree/system-top.dtb"
BIF_PARTITION_ID[device-tree] ?= "0x1c000000"

# specify BIF partition attributes for u-boot
BIF_PARTITION_ATTR[u-boot-xlnx] ?= "core=a72-0, exception_level = el-2"
BIF_PARTITION_IMAGE[u-boot-xlnx] ?= "${RECIPE_SYSROOT}/boot/u-boot.elf"
BIF_PARTITION_ID[u-boot-xlnx] ?= "0x1c000000"

# specify BIF partition attributes for arm-trusted-firmware
BIF_PARTITION_ATTR[arm-trusted-firmware] ?= "core=a72-0, exception_level = el-3, trustzone"
BIF_PARTITION_IMAGE[arm-trusted-firmware] ?= "${RECIPE_SYSROOT}/boot/arm-trusted-firmware.elf"
BIF_PARTITION_ID[arm-trusted-firmware] ?= "0x1c000000"
