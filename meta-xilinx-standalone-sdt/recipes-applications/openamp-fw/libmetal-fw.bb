SUMMARY = "libmetal-only RPU firmware payload (OpenAMP base)."
DESCRIPTION = "Pre-built RPU baremetal payload that only initialises \
libmetal (no OpenAMP demo on top), used as a base for custom RPU \
firmware."
require openamp-fw.inc

FW_MCDEPENDS = "mc::${MACHINE}-${TARGET_MC}:libmetal-xlnx:do_deploy"
FW_FILE = "${FW_DEPLOY_DIR}/metal.elf"
OPENAMP_FW_NAME = "metal.elf"
OPENAMP_FW_SRC_NAME = "${OPENAMP_FW_NAME}"
OPENAMP_XLNX_RECIPE = "${OPENAMP_FW_NAME}"

PROVIDES:append:armv7r = " libmetal-fw "
PROVIDES:append:armv8r = " libmetal-fw "

RPROVIDES:${PN} += "libmetal-fw"
