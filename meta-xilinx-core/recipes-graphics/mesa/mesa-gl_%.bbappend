PACKAGECONFIG:append = " dri3 gallium"

do_install:append:zynqmp () {
    rm -rf ${D}${includedir}/KHR/*
}
