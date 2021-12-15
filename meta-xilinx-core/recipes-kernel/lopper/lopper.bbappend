DEPENDS = " \
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

RDEPENDS:${PN} += " \
    python3-flask \
    python3-flask-restful \
    python3-six \
    python3-pandas \
    python3-ruamel-yaml \
    python3-anytree \
    python3-pyyaml \
"

SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=master"
SRCREV = "4fc085c4be031996e7f48dcaf03d0782989c8d58"

do_install() {
    install -d "${D}/${bindir}"
    install -d "${D}/${datadir}/${BPN}"

    install -m 0644 "${S}/README" "${D}/${datadir}/${BPN}"
    install -m 0644 "${S}/README-architecture.txt" "${D}/${datadir}/${BPN}"
    install -m 0644 "${S}/README.pydoc" "${D}/${datadir}/${BPN}"
    install -m 0644 "${S}/LICENSE.md" "${D}/${datadir}/${BPN}"

    install -d "${D}/${datadir}/${BPN}/assists"
    #install -m 0644 "${S}/assists/"* "${D}/${datadir}/${BPN}/assists/"
    cp -r "${S}/assists/"* "${D}/${datadir}/${BPN}/assists/"

    install -d "${D}/${datadir}/${BPN}/lops"
    install -m 0644 "${S}/lops/"* "${D}/${datadir}/${BPN}/lops/"

    install -d "${D}/${datadir}/${BPN}/device-trees"
    install -m 0644 "${S}/device-trees/"* "${D}/${datadir}/${BPN}/device-trees/"

    install -m 0644 "${S}/"lopper.ini "${D}/${datadir}/${BPN}/"

    install -m 0755 "${S}/"lopper*.py "${D}/${datadir}/${BPN}/"
    sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' ${D}/${datadir}/${BPN}/lopper.py
    sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' ${D}/${datadir}/${BPN}/lopper_sanity.py

    datadir_relpath=${@os.path.relpath(d.getVar('datadir'), d.getVar('bindir'))}
    ln -s "${datadir_relpath}/${BPN}/lopper.py" "${D}/${bindir}/"
}
