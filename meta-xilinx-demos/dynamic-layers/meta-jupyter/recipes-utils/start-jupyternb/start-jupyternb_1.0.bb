SUMMARY = "Start Jupyter at system boot"

SRC_URI = " file://start-jupyter.sh \
            file://jupyter-setup.sh \
            file://jupyter_notebook_config.py \
	    file://jupyter-setup.service \
	"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://start-jupyter.sh;beginline=2;endline=24;md5=f29b6e59838b939312f578e77087ada3"

JUPYTER_STARTUP_PACKAGES += " \
        python3-jupyter-core \
        bash \
        "

inherit update-rc.d systemd
PROVIDES = "start-jupyter"
RPROVIDES:${PN} = "start-jupyter"

RDEPENDS:${PN} = " ${JUPYTER_STARTUP_PACKAGES}"

INITSCRIPT_NAME = "jupyter-setup.sh"
INITSCRIPT_PARAMS = "start 99 S ."

SYSTEMD_PACKAGES="${PN}"
SYSTEMD_SERVICE:${PN}="jupyter-setup.service"
SYSTEMD_AUTO_ENABLE:${PN}="disable"

S = "${WORKDIR}"

FILES:${PN} += "${base_sbindir} ${systemd_user_unitdir} ${datadir}"

do_install() {

    if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
       install -d ${D}${sysconfdir}/init.d/
       install -m 0755 ${WORKDIR}/jupyter-setup.sh ${D}${sysconfdir}/init.d/jupyter-setup.sh
    fi

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/jupyter-setup.service ${D}${systemd_system_unitdir}

    install -d ${D}${systemd_user_unitdir}
    install -m 0644 ${WORKDIR}/jupyter-setup.service ${D}${systemd_user_unitdir}

    install -d ${D}${base_sbindir}
    install -m 0755 ${WORKDIR}/start-jupyter.sh ${D}${base_sbindir}/start-jupyter.sh

    install -d ${D}${sysconfdir}/jupyter/
    install -m 0644 ${WORKDIR}/jupyter_notebook_config.py ${D}${sysconfdir}/jupyter

    install -d ${D}${datadir}/example-notebooks
}
