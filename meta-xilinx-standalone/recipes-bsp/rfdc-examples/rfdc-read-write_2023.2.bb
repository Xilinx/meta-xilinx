SUMMARY = "Simple rfdc-read-write application"

require rfdc-examples.inc

do_compile () {
    make all BOARD_FLAG=${FLAG} OUTS=${B}/rfdc-read-write RFDC_OBJS=xrfdc_read_write_example.o
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/rfdc-read-write ${D}${bindir}
}

