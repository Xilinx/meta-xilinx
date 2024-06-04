ESCRIPTION = "PSM Firmware"
SUMMARY = "PSM firmware for versal devices"

inherit xlnx-embeddedsw deploy

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

# This is the default in most BSPs.  A MACHINE.conf can override this!
PSM_FIRMWARE_IMAGE_NAME ??= "psm-firmware-${MACHINE}"

inherit image-artifact-names

PSM_FIRMWARE_BASE_NAME ?= "${PSM_FIRMWARE_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "psmfw.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
