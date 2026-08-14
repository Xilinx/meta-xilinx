GSTREAMER_PACKAGES += " \
	gst-perf \
	gst-shark \
	gstd \
	${@bb.utils.contains_any('LICENSE_FLAGS_ACCEPTED', 'commercial commercial_gstreamer1.0-omx', \
		bb.utils.contains_any('MACHINE_FEATURES', 'vcu vdu vcu2', \
			'gstreamer1.0-omx', '', d), '', d)} \
	"
