require openamp-fw.inc

PROVIDES:append:armv7r = " openamp-fw-rpc-demo "
PROVIDES:append:armv8r = " openamp-fw-rpc-demo "

OPENAMP_FW_NAME = "image_rpc_demo"
OPENAMP_XLNX_RECIPE = "open-amp-xlnx-proxy"
OPENAMP_WITH_PROXY = "ON"

# NOTE: BSP should have flag ESW_CFLAGS:append = " -DUNDEFINE_FILE_OPS=1 "

RPROVIDES:${PN} += "openamp-fw-rpc-demo"

python() {
    preferred = d.getVar('PREFERRED_PROVIDER_openamp-fw-rpc-demo')
    if not preferred or preferred == d.getVar('PN'):
        d.setVar('BB_DONT_CACHE', '1')
        d.appendVar('PROVIDES', ' openamp-fw-rpc-demo')
}
