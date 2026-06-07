# meta-microblaze

This layer provides support specific to the MicroBlaze architecture

## Unsupported Content

This layer may contain code, machine configurations, and tune files for the
MicroBlaze V (MB-V) RISC-V soft processor and classic MicroBlaze soft
processor.

MicroBlaze-V (RISC-V) support will adjust OpenEmbedded-Core configurations
as necessary.

Classic MicroBlaze is only supported for baremetal applications, specifically
firmware for AMD FPGAs.  Linux and other operating systems are no longer
supported.

When used with AMD-EDF, MicroBlaze V is **not a supported configuration**
at this time.  Any sources in this layer are used for ongoing development,
but they are not validated, packaged or covered by the AMD Embedded
Development Framework (EDF) documentation, and no support is offered for
them.

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
	layers: meta-xilinx-core
	branch: wrynose
