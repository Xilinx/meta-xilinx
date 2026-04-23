#
# Copyright (C) 2026, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# Unified SPI/OSPI image generation class.
#
# Usage in recipe:
#   inherit amd-spi-image deploy image-artifact-names
#
#   SPI_IMAGE_SIZE = "0x1000_0000"
#   SPI_COMPONENTS = "imgsel bootbin"
#
#   SPI_OFFSET[imgsel]  = "0x0 0x6_0000"
#   SPI_SOURCE[imgsel]  = "image-selector-${MACHINE}.bin"
#
#   SPI_OFFSET[bootbin] = "0x158_0000 0x87C_0000"
#   SPI_SOURCE[bootbin] = "boot.bin"
#
#   SPI_DEPLOY_DEPENDS = "virtual/imgsel virtual/boot-bin"
#
# Source types:
#   - Bare filename: resolved from ${DEPLOY_DIR_IMAGE}
#   - Full path: used as-is
#   - @empty: reserved space, stays 0xFF
#   - @inline:<name>: built-in generators (version, sha256) or custom
#   - @manifest: embedded manifest (layout info, generated during compile)
#
# Custom inline generators:
#   Define a function named spi_inline_<name>(d, comp, buf) that returns bytes.
#   Then use @inline:<name> as the source.
#

INHIBIT_DEFAULT_DEPS = "1"

SPI_IMAGE_SIZE ??= ""
SPI_OUTPUT_SIZE ??= "${SPI_IMAGE_SIZE}"
SPI_COMPONENTS ??= ""
SPI_VERSION ??= "${PN}-${MACHINE}-${PV}"
SPI_DEPLOY_DEPENDS ??= ""

# Only do_deploy artifacts are consumed (from DEPLOY_DIR_IMAGE), not sysroot
do_compile[depends] += "${@' '.join(x + ':do_deploy' for x in (d.getVar('SPI_DEPLOY_DEPENDS') or '').split())}"

IMAGE_NAME_SUFFIX = ""

def spi_parse_layout(d):
    """Parse and validate SPI_COMPONENTS and varflags into component list.

    Returns (components, output_size, flash_size) where:
    - output_size: size of generated image file (SPI_OUTPUT_SIZE)
    - flash_size: physical flash size (SPI_IMAGE_SIZE), may be larger

    Validates no overlaps, all offsets within bounds, and calculates
    the maximum size available for each component.
    """
    components = (d.getVar("SPI_COMPONENTS") or "").split()
    if not components:
        bb.fatal("SPI_COMPONENTS is empty - set it in your recipe to list flash components")

    flash_size_str = d.getVar("SPI_IMAGE_SIZE")
    if not flash_size_str:
        bb.fatal("SPI_IMAGE_SIZE is not set - set the physical flash size in the machine config")
    flash_size = int(flash_size_str, 0)

    output_size_str = d.getVar("SPI_OUTPUT_SIZE")
    if not output_size_str:
        bb.fatal("SPI_OUTPUT_SIZE is not set (defaults to SPI_IMAGE_SIZE which is also unset)")
    output_size = int(output_size_str, 0)

    # Warn if output size exceeds flash size
    if output_size > flash_size:
        bb.warn("SPI_OUTPUT_SIZE (0x%x) exceeds SPI_IMAGE_SIZE (0x%x)" % (output_size, flash_size))

    offset_flags = d.getVarFlags("SPI_OFFSET") or {}
    source_flags = d.getVarFlags("SPI_SOURCE") or {}

    result = []
    for name in components:
        offset_str = offset_flags.get(name)
        source = source_flags.get(name)

        if not offset_str:
            bb.fatal("SPI_OFFSET[%s] is not set - add SPI_OFFSET[%s] to your recipe" % (name, name))
        if not source:
            bb.fatal("SPI_SOURCE[%s] is not set - add SPI_SOURCE[%s] to your recipe" % (name, name))

        # Expand variables in offset and source (supports indirection)
        offset_str = d.expand(offset_str)
        source = d.expand(source)

        # Parse space-separated offsets (multi-offset support)
        offsets = []
        for o in offset_str.split():
            offsets.append(int(o.replace("_", ""), 0))

        result.append({
            "name": name,
            "offsets": offsets,
            "source": source,
        })

    # Validate layout: check bounds, overlaps, and calculate max_size
    regions = []
    for comp in result:
        for offset in comp["offsets"]:
            regions.append((offset, comp["name"], comp))
    regions.sort(key=lambda x: x[0])

    prev_offset = None
    prev_name = None
    for i, (offset, name, comp) in enumerate(regions):
        if offset >= output_size:
            bb.fatal("SPI component '%s' offset 0x%x exceeds SPI_OUTPUT_SIZE 0x%x" % (name, offset, output_size))
        if prev_offset is not None and offset == prev_offset:
            bb.fatal("SPI overlap: '%s' and '%s' both at offset 0x%x" % (prev_name, name, offset))
        prev_offset = offset
        prev_name = name

    offset_to_next = {}
    for i, (offset, name, comp) in enumerate(regions):
        next_offset = regions[i + 1][0] if i + 1 < len(regions) else output_size
        offset_to_next[offset] = next_offset - offset
    for comp in result:
        comp["max_size"] = min(offset_to_next[o] for o in comp["offsets"])

    return result, output_size, flash_size


