do_install:append:zynqmp () {
    rm -rf ${D}${includedir}/KHR/*
}
