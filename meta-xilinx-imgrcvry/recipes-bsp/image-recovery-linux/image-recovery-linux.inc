DESCRIPTION = "Image Recovery"

inherit autotools-brokensep

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
