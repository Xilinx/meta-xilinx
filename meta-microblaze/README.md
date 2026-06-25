# meta-microblaze

This layer provides support specific to the MicroBlaze architecture

## Unsupported Content

This layer contains code, machine configurations, and tune files for the
MicroBlaze V (MB-V) RISC-V soft processor (for example the `mbv` machine
include under `conf/machine/include/mbv/` and any `microblaze-v-*` machine,
SoC, or kernel recipes that consume it). MicroBlaze V is **not a supported
configuration** in this release; support has been deferred to a future
release. The sources are retained in the layer for ongoing development, but
they are not validated, packaged, or covered by the AMD Embedded Development
Framework (EDF) documentation for this release, and no support is offered
for them.

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
	layers: meta-xilinx-core
	branch: scarthgap or AMD release version (e.g. rel-v2026.1)
