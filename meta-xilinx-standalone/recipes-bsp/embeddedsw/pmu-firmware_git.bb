# Only should be used for development
DEFAULT_PREFERENCE = "-1"

require pmu-firmware.inc

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
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
        oe_runmake -C $(dirname $i) -s include
    done
    for i in $(cat seq.mak); do
        echo Libs Seq: $i
        oe_runmake -C $(dirname $i) -s libs
    done

    # the Makefile in ${B}/../misc/Makefile, does not handle CC, AR, AS, etc
    # properly. So do its job manually. Preparing the includes first, then libs.
    for i in $(ls ${BSP_TARGETS_DIR}/*/src/Makefile); do
        echo Include: $i
        oe_runmake -C $(dirname $i) -s include
    done
    for i in $(ls ${BSP_TARGETS_DIR}/*/src/Makefile); do
        echo Libs: $i
        oe_runmake -C $(dirname $i) -s libs
    done

    # --build-id=none is required due to linker script not defining a location for it.
    # Again, recipe-systoot include is necessary
    echo Construct: executable
    oe_runmake executable.elf CC_FLAGS="-MMD -MP -Wl,--build-id=none -I${STAGING_DIR_TARGET}/usr/include"

    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
