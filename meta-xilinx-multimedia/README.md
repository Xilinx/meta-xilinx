# meta-xilinx-multimedia

This layer contains multimedia related recipes and bbappends for AMD Adaptive
SoC's and specific FPGA targets.

Inclusion of this layer does not necessarily introduce the additional
hardware support.  See the bbappends for specific rules on enabling VCU, VDU,
VCU2 and ISP specific software, usually through MACHINE_FEATURES.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: wrynose

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-perl, meta-python, meta-filesystems, meta-gnome,
            meta-multimedia, meta-networking, meta-webserver, meta-xfce,
            meta-initramfs.
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

For example, the `dynamic-layers/meta-jupyter` hook in this layer reacts to the `jupyter-layer` collection, which is intentionally **not** listed in `LAYERDEPENDS`/`LAYERRECOMMENDS`.
