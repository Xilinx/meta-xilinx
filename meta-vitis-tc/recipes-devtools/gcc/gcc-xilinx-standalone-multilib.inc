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
rv32i-ilp32--a*zicsr*zifencei*zicbom;\
rv32ib-ilp32--a*zicsr*zifencei*zicbom;\
rv32ic-ilp32--a*zicsr*zifencei*zicbom;\
rv32icb-ilp32--a*zicsr*zifencei*zicbom;\
rv32im-ilp32--a*zicsr*zifencei*zicbom;\
rv32imb-ilp32--a*zicsr*zifencei*zicbom;\
rv32imc-ilp32--zicsr*zifencei*zicbom;\
rv32imcb-ilp32--a*zicsr*zifencei*zicbom;\
rv32imac-ilp32--b*zicbom;\
rv32imac_zicsr_zifencei-ilp32--b*zicbom;\
rv32imf_zicsr-ilp32f--b*a*zicsr*zifencei*zicbom;\
rv32imfc_zicsr-ilp32f--b*zifencei*zicbom;\
rv32imafc_zicsr-ilp32f--b*zifencei*zicbom;\
rv32imfdc_zicsr-ilp32d--b*a*zifencei*zicbom;\
rv64i-lp64--b*a*zicsr*zifencei*zicbom;\
rv64ic-lp64--b*a*zicsr*zifencei*zicbom;\
rv64im-lp64--b*a*zicsr*zifencei*zicbom;\
rv64imc-lp64--b*zicsr*zifencei*zicbom;\
rv64imac-lp64--b*zicsr*zifencei*zicbom;\
rv64imf_zicsr-lp64f-rv64imf-b*a*zifencei*zicbom;\
rv64imfc_zicsr-lp64f-rv64imfc-b*zifencei*zicbom;\
rv64imafc_zicsr-lp64f-rv64imafc-b*zifencei*zicbom;\
rv64imfdc_zicsr-lp64d-rv64imfdc-b*a*zifencei;\
rv64imafdcb_zicbom_zicsr_zifencei-lp64d--\
"

EXTRA_OECONF:append:xilinx-standalone:riscv32:baremetal-multilib-tc = " \
	--with-multilib-generator='${RISCV_MULTILIB}' \
	"
EXTRA_OECONF:append:xilinx-standalone:riscv64:baremetal-multilib-tc = " \
	--with-multilib-generator='${RISCV_MULTILIB}' \
	"
