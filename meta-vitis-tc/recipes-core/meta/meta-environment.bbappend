# If this is a baremetal build, we want to further optimize the toolchain
# scripts
BAREMETAL_ENV = ""
BAREMETAL_ENV:xilinx-standalone:baremetal-multilib-tc = "plnx-baremetal-toolchain-scripts"

inherit ${BAREMETAL_ENV}

ORIGINAL_TARGET_ARCH := "${TARGET_ARCH}"
ORIGINAL_TARGET_VENDOR := "${TARGET_VENDOR}"
ORIGINAL_TARGET_SYS := "${TARGET_SYS}"
ORIGINAL_TARGET_PREFIX := "${TARGET_PREFIX}"
