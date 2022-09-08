# Set Board DTSI files for generic manchine configuration files based on HDF_MACHINE.

# microblaze-generic.conf uses HDF_MACHINE = "kc705-microblazeel", Hence set kc705-full dtsi file.
YAML_MAIN_MEMORY_CONFIG:microblaze-generic ?= "mig_7series_0"
YAML_CONSOLE_DEVICE_CONFIG:microblaze-generic ?= "axi_uartlite_0"
YAML_DT_BOARD_FLAGS:microblaze-generic ?= "{BOARD kc705-full}"

# zynq-generic.conf uses HDF_MACHINE = "zc702-zynq7", Hence set zc702 dtsi file.
YAML_DT_BOARD_FLAGS:zynq-generic ?= "{BOARD zc702}"

# zynqmp-generic.conf uses HDF_MACHINE = "zcu102-zynqmp", Hence set zcu102-rev1.0 dtsi file.
YAML_DT_BOARD_FLAGS:zynqmp-generic ?= "{BOARD zcu102-rev1.0}"

# versal-generic.conf file uses HDF_MACHINE = "vck190-versal", Hence set versal-vck190-reva-x-ebm-01-reva dtsi file.
YAML_DT_BOARD_FLAGS:versal-generic ?= "{BOARD versal-vck190-reva-x-ebm-01-reva}"

# versal-net-generic.conf uses HDF_MACHINE = "versal-net-generic", Hence set versal-net-ipp-rev1.9 dtsi file.
YAML_DT_BOARD_FLAGS:versal-net-generic ?= "{BOARD versal-net-ipp-rev1.9}"
