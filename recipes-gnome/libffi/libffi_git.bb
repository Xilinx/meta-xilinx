SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e54c573c49435ccbbd3f6dc9e49a065e"

S = "${WORKDIR}/git"
PR = "r0"

BRANCH = "microblaze-support-rebase"
SRC_URI = "git://developer.petalogix.com/petalogix/internal/git/private/dev/nrossi/libffi;protocol=ssh;branch=${BRANCH}"

SRCREV="${AUTOREV}"

EXTRA_OECONF += "--disable-builddir"

inherit autotools

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

BBCLASSEXTEND = "native nativesdk"

autotools_do_configure() {
	if [ -e ${S}/configure ]; then
		oe_runconf
	else
		bbnote "nothing to configure"
	fi
}
