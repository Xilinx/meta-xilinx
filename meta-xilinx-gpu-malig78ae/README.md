# meta-xilinx-gpu-malig78ae

This layer contains recipes for MALI g78ae GPU drivers and user space libraries for
AMD FPGA devices which has MALI g78ae GPU.

> **Note:**
> 1. `MACHINE_FEATURES += "malig78ae"` is included in machine conf files for
>    devices by gen-machineconf tools. If user is not using gen-machineconf to
>    generate the machine conf files then add it manually.
> 2. MALI g78ae and mesa drivers are mutually exclusive.

## How to enable MALI g78ae drivers

1. Follow [Building Instructions](../README.building.md) upto step 4.

2. Enable libmali DISTRO_FEATURE and malig78ae MACHINE_FEATURES by adding these
   variables to the end of the conf/local.conf file as shown below.
```
DISTRO_FEATURES:append = " libmali"
MACHINE_FEATURES += "malig78ae"
```

3. Continue [Building Instructions](../README.building.md) from step 5.

## How to enable mesa drivers

mesa does not currently support the mali g78ae.

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
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-standalone, meta-xilinx-mali400
	branch: scarthgap or AMD release version (e.g. rel-v2025.1)
