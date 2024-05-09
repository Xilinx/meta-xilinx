require gstreamer-xilinx-1.20.5.inc

SRC_URI:append = " \
           file://run-ptest \
           file://0001-tests-respect-the-idententaion-used-in-meson.patch;striplevel=3 \
           file://0002-tests-add-support-for-install-the-tests.patch;striplevel=3 \
           file://0003-tests-use-a-dictionaries-for-environment.patch;striplevel=3 \
           file://0004-tests-add-helper-script-to-run-the-installed_tests.patch;striplevel=3 \
           file://0005-bin-Fix-race-conditions-in-tests.patch;striplevel=3 \
           "

PACKAGECONFIG:append = " tracer-hooks coretracers"

S = "${WORKDIR}/git/subprojects/gstreamer"
