require gcc-xilinx-standalone-multilib.inc

# We want to use the stock multilib configs, when available
EXTRACONFFUNCS:xilinx-standalone:baremetal-multilib-tc = ""

EXTRA_OECONF:append:xilinx-standalone:baremetal-multilib-tc = " \
        --enable-multilib \
"
