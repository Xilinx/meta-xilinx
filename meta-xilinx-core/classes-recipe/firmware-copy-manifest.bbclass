# Copies manifest from multiconfig deploy to sysroot for aggregation.
# Set FW_MANIFEST_SRC to source path base (without .manifest.json extension).
# Set FW_MANIFEST_NAME to component name for output files.

SYSROOT_DIRS += "/sysroot-only"

do_copy_manifest_sysroot() {
    src="${FW_MANIFEST_SRC}.manifest.json"
    dst="${D}/sysroot-only/manifest-components/${FW_MANIFEST_NAME}.json"
    install -d ${D}/sysroot-only/manifest-components
    if [ -e "$src" ]; then
        install -m 0644 "$src" "$dst"
    else
        bbwarn "Manifest not found: $src"
    fi
}

do_copy_manifest_sysroot[vardeps] = "FW_MANIFEST_SRC FW_MANIFEST_NAME"

do_install[postfuncs] += "do_copy_manifest_sysroot"
