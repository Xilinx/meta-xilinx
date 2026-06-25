SUMMARY = "AMD Zynq UltraScale+ MPSoC Platform Management Unit (PMU) \
firmware (MicroBlaze)."
DESCRIPTION = "PMU Firmware"

inherit xlnx-embeddedsw deploy shared-manifest

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "none"
COMPATIBLE_MACHINE:zynqmp = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# This is the default in most BSPs.  A MACHINE.conf can override this!
PMU_FIRMWARE_IMAGE_NAME ??= "pmu-firmware-${MACHINE}"

inherit image-artifact-names

MANIFEST_COMPONENT_NAME ?= "pmu-firmware"
MANIFEST_SRCREV_VARS = "SRCREV SRCREV_embeddedsw ESW_REV"
MANIFEST_DEPLOY_BAREMETAL = "do_shared_manifest_deploy"

PMU_FIRMWARE_BASE_NAME ?= "${PMU_FIRMWARE_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "executable.elf"

# Disable buildpaths QA check warnings.
INSANE_SKIP:${PN} += "buildpaths"
