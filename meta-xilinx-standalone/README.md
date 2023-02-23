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

## Standalone Firmware

The standalone firmware is a genericly configured firmware, it can be
build either in a single standalong configuration, or via an automated
multiconfig approach only when needed.

* multiconfig setup

To automatically build the standalone firmware with a Linux build, you need
to only add the following to your conf/local.conf file.  This will use
the multiconfig mechanism within the Yocto Project to build the corresponding
standalone firmware on demand.

Edit the conf/local.conf file, add:

# For zynqmp-generic
BBMULTICONFIG += "fsbl-fw zynqmp-pmufw"

# For versal-generic
BBMULTICONFIG += "versal-fw"

To build:

# For zynqmp, select a zynqmp machine or the generic one
MACHINE=zynqmp-generic bitbake fsbl pmufw

# For versal, select a versal machine or the generic one
MACHINE=versal-generic bitbake plmfw psmfw


## Dependencies

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: master or xilinx current release version (e.g. hosister)

	URI: git://git.yoctoproject.org/meta-xilinx.git
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-bsp
	branch: master or xilinx current release version (e.g. hosister)

