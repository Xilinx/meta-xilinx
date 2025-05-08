# Include mxv-xen-boot-cmd.inc only if BOOTMODE = "xen" is set from configuration
# file and xen enabled in DISTRO_FEATURES.
include ${@'mxv-xen-boot-cmd.inc' if d.getVar('BOOTMODE') == 'xen' and bb.utils.contains('DISTRO_FEATURES', 'xen', True, False, d) else ''}
