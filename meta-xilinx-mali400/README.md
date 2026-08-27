# meta-xilinx-mali400

This layer contains recipes for MALI-400 GPU drivers and user space libraries for
AMD Zynq UltraScale+ MPSoC EG/EV devices which has MALI-400 GPU. It also includes
support for open source DRM LIMA drivers. 

> **Note:**
> 1. `MACHINE_FEATURES += "mali400"` is included in machine conf files for EG/EV
>    devices by gen-machineconf tools. If user is not using gen-machineconf to
>    generate the machine conf files then add it manually.
> 2. MALI-400 and DRM LIMA driver are mutually exclusive.

## How to enable MALI-400 drivers

1. Follow [Building Instructions](../README.building.md) upto step 4.

2. Enable libmali DISTRO_FEATURE and mali400 MACHINE_FEATURES by adding these
   variables to the end of the conf/local.conf file as shown below.
```
DISTRO_FEATURES:append = " libmali"
MACHINE_FEATURES += "mali400"
```

3. Continue [Building Instructions](../README.building.md) from step 5.

## How to enable DRM LIMA drivers

1. Follow [Building Instructions](../README.building.md) upto step 4.

2. Remove libmali DISTRO_FEATURE and enable mali400 MACHINE_FEATURES by adding
   these variables to the end of the conf/local.conf file as shown below.
```
DISTRO_FEATURES:remove = " libmali"
MACHINE_FEATURES += "mali400"
```

3. Continue [Building Instructions](../README.building.md) from step 5.

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
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-standalone
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

For example, the `dynamic-layers/meta-qt6`, `dynamic-layers/meta-ros/meta-ros-common`, and `dynamic-layers/meta-ros/meta-ros2-lyrical` hooks in this layer react to the `qt6-layer`, `ros-common-layer`, and `ros2-lyrical-layer` collections respectively, none of which are listed in `LAYERDEPENDS`/`LAYERRECOMMENDS` here.
