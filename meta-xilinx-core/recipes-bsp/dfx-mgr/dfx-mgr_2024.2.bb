SUMMARY  = "Xilinx dfx-mgr libraries"
DESCRIPTION = "Xilinx Runtime User Space Libraries and Binaries"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d67bcef754e935bf77b6d7051bd62b5e"

REPO ?= "git://github.com/Xilinx/dfx-mgr.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

BRANCH = "xlnx_rel_v2024.2"
SRCREV = "839e8e646c54a63326e36c48a7bd879f5e8efa31"
SOMAJOR = "2"
SOMINOR = "0"
SOVERSION = "${SOMAJOR}.${SOMINOR}"

# Don't allow building for Zynq and Microblaze MACHINE unless it is supported.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

S = "${WORKDIR}/git"

inherit cmake update-rc.d systemd

DEPENDS += " libwebsockets inotify-tools libdfx zocl libdrm systemd"
RDEPENDS:${PN} += " freeipmi"
EXTRA_OECMAKE += " \
               -DCMAKE_SYSROOT:PATH=${RECIPE_SYSROOT} \
		"

INITSCRIPT_NAME = "dfx-mgr.sh"
INITSCRIPT_PARAMS = "start 99 S ."

SYSTEMD_PACKAGES="${PN}"
SYSTEMD_SERVICE:${PN}="dfx-mgr.service"
SYSTEMD_AUTO_ENABLE:${PN}="enable"


do_install(){
	install -d ${D}${bindir}
	install -d ${D}${libdir}
	install -d ${D}${includedir}
	install -d ${D}${base_libdir}/firmware/xilinx
	install -d ${D}${sysconfdir}/dfx-mgrd

	cp ${B}/example/sys/linux/dfx-mgrd-static ${D}${bindir}/dfx-mgrd
	cp ${B}/example/sys/linux/dfx-mgr-client-static ${D}${bindir}/dfx-mgr-client
	chrpath -d ${D}${bindir}/dfx-mgrd
	chrpath -d ${D}${bindir}/dfx-mgr-client
	install -m 0644 ${S}/src/dfxmgr_client.h ${D}${includedir}

       	oe_soinstall ${B}/src/libdfx-mgr.so.${SOVERSION} ${D}${libdir}

	install -m 0755 ${S}/src/daemon.conf ${D}${sysconfdir}/dfx-mgrd/

 	if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
		install -d ${D}${sysconfdir}/init.d/
		install -m 0755 ${S}/src/dfx-mgr.sh ${D}${sysconfdir}/init.d/
	fi

	install -m 0755 ${S}/src/dfx-mgr.sh ${D}${bindir}
	install -m 0755 ${S}/src/scripts/xlnx-firmware-detect ${D}${bindir}

	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${S}/src/dfx-mgr.service ${D}${systemd_system_unitdir}
}

PACKAGES =+ "libdfx-mgr"

FILES:${PN} += "${base_libdir}/firmware/xilinx"
FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES','sysvinit','${sysconfdir}/init.d/dfx-mgr.sh', '', d)} ${systemd_system_unitdir}"
FILES:libdfx-mgr = "${libdir}/libdfx-mgr.so.${SOVERSION} ${libdir}/libdfx-mgr.so.${SOMAJOR}"
