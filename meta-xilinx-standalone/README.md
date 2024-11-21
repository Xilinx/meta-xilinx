# meta-xilinx-standalone

This layer is meant to augment Yocto/OE functionality to provide a 
Baremetal/Standalone distribution as well as a generic version of various
firmware that is required to boot a ZynqMP or Versal system.

For optimized versions of the firmware and additional components you must
use the meta-xilinx-tools layer.

## Building

The software in this layer may be used in either a standard single
configuration build, or a multiconfig build.  A multiconfig build, along
with the MACHINES defined in meta-xilinx-bsps will automate the generation
of certain firmwares.
---
## Standalone Firmware

The standalone firmware is a generically configured firmware, it can be
build either in a single standalone configuration, or via an automated
multiconfig approach only when needed.

* multiconfig setup

To automatically build the standalone firmware with a Linux build, you need
to only add the following to your conf/local.conf file.  This will use
the multiconfig mechanism within the Yocto Project to build the corresponding
standalone firmware on demand.

Edit the conf/local.conf file, add:

```
# For zynqmp-generic
BBMULTICONFIG += "fsbl-fw zynqmp-pmufw"
```

```
# For versal-generic
BBMULTICONFIG += "versal-fw"
```

**To build:**

```
# For zynqmp, select a zynqmp machine or the generic one
$ MACHINE=zynqmp-generic bitbake fsbl pmufw
```

```
# For versal, select a versal machine or the generic one
$ MACHINE=versal-generic bitbake plmfw psmfw
```
---

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
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-bsp
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)
