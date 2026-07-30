# Mali-400 specific glmark2 changes (machine specific).
#
# These changes are specific to the Mali-400 GPU and must only be applied on
# machines that enable the mali400 feature.
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " \
    file://0001-Make-RGB565-as-default-EGLconfig.patch \
    file://0001-Fix-Mali-400-precision-overflow-in-shader-calculatio.patch \
"
