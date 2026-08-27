# meta-xilinx-demos

This layer contains demos recipes and packagegroup for PL firmware, openamp,
jupyter notebook, ros, qt5, multimedia, sdfec etc for AMD Adaptive SoC's and
FPGA's target images.

> **Note:** Some of the demos recipes and packagegroup are moved from meta-petalinux
> layer to meta-xilinx-demos layer and these packagegroup are renamed.

## How to enable demos for target image

1. Follow [Building Instructions](../README.building.md) upto step 4.

2. Add meta-xilinx-demos to bblayers.conf as shown below.
```
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-demos
```

3. Add required demos recipes or packagegroup to target image using IMAGE_INSTALL
   variable to the end of the conf/local.conf file as shown below. For example
   include gpio-demo application.
```
IMAGE_INSTALL:append = " gpio-demo"
```

4. Continue [Building Instructions](../README.building.md) from step 5.

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

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core, meta-xilinx-microblaze, meta-xilinx-standalone.
	branch: wrynose

	URI: https://github.com/Xilinx/meta-jupyter
	branch: wrynose

	URI: https://github.com/OpenAMP/meta-openamp
	branch: wrynose

	URI: https://github.com/meta-qt5/meta-qt5
	branch: wrynose

	URI: https://github.com/Xilinx/meta-ros
	layers: meta-ros-common, meta-ros2, meta-ros2-jazzy
	branch: wrynose

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
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

Currently, every `BBFILES_DYNAMIC` hook in this layer's `conf/layer.conf` (`jupyter-layer`, `openamp-layer`, `qt6-layer`) is also covered by `LAYERRECOMMENDS`.
