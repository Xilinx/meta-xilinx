# meta-xilinx-standalone

This layer is meant to augment Yocto/OE functionality to provide a
Baremetal/Standalone distribution as well as a generic version of various
firmware that is required to boot a ZynqMP or Versal system.

For optimized versions of the firmware and additional components you must use
the meta-xilinx-tools layer.

## Building

The software in this layer may be used in either a standard single configuration
build, or a multiconfig build. A multiconfig build, along with the MACHINES
defined in bsp layer will automate the generation of certain firmwares.

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

## Customizing ESW components

### How to patch embeddedsw components

This section describes on how to patches to embeddedsw-source recipe in Yocto
XSCT or SDT build flows. In 2025.1 and later release all embeddedsw firmware
recipes such as pmu-firmware, plm-firmware etc. uses source from embeddedsw-source
recipe. If you apply the patch to embeddedsw-source recipe it will be applied to
all embeddedsw firmware recipes. Also it is recommended to create a new custom
layer to apply the patches.

1. Create and add a new layer to build.
```
$ bitbake-layers create-layer /<path-to-layer>/sources/meta-custom
$ bitbake-layers add-layer /<path-to-layer>/sources/meta-custom
```

2. Create a recipes-bsp/embeddedsw-source directory and embeddedsw-source_${ESW_VER}.bbappend.
   file in newly created layer.
> **Note:** ${ESW_VER} variable referes to version to which you need to apply
> the patch.
```

$ mkdir -p /<path-to-layer>/sources/meta-custom/recipes-bsp/embeddedsw-source/files
$ touch /<path-to-layer>/sources/meta-custom/recipes-bsp/embeddedsw-source/embeddedsw-source_2025.1.bbappend
```
3. Create the patch for embeddedsw repo and copy the patch to files directory.
```
$ cp -r <path-to-embeddedsw-repo-patch>/0001-PLM.ptach /<path-to-layer>/sources/meta-custom/recipes-bsp/embeddedsw-source/files
```
4. Include the patch to embeddedsw-source_${ESW_VER}.bbappend.
```
SRC_URI:append = " \
    file://0001-PLM.ptach \
	"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
```
5. Now rebuild the embeddedsw firmware recipes.
```
$ bitbake embeddedsw-source -c clean; bitbake plm-firmware
```

### How to build embeddedsw firmware recipes using EXTERNALSRC method.

1. Inherit externalsrc bbclass and set the EXTERNALSRC:pn-emebddedsw-source variable
   to point the external embeddedsw source code in local.conf as shown below.
```
INHERIT += "externalsrc"
EXTERNALSRC:pn-embeddedsw-source-${ESW_VER} = "<path-to-ext-embeddedsw-source-tree>"
```
2. Now rebuild the embeddedsw firmware recipes.
```
$ bitbake embeddedsw-source plm-firmware -c clean; bitbake plm-firmware
```

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
	branch: scarthgap or AMD release version (e.g. rel-v2025.1)
