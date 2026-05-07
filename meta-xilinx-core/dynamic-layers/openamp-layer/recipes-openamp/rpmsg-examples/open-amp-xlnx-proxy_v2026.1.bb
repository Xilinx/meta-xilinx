SUMMARY = "OpenAMP rpmsg RPC/proxy example application (xlnx)."
DESCRIPTION = "OpenAMP rpmsg RPC/proxy example app, demonstrating \
remote POSIX system-call proxying from a remote firmware back to Linux \
running on the APU."
require open-amp-xlnx-demos_v2026.1.inc

PROVIDES += "open-amp-xlnx-proxy"

OPENAMP_APP_NAME = "rpc_demo"

RPROVIDES:${PN}-dbg += "open-amp-xlnx-proxy-dbg"
RPROVIDES:${PN}-dev += "open-amp-xlnx-proxy-dev"
RPROVIDES:${PN}-lic += "open-amp-xlnx-proxy-lic"
RPROVIDES:${PN}-src += "open-amp-xlnx-proxy-src"
RPROVIDES:${PN}-staticdev += "open-amp-xlnx-proxy-staticdev"
