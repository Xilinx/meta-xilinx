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
	qtquickcontrols-qmlplugins \
	qtcharts \
	${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
	"
RDEPENDS:${PN} = "${QT_PACKAGES}"

QT_EXTENDED_PACKAGES = " \
	ruby \
	qtbase-mkspecs \
	qtbase-plugins \
	qtsystems-mkspecs \
	qttranslations-qtbase \
	qttranslations-qthelp \
	qtconnectivity-mkspecs \
	qttranslations-qtconnectivity \
	qtdeclarative-mkspecs \
	qttranslations-qtdeclarative \
	qtimageformats-plugins \
	qtlocation-mkspecs \
	qtlocation-plugins \
	qttranslations-qtmultimedia \
	qtscript-mkspecs \
	qttranslations-qtscript \
	qtsensors-mkspecs \
	qtsensors-plugins \
	qtserialport-mkspecs \
	qtsvg-mkspecs \
	qtsvg-plugins \
	qtwebsockets-mkspecs \
	qttranslations-qtwebsockets \
	qtwebchannel-mkspecs \
	qtxmlpatterns-mkspecs \
	qttranslations-qtxmlpatterns \
	qtwebkit-mkspecs \
	${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'qtx11extras', '', d)} \
	qtgraphicaleffects-qmlplugins \
	"

RDEPENDS:${PN}-extended = "${QT_PACKAGES} ${QT_EXTENDED_PACKAGES}"
