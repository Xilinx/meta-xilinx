BOARD_ARCH ?= "${@['${BOARD}', '${MACHINE_ARCH}'][d.getVar('BOARD')=='']}"
BOARDVARIANT_ARCH ?= "${@['${BOARD}_${BOARD_VARIANT}','${BOARD_ARCH}'][d.getVar('BOARD_VARIANT')=='']}"

PACKAGE_EXTRA_ARCHS:append = "${@['', ' ${BOARD_ARCH}']['${BOARD}' != '' and '${BOARD_ARCH}' != '${MACHINE_ARCH}']}"
# We don't add BOARDVARIANT_ARCH as that will be automatic via MACHINE
#PACKAGE_EXTRA_ARCHS:append = "${@['', ' ${BOARDVARIANT_ARCH}']['${BOARD_VARIANT}' != '' and '${BOARDVARIANT_ARCH}' != '${MACHINE_ARCH}']}"
