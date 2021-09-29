require gdb-common.inc

inherit gettext

#LDFLAGS:append = " -s"
#export CFLAGS:append=" -L${STAGING_LIBDIR}"

# cross-canadian must not see this
PACKAGES =+ "gdbserver"
FILES:gdbserver = "${bindir}/gdbserver"

ALLOW_EMPTY:gdbserver:riscv64 = "1"
ALLOW_EMPTY:gdbserver:riscv32 = "1"

