SUMMARY = "Device tree lopper"
DESCRIPTION = "Tool to subset a system device tree"
SECTION = "bootloader"
LICENSE = "BSD-3-Clause"
DEPENDS += "python3-dtc"

RDEPENDS_${PN} += "python3-dtc"

SRC_URI = "git://gitenterprise.xilinx.com/brucea/lopper.git"

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=fe0b8a4beea8f0813b606d15a3df3d3c"

SRCREV = "70cd4578c8a0ca202ea063cb83768267ea69dd2a"

S = "${WORKDIR}/git"

do_configure() {
	:
}

do_compile() {
	sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' lopper.py
}

do_install() {
	datadirrelpath=${@os.path.relpath(d.getVar('datadir'), d.getVar('bindir'))}

	mkdir -p ${D}/${bindir}
	mkdir -p ${D}/${datadir}/lopper
	cp -r ${S}/* ${D}/${datadir}/lopper/.
        ln -s ${datadirrelpath}/lopper/lopper.py ${D}/${bindir}/.
}

BBCLASSEXTEND = "native nativesdk"
