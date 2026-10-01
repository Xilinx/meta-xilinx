FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# The default xorg.conf shipped by meta-xilinx-core targets the proprietary
# Mali-400 userspace (armsoc). When the open source Lima driver is used
# instead (libmali not in DISTRO_FEATURES), install the modesetting/glamor
# config. Guarded on the mali400 machine feature so this never applies to the
# G78AE (versal-2ve-2vm) machines or to machines without a Mali-400 GPU.
LIMA_XORG = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', '', 'xorg-lima.conf', d)}"
LIMA_XORG_CONF = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${LIMA_XORG}', '', d)}"
LIMA_XORG_CONF[vardepsexclude] = "MACHINE_FEATURES"

SRC_URI:append = "${@' file://${LIMA_XORG_CONF}' if d.getVar('LIMA_XORG_CONF') else ''}"

# Content depends on the selected GPU driver.
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install:append() {
	if [ -n "${LIMA_XORG_CONF}" ]; then
		install -d ${D}${sysconfdir}/X11
		install -m 0644 ${S}/${LIMA_XORG_CONF} ${D}${sysconfdir}/X11/xorg.conf
	fi
}
