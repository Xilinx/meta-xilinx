SRC_URI += " \
    file://microblaze-generic.cfg \
    "

# Disable buildpaths QA check warnings for u-boot-xlnx.elf.
INSANE_SKIP:${PN}-elf += "buildpaths"
