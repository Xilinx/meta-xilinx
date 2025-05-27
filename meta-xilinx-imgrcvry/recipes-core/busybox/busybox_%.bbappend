FILESEXTRAPATHS:prepend:imgrcvry := "${THISDIR}/busybox:"

SRC_URI:append:imgrcvry = " file://httpd_busybox.cfg"
