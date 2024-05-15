FILESEXTRAPATHS:prepend := "${THISDIR}/qtwayland:"
# file://0001-qwaylandwindow.cpp-Do-not-destroy-shell-suface-befor.patch 
# file://0002-Handle-maximize-minimize-fullscreen-in-xdg_shell_v6.patch
SRC_URI:append = " \
    file://0003-qwaylandeglwindow.cpp-Bind-the-context-before-callin.patch \
"
# file://0001-Fix-regression-in-QWaylandGlContext-makeCurrent-for-.patch

PACKAGECONFIG = " \
    wayland-client \
    wayland-server \
    wayland-egl \
    wayland-drm-egl-server-buffer \
"

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('DEPENDS', 'virtual/libgles1 virtual/libgles2 virtual/egl virtual/libgbm', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
