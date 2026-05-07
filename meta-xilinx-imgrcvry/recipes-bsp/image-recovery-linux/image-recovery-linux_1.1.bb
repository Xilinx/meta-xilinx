SUMMARY = "AMD Xilinx image-recovery Linux helper kernel for \
boot-image fall-back on supported boards."
DESCRIPTION = "Slim Linux kernel image used by the AMD Xilinx \
image-recovery boot path: when the primary boot partition is corrupt, \
the boot ROM falls back to this image so the system can still come up \
and re-flash a working primary image."
require image-recovery-linux.inc

require image-recovery-linux-source-${PV}.inc

PE = "1"
