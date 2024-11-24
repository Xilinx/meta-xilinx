require recipes-bsp/embeddedsw/embeddedsw-source.inc


BPN = "embeddedsw-source"
EXCLUDE_FROM_WORLD = "1"

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/${PV}:${FILE_DIRNAME}/embeddedsw"

SRC_URI = " \
	${EMBEDDEDSW_SRCURI} \
	file://fix-xil-assert-filename.patch \
	file://makefile-skip-copy_bsp.sh.patch \
	file://fsbl-fixups.patch \
	file://0001-versal_fw-Fixup-core-makefiles.patch \
	"
