DESCRIPTION = "ASLA supported packages"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

ALSA_PACKAGES = " \
	libasound \
	alsa-plugins \
	alsa-tools \
	alsa-utils \
	alsa-utils-scripts \
	${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio-server pulseaudio-client-conf-sato pulseaudio-misc', '', d)} \
	"
RDEPENDS:${PN} = "${ALSA_PACKAGES}"
