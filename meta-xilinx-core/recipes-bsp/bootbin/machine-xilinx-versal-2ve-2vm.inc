# Extra args for versal_2ve_2vm
BOOTGEN_EXTRA_ARGS += "-dump bh"

# Specify BIF common attribute for FSBL
BIF_COMMON_ATTR ?= ""

# Specify BIF optional attributes
BIF_OPTIONAL_DATA ?= ""

# Specify BIF partition attributes required for BOOT.bin
BIF_FSBL_ATTR       ??= "base-pdi plmfw"
BIF_ATF_ATTR        ??= "arm-trusted-firmware"
BIF_OPTEE_ATTR      ??= "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)}"
BIF_ASU_ATTR        ??= ""
BIF_DEVICETREE_ATTR ??= "device-tree"
BIF_SSBL_ATTR       ??= "u-boot-xlnx"
BIF_PARTITION_ATTR  ??= "${BIF_FSBL_ATTR} ${BIF_ASU_ATTR} ${BIF_DEVICETREE_ATTR} ${BIF_ATF_ATTR} ${BIF_OPTEE_ATTR} ${BIF_SSBL_ATTR}"

# Specify BIF partition attributes for base-pdi
BIF_PARTITION_ATTR[base-pdi] ?= "type=bootimage"
BIF_PARTITION_IMAGE[base-pdi] ?= "${RECIPE_SYSROOT}/boot/base-design.pdi"

# Specify BIF partition attributes for plmfw
BIF_PARTITION_ATTR[plmfw] ?= "type=bootloader"
BIF_PARTITION_IMAGE[plmfw] ?= "${RECIPE_SYSROOT}/boot/plmfw.elf"

# Specify BIF partition attributes for device-tree
BIF_PARTITION_ATTR[device-tree] ?= "type=raw, load=0x1000000"
BIF_PARTITION_IMAGE[device-tree] ?= "${RECIPE_SYSROOT}/boot/devicetree/system-top.dtb"
BIF_PARTITION_ID[device-tree] ?= "0x1c000000"

# Specify BIF partition attributes for u-boot
BIF_PARTITION_ATTR[u-boot-xlnx] ?= "core=a78-0, cluster=0, exception_level = el-2"
BIF_PARTITION_IMAGE[u-boot-xlnx] ?= "${RECIPE_SYSROOT}/boot/u-boot.elf"
BIF_PARTITION_ID[u-boot-xlnx] ?= "0x1c000000"

# Specify BIF partition attributes for arm-trusted-firmware
BIF_PARTITION_ATTR[arm-trusted-firmware] ?= "core=a78-0, cluster=0, exception_level = el-3, trustzone"
BIF_PARTITION_IMAGE[arm-trusted-firmware] ?= "${RECIPE_SYSROOT}/boot/arm-trusted-firmware.elf"
BIF_PARTITION_ID[arm-trusted-firmware] ?= "0x1c000000"

# Specify BIF partition attributes for optee
BIF_PARTITION_ATTR[optee-os] ?= "core=a78-0, cluster=0, exception_level = el-1, trustzone"
BIF_PARTITION_IMAGE[optee-os] ?= "${RECIPE_SYSROOT}/${nonarch_base_libdir}/firmware/tee.elf"
BIF_PARTITION_ID[optee-os] ?= "0x1c000000"

# Specify BIF partition attributes for asufw
BIF_PARTITION_ATTR[asufw] ?= "core=asu"
BIF_PARTITION_IMAGE[asufw] ?= "${RECIPE_SYSROOT}/boot/asufw.elf"

# Force the newly created CDO file to be made available in the deploy directory
do_extract_cdo() {
    :
}

do_extract_cdo[noexec] = "1"
do_extract_cdo[depends] += "extract-cdo:do_deploy"

addtask do_extract_cdo before do_build after do_deploy
