FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-socket-downgrade-not-supported-logging-for-SO_PASSSE.patch"
