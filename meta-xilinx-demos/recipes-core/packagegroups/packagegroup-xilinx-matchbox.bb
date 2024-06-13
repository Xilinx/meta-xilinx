DESCRIPTION = "Matchbox related packages"

inherit packagegroup features_check

REQUIRED_DISTRO_FEATURES = "x11"

FILEMANAGER ?= "pcmanfm"

MATCHBOX_PACKAGES = " \
	matchbox-config-gtk \
	matchbox-desktop \
	matchbox-keyboard \
	matchbox-keyboard-applet \
	matchbox-panel-2 \
	matchbox-session \
	matchbox-terminal \
	matchbox-theme-sato \
	matchbox-session-sato \
	matchbox-wm \
	settings-daemon \
	adwaita-icon-theme \
	l3afpad \
	${FILEMANAGER} \
	shutdown-desktop \
	${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio-server pulseaudio-client-conf-sato pulseaudio-misc', '', d)} \
	"

RDEPENDS:${PN} = "packagegroup-core-x11 ${MATCHBOX_PACKAGES}"
