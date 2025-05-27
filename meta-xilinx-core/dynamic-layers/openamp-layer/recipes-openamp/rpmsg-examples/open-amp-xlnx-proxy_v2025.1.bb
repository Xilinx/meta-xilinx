require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
require ${LAYER_PATH_openamp-layer}/vendor/xilinx/meta-xilinx-standalone-sdt/recipes-openamp/open-amp/open-amp-xlnx-demos.inc

require open-amp-xlnx-demos_v2025.1.inc

PROVIDES += "open-amp-xlnx-proxy"

OPENAMP_APP_NAME = "rpc_demo"

RPROVIDES:${PN}-dbg += "open-amp-xlnx-proxy-dbg"
RPROVIDES:${PN}-dev += "open-amp-xlnx-proxy-dev"
RPROVIDES:${PN}-lic += "open-amp-xlnx-proxy-lic"
RPROVIDES:${PN}-src += "open-amp-xlnx-proxy-src"
RPROVIDES:${PN}-staticdev += "open-amp-xlnx-proxy-staticdev"
