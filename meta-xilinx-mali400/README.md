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
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: scarthgap

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-standalone
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)
