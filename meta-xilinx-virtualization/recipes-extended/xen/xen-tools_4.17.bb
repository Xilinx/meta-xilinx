# xen 4.17.0 release sha
SRCREV ?= "11560248ffda3f00f20bbdf3ae088af474f7f2a3"

XEN_URI ?= "git://xenbits.xen.org/xen.git"
XEN_REL ?= "4.17"
XEN_BRANCH ?= "stable-${XEN_REL}"

SRC_URI = " \
    ${XEN_URI};branch=${XEN_BRANCH} \
    file://0001-python-pygrub-pass-DISTUTILS-xen-4.15.patch \
    "

LIC_FILES_CHKSUM ?= "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

S = "${WORKDIR}/git"

require xen.inc
require xen-tools.inc
