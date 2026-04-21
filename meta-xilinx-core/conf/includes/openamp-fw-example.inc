# all recipes using this *inc need to define the following variables
# the following variables have the specified defaults
# (which can be overridden)
#    OPENAMPFW_BOARD: the board name for these demos
#        default: first string of parent recipe up to first "-"
#    OPENAMPFW_CORE: the core on which the demo is to run
#        default: "r5"
#    OPENAMPFW_SLOT: the core slot on which the demo is to run
#        default: "0"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "${MACHINE}"
COMPATIBLE_MACHINE:versal-2ve-2vm = "${MACHINE}"
COMPATIBLE_MACHINE:kria = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp = "${MACHINE}"

do_compile[noexec] = "1"

OPENAMPFW_BASEDIR = "${nonarch_base_libdir}/firmware/xilinx"
OPENAMPFW_BOARD ?= "${@d.getVar("BPN").split("-")[0]}"
OPENAMPFW_CORE ?= "r5"
OPENAMPFW_CORE:versal-2ve-2vm ?= "r52"
OPENAMPFW_SLOT ?= "0"
OPENAMPFW_PKGDIR_SUFFIX ?= ""
OPENAMPFW_PKGDIR_SUFFIX:kria ?= "openamp-fw-examples"
OPENAMPFW_PKGDIR_SUFFIX:zynqmp ?= "openamp-fw-examples"
OPENAMPFW_PKGDIR_SUFFIX:versal ?= "openamp-fw-examples"
OPENAMPFW_PKGDIR_SUFFIX:versal-net ?= "openamp-zephyr-demo"
OPENAMPFW_PKGDIR_SUFFIX:versal-2ve-2vm ?= "openamp-zephyr-demo"

S = "${WORKDIR}"

do_install() {
    if [ -z "${OPENAMPFW_CORE}" ]; then
        bberror "OPENAMPFW_CORE is not set"
        exit 1
    fi
    if [ -z "${OPENAMPFW_SLOT}" ]; then
        bberror "OPENAMPFW_SLOT is not set"
        exit 1
    fi
    if [ -z "${OPENAMPFW_PKGDIR_SUFFIX}" ]; then
        bberror "OPENAMPFW_PKGDIR_SUFFIX is not set"
        exit 1
    fi

    ELF_DIR=""
    match_count=0

    for d in "${S}"/*"${OPENAMPFW_PKGDIR_SUFFIX}"; do
        [ -d "$d" ] || continue
        ELF_DIR="$d"
        break
    done

    if [ -z "$ELF_DIR" ]; then
        bberror "ERROR: No subdirectory in ${S} ends with '${OPENAMPFW_PKGDIR_SUFFIX}'" >&2
        exit 1
    fi

    elf_found=0
    for DEMO in $(ls ${ELF_DIR}/*elf); do
        elf_found=1
        DEMO=$(basename -s .elf ${DEMO})
        FULL_NAME=${OPENAMPFW_BOARD}-${OPENAMPFW_CORE}-${OPENAMPFW_SLOT}-${DEMO}
        bbnote "DEMO:  ${DEMO}"
        bbnote "BOARD: ${OPENAMPFW_BOARD}"
        bbnote "CORE:  ${OPENAMPFW_CORE}"
        bbnote "SLOT:  ${OPENAMPFW_SLOT}"
        bbnote "PKGDIR:${OPENAMPFW_PKGDIR_SUFFIX}"
        bbnote "FULLNM:${FULL_NAME}"

        DESTINATION=${D}${OPENAMPFW_BASEDIR}/${OPENAMPFW_BOARD}/rpu/${OPENAMPFW_SLOT}

        install -d ${DESTINATION}
        install -Dm 0644 ${S}/*${OPENAMPFW_PKGDIR_SUFFIX}/${DEMO}.elf ${DESTINATION}/${DEMO}.elf
   done
   if [ ${elf_found} -ne 1 ]; then
       bberror "no *elf files found to deploy"
       exit 1
   fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP:${PN} += "arch usrmerge"

FILES:${PN} += "${OPENAMPFW_BASEDIR}"
