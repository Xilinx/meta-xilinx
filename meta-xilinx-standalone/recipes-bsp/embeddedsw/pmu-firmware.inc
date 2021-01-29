DESCRIPTION = "PMU Firmware"

PROVIDES = "virtual/pmu-firmware"

require embeddedsw.inc

SRC_URI += "file://fix-zynqmp-assert.patch"

inherit deploy

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "microblaze-pmu"

S = "${WORKDIR}/git"
B = "${S}/lib/sw_apps/zynqmp_pmufw/src"

# The makefile does not handle parallelization
PARALLEL_MAKE = ""

do_configure() {
    # manually do the copy_bsp step first, so as to be able to fix up use of
    # mb-* commands
    ${B}/../misc/copy_bsp.sh
}

COMPILER = "${CC}"
COMPILER_FLAGS = "-O2 -c"
EXTRA_COMPILER_FLAGS = "-g -Wall -Wextra -Os -flto -ffat-lto-objects"
ARCHIVER = "${AR}"

BSP_DIR ?= "${B}/../misc/zynqmp_pmufw_bsp"
BSP_TARGETS_DIR ?= "${BSP_DIR}/psu_pmu_0/libsrc"

def bsp_make_vars(d):
    s = ["COMPILER", "CC", "COMPILER_FLAGS", "EXTRA_COMPILER_FLAGS", "ARCHIVER", "AR", "AS"]
    return " ".join(["\"%s=%s\"" % (v, d.getVar(v)) for v in s])

do_compile() {
    # the Makefile in ${B}/../misc/Makefile, does not handle CC, AR, AS, etc
    # properly. So do its job manually. Preparing the includes first, then libs.
    for i in $(ls ${BSP_TARGETS_DIR}/*/src/Makefile); do
        oe_runmake -C $(dirname $i) -s include ${@bsp_make_vars(d)}
    done
    for i in $(ls ${BSP_TARGETS_DIR}/*/src/Makefile); do
        oe_runmake -C $(dirname $i) -s libs ${@bsp_make_vars(d)}
    done

    # --build-id=none is required due to linker script not defining a location for it.
    # Again, recipe-systoot include is necessary
    oe_runmake CC="${CC}" CC_FLAGS="-MMD -MP -Wl,--build-id=none -I${STAGING_DIR_TARGET}/usr/include"
}

do_install() {
    :
}

PMU_FIRMWARE_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {
    install -Dm 0644 ${B}/executable.elf ${DEPLOYDIR}/${PMU_FIRMWARE_BASE_NAME}.elf
    ln -sf ${PMU_FIRMWARE_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${B}/executable.elf ${B}/executable.bin
    install -m 0644 ${B}/executable.bin ${DEPLOYDIR}/${PMU_FIRMWARE_BASE_NAME}.bin
    ln -sf ${PMU_FIRMWARE_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_install
