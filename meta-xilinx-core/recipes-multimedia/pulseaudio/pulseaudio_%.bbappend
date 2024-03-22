# This change appears to only affect ZynqMP configurations
# but needs to be applied generically to all aarch64 since it affects a lot of
# dependencies.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
		file://0001-default.pai.in-disable-tsched-system-timer-based-mod.patch \
		"
