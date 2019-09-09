PYNQ_NOTEBOOK_DIR ?= "${datadir}/notebooks"

PYNQ_ARCH_arm = "armv7l"
PYNQ_ARCH_aarch64 = "aarch64"

CMA_ARCH_arm = "32"
CMA_ARCH_aarch64 = "64"

PYNQ_BUILD_ARCH="${PYNQ_ARCH_${TARGET_ARCH}}"
PYNQ_BUILD_ROOT="${STAGING_DIR_TARGET}"
BBCLASSEXTEND = "native nativesdk"

FILES_${PN}-notebooks += "${PYNQ_NOTEBOOK_DIR}"
PACKAGES += "${PN}-notebooks"

# Used for setup.py on PYNQ
BOARD_NAME_ultra96-zynqmp = "Ultra96"
