SUMMARY = "OpenAMP rpmsg echo example application (xlnx)."
DESCRIPTION = "OpenAMP rpmsg echo example app, paired with a remote \
firmware so that messages sent from Linux on the APU are echoed back \
from the RPU/MicroBlaze remote core."
require open-amp-xlnx-demos_v2025.1.inc

PROVIDES += "open-amp-xlnx-echo"

OPENAMP_APP_NAME = "echo"

RPROVIDES:${PN}-dbg += "open-amp-xlnx-echo-dbg"
RPROVIDES:${PN}-dev += "open-amp-xlnx-echo-dev"
RPROVIDES:${PN}-lic += "open-amp-xlnx-echo-lic"
RPROVIDES:${PN}-src += "open-amp-xlnx-echo-src"
RPROVIDES:${PN}-staticdev += "open-amp-xlnx-echo-staticdev"
