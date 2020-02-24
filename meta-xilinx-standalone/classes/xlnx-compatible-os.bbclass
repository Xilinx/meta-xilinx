# We assume everything is Linux compatible, deviations to this
# must define their own compatible OS
COMPATIBLE_OS ?= "linux${LIBCEXTENSION}${ABIEXTENSION}"

python() {
    # Only do this check for target recipes
    if d.getVar('CLASSOVERRIDE') != "class-target":
        return

    need_os = d.getVar('COMPATIBLE_OS')
    if need_os:
        import re
        target_os = d.getVar('TARGET_OS')
        if not re.match(need_os, target_os):
            raise bb.parse.SkipRecipe("incompatible with os %s (not in COMPATIBLE_OS '%s')" % (target_os, need_os))
}
