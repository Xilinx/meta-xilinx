LINUX_VERSION = "5.4"
SRCREV ?= "22b71b41620dac13c69267d2b7898ebfb14c954e"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " \
	file://perf-fix-build-with-binutils.patch \
	file://0001-perf-bench-Share-some-global-variables-to-fix-build-.patch \
	file://0001-perf-tests-bp_account-Make-global-variable-static.patch \
	file://0001-perf-cs-etm-Move-definition-of-traceid_list-global-v.patch \
	file://0001-libtraceevent-Fix-build-with-binutils-2.35.patch \
"


