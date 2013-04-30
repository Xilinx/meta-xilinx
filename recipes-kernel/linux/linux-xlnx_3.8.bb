# See include file for common information
include linux-xlnx.inc

# Kernel version and SRCREV correspond to:
#   github.com/Xilinx v14.5 tag
# If required for a custom layer:
#   Override SRCREV to point to a different commit in a bbappend file to
#   Or add patches as required

# This version doesn't build in Poky 1.3, patch required http://patches.openembedded.org/patch/38283/ 
LINUX_VERSION = "3.8"
SRCREV = "6a0bedad60e2bca8d9b50bf81b9895e29e31a6d7"
