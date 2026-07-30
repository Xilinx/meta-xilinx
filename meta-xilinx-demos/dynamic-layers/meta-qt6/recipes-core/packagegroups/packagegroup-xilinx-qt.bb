SUMMARY = "Packagegroup pulling in the Qt 6 runtime used by the AMD \
Xilinx graphical demos."
DESCRIPTION = "Qt packages"

# Workaround for DISTRO_FEATURES wayland only set on 64-bit ARM machines
PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup features_check

PACKAGES = "${PN} ${PN}-extended"
PROVIDES = "${PACKAGES}"

ANY_OF_DISTRO_FEATURES = "x11 fbdev wayland"

QT_PACKAGES = " \
	qtbase \
	qtbase-plugins \
	qtbase-examples \
	qtdeclarative-qmlplugins \
	qtcharts \
	${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
	"
RDEPENDS:${PN} = "${QT_PACKAGES}"

# qtlocation-plugins/-qmlplugins and qttranslations-qt-help are only RRECOMMENDS
# of their modules, so list them explicitly to keep them in the image.
QT_EXTENDED_PACKAGES = " \
	qtbase-plugins \
	qttranslations-qtbase \
	qttranslations-qt-help \
	qtconnectivity \
	qttranslations-qtconnectivity \
	qtdeclarative \
	qttranslations-qtdeclarative \
	qtimageformats-plugins \
	qtlocation \
	qtlocation-plugins \
	qtlocation-qmlplugins \
	qttranslations-qtmultimedia \
	qtmultimedia \
	qtsensors \
	qtsensors-plugins \
	qtserialport \
	qtsvg \
	qtsvg-plugins \
	qtwebsockets \
	qttranslations-qtwebsockets \
	qtwebchannel \
	qt5compat \
	qt5compat-qmlplugins \
	"

RDEPENDS:${PN}-extended = "${QT_PACKAGES} ${QT_EXTENDED_PACKAGES}"
