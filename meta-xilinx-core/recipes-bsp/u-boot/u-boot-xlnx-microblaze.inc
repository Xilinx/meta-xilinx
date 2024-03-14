SRC_URI += " \
    file://microblaze-generic.cfg \
    "

# Disable buildpaths and arch QA check warnings for u-boot-xlnx.elf.
INSANE_SKIP:${PN}-elf += "buildpaths arch"
