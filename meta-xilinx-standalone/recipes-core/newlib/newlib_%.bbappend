COMPATIBLE_HOST:xilinx-standalone = ".*-elf"
COMPATIBLE_HOST:arm:xilinx-standalone = "[^-]*-[^-]*-eabi"

# newlib 4.6.0 changed __stack_chk_fail to call write(2,...) and raise(SIGABRT)
# which are unavailable in bare-metal environments, causing link failures.
# Remove those calls so __stack_chk_fail simply calls _exit(127).
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append:xilinx-standalone = " file://0001-newlib-ssp-Remove-write-and-raise-calls-from-stack_c.patch"

EXTRA_OECONF:append:xilinx-standalone = " \
	--enable-newlib-io-c99-formats \
	--enable-newlib-io-long-long \
	--enable-newlib-io-float \
	--enable-newlib-io-long-double \
"

# Avoid trimmping CCARGS from CC by newlib configure
do_configure:prepend:xilinx-standalone(){
    export CC_FOR_TARGET="${CC}"
}
