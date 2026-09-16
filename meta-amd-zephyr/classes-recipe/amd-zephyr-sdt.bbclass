#
# Copyright (C) 2025-2026, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# This bbclass is inherited by Zephyr recipes to consume Lopper-generated
# device-tree and Kconfig artifacts, with optional linker-script support,
# before the do_configure task.

DEPENDS += "lopper-native dtc-native python3-dtc-native"

# Set default value to zephyr/boards/amd/ directory .
ZEPHYR_BOARD_VENDOR ??= "amd"

# Set default value to zephyr/boards/amd/<board>/<board>.dts file.
ZEPHYR_BOARD_DTS ??= "${ZEPHYR_BASE}/boards/${ZEPHYR_BOARD_VENDOR}/${BOARD}/${BOARD}.dts"

# Set default value to zephyr/boards/amd/mbv32/Kconfig.defconfig file.
ZEPHYR_BOARD_KCONFIG_DEFCONFIG ??= "${ZEPHYR_BASE}/boards/${ZEPHYR_BOARD_VENDOR}/${BOARD}/Kconfig.defconfig"

# Lopper transformed file variables.
CONFIG_DTFILE ??= "undefined"
ZEPHYR_SDT_LINKER_SCRIPT ??= ""

# Lopper transformed file name is board_Kconfig.defconfig which needs to
# set in zephyr machine or multiconfig file.
ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG ??= "undefined"

def amd_zephyr_sdt_linker_args(d):
    linker = d.getVar('ZEPHYR_SDT_LINKER_SCRIPT') or ''
    if not linker:
        return ''
    return " -DCONFIG_HAVE_CUSTOM_LINKER_SCRIPT=y " \
           "-DCONFIG_CUSTOM_LINKER_SCRIPT='\"%s\"'" % linker

def amd_zephyr_sdt_linker_checksum(d):
    linker = d.getVar('ZEPHYR_SDT_LINKER_SCRIPT') or ''
    return '%s:True' % linker if linker else ''

EXTRA_OECMAKE:append = "${@amd_zephyr_sdt_linker_args(d)}"
do_configure[file-checksums] += "${@amd_zephyr_sdt_linker_checksum(d)}"

python do_copy_lopper_zephyr_files() {
    zephyr_board_dts = d.getVar('ZEPHYR_BOARD_DTS')
    bb.debug(2, "AMD Zephyr Board DTS File: %s" % (zephyr_board_dts))

    zephyr_sdt_board_dts = d.getVar('CONFIG_DTFILE') or ''
    bb.debug(2, "CONFIG_DTFILE File: %s" % (zephyr_sdt_board_dts))

    zephyr_board_kconfig_defconfig = d.getVar('ZEPHYR_BOARD_KCONFIG_DEFCONFIG')
    bb.debug(2, "AMD Zephyr Board Kconfig.defconfig File: %s" % (zephyr_board_kconfig_defconfig))

    zephyr_sdt_board_kconfig_defconfig = d.getVar('ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG') or ''
    bb.debug(2, "ZEPHYR_SDT_BOARD_KCONFIG_DEFCONFIG File: %s" % (zephyr_sdt_board_kconfig_defconfig))

    zephyr_sdt_linker_script = d.getVar('ZEPHYR_SDT_LINKER_SCRIPT') or ''
    if zephyr_sdt_linker_script:
        if not os.path.isfile(zephyr_sdt_linker_script):
            bb.fatal("ZEPHYR_SDT_LINKER_SCRIPT does not exist: %s" % \
                     zephyr_sdt_linker_script)
        bb.note("Using Lopper generated Zephyr linker script: %s" % \
                zephyr_sdt_linker_script)

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

# zephyr-toolchain-zephyr.inc sets INHIBIT_DEFAULT_DEPS, so no Yocto cross
# toolchain is built for these recipes and the exported CC/LD/AR/... point at
# binaries which do not exist. Zephyr itself ignores them and uses the SDK, but
# external module build systems invoked by Zephyr (for example Trusted
# Firmware-A, which is built with its own Makefile) honour them and fail
# instead of using the SDK passed through CROSS_COMPILE. Keep them out of the
# task environment.
#
# TEMPORARY WORKAROUND: this belongs in meta-zephyr's
# zephyr-toolchain-zephyr.inc and a patch has been submitted upstream:
# https://lists.yoctoproject.org/g/yocto-patches/topic/121279971
# Drop this once that patch is accepted and merged into meta-zephyr.
ZEPHYR_UNEXPORT_TOOLS ?= "AR AS CC CPP CXX LD NM OBJCOPY OBJDUMP RANLIB STRIP"

python() {
    # Need to allow bbappends to change the check
    check_zephyr_variables(d)

    if d.getVar('ZEPHYR_TOOLCHAIN_VARIANT') == 'zephyr':
        for tool in (d.getVar('ZEPHYR_UNEXPORT_TOOLS') or '').split():
            d.setVarFlag(tool, 'unexport', '1')
}
