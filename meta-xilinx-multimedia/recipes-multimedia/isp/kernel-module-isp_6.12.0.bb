# Define BRANCH and SRCREV for the 6.12 kernel.
SRC_BRANCH = "2025.2_freeze"
SRCREV = "4ef881f7d57a415076d288a75780b0b6d53d1b01"

include kernel-module-isp.inc

PV .= "+git"
