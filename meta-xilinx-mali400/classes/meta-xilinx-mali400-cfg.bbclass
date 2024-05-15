# We need to load the meta-xilinx-mali400 config components, only if "libmali"
# is in the distro features.  Since we don't know the distro flags during
# layer.conf load time, we delay using a special bbclass that simply includes
# the META_XILINX_MALI400_CONFIG_PATH file.

include ${@bb.utils.contains('DISTRO_FEATURES', 'libmali', '${META_XILINX_MALI400_CONFIG_PATH}', '', d)}
