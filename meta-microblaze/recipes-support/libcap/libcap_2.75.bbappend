FILESEXTRAPATHS:append := ":${THISDIR}/files"
SRC_URI += " \
	file://0001-Add-support-for-some-less-mainstream-architectures.patch \
	file://0001-Extend-support-further-to-__m68k__-and-possibly-__sp.patch \
	file://0001-Be-more-systematic-using-the-kernel-signal-handler-A.patch \
	file://0001-First-attempt-at-PSX-support-for-microblaze-arc-and-.patch \
	"
