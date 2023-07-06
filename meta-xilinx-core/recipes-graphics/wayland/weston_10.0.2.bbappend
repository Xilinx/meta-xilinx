ZYNQMP_WARN_DEFAULT = "0"
ZYNQMP_WARN_DEFAULT:zynqmp = "${@bb.utils.contains('DISTRO_FEATURES', 'libmali', '1', '0', d)}"
ZYNQMP_WARN = "${@bb.utils.contains('MACHINE_FEATURES', 'mali400', '${ZYNQMP_WARN_DEFAULT}', '0', d)}"

python() {
    if d.getVar('ZYNQMP_WARN') == "1":
        raise bb.parse.SkipRecipe("Weston 10.0.0.2 requires GLES 3 interfaces which are not available when libmali enabled.  Use Weston 9.0.0.0 instead.")
}
