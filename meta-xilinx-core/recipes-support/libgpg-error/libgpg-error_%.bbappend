FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:microblaze = " file://lock-obj-pub.microblazeel-unknown-linux-gnu.h"

do_configure:append:microblaze () {
	cp ${WORKDIR}/lock-obj-pub.microblazeel-unknown-linux-gnu.h ${S}/src/syscfg/
}

