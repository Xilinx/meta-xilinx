# Ultra96 Compiler Flags
ULTRA96_VERSION ?= "1"
YAML_COMPILER_FLAGS:append:ultra96-zynqmp = " \
    -DBOARD_SHUTDOWN_PIN=2 \
    -DBOARD_SHUTDOWN_PIN_STATE=0 \
    -DENABLE_MOD_ULTRA96 \
    ${@bb.utils.contains('ULTRA96_VERSION', '2', ' -DULTRA96_VERSION=2 ', ' -DULTRA96_VERSION=1 ', d)} \
    "
