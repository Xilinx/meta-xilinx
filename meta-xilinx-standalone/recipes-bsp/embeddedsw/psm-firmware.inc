SUMMARY = "PSM firmware for versal devices"

DESCRIPTION = "AMD Versal / Versal NET Platform Security Manager (PSM) \
firmware (MicroBlaze) that handles system-level power-management and \
security operations."
inherit xlnx-embeddedsw deploy shared-manifest

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# This is the default in most BSPs.  A MACHINE.conf can override this!
PSM_FIRMWARE_IMAGE_NAME ??= "psm-firmware-${MACHINE}"

inherit image-artifact-names

MANIFEST_COMPONENT_NAME ?= "psm-firmware"
MANIFEST_SRCREV_VARS = "SRCREV SRCREV_embeddedsw ESW_REV"

PSM_FIRMWARE_BASE_NAME ?= "${PSM_FIRMWARE_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "psmfw.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
