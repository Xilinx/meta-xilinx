FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_ultra96-zynqmp = "\
            file://mipi-config-Ultra96.cfg \
            "

