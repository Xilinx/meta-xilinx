inherit esw python3native esw_apps_common deploy bootgen-bif

PE = "1"
ESW_VER = "2026.1"

IMGSEL_DEPENDS ?= ""
IMGSEL_DEPENDS:zynqmp ?= "libxil xiltimer bootgen-native"
IMGSEL_DEPENDS:versal ?= "xilpdi xilplmi xilloader xilpm xilsecure xilpuf xiltimer xilffs bootgen-native base-pdi"
IMGSEL_DEPENDS:versal-2ve-2vm ?= "xilpdi xilplmi xilloader xilpm-ng xilsecure xilpuf xiltimer xilffs xilocp xilcert bootgen-native base-pdi"

DEPENDS += "${IMGSEL_DEPENDS}"

RCONFLICTS:${PN} = "image-selector-xsct"

ESW_COMPONENT_SRC = "/src/"
ESW_EXECUTABLE_NAME = "imgsel"

SRC_URI:append = " git://github.com/Xilinx/image-selector.git;protocol=https;branch=main;destsuffix=image-selector;name=image-selector"
SRCREV_image-selector = "283bcb2b49eaa2ff1eae2f0d926e1844797352c2"

OECMAKE_SOURCEPATH = "${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}"

# BIF configuration
BIF_FILE_PATH = "${B}/${PN}.bif"

# imgsel partition attributes differ between ZynqMP and Versal BIF formats
IMGSEL_BIF_ATTRS ?= ""
IMGSEL_BIF_ATTRS:zynqmp = "bootloader, destination_cpu=a53-0"
IMGSEL_BIF_ATTRS:versal = "id=0x01, type=bootloader"
IMGSEL_BIF_ATTRS:versal-2ve-2vm = "id=0x01, type=bootloader"

BIF_PARTITION_ATTR:zynqmp = "imgsel"
BIF_PARTITION_ATTR[imgsel] = "${IMGSEL_BIF_ATTRS}"
BIF_PARTITION_IMAGE[imgsel] = "${B}/${ESW_EXECUTABLE_NAME}.elf"

# Versal: top-level attrs, image blocks with CDO partitions
PMC_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/pmc_data.cdo"
LPD_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/lpd_data.cdo"

IMGSEL_ID_CODE_FILE = "${B}/imgsel-idcode.txt"
IMGSEL_ID_CODE_DEFAULT = "0x14ca8093"

BIF_TOPLEVEL_ATTR:versal = "id_code extended_id_code id"
BIF_TOPLEVEL_ATTR:versal-2ve-2vm = "id_code extended_id_code id"
BIF_TOPLEVEL_ATTR[extended_id_code] = "0x01"
BIF_TOPLEVEL_ATTR[id] = "0x2"

BIF_PARTITION_ATTR:versal = "imgsel pmc-data lpd-data"
BIF_PARTITION_ATTR:versal-2ve-2vm = "imgsel pmc-data lpd-data"

BIF_PARTITION_ATTR[pmc-data] = "id=0x09, type=pmcdata, load=0xf2000000"
BIF_PARTITION_IMAGE[pmc-data] = "${PMC_DATA_CDO}"
BIF_PARTITION_ID[pmc-data] = "0x1c000001"

BIF_PARTITION_ATTR[lpd-data] = "id=0x0C, type=cdo"
BIF_PARTITION_IMAGE[lpd-data] = "${LPD_DATA_CDO}"
BIF_PARTITION_ID[lpd-data] = "0x4210002"

BIF_PARTITION_ID[imgsel] = "0x1c000001"

BIF_PARTITION_NAME[0x1c000001] = "pmc_subsys"
BIF_PARTITION_NAME[0x4210002] = "lpd"

OECMAKE_SOURCEPATH = "${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}"

# BIF configuration
BIF_FILE_PATH = "${B}/${PN}.bif"

# imgsel partition attributes differ between ZynqMP and Versal BIF formats
IMGSEL_BIF_ATTRS ?= ""
IMGSEL_BIF_ATTRS:zynqmp = "bootloader, destination_cpu=a53-0"
IMGSEL_BIF_ATTRS:versal = "id=0x01, type=bootloader"
IMGSEL_BIF_ATTRS:versal-2ve-2vm = "id=0x01, type=bootloader"

BIF_PARTITION_ATTR:zynqmp = "imgsel"
BIF_PARTITION_ATTR[imgsel] = "${IMGSEL_BIF_ATTRS}"
BIF_PARTITION_IMAGE[imgsel] = "${B}/${ESW_EXECUTABLE_NAME}.elf"

# Versal: top-level attrs, image blocks with CDO partitions
PMC_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/pmc_data.cdo"
LPD_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/lpd_data.cdo"

IMGSEL_ID_CODE_FILE = "${B}/imgsel-idcode.txt"
IMGSEL_ID_CODE_DEFAULT = "0x14ca8093"

