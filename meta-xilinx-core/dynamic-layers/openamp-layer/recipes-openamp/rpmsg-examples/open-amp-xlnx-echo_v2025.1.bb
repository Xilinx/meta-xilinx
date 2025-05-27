require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
require ${LAYER_PATH_openamp-layer}/vendor/xilinx/meta-xilinx-standalone-sdt/recipes-openamp/open-amp/open-amp-xlnx-demos.inc

require open-amp-xlnx-demos_v2025.1.inc

PROVIDES += "open-amp-xlnx-echo"

OPENAMP_APP_NAME = "echo"

RPROVIDES:${PN}-dbg += "open-amp-xlnx-echo-dbg"
RPROVIDES:${PN}-dev += "open-amp-xlnx-echo-dev"
RPROVIDES:${PN}-lic += "open-amp-xlnx-echo-lic"
RPROVIDES:${PN}-src += "open-amp-xlnx-echo-src"
RPROVIDES:${PN}-staticdev += "open-amp-xlnx-echo-staticdev"
