# Include xen-boot-cmd.inc only if ENABLE_XEN_UBOOT_SCR is set from configuration
# file and xen enabled in DISTRO_FEATURES.
ENABLE_XEN_UBOOT_SCR ?= ""
include ${@'mxv-xen-boot-cmd.inc' if d.getVar('ENABLE_XEN_UBOOT_SCR') == '1' and bb.utils.contains('DISTRO_FEATURES', 'xen', True, False, d) else ''}

