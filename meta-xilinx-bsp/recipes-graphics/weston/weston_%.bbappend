FILESEXTRAPATHS_prepend_zynqmp := "${THISDIR}/files:"

SRC_URI_append_zynqmp = " file://0001-gl-renderer.c-Use-gr-egl_config-to-create-pbuffer-su.patch"
