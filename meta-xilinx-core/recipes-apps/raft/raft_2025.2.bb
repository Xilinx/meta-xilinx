SUMMARY = "RAFT python application"
DESCRIPTION = "AMD Xilinx RAFT (Remote API Framework) python \
application: a JSON-RPC server exposing the on-target RFSoC / DFE \
driver stack so a remote host can configure and stream samples through \
the data converters and DFE PL DSP IP."
LICENSE = "MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = " \
    file://${WORKDIR}/git/LICENSE;md5=cc21c526211d34984839aa67dd16f172 \
    file://${WORKDIR}/git/docs/LICENSE;md5=d8f0ffdbc8d019bc821a5a07bdca1406 \
"
BRANCH = "2025.2"
SRC_URI = "git://github.com/Xilinx/RAFT;protocol=https;branch=${BRANCH}"
SRCREV = "5ec18717b2f60b40c7634a48c06d13725df9cfbb"

inherit update-rc.d systemd

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zcu208-zynqmp = "${MACHINE}"
COMPATIBLE_MACHINE:zcu216-zynqmp = "${MACHINE}"
COMPATIBLE_MACHINE:zynqmp-generic = "${MACHINE}"


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
    python3-gpiod \
    "

PACKAGECONFIG[raftnotebooks] = "enabled,disabled,,packagegroup-xilinx-jupyter"
PACKAGECONFIG[raftstartup] = "enabled,disabled,,librfdc librfclk libmetal"
PACKAGECONFIG[raftstartupsc] = "enabled,disabled,,python3-psutil python3-periphery python3-gpiod"

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
PACKAGECONFIG:append:zynqmp-generic = "raftstartupsc"

FILES:${PN} += " \
    ${datadir}/raft/* \
    ${datadir}/notebooks \
    ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','${sysconfdir}/*', '', d)} \
    "
