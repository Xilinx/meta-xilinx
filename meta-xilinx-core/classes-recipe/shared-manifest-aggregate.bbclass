# Aggregates per-component manifest data into a single file.
#
# Each tracked component gets an entry built from two sources:
#   - SCM / semantic provenance from the producer's sysroot fragment
#     (branch, srcrev, or values like a PDI uuid), if the producer emits one;
#   - content identity (file + sha256) hashed here, at assembly time, from the
#     partition images the recipe lists in MANIFEST_COMPONENT_IMAGES
#     ("<component>=<path>" tokens).
# Hashing the recorded image (resolved through symlinks) keeps the manifest
# tied to the exact bytes that land in BOOT.BIN, with no per-recipe hashing.

MANIFEST_AGGREGATE_COMPONENTS ?= ""
MANIFEST_AGGREGATE_DERIVED ?= ""
MANIFEST_COMPONENT_IMAGES ?= ""
MANIFEST_AGGREGATE_DEPENDS ?= ""
MANIFEST_AGGREGATE_OUTPUT ?= "${WORKDIR}/${PN}-aggregate.manifest.json"
MANIFEST_AGGREGATE_DEPLOY_NAME ?= "${PN}.manifest.json"
MANIFEST_AGGREGATE_LINK_NAME ?= ""

DEPENDS += "${MANIFEST_AGGREGATE_DEPENDS}"

python do_manifest_aggregate() {
    import json, os

    output = d.getVar("MANIFEST_AGGREGATE_OUTPUT")

    def _write(obj):
        # Rewrite from scratch so a rebuild never leaves stale content behind.
        bb.utils.mkdirhier(os.path.dirname(output))
        with open(output, "w") as f:
            json.dump(obj, f, indent=2, sort_keys=False)
            f.write("\n")

    # Union DERIVED (auto from BIF) + COMPONENTS (explicit, e.g. rollback-counter);
    # two vars so an override-append on one does not clobber the other.
    components = []
    for name in ((d.getVar("MANIFEST_AGGREGATE_DERIVED") or "").split() +
                 (d.getVar("MANIFEST_AGGREGATE_COMPONENTS") or "").split()):
        if name not in components:
            components.append(name)
    if not components:
        # A real BOOT.BIN always has at least one partition; empty means misconfig.
        bb.fatal("manifest-aggregate: no components to track for %s; check "
                 "BIF_PARTITION_ATTR / MANIFEST_AGGREGATE_*" % d.getVar("PN"))

    sysroot = d.getVar("RECIPE_SYSROOT") + "/sysroot-only/manifest-components"
    bundle, missing = {}, []

    # component -> partition image path bootgen embeds ("comp=path" tokens).
    images = {}
    for tok in (d.getVar("MANIFEST_COMPONENT_IMAGES") or "").split():
        if "=" in tok:
            comp, path = tok.split("=", 1)
            images[comp] = path

    for name in components:
        entry = {}

        # Producer provenance fragment (SCM / semantic), if any.
        frag = os.path.join(sysroot, name + ".json")
        if os.path.exists(frag):
            try:
                with open(frag) as f:
                    entry.update(json.load(f))
            except (json.JSONDecodeError, IOError) as e:
                bb.warn("manifest-aggregate: error reading %s: %s" % (frag, e))

        # Content identity from the partition image bootgen embeds.
        img = images.get(name)
        if img:
            real = os.path.realpath(img)
            if os.path.exists(real):
                entry["file"] = os.path.basename(real)
                entry["sha256"] = bb.utils.sha256_file(real)
            else:
                bb.warn("manifest-aggregate: image for '%s' not found: %s" % (name, img))

        if entry:
            bundle[name] = entry
        else:
            missing.append(name)

    # A component with neither provenance fragment nor image ships untracked.
    for name in sorted(missing):
        bb.warn("manifest-aggregate: '%s' is in BOOT.BIN but not tracked in the "
                "manifest; wire a producer fragment or MANIFEST_COMPONENT_IMAGES" % name)
    _write(bundle)
}

do_manifest_aggregate[vardeps] = "MANIFEST_AGGREGATE_DERIVED MANIFEST_AGGREGATE_COMPONENTS MANIFEST_AGGREGATE_OUTPUT"
addtask manifest_aggregate after do_prepare_recipe_sysroot before do_configure

do_deploy:append() {
    if [ -f "${MANIFEST_AGGREGATE_OUTPUT}" ]; then
        install -m 0644 "${MANIFEST_AGGREGATE_OUTPUT}" "${DEPLOYDIR}/${MANIFEST_AGGREGATE_DEPLOY_NAME}"
        if [ -n "${MANIFEST_AGGREGATE_LINK_NAME}" ]; then
            ln -sf "${MANIFEST_AGGREGATE_DEPLOY_NAME}" "${DEPLOYDIR}/${MANIFEST_AGGREGATE_LINK_NAME}"
        fi
    fi
}
