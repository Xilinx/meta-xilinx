BOARD ??= ""
BOARD_VARIANT ??= ""

MACHINEOVERRIDES .= "${@['', ':${BOARD}']['${BOARD}' != '']}"
MACHINEOVERRIDES .= "${@['', ':${BOARD}-${BOARD_VARIANT}']['${BOARD_VARIANT}' != '']}"


BOARD_ARCH ?= "${@['${BOARD}', '${MACHINE_ARCH}'][d.getVar('BOARD')=='']}"
BOARDVARIANT_ARCH ?= "${@['${BOARD}-${BOARD_VARIANT}','${BOARD_ARCH}'][d.getVar('BOARD_VARIANT')=='']}"

PACKAGE_EXTRA_ARCHS_append = "${@['', ' ${BOARD_ARCH}'][d.getVar('BOARDVARIANT_ARCH') != d.getVar('MACHINE_ARCH')]}"
PACKAGE_EXTRA_ARCHS_append = "${@['', ' ${BOARDVARIANT_ARCH}'][d.getVar('BOARDVARIANT_ARCH') != d.getVar('BOARD_ARCH')]}"
