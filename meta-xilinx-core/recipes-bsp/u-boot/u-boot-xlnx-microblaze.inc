SRC_URI += " \
    file://microblaze-generic.cfg \
    file://microblaze-generic-top.h \
    "

do_configure:prepend () {
    install ${WORKDIR}/microblaze-generic-top.h ${S}/include/configs/
}

# Disable buildpaths QA check warnings for u-boot-xlnx.elf.
INSANE_SKIP:${PN}-elf += "buildpaths"
