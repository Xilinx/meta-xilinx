# U-boot fitimage/blob generator

UBOOT_IMAGE_BLOB ?= ""
DT_BLOB_DIR ?= "${B}/arch/arm/dts/dt-blob"
UBOOT_BLOB_NAME ?= "${MACHINE}-fit-dtb${IMAGE_VERSION_SUFFIX}.blob"

IMPORT_CC_DTBS ?= ""
CC_DTBS_DUP ?= ""

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
