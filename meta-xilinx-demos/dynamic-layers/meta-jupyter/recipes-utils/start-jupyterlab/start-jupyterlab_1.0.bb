#
# This is the jupyter-lab startup daemon
#

SUMMARY = "Start Jupyter-lab server at system boot"

SRC_URI = " \
	file://jupyter_server_config.py \
	file://jupyter-setup.sh \
	file://jupyter-setup.service \
	file://overrides.json \
	file://start-jupyter.sh \
	"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

JUPYTER_STARTUP_PACKAGES += " \
	python3-jupyterlab \
	bash \
	procps \
	"

RDEPENDS:${PN} = " ${JUPYTER_STARTUP_PACKAGES}"

PROVIDES = "start-jupyter"
RPROVIDES:${PN} = "start-jupyter"

inherit update-rc.d systemd

INITSCRIPT_NAME = "jupyter-setup.sh"
INITSCRIPT_PARAMS = "start 99 3 5 . stop 20 0 1 2 6 ."

SYSTEMD_PACKAGES="${PN}"
SYSTEMD_SERVICE:${PN}="jupyter-setup.service"
SYSTEMD_AUTO_ENABLE:${PN}="disable"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${datadir}/jupyter/lab/settings
    install -m 0644 ${WORKDIR}/overrides.json ${D}${datadir}/jupyter/lab/settings/

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
    install -m 0644 ${WORKDIR}/jupyter_server_config.py ${D}${sysconfdir}/jupyter
}

FILES:${PN} += " \
	${base_sbindir} \
	${datadir}/jupyter/lab/settings \
	${systemd_user_unitdir} \
	"
