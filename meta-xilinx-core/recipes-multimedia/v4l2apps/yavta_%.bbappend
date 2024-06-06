FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
		file://0001-Add-support-for-3-planar-YUV444P-8bpc.patch \
		file://0002-Add-support-3-planar-YUV-444-10bpc-pixel-format-in-c.patch \
		"
