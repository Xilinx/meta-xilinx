# Avoid default, since we know our ISA.
EXTRA_OEMAKE:append = " PLATFORM_RISCV_ISA=${TUNE_RISCV_MARCH}"