def spi_validate_files(d, components):
    """Validate source files exist and fit in allocated space."""
    import os

    deploy_dir = d.getVar("DEPLOY_DIR_IMAGE")

    for comp in components:
        source = comp["source"]

        if source == "@empty":
            comp["size"] = 0
            comp["path"] = None
            comp["reserved"] = True
        elif source.startswith("@inline:"):
            comp["generator"] = source[8:]
            comp["path"] = None
        elif source == "@manifest":
            comp["manifest"] = True
            comp["path"] = None
        else:
            # File source - resolve path
            if source.startswith("/"):
                path = source
            else:
                path = deploy_dir + "/" + source

            if not os.path.exists(path):
                bb.fatal("SPI component '%s': source file not found: %s (check SPI_DEPLOY_DEPENDS)" % (comp["name"], path))

            size = os.path.getsize(path)
            if size > comp["max_size"]:
                bb.fatal("SPI component '%s': file size %d exceeds allocated space %d (source: %s)" %
                         (comp["name"], size, comp["max_size"], path))
            comp["size"] = size
            comp["path"] = path


def spi_run_generator(d, name, comp, buf):
    """Run an inline generator by name."""
    import hashlib

    if name == "version":
        return ((d.getVar("SPI_VERSION") or "") + "\x00").encode("utf-8")
    elif name == "sha256":
        return hashlib.sha256(buf.getbuffer()).digest()

    # Custom generator by naming convention (spi_inline_<name>)
    func_name = "spi_inline_" + name
    if func_name in globals():
        return globals()[func_name](d, comp, buf)

    bb.fatal("Unknown SPI inline generator: '%s' - define spi_inline_%s(d, comp, buf) in your recipe" % (name, name))


def spi_write_component(comp, data, buf):
    """Write component data to buffer and validate size."""
    if len(data) > comp["max_size"]:
        bb.fatal("SPI component '%s': size %d exceeds allocated space %d" %
                 (comp["name"], len(data), comp["max_size"]))
    comp["size"] = len(data)
    comp["data"] = data
    for offset in comp["offsets"]:
        buf.seek(offset)
        buf.write(data)


def spi_assemble(d, components, output_size, manifest_data=None):
    """Assemble SPI image from components."""
    import io

    buf = io.BytesIO()
    buf.write(b'\xFF' * output_size)

    sha256_comps = []

    for comp in components:
        path = comp.get("path")
        generator = comp.get("generator")

        if comp.get("data") is not None:
            # Pre-loaded by caller (e.g. so spi_generate_manifest sees the bytes).
            data = comp["data"]
        elif path:
            with open(path, "rb") as f:
                data = f.read()
        elif comp.get("manifest"):
            if manifest_data is None:
                bb.fatal("SPI component '%s': manifest data not provided" % comp["name"])
            data = manifest_data
        elif generator:
            if generator == "sha256":
                sha256_comps.append(comp)
                continue
            data = spi_run_generator(d, generator, comp, buf)
        else:
            continue

        spi_write_component(comp, data, buf)

    # SHA256 must run after all other content is written
    for comp in sha256_comps:
        data = spi_run_generator(d, "sha256", comp, buf)
        spi_write_component(comp, data, buf)

    return buf


def spi_generate_manifest(d, components, output_size, flash_size):
    """Generate the canonical manifest dict.

    Single source of truth: both the embedded copy and the sidecar
    are produced from this. The sidecar adds image.sha256 (the only
    field that genuinely cannot be self-referential); see do_compile().

    Per-component sha256 is emitted whenever comp["data"] is set, so
    callers must pre-load file-backed components before invoking this.
    """
    import hashlib
    import os

    manifest = {
        "image": {
            "name": d.getVar("IMAGE_LINK_NAME"),
            "machine": d.getVar("MACHINE"),
            "size": "0x%x" % output_size,
        },
        "layout": [],
    }

    # Add flash_size if different from output size
    if flash_size != output_size:
        manifest["image"]["flash_size"] = "0x%x" % flash_size

    # Add optional image metadata
    if d.getVar("DISTRO"):
        manifest["image"]["distro"] = d.getVar("DISTRO")
    if d.getVar("DISTRO_VERSION"):
        manifest["image"]["distro_version"] = d.getVar("DISTRO_VERSION")
    if d.getVar("SPI_VERSION"):
        manifest["image"]["version"] = d.getVar("SPI_VERSION")
    if d.getVar("SPI_FLASH_TYPE"):
        manifest["image"]["flash_type"] = d.getVar("SPI_FLASH_TYPE")
    if d.getVar("SPI_ERASE_BLOCK_SIZE"):
        manifest["image"]["erase_block_size"] = "0x%x" % int(d.getVar("SPI_ERASE_BLOCK_SIZE"), 0)

    # Build list of entries, one per offset
    entries = []
    for comp in components:
        for offset in comp["offsets"]:
            entry = {
                "name": comp["name"],
                "offset": "0x%x" % offset,
                "max_size": "0x%x" % comp["max_size"],
            }

            if comp.get("size"):
                entry["size"] = "0x%x" % comp["size"]

            if comp.get("data") is not None:
                entry["sha256"] = hashlib.sha256(comp["data"]).hexdigest()

            if comp.get("path"):
                entry["source"] = os.path.basename(comp["path"])

            if comp.get("generator"):
                entry["generator"] = comp["generator"]

            if comp.get("reserved"):
                entry["type"] = "reserved"

            entries.append((offset, entry))

    # Sort by offset
    entries.sort(key=lambda x: x[0])
    manifest["layout"] = [e[1] for e in entries]

    return manifest


