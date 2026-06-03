# plugin_uefi_capsule meson option was removed in fwupd 2.0.x; option no longer exists

# ESP mounting, not strictly necessary
RRECOMMENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'polkit', 'udisks2', '', d)}"
