require recipes-devtools/qemu/qemu-native.inc
require qemu-xilinx.inc

DEPENDS = "glib-2.0-native zlib-native"

SRC_URI_remove = "file://0010-fix-libcap-header-issue-on-some-distro.patch"
SRC_URI_remove = "file://0011-cpus.c-Add-error-messages-when-qemi_cpu_kick_thread-.patch"

do_install_append(){
       rm -rf ${D}${datadir}/icons
}
