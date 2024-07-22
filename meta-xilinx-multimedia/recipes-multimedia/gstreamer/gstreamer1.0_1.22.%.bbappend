require gstreamer-xilinx-1.22.%.inc

SRC_URI:append = " \
           file://run-ptest \
           file://0001-tests-respect-the-idententaion-used-in-meson.patch \
           file://0002-tests-add-support-for-install-the-tests.patch \
           file://0003-tests-use-a-dictionaries-for-environment.patch \
           file://0004-tests-add-helper-script-to-run-the-installed_tests.patch \
           "

PACKAGECONFIG:append = " tracer-hooks coretracers"

S = "${WORKDIR}/git/subprojects/gstreamer"
