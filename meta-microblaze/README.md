# meta-microblaze

This layer provides support specific to the MicroBlaze architecture

## Unsupported Content

This layer may contain code, machine configurations, and tune files for the
MicroBlaze V (MB-V) RISC-V soft processor and classic MicroBlaze soft
processor.

MicroBlaze-V (RISC-V) support will adjust OpenEmbedded-Core configurations
as necessary.

Classic MicroBlaze is only supported for baremetal applications, specifically
firmware for AMD FPGAs.  Linux and other operating systems are no longer
supported.

When used with AMD-EDF, MicroBlaze V is **not a supported configuration**
at this time.  Any sources in this layer are used for ongoing development,
but they are not validated, packaged or covered by the AMD Embedded
Development Framework (EDF) documentation, and no support is offered for
them.

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
	layers: meta-xilinx-core

	URI: https://git.yoctoproject.org/openembedded-core
	layers: meta
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

For example, the `dynamic-layers/meta-xilinx-core` hook in this layer reacts to the `xilinx` collection, which is intentionally **not** listed in `LAYERDEPENDS`/`LAYERRECOMMENDS`.
