# Minimum board memory required for each type
QB_MEM:microblaze = "-m 2G"
QB_MEM:zynq       = "-m 1G"
QB_MEM:zynqmp     = "-m 4G"
QB_MEM:versal     = "-m 16G"
QB_MEM:versal-net = "-m 16G"

# Zynq can't build qemu-xilinx or it's ptests
PTESTS_FAST:remove:zynq = "qemu-ptest"
PTESTS_PROBLEMS:append:zynq = " qemu-ptest"

# Microblaze has some limitations
# graphics is the primary one, bluez5 requires graphics support by default
PTESTS_FAST:remove:microblaze = "pango-ptest wayland-ptest qemu-ptest bluez5-ptest python3-bcrypt qemu"
PTESTS_PROBLEMS:append:microblaze = " pango-ptest wayland-ptest qemu-ptest bluez5-ptest python3-bcrypt qemu"

