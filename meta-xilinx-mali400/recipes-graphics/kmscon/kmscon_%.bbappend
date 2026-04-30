FILESEXTRAPATHS:prepend := "${THISDIR}/kmscon:"

# Fix cross-compilation: genunifont is a native tool that needs
# native zlib, not the target zlib
DEPENDS += "zlib-native"
SRC_URI += "file://0001-meson-Use-native-zlib-for-genunifont.patch"

# Software DRM rendering, pango fonts, systemd
# seat management. No OpenGL (avoids drm3d which conflicts with
# render-only GPUs like mali400).
PACKAGECONFIG = "video_drm2d font_pango multi_seat"
