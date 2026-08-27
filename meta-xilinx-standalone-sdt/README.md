# meta-xilinx-standalone-sdt

This layer contains System Device Tree build metadata such as multiconfig operating
environment(baremetal, freertos etc) boot firmware drivers, libraries and
applications recipes.

See [SDT Build Instructions](README.sdt.bsp.md) for SDT build workflows.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: wrynose

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: wrynose

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: wrynose

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core, meta-xilinx-standalone
	branch: wrynose

	URI:
        https://git.yoctoproject.org/meta-virtualization (official version)
        https://github.com/Xilinx/meta-virtualization (development and AMD release)
	branch: wrynose

	URI:
        https://github.com/OpenAMP/meta-openamp (official version)
        https://github.com/Xilinx/meta-openamp (development and AMD release)
	branch: wrynose

## Note: 

* **dynamic-layers vs. LAYERRECOMMENDS**

`conf/layer.conf` in this layer defines one or more `BBFILES_DYNAMIC` entries
of the form `<collection>:<path>/*.bb(append)`. Each entry only takes effect
if `<collection>` is already enabled in the current build (i.e. some other
layer providing that BitBake collection is present in `bblayers.conf`); if it
isn't, the corresponding recipes/bbappends under that path are silently
skipped.

Not every `<collection>` referenced this way is also listed in
`LAYERDEPENDS`/`LAYERRECOMMENDS` for this layer. A `LAYERDEPENDS` entry means
the collection is required for this layer to parse; a `LAYERRECOMMENDS` entry
is a soft suggestion that `bitbake-layers` will offer to add automatically.
Some `BBFILES_DYNAMIC` hooks are neither: this layer will opportunistically
augment its recipes with extra content *if* the collection happens to
already be part of the build, but it does not require, and does not
recommend, adding that collection just to satisfy the hook. There is no
formal BitBake term for this "augment-if-present, don't-recommend"
relationship, so it is called out here to avoid confusion when auditing
`LAYERDEPENDS`/`LAYERRECOMMENDS` completeness against `BBFILES_DYNAMIC`.

Currently, the `dynamic-layers/openamp-layer` hook in this layer's `conf/layer.conf` reacts to the `openamp-layer` collection, which is also covered by `LAYERRECOMMENDS`.
