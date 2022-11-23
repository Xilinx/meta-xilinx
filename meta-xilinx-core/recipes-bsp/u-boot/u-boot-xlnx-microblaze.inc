SRC_URI += " \
    file://microblaze-generic.cfg \
    file://microblaze-generic-top.h \
    "

do_configure:prepend () {
    install ${WORKDIR}/microblaze-generic-top.h ${S}/include/configs/
}