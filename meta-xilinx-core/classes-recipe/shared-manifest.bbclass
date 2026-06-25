# Generates per-component JSON manifest with branch/srcrev metadata.
# Stages to sysroot for aggregation. Only deploys to DEPLOYDIR for
# baremetal builds (multiconfig producers) where sysroot is not shared.
#
# Variables:
#   MANIFEST_COMPONENT_NAME - Component identifier (default: ${PN})
#   MANIFEST_BRANCH_VARS - Space-separated list of vars to check for branch
#   MANIFEST_SRCREV_VARS - Space-separated list of vars to check for srcrev
#   MANIFEST_COMPONENT_FIELDS - Space-separated list of custom field names
#   MANIFEST_COMPONENT_FIELD_<name> - Value for custom field <name>

SYSROOT_DIRS += "/sysroot-only"

MANIFEST_COMPONENT_NAME ??= "${PN}"
MANIFEST_BRANCH_VARS ?= "BRANCH SRCBRANCH UBRANCH MANIFEST_BRANCH"
MANIFEST_SRCREV_VARS ?= "SRCREV MANIFEST_SRCREV"
MANIFEST_COMPONENT_FIELDS ?= ""

def _shared_manifest_entry(d):
    e = {}
    for key, varlist, skip in [
        ("branch", (d.getVar("MANIFEST_BRANCH_VARS") or "").split(), set()),
        ("srcrev", (d.getVar("MANIFEST_SRCREV_VARS") or "").split(), {"INVALID"}),
    ]:
        for v in varlist:
            val = d.getVar(v)
            if not val or val in skip:
                continue
            if val.startswith("AUTOINC"):
                bb.debug(1, "shared-manifest: %s=%s starts with AUTOINC, skipping" % (v, val))
                continue
            e[key] = val
            break
    for field in (d.getVar("MANIFEST_COMPONENT_FIELDS") or "").split():
        val = d.getVar("MANIFEST_COMPONENT_FIELD_%s" % field)
        if val:
            e[field] = val
    return e

python do_shared_manifest_sysroot() {
    import json, os
    e = _shared_manifest_entry(d)
    if not e:
        bb.debug(1, "shared-manifest: %s has no metadata, skipping" % d.getVar("PN"))
        return
    name = d.getVar("MANIFEST_COMPONENT_NAME")
    path = os.path.join(d.getVar("D"), "sysroot-only/manifest-components", name + ".json")
    bb.utils.mkdirhier(os.path.dirname(path))
    with open(path, "w") as f:
        json.dump(e, f, indent=2, sort_keys=False)
        f.write("\n")
}

python do_shared_manifest_deploy() {
    import json, os
    e = _shared_manifest_entry(d)
    if not e:
        return
    name = d.getVar("MANIFEST_COMPONENT_NAME")
    path = os.path.join(d.getVar("DEPLOYDIR"), name + ".manifest.json")
    bb.utils.mkdirhier(os.path.dirname(path))
    with open(path, "w") as f:
        json.dump(e, f, indent=2, sort_keys=False)
        f.write("\n")
}

# Static vardeps - dynamic computation removed to avoid AUTOREV parse-time conflicts
do_shared_manifest_sysroot[vardeps] = "MANIFEST_COMPONENT_NAME MANIFEST_BRANCH_VARS MANIFEST_SRCREV_VARS MANIFEST_COMPONENT_FIELDS"
do_shared_manifest_deploy[vardeps] = "MANIFEST_COMPONENT_NAME MANIFEST_BRANCH_VARS MANIFEST_SRCREV_VARS MANIFEST_COMPONENT_FIELDS"


do_install[postfuncs] += "do_shared_manifest_sysroot"

# Auto-enable deploy for baremetal builds (multiconfig producers)
MANIFEST_DEPLOY_BAREMETAL = "${@"do_shared_manifest_deploy" if d.getVar("TCLIBC") in ["newlib", "baremetal"] else ""}"
do_deploy[postfuncs] += "${MANIFEST_DEPLOY_BAREMETAL}"
