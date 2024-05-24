APP_DIR = "undefined"
APP_DIR:zynq = "zynq_fsbl"
APP_DIR:zynqmp = "zynqmp_fsbl"

B = "${S}/lib/sw_apps/${APP_DIR}/src"

# The makefile does not handle parallelization
PARALLEL_MAKE = "-j1"

COMPILER = "${CC}"
COMPILER_FLAGS = "-O2 -c"
EXTRA_COMPILER_FLAGS = "-g -Wall -Wextra ${ESW_CFLAGS}"
ARCHIVER = "${AR}"

BSP_DIR ?= "${B}/../misc/undefined"
BSP_DIR:zynq   ?= "${B}/../misc/zynq_fsbl_bsp"
BSP_DIR:zynqmp ?= "${B}/../misc/zynqmp_fsbl_bsp"
BSP_TARGETS_DIR ?= "${BSP_DIR}/psu_cortexa53_0/libsrc"

# FSBL components expect AS to be CC.
AS = "${CC}"
LINKER = "${CC}"
DUMP = "${OBJDUMP} -xSD"

def bsp_make_vars(d):
    s = ["COMPILER", "CC", "COMPILER_FLAGS", "EXTRA_COMPILER_FLAGS", "ARCHIVER", "AR", "AS", "LINKER", "DUMP"]
    return " ".join(["\"%s=%s\"" % (v, d.getVar(v)) for v in s])

EXTRA_OEMAKE = "${@bsp_make_vars(d)}"

ARM_INSTRUCTION_SET:eabi:arm = "arm"

do_install() {
    :
}

PACKAGES = ""

ESW_COMPONENT = "fsbl.elf"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${FSBL_BASE_NAME}.elf
    ln -sf ${FSBL_BASE_NAME}.elf ${DEPLOYDIR}/${FSBL_IMAGE_NAME}.elf
}

addtask deploy before do_build after do_install
