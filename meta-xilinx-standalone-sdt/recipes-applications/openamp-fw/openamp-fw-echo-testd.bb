require openamp-fw.inc

PROVIDES:append:armv7r = " openamp-fw-echo-testd "
PROVIDES:append:armv8r = " openamp-fw-echo-testd "

OPENAMP_FW_NAME = "image_echo_test"
OPENAMP_XLNX_RECIPE = "open-amp-xlnx-echo"

RPROVIDES:${PN} += "openamp-fw-echo-testd"

python() {
    preferred = d.getVar('PREFERRED_PROVIDER_openamp-fw-echo-testd')
    if not preferred or preferred == d.getVar('PN'):
        d.setVar('BB_DONT_CACHE', '1')
        d.appendVar('PROVIDES', ' openamp-fw-echo-testd')
}
