inherit amd-zephyr-sdt

# To use SRC_URI_ZEPHYR pointing to zephyr-amd github tree instead of upstream
# zephyr tree we need add amd-zephyr-kernel-src_${PREFERRED_VERSION_zephyr-kernel}_${XILINX_RELEASE_VERSION}
# include file.
require amd-zephyr-kernel-src_${PREFERRED_VERSION_zephyr-kernel}_${XILINX_RELEASE_VERSION}.inc
