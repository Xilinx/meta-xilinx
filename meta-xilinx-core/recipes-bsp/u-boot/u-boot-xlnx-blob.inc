# U-boot fitimage/blob generator

UBOOT_IMAGE_BLOB_DEFAULT = ""
UBOOT_IMAGE_BLOB ?= "${UBOOT_IMAGE_BLOB_DEFAULT}"
DT_BLOB_DIR ?= "${B}/arch/arm/dts/dt-blob"
UBOOT_BLOB_NAME ?= "${MACHINE}-fit-dtb${IMAGE_VERSION_SUFFIX}.blob"

IMPORT_CC_DTBS ?= ""
CC_DTBS_DUP ?= ""
IMPORT_CC_UBOOT_DTBS ?= ""

MKIMAGE_DTBLOB_OPTS ?= "-E -B 0x8"

# Everything is swtiched on with UBOOT_IMAGE_BLOB = '1'
inherit ${@'image-artifact-names' if d.getVar('UBOOT_IMAGE_BLOB') == "1" else ''}

python() {
    if d.getVar('UBOOT_IMAGE_BLOB') == "1":
        d.appendVarFlag('do_compile', 'postfuncs', ' do_blob_generate')
        d.appendVarFlag('do_compile', 'cleandirs', ' ${DT_BLOB_DIR}')
        d.appendVar('PROVIDES', ' u-boot-xlnx-fit-blob')
        d.appendVar('DEPENDS', ' u-boot-mkimage-native')
}

dtblob_emit_its_section() {
	case $2 in
	header)
		cat << EOF > $1
/dts-v1/;

/ {
	description = "DT Blob Creation";
EOF
	;;
	imagestart)
		cat << EOF >> $1

	images {
EOF
	;;
	confstart)
		cat << EOF >> $1

	configurations {
EOF
	;;
	sectend)
		cat << EOF >> $1
	};
EOF
	;;
	fitend)
		cat << EOF >> $1
};
EOF
	;;
	esac
}

dtblob_emit_dtb () {
	dtb_csum="md5"
	cat << EOF >> $1
		fdt-$2 {
			description = "$(basename $3 .dtb)";
			data = /incbin/("$3");
			type = "flat_dt";
			arch = "arm64";
			compression = "none";
			hash-1 {
				algo = "$dtb_csum";
				};
			};
EOF
}

#1.file name
#2.config node
#3.config node description
#4.DTB count
dtblob_emit_config () {
	default_dtb=1
	if [ $4 -eq $default_dtb ]; then
		cat << EOF >> $1
		default = "config_$4";
EOF
	fi
	cat  << EOF >> $1
		config_$4 {
			description = "$3";
			fdt = "fdt-$2";
		};
EOF
}

