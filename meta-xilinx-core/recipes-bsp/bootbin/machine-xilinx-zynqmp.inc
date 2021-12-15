# specify BIF common attribute for FSBL
BIF_COMMON_ATTR ?= ""

# specify BIF partition attributes required for BOOT.bin
BIF_PARTITION_ATTR = "fsbl pmufw arm-trusted-firmware device-tree u-boot-xlnx"

# specify BIF partition attributes for FSBL
# bootloader is FSBL. Location where FSBL binary is present and dependency to build FSBL
BIF_PARTITION_ATTR[fsbl] ?= "bootloader, destination_cpu=a53-0"
BIF_PARTITION_IMAGE[fsbl] ?= "${RECIPE_SYSROOT}/boot/fsbl.elf"

# specify BIF partition attributes for PMU Firmware
# destination cpu for PMU. Location where PMU binary is present and dependency to build PMU Firmware
BIF_PARTITION_ATTR[pmufw] ?= "destination_cpu=pmu"
BIF_PARTITION_IMAGE[pmufw] ?= "${RECIPE_SYSROOT}/boot/pmufw.elf"

# specify BIF partition attributes for ATF
# destination cpu for ATF, security levels. Location where ATF binary is present (dependency is not required as ATF is always built for ZU+, see zcu102-zynqmp.conf)
BIF_PARTITION_ATTR[arm-trusted-firmware] ?= "destination_cpu=a53-0,exception_level=el-3,trustzone"
BIF_PARTITION_IMAGE[arm-trusted-firmware] ?= "${RECIPE_SYSROOT}/boot/arm-trusted-firmware.elf"

# specify BIF partition attributes for u-boot
# destination cpu for u-boot, security levels. Location where u-boot binary is present (dependency is not required as u-boot is always built for ZU+, see zcu102-zynqmp.conf)
BIF_PARTITION_ATTR[u-boot-xlnx] ?= "destination_cpu=a53-0,exception_level=el-2"
BIF_PARTITION_IMAGE[u-boot-xlnx] ?= "${RECIPE_SYSROOT}/boot/u-boot.elf"

# specify BIF partition attributes for dtb
BIF_PARTITION_ATTR[device-tree] ?= "destination_cpu=a53-0,load=0x100000"
BIF_PARTITION_IMAGE[device-tree] ?= "${RECIPE_SYSROOT}/boot/devicetree/system-top.dtb"

# enable bitstream-Note this is not enabled by default (missing in BIF_PARTITION_ATTR)
BIF_PARTITION_ATTR[bitstream] ?= "destination_device=pl"
BIF_PARTITION_IMAGE[bitstream] ?= "${DEPLOY_DIR_IMAGE}/download-${MACHINE}.bit"
