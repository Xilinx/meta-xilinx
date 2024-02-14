require openamp-fw.inc

PROVIDES:append:armv7r = " openamp-fw-rpc-demo "

OPENAMP_FW_SRC_NAME = "rpc_demo.out"
OPENAMP_FW_NAME = "image_rpc_demo"
OPENAMP_WITH_PROXY = "ON"

# NOTE: BSP should have flag ESW_CFLAGS:append = " -DUNDEFINE_FILE_OPS=1 "
