FILESEXTRAPATHS:prepend := "${THISDIR}/linux-xlnx:"

require linux-microblaze.inc

# Zynq default generic KMACHINE
COMPATIBLE_MACHINE:zynq = "zynq"
KMACHINE:zynq = "zynq"

# ZynqMP default generic KMACHINE
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
KMACHINE:zynqmp = "zynqmp"

# MicroBlaze default generic KMACHINE
KMACHINE:microblaze = "microblaze"
COMPATIBLE_MACHINE:microblaze = "microblaze"
