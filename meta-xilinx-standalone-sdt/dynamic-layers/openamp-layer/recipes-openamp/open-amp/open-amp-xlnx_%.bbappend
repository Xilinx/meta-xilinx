require ${@'${LAYER_PATH_openamp-layer}/vendor/xilinx/meta-xilinx-standalone-sdt/recipes-openamp/open-amp/open-amp-xlnx.inc' if d.getVar('XILINX_WITH_ESW') == 'sdt' else ''}
