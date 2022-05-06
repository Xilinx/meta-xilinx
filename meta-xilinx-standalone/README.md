# meta-xilinx-standalone

This layer is meant to augment Yocto/OE functionality to provide a 
Baremetal/Standalone Toolchain as well as a generic version of various
firmware that is required to boot a ZynqMP or Versal system.

For optimized versions of the firmware and additional components you must
use the meta-xilinx-tools layer.

## Building

The software in this layer may be used in either a standard single
configuration build, or a multiconfig build.  A multiconfig build, along
with the MACHINES defined in meta-xilinx-bsps will automate the generation
of certain firmwares.

## Toolchains

To build standalone toolchains similar to those embedded with the
Xilinx xsct tooling:

Use one of the custom machines:
  aarch32-tc - 32-bit ARM toolchains (various)
  aarch64-tc - 64-bit ARM toolchains (various)
  arm-rm-tc  - ARM Cortex-R (and various)
  microblaze-tc - Microblaze toolchains (various)

MACHINE=<machine> DISTRO=xilinx-standalone bitbake meta-toolchain

## Standalone Firmware

The standalone firmware is a genericly configured firmware, it can be
build either in a single standalong configuration, or via an automated
multiconfig approach only when needed.

* single configuration

Select a machine:
  cortexa53-zynqmp - ZynqMP based Cortex-A53 target
    Valid Targets: fsbl-firmware, meta-toolchain

  cortexa72-versal - Versal based Cortex-A72 target
    Valid Targets: meta-toolchain

  cortexa9-zynq    - Zynq based cortex-A9 target
    Valid Targets: meta-toolchain

  cortexr5-versal  - Versal based Cortex-R5 target
    Valid Targets: meta-toolchain

  cortexr5-zynqmp  - ZynqMP based Cortex-R5 target
    Valid Targets: meta-toolchain

  microblaze-versal-fw - Microblaze for Versal PSM/PLM firmware
    Valid Targets: psm-firmware, plm-firmware, meta-toolchain

  microblaze-zynqmp-pmu - Microblaze for ZynqMP PMU firmware
    Valid Target: pmu-firmware, meta-toolchain


To build you should use a command similar to:
MACHINE=<machine> DISTRO=xilinx-standalone bitbake <recipe>


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

