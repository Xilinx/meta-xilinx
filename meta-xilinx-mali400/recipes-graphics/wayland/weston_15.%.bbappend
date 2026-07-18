FILESEXTRAPATHS:prepend:zynqmp := "${THISDIR}/files:"

require weston.inc

ZYNQMP_WARN_DEFAULT = "0"
ZYNQMP_WARN_DEFAULT:zynqmp = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', '1', '0', d)}"
ZYNQMP_WARN = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${ZYNQMP_WARN_DEFAULT}', '0', d)}"

python() {
    if d.getVar('ZYNQMP_WARN') == "1":
        raise bb.parse.SkipRecipe("Weston %s requires GLES 3 interfaces which are not available when libmali enabled.  Use Weston 9.0.0.0 instead." % (d.getVar('PV')))
}

# On the open (mesa) driver path, weston 15 can fail output init when the DRM
# primary plane exposes only the alpha format (ARGB8888 / AR24) and not the
# default opaque XRGB8888 (XR24). Add a native XR24->AR24 fallback so the output
# still comes up. See the patch header for the full rationale.
SRC_URI:append:zynqmp = " file://0001-backend-drm-fall-back-to-alpha-format-when-primary-plane.patch"

# Applying a zynqmp-only source patch makes the resulting package no longer
# generic, so it must be MACHINE_ARCH for the machines that get the patch. On
# the open (mesa) path weston.inc leaves PACKAGE_ARCH tune-based (the libmali
# MACHINE_ARCH trigger there does not fire), so without this a same-tune
# non-zynqmp board could share sstate with the patched zynqmp build. Since the
# patch is applied :zynqmp, scope the arch override :zynqmp as well.
PACKAGE_ARCH:zynqmp = "${MACHINE_ARCH}"
