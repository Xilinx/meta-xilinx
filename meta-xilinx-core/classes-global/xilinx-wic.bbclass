# Prepend this layer's "scripts" directory to the PATH used by bitbake
# which will cause this layer's version of wic to be found before poky's
PATH:prepend = "${LAYERPATH_xilinx}/scripts:"
