# specify BIF common attribute for FSBL
BIF_COMMON_ATTR ?= ""

# specify BIF partition attributes required for BOOT.bin
BIF_PARTITION_ATTR ?= "fsbl pmu atf u-boot"

# specify BIF partition attributes for FSBL
# bootloader is FSBL. Location where FSBL binary is present and dependency to build FSBL
BIF_PARTITION_ATTR[fsbl] ?= "bootloader, destination_cpu=a53-0"
BIF_PARTITION_IMAGE[fsbl] ?= "${DEPLOY_DIR_IMAGE}/fsbl-${MACHINE}.elf"
BIF_PARTITION_DEPENDS[fsbl] ?= "virtual/fsbl:do_deploy"

# specify BIF partition attributes for PMU Firmware
# destination cpu for PMU. Location where PMU binary is present and dependency to build PMU Firmware
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-${MACHINE}"
BIF_PARTITION_ATTR[pmu] ?= "destination_cpu=pmu"
BIF_PARTITION_IMAGE[pmu] ?= "${DEPLOY_DIR_IMAGE}/${PMU_FIRMWARE_IMAGE_NAME}.elf"
BIF_PARTITION_DEPENDS[pmu] ?= "virtual/pmu-firmware:do_deploy"

# specify BIF partition attributes for ATF
# destination cpu for ATF, security levels. Location where ATF binary is present (dependency is not required as ATF is always built for ZU+, see zcu102-zynqmp.conf)
BIF_PARTITION_ATTR[atf] ?= "destination_cpu=a53-0,exception_level=el-3,trustzone"
BIF_PARTITION_IMAGE[atf] ?= "${DEPLOY_DIR_IMAGE}/arm-trusted-firmware.elf"
BIF_PARTITION_DEPENDS[atf] ?= "arm-trusted-firmware:do_deploy"

# specify BIF partition attributes for u-boot
# destination cpu for u-boot, security levels. Location where u-boot binary is present (dependency is not required as u-boot is always built for ZU+, see zcu102-zynqmp.conf)
BIF_PARTITION_ATTR[u-boot] ?= "destination_cpu=a53-0,exception_level=el-2"
BIF_PARTITION_IMAGE[u-boot] ?= "${DEPLOY_DIR_IMAGE}/u-boot-${MACHINE}.elf"
BIF_PARTITION_DEPENDS[u-boot] ?= "virtual/bootloader:do_deploy"

# specify BIF partition attributes for dtb
BIF_PARTITION_ATTR[dtb] ?= "load=0x100000"
BIF_PARTITION_IMAGE[dtb] ?= "${DEPLOY_DIR_IMAGE}/system.dtb"
BIF_PARTITION_DEPENDS[dtb] ?= "virtual/dtb:do_deploy"

# enable bitstream-Note this is not enabled by default (missing in BIF_PARTITION_ATTR)
BIF_PARTITION_ATTR[bitstream] ?= "destination_device=pl"
BIF_PARTITION_IMAGE[bitstream] ?= "${DEPLOY_DIR_IMAGE}/download-${MACHINE}.bit"
BIF_PARTITION_DEPENDS[bitstream] ?= "virtual/bitstream:do_deploy"
