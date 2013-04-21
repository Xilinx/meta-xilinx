# See include file for common information
include linux-xlnx.inc

# Kernel version and SRCREV correspond to:
#   github.com/Xilinx v14.5 tag
# If required for a custom layer:
#   Override SRCREV to point to a different commit in a bbappend file to
#   Or add patches as required

LINUX_VERSION = "3.8"
# This version doesn't build at moment, suspect defconfig at this point...
SRCREV = "6a0bedad60e2bca8d9b50bf81b9895e29e31a6d7"
