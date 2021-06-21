#specify BIF partition attributes required for BOOT.bin
BIF_PARTITION_ATTR ?= "fsbl bitstream u-boot"

#specify BIF partition attributes for FSBL
#bootloader is FSBL. Location where FSBL binary is present and dependency to build FSBL
BIF_PARTITION_ATTR[fsbl] ?= "bootloader"
BIF_PARTITION_IMAGE[fsbl] ?= "${DEPLOY_DIR_IMAGE}/fsbl-${MACHINE}.elf"
BIF_PARTITION_DEPENDS[fsbl] ?= "virtual/fsbl:do_deploy"

#specify BIF partition attributes for u-boot
#Location where u-boot binary is present
BIF_PARTITION_IMAGE[u-boot] ?= "${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.elf"
BIF_PARTITION_DEPENDS[u-boot] ?= "virtual/bootloader:do_deploy"

# enable bitstream-Note this is not enabled by default (missing in BIF_PARTITION_ATTR)
BIF_PARTITION_IMAGE[bitstream] ?= "${DEPLOY_DIR_IMAGE}/download-${MACHINE}.bit"
BIF_PARTITION_DEPENDS[bitstream] ?= "virtual/bitstream:do_deploy"
