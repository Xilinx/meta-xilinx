inherit esw features_check

ESW_COMPONENT_SRC = "/lib/sw_services/xilffs/src/"
ESW_COMPONENT_NAME = "libxilffs.a"

PACKAGECONFIG ??= "read_only word_access"
PACKAGECONFIG[use_mkfs]   ="-DXILFFS_use_mkfs=ON,-DXILFFS_use_mkfs=OFF,,"
PACKAGECONFIG[read_only]  ="-DXILFFS_read_only=ON,-DXILFFS_read_only=OFF,,"
PACKAGECONFIG[word_access]="-DXILFFS_word_access=ON,-DXILFFS_word_access=OFF,,"

DEPENDS += "xilstandalone libxil"
