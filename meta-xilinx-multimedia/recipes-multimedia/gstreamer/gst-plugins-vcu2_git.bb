SUMMARY = "Allegro DVT GStreamer plugin for the AMD VCU2"

require gst-plugins-vcu.inc

VCU_VARIANT = "vcu2"
VCU_CTRLSW_INCDIR = "vcu2-ctrl-sw/include"

REQUIRED_MACHINE_FEATURES = "vcu2"

DEPENDS += "vcu2-ctrlsw"
RDEPENDS:${PN} += "libvcu2-ctrlsw kernel-module-vcu2"
