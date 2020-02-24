COMPATIBLE_OS_xilinx-standalone = "${TARGET_OS}"

TOOLCHAIN_TARGET_TASK_xilinx-standalone = "${@multilib_pkg_extend(d, 'packagegroup-newlib-standalone-sdk-target')}"
