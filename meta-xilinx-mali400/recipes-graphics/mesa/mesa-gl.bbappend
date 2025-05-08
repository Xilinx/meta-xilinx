PACKAGECONFIG:append = " dri3 gallium"

# If we're using libmali-xlnx, then we need to bring it in for the KHR/khrplatform.h file
DEPENDS .= "${@' libmali-xlnx' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else ''}"
RDEPENDS:libgl-mesa-dev .= "${@' libmali-xlnx-dev' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else ''}"

do_install:append () {
    if ${@'true' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else 'false'} ; then
        rm -rf ${D}${includedir}/KHR/*
    fi
}

# If we require libmali-xlnx, this becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH = "${@'${MALI_PACKAGE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