python do_compile() {
    import json
    import hashlib
    import os

    output_dir = d.getVar("B")
    image_name = d.getVar("IMAGE_NAME")

    # Parse and validate layout
    components, output_size, flash_size = spi_parse_layout(d)
    spi_validate_files(d, components)

    # Pre-read file-backed components so the manifest can hash them.
    # spi_assemble() will reuse this data instead of re-reading.
    for comp in components:
        if comp.get("path"):
            with open(comp["path"], "rb") as f:
                comp["data"] = f.read()

    # Build the manifest exactly once. Embedded and sidecar are derived
    # from this same dict so they cannot drift; only image.sha256 is
    # added below for the sidecar (it is intrinsically self-referential
    # and must be applied after the image is assembled).
    manifest = spi_generate_manifest(d, components, output_size, flash_size)

    manifest_data = None
    if any(c.get("manifest") for c in components):
        manifest_data = (json.dumps(manifest, indent=2) + "\n").encode("utf-8")

    buf = spi_assemble(d, components, output_size, manifest_data)

    # Sidecar manifest = canonical manifest + image.sha256.
    # This is the ONLY permitted divergence from the embedded copy.
    sidecar = dict(manifest)
    sidecar["image"] = dict(manifest["image"])
    sidecar["image"]["sha256"] = hashlib.sha256(buf.getbuffer()).hexdigest()

    # Write image
    image_path = output_dir + "/" + image_name + ".bin"
    with open(image_path, "wb") as f:
        f.write(buf.getbuffer())

    # Write manifest
    manifest_path = output_dir + "/" + image_name + ".manifest.json"
    with open(manifest_path, "w") as f:
        json.dump(sidecar, f, indent=2)
        f.write("\n")

    bb.note("SPI image: %s (%d bytes)" % (image_path, output_size))
    bb.note("Manifest: %s" % manifest_path)
}

do_compile[vardeps] += "SPI_COMPONENTS SPI_IMAGE_SIZE SPI_OUTPUT_SIZE SPI_VERSION SPI_DEPLOY_DEPENDS \
    SPI_FLASH_TYPE SPI_ERASE_BLOCK_SIZE"
do_compile[vardepsexclude] += "DATETIME"

# Required by the image deployment
IMGDEPLOYDIR ??= "${DEPLOYDIR}"

do_deploy() {
    install -Dm 644 ${B}/${IMAGE_NAME}.bin ${DEPLOYDIR}/${IMAGE_NAME}.bin
    ln -sf ${IMAGE_NAME}.bin ${DEPLOYDIR}/${IMAGE_LINK_NAME}.bin

    install -Dm 644 ${B}/${IMAGE_NAME}.bin.gz ${DEPLOYDIR}/${IMAGE_NAME}.bin.gz
    ln -sf ${IMAGE_NAME}.bin.gz ${DEPLOYDIR}/${IMAGE_LINK_NAME}.bin.gz

    install -Dm 644 ${B}/${IMAGE_NAME}.manifest.json ${DEPLOYDIR}/${IMAGE_NAME}.manifest.json
    ln -sf ${IMAGE_NAME}.manifest.json ${DEPLOYDIR}/${IMAGE_LINK_NAME}.manifest.json
}

addtask deploy after do_compile before do_build

do_compress() {
    gzip -f -9 -n -c --rsyncable ${B}/${IMAGE_NAME}.bin > ${B}/${IMAGE_NAME}.bin.gz
}

addtask compress after do_compile before do_deploy

EXPORT_FUNCTIONS do_compile

# Generate a qemuboot.conf file for this output
inherit ${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'qemuboot-xilinx', '', d)}
do_deploy[postfuncs] += "${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'do_write_qemuboot_conf', '', d)}"

# Need this for the flash_stripe.py tool
DEPENDS += "qemu-xilinx-multiarch-helper-native"

# Avoid circular dependencies
EXTRA_IMAGEDEPENDS:remove := "${PN}"
python() {
    def extraimage_getdepends(task):
        deps = ""
        for dep in (d.getVar('EXTRA_IMAGEDEPENDS') or "").split():
            if ":" in dep:
                deps += " %s " % (dep)
            else:
                deps += " %s:%s" % (dep, task)
        return deps

    deps = " " + extraimage_getdepends('do_populate_sysroot')
    d.appendVarFlag('do_deploy', 'depends', deps)
}
