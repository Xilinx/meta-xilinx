BOARD ??= ""
BOARD_VARIANT ??= ""

# Change the MACHINE to the BOARD-VARIANT (or BOARD)
ORIG_MACHINE := "${MACHINE}"
MACHINE := "${@['${BOARD}-${BOARD_VARIANT}', '${BOARD}' or '${ORIG_MACHINE}']['${BOARD_VARIANT}' == '']}"

# Add the BOARD and BOARD_VARIANT to the overrides
MACHINEOVERRIDES =. "${@['', '${BOARD}:']['${BOARD}' != '' and '${MACHINE}' != '${BOARD}']}"
# We don't add BOARD-BOARD_VARIANT as that will be automatic via MACHINE
#MACHINEOVERRIDES =. "${@['', '${BOARD}-${BOARD_VARIANT}:']['${BOARD_VARIANT}' != '' and '${MACHINE}' != '${BOARD_VARIANT}']}"

include conf/machine/include/${BOARD}.inc
include conf/machine/include/${BOARD}-${BOARD_VARIANT}.inc
