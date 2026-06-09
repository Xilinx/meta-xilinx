SUMMARY = "Simple rfdc-selftest application"

require rfdc-examples.inc

do_compile (){
    make all BOARD_FLAG=${FLAG} OUTS=${B}/rfdc-selftest RFDC_OBJS=xrfdc_selftest_example.o
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/rfdc-selftest ${D}${bindir}
}
