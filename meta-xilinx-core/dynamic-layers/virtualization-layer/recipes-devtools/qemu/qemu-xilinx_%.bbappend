require ${@bb.utils.contains('DISTRO_FEATURES', 'vmsep', '${BPN}-package-split.inc', '', d)}
