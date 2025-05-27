# tag: RELEASE-4.20-dev
SRCREV ?= "3128d7248f2ad389b8e9a3e252958cbfbd1898ee"

XEN_REL ?= "4.20.0"
XEN_BRANCH ?= "staging"

SRC_URI = " \
    git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH} \
    file://0001-menuconfig-mconf-cfg-Allow-specification-of-ncurses-location.patch \
    file://0001-arm-silence-gcc14-warning-error-on-irq-bounds-check.patch \
    "

LIC_FILES_CHKSUM ?= "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

PV = "${XEN_REL}+stable"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE ??= "-1"

require recipes-extended/xen/xen.inc
require recipes-extended/xen/xen-hypervisor.inc