BIF_TOPLEVEL_ATTR:versal = "id_code extended_id_code id"
BIF_TOPLEVEL_ATTR:versal-2ve-2vm = "id_code extended_id_code id"
BIF_TOPLEVEL_ATTR[extended_id_code] = "0x01"
BIF_TOPLEVEL_ATTR[id] = "0x2"

BIF_PARTITION_ATTR:versal = "imgsel pmc-data lpd-data"
BIF_PARTITION_ATTR:versal-2ve-2vm = "imgsel pmc-data lpd-data"

BIF_PARTITION_ATTR[pmc-data] = "id=0x09, type=pmcdata, load=0xf2000000"
BIF_PARTITION_IMAGE[pmc-data] = "${PMC_DATA_CDO}"
BIF_PARTITION_ID[pmc-data] = "0x1c000001"

BIF_PARTITION_ATTR[lpd-data] = "id=0x0C, type=cdo"
BIF_PARTITION_IMAGE[lpd-data] = "${LPD_DATA_CDO}"
BIF_PARTITION_ID[lpd-data] = "0x4210002"

BIF_PARTITION_ID[imgsel] = "0x1c000001"

BIF_PARTITION_NAME[0x1c000001] = "pmc_subsys"
BIF_PARTITION_NAME[0x4210002] = "lpd"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    install -m 0644 *.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}/
    install -m 0644 ${S}/cmake/UserConfig.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    )
}

# Extract id_code from base-pdi when BIF requires it
do_extract_idcode() {
    # Skip if BIF doesn't need id_code (e.g. ZynqMP)
    case "${BIF_TOPLEVEL_ATTR}" in
        *id_code*) ;;
        *) return ;;
    esac

    if [ ! -f ${RECIPE_SYSROOT}/boot/base-design.pdi ]; then
        bbwarn "base-design.pdi not found, using default id_code ${IMGSEL_ID_CODE_DEFAULT}"
        echo "${IMGSEL_ID_CODE_DEFAULT}" > ${IMGSEL_ID_CODE_FILE}
        return
    fi

    id_code=$(bootgen -arch ${BOOTGEN_ARCH} -read ${RECIPE_SYSROOT}/boot/base-design.pdi iht \
        | grep -w id_code | cut -d ':' -f2 | xargs | cut -d ' ' -f1)

    if [ -z "$id_code" ]; then
        bbwarn "Failed to parse id_code from base-design.pdi, using default ${IMGSEL_ID_CODE_DEFAULT}"
        id_code="${IMGSEL_ID_CODE_DEFAULT}"
    fi
    echo "$id_code" > ${IMGSEL_ID_CODE_FILE}
}
do_extract_idcode[dirs] = "${B}"
addtask do_extract_idcode after do_configure before do_generate_bif

# Python task: generate BIF from class variables
python do_generate_bif() {
    toplevel_attrs = (d.getVar("BIF_TOPLEVEL_ATTR") or "").split()
    if "id_code" in toplevel_attrs:
        idcode_file = d.getVar("IMGSEL_ID_CODE_FILE")
        with open(idcode_file) as f:
            id_code = f.read().strip()
        d.setVarFlag("BIF_TOPLEVEL_ATTR", "id_code", id_code)

    bootgen_bif_generate(d)
}

CDO_DEPENDS ?= ""
CDO_DEPENDS:versal ?= "cdo-deploy:do_deploy"
CDO_DEPENDS:versal-2ve-2vm ?= "cdo-deploy:do_deploy"

do_generate_bif[depends] += "${CDO_DEPENDS}"
do_generate_bif[vardeps] += "\
    BIF_FILE_PATH \
    BIF_OPTIONAL_DATA \
    BIF_PARTITION_ATTR \
    BIF_PARTITION_ID \
    BIF_PARTITION_IMAGE \
    BIF_PARTITION_NAME \
    BIF_TOPLEVEL_ATTR \
    IMGSEL_ID_CODE_DEFAULT \
    IMGSEL_BIF_ATTRS \
"
addtask do_generate_bif after do_compile do_extract_idcode before do_bootgen

do_bootgen[dirs] = "${B}"
do_bootgen() {
    bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} -w -o ${B}/${PN}.bin
}
addtask do_bootgen after do_generate_bif before do_install

do_install[noexec] = "1"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_EXECUTABLE_NAME}.elf ${DEPLOYDIR}/${PN}.elf
    ln -sf ${PN}.elf ${DEPLOYDIR}/${PN}-${MACHINE}.elf
    install -Dm 0644 ${B}/${PN}.bin ${DEPLOYDIR}/${PN}.bin
    ln -sf ${PN}.bin ${DEPLOYDIR}/${PN}-${MACHINE}.bin
}

addtask deploy before do_build after do_install
