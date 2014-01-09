#
# MicroBlaze Yocto Built Toolchain fails to compile shadow
# --------------------------------------------------------
# There is an issue with the GCC 4.8 toolchain built by Yocto which fails to
# correctly built the 'shadow' package due to an assembler error.
#
# {standard input}: Assembler messages:
# {standard input}:754: Error: operation combines symbols in different segments
#
# The current workaround for this issue is to build the entire repository
# without debug symbols, or to disable debug symbols only for shadow.
#

DEBUG_FLAGS_microblaze = ""
