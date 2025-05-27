# any kernel recipe with fragment support, that sets LINUX_VERSION to one of the tested
# values, will get the appropriate fragments included in their SRC_URI

LINUX_MAJOR = "${@(d.getVar('LINUX_VERSION') or "x.y").split('.')[0]}"
LINUX_MINOR = "${@(d.getVar('LINUX_VERSION') or "x.y").split('.')[1]}"

KERNEL_META_TYPE = "${@'yocto' if d.getVar('SRC_URI').find('type=kmeta') > 0 and d.getVar('SKIP_META_VIRT_KERNEL_INCLUDE') == None else 'none'}"

include ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'linux-${KERNEL_META_TYPE}_${LINUX_MAJOR}.${LINUX_MINOR}_virtualization.inc', '', d)}
