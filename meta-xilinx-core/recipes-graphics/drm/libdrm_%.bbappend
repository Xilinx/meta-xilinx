FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


SRC_URI:append = " \
	file://0001-libdrm-Update-drm-header-file-with-XV15-and-XV20.patch \
	file://0002-modetest-Add-semiplanar-10bit-pattern-support-for-mo.patch \
	file://0003-modetest-fix-smpte-colour-pattern-issue-for-XV20-and.patch \
	file://0004-modetest-Add-YUV444-and-X403-format-support-for-mode.patch \
	file://0005-Update-libdrm-drm_fourcc.h-to-add-VCU2-tiled-formats.patch \
	file://0006-libdrm-Add-support-for-some-YUV-DRM-formats.patch \
	file://0007-modetest-Add-pattern-support-for-xilinx-tiled-format.patch \
"
