# Enable DRM_LIMA support if DISTRO_FEATURES != 'libmali' and MACHINE_FEATURES mali400 is enabled
MALI_FEATURE = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', '', 'features/drm-lima/drm-lima.scc', d)}"
KERNEL_FEATURES:append = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', ' ${MALI_FEATURE}', '', d)}"
