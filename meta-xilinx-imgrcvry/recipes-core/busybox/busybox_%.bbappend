FILESEXTRAPATHS:prepend:imgrcvry := "${THISDIR}/busybox:"

IMGRCVRY_BUSYBOX_CFG = ""
IMGRCVRY_BUSYBOX_CFG:imgrcvry = "file://httpd_busybox.cfg"

SRC_URI += "${IMGRCVRY_BUSYBOX_CFG}"
