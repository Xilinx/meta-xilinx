# Both arm and armv*r overrides are set w/ cortex r5
# So only set rmprofile if armv*r is defined.
ARM_PROFILE = "aprofile"
ARM_PROFILE:armv7r = "rmprofile"
ARM_PROFILE:armv8r = "rmprofile"
EXTRA_OECONF:append:xilinx-standalone:arm:baremetal-multilib-tc = " \
        --with-multilib-list=${ARM_PROFILE} \
        "

# RISC V configuration
RISCV_MULTILIB = "\
rv32i-ilp32--;rv32ic-ilp32--;rv32im-ilp32--;rv32imc-ilp32--;rv32imac-ilp32--;rv32e-ilp32e--;\
rv32imf-ilp32f--;rv32imfc-ilp32f--;rv32imafc-ilp32f--;rv32imfdc-ilp32d--;\
rv64i-lp64--;rv64ic-lp64--;rv64im-lp64--;rv64imc-lp64--;rv64imac-lp64--;\
rv64imf-lp64f--;rv64imfc-lp64f--;rv64imafc-lp64f--;rv64imfdc-lp64d--\
"

EXTRA_OECONF:append:xilinx-standalone:riscv32:baremetal-multilib-tc = " \
	--with-multilib-generator='${RISCV_MULTILIB}' \
	"
EXTRA_OECONF:append:xilinx-standalone:riscv64:baremetal-multilib-tc = " \
	--with-multilib-generator='${RISCV_MULTILIB}' \
	"
