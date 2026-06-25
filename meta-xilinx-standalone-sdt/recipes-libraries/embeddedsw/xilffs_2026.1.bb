SUMMARY = "AMD Xilinx FAT filesystem library (xilffs)."
DESCRIPTION = "Wrapper around the FatFs FAT filesystem library, \
integrated with the AMD Xilinx embeddedsw stack for use by baremetal \
firmware."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilffs/src/"
ESW_COMPONENT_NAME = "libxilffs.a"

XILFFS_RAMFS_INTERFACE ?= "2"
XILFFS_RAMFS_START_ADDR ?= "0x10000000"
XILFFS_RAMFS_SIZE ?= "1048576"

PACKAGECONFIG ??= "read_only word_access"
PACKAGECONFIG[use_mkfs]    = "-DXILFFS_use_mkfs=ON,-DXILFFS_use_mkfs=OFF,,"
PACKAGECONFIG[read_only]   = "-DXILFFS_read_only=ON,-DXILFFS_read_only=OFF,,"
PACKAGECONFIG[word_access] = "-DXILFFS_word_access=ON,-DXILFFS_word_access=OFF,,"
PACKAGECONFIG[ramfs]       = "-DXILFFS_fs_interface=${XILFFS_RAMFS_INTERFACE} -DXILFFS_ramfs_start_addr=${XILFFS_RAMFS_START_ADDR} -DXILFFS_ramfs_size=${XILFFS_RAMFS_SIZE},,,"

EXTRA_OECMAKE += "${@'-DXILFFS_num_logical_vol=10 -DXILFFS_enable_multi_partition=ON' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else ''}"

DEPENDS += "xilstandalone libxil"
