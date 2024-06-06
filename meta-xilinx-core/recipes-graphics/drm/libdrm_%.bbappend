FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


SRC_URI:append = " \
	file://0001-PATCH-libdrm-Update-drm-header-file-with-XV15-and-XV.patch \
	file://0002-modetest-call-drmModeCrtcSetGamma-only-if-add_proper.patch \
	file://0003-modetest-Add-semiplanar-10bit-pattern-support-for-mo.patch \
	file://0004-modetest-fix-smpte-colour-pattern-issue-for-XV20-and.patch \
	file://0001-modetest-Add-YUV444-and-X403-format-support-for-mode.patch \
	file://0001-headers-Sync-with-HDR-from-v5.15.patch \
"
