SUMMARY = "RAFT python application"
LICENSE = "MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = " \
    file://${WORKDIR}/git/LICENSE;md5=cc21c526211d34984839aa67dd16f172 \
    file://${WORKDIR}/git/docs/LICENSE;md5=d8f0ffdbc8d019bc821a5a07bdca1406 \
"
BRANCH = "2024.1"
SRC_URI = "git://github.com/Xilinx/RAFT;protocol=https;branch=${BRANCH}"
SRCREV = "87ea8f4c5ac52fcbc465f41e681fc77aaee9a285"

inherit update-rc.d systemd

S = "${WORKDIR}/git"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zcu208-zynqmp = "${MACHINE}"
COMPATIBLE_MACHINE:zcu216-zynqmp = "${MACHINE}"
COMPATIBLE_MACHINE:system-controller = "${MACHINE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

INITSCRIPT_NAME = "raft-startup"
INITSCRIPT_PARAMS = "start 99 S ."

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "raft-startup.service"
SYSTEMD_AUTO_ENABLE:${PN}="enable"

DEPENDS += "libmetal"

RDEPENDS:${PN} += "\
    python3 \
    python3-pyro4 \
    python3-cffi \
    python3-serpent \
    bash \
    "

PACKAGECONFIG[raftnotebooks] = "enabled,disabled,,packagegroup-xilinx-jupyter"
PACKAGECONFIG[raftstartup] = "enabled,disabled,,librfdc librfclk libmetal"
PACKAGECONFIG[raftstartupsc] = "enabled,disabled,,python3-psutil python3-periphery"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        SYSCONFDIR=${D}${sysconfdir}/init.d/
    else
        SYSCONFDIR=''
    fi
    oe_runmake install DESTDIR=${D}\
    NOTEBOOKS=${@bb.utils.contains('PACKAGECONFIG','raftnotebooks','enabled','', d)}\
    STARTUPSC=${@bb.utils.contains('PACKAGECONFIG','raftstartupsc','enabled','',d)}\
    STARTUP=${@bb.utils.contains('PACKAGECONFIG','raftstartup','enabled','',d)}\
    BINDIR=${D}${bindir}\
    SYSTEM_UNIT_DIR=${D}${systemd_system_unitdir}\
    SYSCONF_DIR=${SYSCONFDIR}
}

PACKAGECONFIG:append:zcu208-zynqmp = "raftnotebooks raftstartup"
PACKAGECONFIG:append:zcu216-zynqmp = "raftnotebooks raftstartup"
PACKAGECONFIG:append:system-controller = "raftstartupsc"

FILES:${PN} += " \
    ${datadir}/raft/* \
    ${datadir}/notebooks \
    ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','${sysconfdir}/*', '', d)} \
    "
