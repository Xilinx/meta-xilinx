SUMMARY = "A portable foreign function interface library"
DESCRIPTION = "The `libffi' library provides a portable, high level programming interface to various calling \
conventions.  This allows a programmer to call any function specified by a call interface description at run \
time. FFI stands for Foreign Function Interface.  A foreign function interface is the popular name for the \
interface that allows code written in one language to call code written in another language.  The `libffi' \
library really only provides the lowest, machine dependent layer of a fully featured foreign function interface.  \
A layer must exist above `libffi' that handles type conversions for values passed between the two languages."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e54c573c49435ccbbd3f6dc9e49a065e"

PR = "r0"

SRC_URI = "ftp://sourceware.org/pub/libffi/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "da984c6756170d50f47925bb333cda71"
SRC_URI[sha256sum] = "2ea0db90c2bbcc907c3aefc3f76e9dfc3b35c7a0fb75a4319f5248e0172c1e9e"

EXTRA_OECONF += "--disable-builddir"

inherit autotools

FILES_${PN}-dev += "${libdir}/libffi-${PV}"

BBCLASSEXTEND = "native nativesdk"
