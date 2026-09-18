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

# u-boot SPL requries just the pmu-firmware.bin file, make this available
do_compile:append() {
    ${OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}

do_deploy:append() {
    install -Dm 0644 ${B}/${ESW_COMPONENT}.bin ${DEPLOYDIR}/${PMU_FIRMWARE_BASE_NAME}.bin
    ln -sf ${PMU_FIRMWARE_BASE_NAME}.bin ${DEPLOYDIR}/${PMU_FIRMWARE_IMAGE_NAME}.bin
}
