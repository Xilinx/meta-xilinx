#
# Copyright (C) 2025, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# This bbclass is inherited by zephyr recipes to copy lopper generated files to
# Zephyr board directory before do_configure task.

DEPENDS += "lopper-native dtc-native python3-dtc-native"

# Set default value to zephyr/boards/amd/ directory .
ZEPHYR_BOARD_VENDOR ??= "amd"

# Set default value to zephyr/boards/amd/<board>/<board>.dts file.
ZEPHYR_BOARD_DTS ??= "${ZEPHYR_BASE}/boards/${ZEPHYR_BOARD_VENDOR}/${BOARD}/${BOARD}.dts"

# Set default value to zephyr/boards/amd/mbv32/Kconfig.defconfig file.
ZEPHYR_BOARD_KCONFIG_DEFCONFIG ??= "${ZEPHYR_BASE}/boards/${ZEPHYR_BOARD_VENDOR}/${BOARD}/Kconfig.defconfig"

# Lopper transformed file variables.
CONFIG_DTFILE ??= "undefined"

# Lopper transformed file name is board_Kconfig.defconfig which needs to
# set in zephyr machine or multiconfig file.
ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG ??= "undefined"

python do_copy_lopper_zephyr_files() {
    zephyr_board_dts = d.getVar('ZEPHYR_BOARD_DTS')
    bb.debug(2, "AMD Zephyr Board DTS File: %s" % (zephyr_board_dts))

    zephyr_sdt_board_dts = d.getVar('CONFIG_DTFILE') or ''
    bb.debug(2, "CONFIG_DTFILE File: %s" % (zephyr_sdt_board_dts))

    zephyr_board_kconfig_defconfig = d.getVar('ZEPHYR_BOARD_KCONFIG_DEFCONFIG')
    bb.debug(2, "AMD Zephyr Board Kconfig.defconfig File: %s" % (zephyr_board_kconfig_defconfig))

    zephyr_sdt_board_kconfig_defconfig = d.getVar('ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG') or ''
    bb.debug(2, "ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG File: %s" % (zephyr_sdt_board_kconfig_defconfig))

    # Copy CONFIG_DTFILE to ZEPHYR_BOARD_DTS
    if os.path.isfile(zephyr_sdt_board_dts) and os.path.isfile(zephyr_board_dts):
        bb.utils.copyfile(zephyr_sdt_board_dts, zephyr_board_dts)
        bb.debug(2, "Copy DTS File from %s to %s" \
            % (zephyr_sdt_board_dts, zephyr_board_dts))

    # Copy ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG to ZEPHYR_BOARD_KCONFIG_DEFCONFIG
    if os.path.isfile(zephyr_sdt_board_kconfig_defconfig) and os.path.isfile(zephyr_board_kconfig_defconfig):
        bb.utils.copyfile(zephyr_sdt_board_kconfig_defconfig, zephyr_board_kconfig_defconfig)
        bb.debug(2, "Copy board_Kconfig.defconfig from %s to %s" \
            % (zephyr_sdt_board_kconfig_defconfig, zephyr_board_kconfig_defconfig))

}

do_configure[prefuncs] += "do_copy_lopper_zephyr_files"

def check_zephyr_variables(d):
    # Don't cache this, as the items on disk can change!
    d.setVar('BB_DONT_CACHE', '1')

    if not d.getVar('CONFIG_DTFILE'):
        raise bb.parse.SkipRecipe("CONFIG_DTFILE or ZEPHYR_BOARD_DTS is not defined.")

python() {
    # Need to allow bbappends to change the check
    check_zephyr_variables(d)
}
