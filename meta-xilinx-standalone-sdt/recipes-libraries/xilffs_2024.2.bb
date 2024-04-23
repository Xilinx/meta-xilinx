inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilffs/src/"
ESW_COMPONENT_NAME = "libxilffs.a"

PACKAGECONFIG ??= "read_only word_access"
PACKAGECONFIG[use_mkfs]   ="-DXILFFS_use_mkfs=ON,-DXILFFS_use_mkfs=OFF,,"
PACKAGECONFIG[read_only]  ="-DXILFFS_read_only=ON,-DXILFFS_read_only=OFF,,"
PACKAGECONFIG[word_access]="-DXILFFS_word_access=ON,-DXILFFS_word_access=OFF,,"

EXTRA_OECMAKE += "${@'-DXILFFS_num_logical_vol=10 -DXILFFS_enable_multi_partition=ON' if d.getVar('ESW_MACHINE') == 'psv_pmc_0' else ''}"

DEPENDS += "xilstandalone libxil"
