#Extra args for versal
BOOTGEN_EXTRA_ARGS += "-dump bh"

#Add pdi dependency for versal
DEPENDS += "virtual/base-pdi"

# specify BIF common attribute for FSBL
BIF_COMMON_ATTR ?= ""

# specify BIF partition attributes required for BOOT.bin
BIF_PARTITION_ATTR ?= "pdi plm psm dtb atf u-boot"

# specify BIF partition attributes for pdi
BIF_PARTITION_ATTR[pdi] ?= "type=bootimage"
BIF_PARTITION_IMAGE[pdi] ?= "${RECIPE_SYSROOT}/boot/base-design.pdi"
BIF_PARTITION_DEPENDS[pdi] ?= "virtual/base-pdi:do_populate_sysroot"

# specify BIF partition attributes for plm
PLM_IMAGE_NAME ??= "plm-${MACHINE}"
BIF_PARTITION_ATTR[plm] ?= "type=bootloader"
BIF_PARTITION_IMAGE[plm] ?= "${DEPLOY_DIR_IMAGE}/${PLM_IMAGE_NAME}.elf"
BIF_PARTITION_DEPENDS[plm] ?= "virtual/plm:do_deploy"

# specify BIF partition attributes for psm
PSM_FIRMWARE_IMAGE_NAME ??= "psm-firmware-${MACHINE}"
BIF_PARTITION_ATTR[psm] ?= "core=psm"
BIF_PARTITION_IMAGE[psm] ?= "${DEPLOY_DIR_IMAGE}/${PSM_FIRMWARE_IMAGE_NAME}.elf"
BIF_PARTITION_DEPENDS[psm] ?= "virtual/psm-firmware:do_deploy"

# specify BIF partition attributes for dtb
BIF_PARTITION_ATTR[dtb] ?= "type=raw, load=0x1000"
BIF_PARTITION_IMAGE[dtb] ?= "${DEPLOY_DIR_IMAGE}/system.dtb"
BIF_PARTITION_DEPENDS[dtb] ?= "virtual/dtb:do_deploy"
BIF_PARTITION_ID[dtb] ?= "0x1c000000"

# specify BIF partition attributes for u-boot
BIF_PARTITION_ATTR[u-boot] ?= "core=a72-0, exception_level = el-2"
BIF_PARTITION_IMAGE[u-boot] ?= "${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.elf"
BIF_PARTITION_DEPENDS[u-boot] ?= "virtual/bootloader:do_deploy u-boot-zynq-scr:do_deploy"
BIF_PARTITION_ID[u-boot] ?= "0x1c000000"

# specify BIF partition attributes for atf
BIF_PARTITION_ATTR[atf] ?= "core=a72-0, exception_level = el-3, trustzone"
BIF_PARTITION_IMAGE[atf] ?= "${DEPLOY_DIR_IMAGE}/arm-trusted-firmware.elf"
BIF_PARTITION_DEPENDS[atf] ?= "virtual/arm-trusted-firmware:do_deploy"
BIF_PARTITION_ID[atf] ?= "0x1c000000"
