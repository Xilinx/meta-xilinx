# Ultra96 device tree configuration
YAML_MAIN_MEMORY_CONFIG:ultra96 ?= "psu_ddr_0"
YAML_CONSOLE_DEVICE_CONFIG:ultra96 ?= "psu_uart_1"
YAML_DT_BOARD_FLAGS:ultra96 ?= "{BOARD avnet-ultra96-rev1}"

# ZedBoard device tree configuration
YAML_DT_BOARD_FLAGS:zedboard ?= "{BOARD zedboard}"
