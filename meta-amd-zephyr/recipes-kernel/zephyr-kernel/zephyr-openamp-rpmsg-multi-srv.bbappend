inherit amd-zephyr-sdt

# To use SRC_URI_ZEPHYR pointing to zephyr-amd github tree instead of upstream
# zephyr tree we need add amd-zephyr-kernel-src_${PREFERRED_VERSION_zephyr-kernel}_${XILINX_RELEASE_VERSION}
# include file.
require amd-zephyr-kernel-src_${PREFERRED_VERSION_zephyr-kernel}_${XILINX_RELEASE_VERSION}.inc

SRC_URI_ZEPHYR_OPEN_AMP_SYS_REF = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"
BRANCH  = "2026"
SRCREV_open-amp-sys-ref = "f71199f8ff873d5ba9f3af18fef28c2807dc88d5"

ZEPHYR_SDT_SOC_KCONFIG = "${ZEPHYR_SOC_KCONFIG}"
ZEPHYR_SDT_SOC_KCONFIG_DEFCONFIG = "${ZEPHYR_SOC_KCONFIG_DEFCONFIG}"
ZEPHYR_SOC_VENDOR = "amd"
BOARD = "${ZEPHYR_BOARD}"
ZEPHYR_SOC_FAMILY = "${ZEPHYR_BOARD}"

EXTRA_OECMAKE += " \
    -DOVERLAY_CONFIG="${S}/open-amp-sys-ref/examples/zephyr/rpmsg_multi_services/boards/${BOARD}.conf" \
    -DCONFIG_GIC_SAFE_CONFIG=y \
    "

# The Versal Net and Versal Gen 2 machine overrides are also present when
# building for Cortex-A.  Enable this RPU application only in the Zephyr
# multiconfigs, where the generated RPU board is selected.
COMPATIBLE_MACHINE:versal-net = "${@d.getVar('MACHINE') if d.getVar('ZEPHYR_BOARD') == 'versalnet_rpu' else '^$'}"
COMPATIBLE_MACHINE:versal-2ve-2vm = "${@d.getVar('MACHINE') if d.getVar('ZEPHYR_BOARD') == 'versal2_rpu' else '^$'}"
