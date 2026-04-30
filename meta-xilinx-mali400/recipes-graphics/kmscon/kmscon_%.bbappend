FILESEXTRAPATHS:prepend := "${THISDIR}/kmscon:"

# Fix cross-compilation: genunifont is a native tool that needs
# native zlib, not the target zlib
DEPENDS += "zlib-native"
SRC_URI += "file://0001-meson-Use-native-zlib-for-genunifont.patch"
