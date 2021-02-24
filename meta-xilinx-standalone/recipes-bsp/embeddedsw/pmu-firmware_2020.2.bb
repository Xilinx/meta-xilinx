require pmu-firmware.inc

SRC_URI += " \
            file://0001-zynqmp_pmufw-Fix-reset-ops-for-assert.patch \
            file://0001-zynqmp_pmufw-Correct-structure-header-of-PmResetOps.patch \
            file://0001-sw_apps-versal_plm-Changes-to-ensure-versionless-bui.patch \
            file://0001-versal_psmfw-misc-Update-makefile-for-version-less-b.patch \
            file://zynqmp_pmufw-fixup.patch \
            file://makefile-skip-copy_bsp.sh.patch \
           "

do_configure() {
    # manually do the copy_bsp step first, so as to be able to fix up use of
    # mb-* commands
    . ${B}/../misc/copy_bsp.sh
    echo "$BSP_SEQUENTIAL_MAKEFILES" > ${B}/seq.mak
}

do_compile() {
    # First process the sequential items
    for i in $(cat seq.mak); do
        echo Include Seq: $i
        oe_runmake -C $(dirname $i) -s include ${@bsp_make_vars(d)}
    done
    for i in $(cat seq.mak); do
        echo Libs Seq: $i
        oe_runmake -C $(dirname $i) -s libs ${@bsp_make_vars(d)}
    done

    # the Makefile in ${B}/../misc/Makefile, does not handle CC, AR, AS, etc
    # properly. So do its job manually. Preparing the includes first, then libs.
    for i in $(ls ${BSP_TARGETS_DIR}/*/src/Makefile); do
        echo Include: $i
        oe_runmake -C $(dirname $i) -s include ${@bsp_make_vars(d)}
    done
    for i in $(ls ${BSP_TARGETS_DIR}/*/src/Makefile); do
        echo Libs: $i
        oe_runmake -C $(dirname $i) -s libs ${@bsp_make_vars(d)}
    done

    # --build-id=none is required due to linker script not defining a location for it.
    # Again, recipe-systoot include is necessary
    echo Construct: executable
    oe_runmake executable.elf ${@bsp_make_vars(d)} CC_FLAGS="-MMD -MP -Wl,--build-id=none -I${STAGING_DIR_TARGET}/usr/include"
}
