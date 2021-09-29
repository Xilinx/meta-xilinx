SUMMARY = "Device tree lopper"
DESCRIPTION = "Tool to subset a system device tree"
SECTION = "bootloader"
LICENSE = "BSD-3-Clause"

RDEPENDS:${PN} += " \
    python3-core \
    dtc \
    python3-dtc \
    python3-flask \
    python3-flask-restful \
    python3-six \
    python3-pandas \
    python3-ruamel-yaml \
    python3-anytree \
    python3-pyyaml \
    python3-humanfriendly \
"

SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=master"

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=8e5f5f691f01c9fdfa7a7f2d535be619"

SRCREV = "4fc085c4be031996e7f48dcaf03d0782989c8d58"

S = "${WORKDIR}/git"

do_configure() {
	:
}

do_compile() {
	sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' lopper.py
	sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' lopper_sanity.py
}

do_install() {
	datadirrelpath=${@os.path.relpath(d.getVar('datadir'), d.getVar('bindir'))}

	mkdir -p ${D}/${bindir}
	mkdir -p ${D}/${datadir}/lopper

	cp -r ${S}/README* ${D}/${datadir}/lopper/.
	cp -r ${S}/assists* ${D}/${datadir}/lopper/.
	cp -r ${S}/lop* ${D}/${datadir}/lopper/.
	cp -r ${S}/LICENSE* ${D}/${datadir}/lopper/.
	cp -r ${S}/device-tree* ${D}/${datadir}/lopper/.
	cp -r ${S}/.gitignore ${D}/${datadir}/lopper/.
	if [ -f ${S}/*.dts ]; then
		cp -rf ${S}/*.dts ${D}/${datadir}/lopper/.
	fi

	ln -s ${datadirrelpath}/lopper/lopper.py ${D}/${bindir}/.
}

BBCLASSEXTEND = "native nativesdk"
