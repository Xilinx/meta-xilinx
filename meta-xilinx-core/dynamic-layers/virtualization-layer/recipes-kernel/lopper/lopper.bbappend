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
SRCREV = "17350a773a73c426a826e32e4e093effc718ecf5"

do_install() {
    install -d "${D}/${bindir}"
    install -d "${D}/${datadir}/${BPN}"

    install -m 0644 "${S}/README.md" "${D}/${datadir}/${BPN}"
    install -m 0644 "${S}/README-architecture.md" "${D}/${datadir}/${BPN}"
    install -m 0644 "${S}/README.pydoc" "${D}/${datadir}/${BPN}"
    install -m 0644 "${S}/LICENSE.md" "${D}/${datadir}/${BPN}"

    install -d "${D}/${datadir}/${BPN}/assists"
    cp -r "${S}/lopper/assists/"* "${D}/${datadir}/${BPN}/assists/"

    install -d "${D}/${datadir}/${BPN}/lops"
    install -m 0644 "${S}/lopper/lops/"* "${D}/${datadir}/${BPN}/lops/"

    install -d "${D}/${datadir}/${BPN}/device-trees"
    install -m 0644 "${S}/device-trees/"* "${D}/${datadir}/${BPN}/device-trees/"

    install -m 0644 "${S}/lopper/"lopper.ini "${D}/${datadir}/${BPN}/"

    install -m 0755 "${S}/"lopper*.py "${D}/${datadir}/${BPN}/"
    sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' ${D}/${datadir}/${BPN}/lopper.py
    sed -i 's,#!/usr/bin/python3,#!/usr/bin/env python3,' ${D}/${datadir}/${BPN}/lopper_sanity.py

    datadir_relpath=${@os.path.relpath(d.getVar('datadir'), d.getVar('bindir'))}
    ln -s "${datadir_relpath}/${BPN}/lopper.py" "${D}/${bindir}/"
}
