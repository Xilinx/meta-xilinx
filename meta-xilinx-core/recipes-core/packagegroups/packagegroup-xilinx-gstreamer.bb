DESCRIPTION = "GStreamer packages"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

GSTREAMER_PACKAGES = " \
	gstreamer1.0 \
	gstreamer1.0-python \
	gstreamer1.0-meta-base \
	gstreamer1.0-plugins-base \
	gstreamer1.0-plugins-good \
	gstreamer1.0-plugins-bad \
	gstreamer1.0-rtsp-server \
	gst-shark \
	gstd \
	gst-perf \
	gstreamer1.0-omx \
	"

RDEPENDS:${PN} = "${GSTREAMER_PACKAGES}"
