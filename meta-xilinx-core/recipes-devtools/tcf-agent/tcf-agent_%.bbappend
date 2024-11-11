# On a Zynq system hardware breakpoints are limited, so used software
CFLAGS:append:zynq = " -DENABLE_HardwareBreakpoints=0"