do_blob_generate () {
	oe_runmake -C ${S} O=${B} dtbs
	install -d ${DT_BLOB_DIR}
	for CC_DTB in ${IMPORT_CC_DTBS}; do
		DTBO=$(echo $CC_DTB | cut -d: -f1)
        BASE_DTB=$(echo $CC_DTB | cut -d: -f2)
        OUTPUT_DTB=$(echo $CC_DTB | cut -d: -f3)
        if [ "${SYSTEM_DTB_BLOB}" = "1" ]; then
		BASE_SYS_DTB="${RECIPE_SYSROOT}/${DTB_PATH}/${DTB_FILE_NAME}"
        else
		BASE_SYS_DTB="${B}/arch/arm/dts/${BASE_DTB}"
        fi
        bbnote "fdtoverlay -o ${DT_BLOB_DIR}/${OUTPUT_DTB} -i ${BASE_SYS_DTB} ${B}/arch/arm/dts/${DTBO}"
		if [ -f ${B}/arch/arm/dts/${DTBO} ]; then
        fdtoverlay -o ${DT_BLOB_DIR}/${OUTPUT_DTB} -i ${BASE_SYS_DTB} ${B}/arch/arm/dts/${DTBO}
		fi
	done

	for CC_DTB in ${IMPORT_CC_UBOOT_DTBS}; do
	        DTBO=$(echo $CC_DTB | cut -d: -f1)
	OUTPUT_DTB=$(echo $CC_DTB | cut -d: -f3)
	BASE_SYS_DTB="${RECIPE_SYSROOT}/${DTB_PATH}/${PACKAGE_UBOOT_DTB_NAME}"
		if [ -f ${B}/arch/arm/dts/${DTBO} ] && [ -f ${BASE_SYS_DTB} ]; then
	bbnote "fdtoverlay -o ${DT_BLOB_DIR}/${OUTPUT_DTB} -i ${BASE_SYS_DTB} ${B}/arch/arm/dts/${DTBO}"
	fdtoverlay -o ${DT_BLOB_DIR}/${OUTPUT_DTB} -i ${BASE_SYS_DTB} ${B}/arch/arm/dts/${DTBO}
		fi
	done

	cd ${DT_BLOB_DIR}
	its_filename="dtblob.its"
	dtblob_emit_its_section "${its_filename}" "header"
	dtblob_emit_its_section "${its_filename}" "imagestart"
	for dtb in ${RECIPE_SYSROOT}/${DTB_PATH}/${DTB_FILE_NAME} $(find ${DT_BLOB_DIR} -name '*.dtb' | sort); do
		dtblob_emit_dtb "${its_filename}" "$(basename $dtb .dtb)" "$dtb"
	done
	dtblob_emit_its_section "${its_filename}" "sectend"
	dtblob_emit_its_section "${its_filename}" "confstart"
	dtbcount=1
	for dtb in ${RECIPE_SYSROOT}/${DTB_PATH}/${DTB_FILE_NAME} $(find ${DT_BLOB_DIR} -name '*.dtb' | sort); do
		dtblob_emit_config "${its_filename}" "$(basename $dtb .dtb)" "$(basename $dtb .dtb)" "$dtbcount"
		dtbcount=`expr $dtbcount + 1`
	done

	for CC_DTB_DUP in ${CC_DTBS_DUP}; do
		DTB=$(echo $CC_DTB_DUP | cut -d: -f1)
		DUP_DTB=$(echo $CC_DTB_DUP | cut -d: -f2)
		if [ -f ${DT_BLOB_DIR}/${DTB}.dtb ]; then
			bbnote "Node ${DT_BLOB_DIR}/${DTB} with ${DT_BLOB_DIR}/${DUP_DTB}"
			dtblob_emit_config "${its_filename}" "$DTB" "$DUP_DTB" "$dtbcount"
			dtbcount=`expr $dtbcount + 1`
        else
            bberror "CC_DTBS_DUP file not found ${DT_BLOB_DIR}/${DTB}.dtb"
		fi
	done

	dtblob_emit_its_section "${its_filename}" "sectend"
	dtblob_emit_its_section "${its_filename}" "fitend"

	mkimage ${MKIMAGE_DTBLOB_OPTS} -f "${its_filename}" "${UBOOT_BLOB_NAME}"
}

do_install:append() {
    (
    cd ${B}

    if [ -e "${DT_BLOB_DIR}/${UBOOT_BLOB_NAME}" ]; then
        install -d ${D}/boot
        install -m 0644 ${DT_BLOB_DIR}/${UBOOT_BLOB_NAME} ${D}/boot
        ln -sf `basename ${UBOOT_BLOB_NAME}` ${D}/boot/fit-dtb.blob
    fi
    )
}

do_deploy:prepend() {
    cd ${B}

    if [ -e "${DT_BLOB_DIR}/${UBOOT_BLOB_NAME}" ]; then
            install -m 0644 ${DT_BLOB_DIR}/${UBOOT_BLOB_NAME} ${DEPLOYDIR}/
            ln -sf `basename ${UBOOT_BLOB_NAME}` ${DEPLOYDIR}/fit-dtb.blob
    fi
}
