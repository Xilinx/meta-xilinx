meta-xilinx-standalone
======================

This layer is meant to augment Yocto/OE functionality to provide a 
Baremetal/Standalone Toolchain as well as a generic version of various
firmware that is required to boot a ZynqMP or Versal system.

For optimized versions of the firmware and additional components you must
use the meta-xilinx-tools layer.

Building
--------
The software in this layer may be used in either a standard single
configuration build, or a multiconfig build.  A multiconfig build, along
with the MACHINES defined in meta-xilinx-bsps will automate the generation
of certain firmwares.

To build standalone toolchains similar to those embedded with the
Xilinx xsct tooling:

Use one of the custom machines:
  aarch32-tc - 32-bit ARM toolchains (various)
  aarch64-tc - 64-bit ARM toolchains (various)
  arm-rm-tc  - ARM Cortex-R (and various)
  microblaze-tc - Microblaze toolchains (various)

MACHINE=<machine> DISTRO=xilinx-standalone bitbake meta-toolchain


To build the standalone firmware in a standard single configuration build:

Select a machine:
  cortexa53-zynqmp - ZynqMP based Cortex-A53 target
  cortexa72-versal - Versal based Cortex-A72 target
  cortexa9-zynq    - Zynq based cortex-A9 target
  cortexr5-versal  - Versal based Cortex-R5 target
  cortexr5-zynqmp  - ZynqMP based Cortex-R5 target
  microblaze-versal-fw - Microblaze for Versal PSM/PLM firmware
  microblaze-zyqmp-pmu - Microblaze for ZynqMP PMU firmware

To build ZynqMP PMU Firmware:
MACHINE=microblaze-zynqmp-pmu DISTRO=xilinx-standalone bitbake pmu-firmware

To build Versal PLM and PSM Firwmare
MACHINE=microblaze-versal-fw DISTRO=xilinx-standalone bitbake plm-firmware psm-firmware


In a multiconfig build, add the following pre-defined multiconfigs to enable
firmware building, and automatic packaging by the Linux side software.

Edit the local.conf file, add:

# For zynqmp
BBMULTICONFIG += "zynqmp-pmufw"

# For versal
BBMULTICONFIG += "versal-fw"

To build:

# For zynqmp, select a zynqmp machine or the generic one
MACHINE=zynqmp-generic bitbake pmufw

# For versal, select a versal machine or the generic one
MACHINE=versal-generic bitbake plmfw psmfw


Maintainers, Mailing list, Patches
==================================

Please send any patches, pull requests, comments or questions for this 
layer to the [meta-xilinx mailing list]
(https://lists.yoctoproject.org/listinfo/meta-xilinx):

	meta-xilinx@lists.yoctoproject.org

Maintainers:

	Sai Hari Chandana Kalluri <chandana.kalluri@xilinx.com>
	Mark Hatle <mark.hatle@xilinx.com>

Dependencies
============

This layer depends on:

     URI: git://git.yoctoproject.org/poky

     URI: git://git.yoctoproject.org/meta-xilinx/meta-xilinx-bsp

Usage
=====

1.- Clone this layer along with the specified layers

2.- $ source oe-init-build-env

3.- Add this layer to BBLAYERS on conf/bblayers.conf

4.- Add the following to your conf/local.conf to build for the 
microblaze architecture:

DISTRO="xilinx-standalone"

MACHINE="microblaze-pmu"

5.- Build a package:

for example:

$ bitbake newlib

or

$ bitbake meta-toolchain
