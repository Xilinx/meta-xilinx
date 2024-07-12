# There are some configurations that can result in addition spec files being written
FILES:${PN}-dev:append:xilinx-standalone:class-target:baremetal-multilib-tc = "\
   ${libdir}/*.specs \
"
