inherit esw python3native esw_apps_common deploy

IMGSEL_DEPENDS ?= ""
IMGSEL_DEPENDS:zynqmp ?= "libxil xiltimer bootgen-native"
IMGSEL_DEPENDS:versal ?= "xilpdi xilplmi xilloader xilpm xilsecure xilpuf xiltimer xilffs bootgen-native base-pdi"
IMGSEL_DEPENDS:versal-2ve-2vm ?= "xilpdi xilplmi xilloader xilpm-ng xilsecure xilpuf xiltimer xilffs xilocp xilcert bootgen-native base-pdi"

BOOTGEN_ARCH_DEFAULT:zynqmp = "zynqmp"
BOOTGEN_ARCH_DEFAULT:versal = "versal"
BOOTGEN_ARCH_DEFAULT:versal-2ve-2vm = "versal_2ve_2vm"
BOOTGEN_ARCH ?= "${BOOTGEN_ARCH_DEFAULT}"

DEPENDS += "${IMGSEL_DEPENDS}"

RCONFLICTS:${PN} = "image-selector-xsct"

ESW_COMPONENT_SRC = "/src/"
ESW_EXECUTABLE_NAME = "imgsel"

SRC_URI:append = " git://github.com/Xilinx/image-selector.git;protocol=https;branch=main;destsuffix=image-selector;name=image-selector"
SRCREV_image-selector = "c20b0fdb07d438bef69007d3749a19c1788fdd87"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    install -m 0644 *.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}/
    install -m 0644 ${S}/cmake/UserConfig.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    )
}

OECMAKE_SOURCEPATH = "${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}"

do_compile:append:zynqmp () {
	# Generate .bif for zynqmp platforms
cat > ${WORKDIR}/${PN}.bif << EOF
    the_ROM_image:
    {
            [bootloader,destination_cpu=a53-0] ${B}/${ESW_EXECUTABLE_NAME}.elf
    }
EOF
}

PMC_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/pmc_data.cdo"
LPD_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/lpd_data.cdo"
IMGSEL_BIF_OPTIONAL_DATA ?= ""

CDO_DEPENDS ?= ""
CDO_DEPENDS:versal ?= "cdo-deploy:do_deploy"
CDO_DEPENDS:versal-2ve-2vm ?= "cdo-deploy:do_deploy"

do_compile[depends] += "${CDO_DEPENDS}"

gen_imgsel_bif () {
	# Generate .bif for versal platforms
	if [ ! -e ${PMC_DATA_CDO} ]; then
		bberror "Unable to find pmc_data.cdo file in ${SYSTEM_DTFILE_DIR} to generate image-selector.bin file"
		exit 1
	fi
	if [ ! -e ${LPD_DATA_CDO} ]; then
		bberror "Unable to find lpd_data.cdo file in ${SYSTEM_DTFILE_DIR} to generate image-selector.bin file"
		exit 1
	fi
	base_idcode="0x14ca8093"
	if [ -f ${RECIPE_SYSROOT}/boot/base-design.pdi ]; then
		base_idcode=$(bootgen -arch ${BOOTGEN_ARCH} -read ${RECIPE_SYSROOT}/boot/base-design.pdi iht | grep -w id_code | cut -d ':' -f2 | xargs | cut -d ' ' -f1)
	else
		bbwarn "Unable to find the pdi file ${RECIPE_SYSROOT}/boot/base-design.pdi to get ID_CODE, using ${base_idcode} as id_code."
	fi
cat > ${WORKDIR}/${PN}.bif << EOF
    the_ROM_image:
    {
EOF

	for opt_data in $(echo "${IMGSEL_BIF_OPTIONAL_DATA}" | tr ';' '\n'); do
cat >> ${WORKDIR}/${PN}.bif << EOF
	optionaldata { ${opt_data} }
EOF
	done

cat >> ${WORKDIR}/${PN}.bif << EOF
	id_code = ${base_idcode}
	extended_id_code = 0x01
	id = 0x2
	image {
		name = pmc_subsys
		id = 0x1c000001
		partition
		{ id=0x01, type=bootloader, file=${B}/${ESW_EXECUTABLE_NAME}.elf }
		partition
		{ id=0x09, type=pmcdata, load=0xf2000000, file=${PMC_DATA_CDO} }
	}
	image {
		name = lpd
		id = 0x4210002
		partition
		{ id=0x0C, type=cdo, file=${LPD_DATA_CDO} }
	}
    }
EOF
}

do_compile:append:versal () {
    gen_imgsel_bif
}

do_compile:append:versal-2ve-2vm () {
    gen_imgsel_bif
}

do_compile:append () {
    bootgen -image ${WORKDIR}/${PN}.bif -arch ${BOOTGEN_ARCH} -w -o ${B}/${PN}.bin

    printf "* ${PN}\nSRCREV: ${SRCREV}\nBRANCH: ${BRANCH}\n\n" > ${S}/${PN}.manifest
}

do_install[noexec] = "1"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_EXECUTABLE_NAME}.elf ${DEPLOYDIR}/${PN}.elf
    ln -sf ${PN}.elf ${DEPLOYDIR}/${PN}-${MACHINE}.elf
    install -Dm 0644 ${B}/${PN}.bin ${DEPLOYDIR}/${PN}.bin
    ln -sf ${PN}.bin ${DEPLOYDIR}/${PN}-${MACHINE}.bin

    install -Dm 0644 ${S}/${PN}.manifest ${DEPLOYDIR}/${PN}-${MACHINE}.manifest
}

addtask deploy before do_build after do_install
