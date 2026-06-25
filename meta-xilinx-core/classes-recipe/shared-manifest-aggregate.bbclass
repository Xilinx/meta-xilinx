# Aggregates per-component manifest JSONs from sysroot into a single file.

MANIFEST_AGGREGATE_COMPONENTS ?= ""
MANIFEST_AGGREGATE_DEPENDS ?= ""
MANIFEST_AGGREGATE_OUTPUT ?= "${WORKDIR}/${PN}-aggregate.manifest.json"
MANIFEST_AGGREGATE_DEPLOY_NAME ?= "${PN}.manifest.json"
MANIFEST_AGGREGATE_LINK_NAME ?= ""

DEPENDS += "${MANIFEST_AGGREGATE_DEPENDS}"

python do_manifest_aggregate() {
    import json, os

    components = (d.getVar("MANIFEST_AGGREGATE_COMPONENTS") or "").split()
    if not components:
        return

    sysroot = d.getVar("RECIPE_SYSROOT") + "/sysroot-only/manifest-components"
    bundle, missing = {}, []

    for name in components:
        path = os.path.join(sysroot, name + ".json")
        if os.path.exists(path):
            try:
                with open(path) as f:
                    bundle[name] = json.load(f)
            except (json.JSONDecodeError, IOError) as e:
                bb.warn("manifest-aggregate: error reading %s: %s" % (path, e))
        else:
            missing.append(name)

    if missing:
        bb.warn("manifest-aggregate: missing %s" % ", ".join(sorted(missing)))
    if not bundle:
        return

    output = d.getVar("MANIFEST_AGGREGATE_OUTPUT")
    bb.utils.mkdirhier(os.path.dirname(output))
    with open(output, "w") as f:
        json.dump(bundle, f, indent=2, sort_keys=False)
        f.write("\n")
}

do_manifest_aggregate[vardeps] = "MANIFEST_AGGREGATE_COMPONENTS MANIFEST_AGGREGATE_OUTPUT"
addtask manifest_aggregate after do_prepare_recipe_sysroot before do_configure

do_deploy:append() {
    if [ -f "${MANIFEST_AGGREGATE_OUTPUT}" ]; then
        install -m 0644 "${MANIFEST_AGGREGATE_OUTPUT}" "${DEPLOYDIR}/${MANIFEST_AGGREGATE_DEPLOY_NAME}"
        if [ -n "${MANIFEST_AGGREGATE_LINK_NAME}" ]; then
            ln -sf "${MANIFEST_AGGREGATE_DEPLOY_NAME}" "${DEPLOYDIR}/${MANIFEST_AGGREGATE_LINK_NAME}"
        fi
    fi
}
