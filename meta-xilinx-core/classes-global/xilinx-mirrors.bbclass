# This is added via a bbclass to ensure it's prepended very late.
# If this is done at include time, the own-mirror.bbclass will take
# precedence and can result in older version of in-development
# components from being loaded.  This ensures development mirrors
# are always processed BEFORE other mirrors.
PREMIRRORS:prepend = "${XILINX_MIRRORS} "
