# We need to load the ESW and related config components, only if XILINX_WITH_ESW
# is in defined in some way.  Since we don't know the configuration during
# layer.conf load time, we delay using a special bbclass that simply includes
# the META_XILINX_STANDLONE_CONFIG_PATH file.

include ${@'${META_XILINX_STANDLONE_SDT_CONFIG_PATH}' if d.getVar('XILINX_WITH_ESW') == 'sdt' else ''}
