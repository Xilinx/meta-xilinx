FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = " \
		file://0001-v4l-utils-Add-support-for-new-media-bus-codes.patch \
		file://0001-v4l-utils-Add-support-for-extended-RGB-RGBA-HCWNC-an.patch \
		"
