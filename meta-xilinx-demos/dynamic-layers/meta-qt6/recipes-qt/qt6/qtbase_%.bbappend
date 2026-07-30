# The wayland server-buffer path needs GL_EXT_memory_object (GLES 3.1+); it does
# not build on GLES2. No PACKAGECONFIG knob exists, so force the cmake feature off.
EXTRA_OECMAKE:append = " -DFEATURE_wayland_vulkan_server_buffer=OFF"

RRECOMMENDS:${PN}-plugins:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
"
