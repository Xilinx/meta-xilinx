MICROBLAZEPATCHES = ""
MICROBLAZEPATCHES:microblaze = "gdb-microblaze.inc"

# We don't have ptrace support for on-target microblaze GDB currently.  Need
# to use tcf-agent or other external debug interface.
MB_DOES_NOT_WORK = ""
MB_DOES_NOT_WORK:microblaze = "GDB is not currently supported on Microblaze."

PNBLACKLIST[gdb] = "${MB_DOES_NOT_WORK}"

require ${MICROBLAZEPATCHES}
