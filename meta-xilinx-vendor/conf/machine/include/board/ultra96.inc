# Ultra96 items that need to be configured from zynqmp-generic
KERNEL_DEVICETREE:ultra96 = "xilinx/zynqmp-zcu100-revC.dtb"

# Affects meta-xilinx-tools xsctyaml.bbclass related items
YAML_SERIAL_CONSOLE_STDIN:ultra96 ?= "psu_uart_1"
YAML_SERIAL_CONSOLE_STDOUT:ultra96 ?= "psu_uart_1"

YAML_COMPILER_FLAGS:append:ultra96 = " -DBOARD_SHUTDOWN_PIN=2 -DBOARD_SHUTDOWN_PIN_STATE=0 "

# Enable bluetooth and wifi module
MACHINE_ESSENTIAL_EXTRA_RDEPENDS:append:ultra96 = " linux-firmware-wl18xx linux-firmware-ti-bt-wl180x"

