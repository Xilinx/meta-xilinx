# The Xen block tap components are packaged separately here to support
# the option to build them in a separate recipe from xen-tools.

BLKTAP_PACKAGES ?= " \
    ${PN}-blktap \
    ${PN}-libblktap \
    ${PN}-libblktapctl \
    ${PN}-libblktapctl-dev \
    ${PN}-libblktap-dev \
    ${PN}-libvhd \
    ${PN}-libvhd-dev \
    ${PN}-blktap-staticdev \
    "

BLKTAP_PROVIDES ?= " \
    virtual/blktap \
    virtual/libblktap \
    virtual/libblktapctl \
    virtual/libvhd \
    "

BLKTAP_RRECOMMENDS ?= " \
    virtual/blktap \
    virtual/libblktap \
    virtual/libblktapctl \
    virtual/libvhd \
    "

RPROVIDES:${PN}-blktap = "virtual/blktap"
RPROVIDES:${PN}-libblktap = "virtual/libblktap"
RPROVIDES:${PN}-libblktapctl = "virtual/libblktapctl"
RPROVIDES:${PN}-libvhd = "virtual/libvhd"

FILES:${PN}-blktap-staticdev += "\
    ${libdir}/libblktapctl.a \
    ${libdir}/libvhd.a \
    ${libdir}/libblktap.a \
    "

FILES:${PN}-libblktapctl = "${libdir}/libblktapctl.so.*"
FILES:${PN}-libblktapctl-dev = " \
    ${libdir}/libblktapctl.so \
    ${libdir}/pkgconfig/xenblktapctl.pc \
    ${datadir}/pkgconfig/xenblktapctl.pc \
    "

FILES:${PN}-libvhd = "${libdir}/libvhd.so.*"
FILES:${PN}-libvhd-dev = " \
    ${libdir}/libvhd.so \
    ${libdir}/pkgconfig/vhd.pc \
    ${datadir}/pkgconfig/vhd.pc \
    "

FILES:${PN}-libblktap = "${libdir}/libblktap.so.*"
FILES:${PN}-libblktap-dev = " \
    ${libdir}/libblktap.so \
    ${libdir}/pkgconfig/blktap.pc \
    ${datadir}/pkgconfig/blktap.pc \
    "

FILES:${PN}-blktap = "\
    ${sbindir}/blktapctrl \
    ${sbindir}/img2qcow \
    ${sbindir}/lock-util \
    ${sbindir}/qcow2raw \
    ${sbindir}/qcow-create \
    ${sbindir}/tap-ctl \
    ${sbindir}/tapdisk \
    ${sbindir}/tapdisk2 \
    ${sbindir}/tapdisk-client \
    ${sbindir}/tapdisk-diff \
    ${sbindir}/tapdisk-stream \
    ${sbindir}/td-util \
    ${sbindir}/vhd-update \
    ${sbindir}/vhd-util \
    "
