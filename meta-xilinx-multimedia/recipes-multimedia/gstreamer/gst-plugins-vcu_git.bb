SUMMARY = "Allegro DVT GStreamer plugin for the AMD VCU"

require gst-plugins-vcu.inc

VCU_VARIANT = "vcu"
VCU_CTRLSW_INCDIR = "vcu-ctrl-sw/include"

REQUIRED_MACHINE_FEATURES = "vcu"

DEPENDS += "vcu-ctrlsw"
RDEPENDS:${PN} += "libvcu-ctrlsw kernel-module-vcu"
