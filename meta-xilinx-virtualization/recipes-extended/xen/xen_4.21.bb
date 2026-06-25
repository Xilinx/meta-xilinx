SRCREV ?= "06af9ef22996cecc2024a2e6523cec77a655581e"

XEN_REL ?= "4.21.0"
XEN_BRANCH ?= "stable-4.21"

SRC_URI = " \
    git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH} \
    file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-location.patch \
    file://0001-libxl_nocpuid-fix-build-error.patch \
    "

LIC_FILES_CHKSUM ?= "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

PV = "${XEN_REL}+stable"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE ??= "-1"

require recipes-extended/xen/xen.inc
require recipes-extended/xen/xen-hypervisor.inc
