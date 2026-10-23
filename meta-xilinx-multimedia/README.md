# meta-xilinx-multimedia

This layer contains multimedia related recipes and bbappends for AMD Adaptive
SoC's and specific FPGA targets.

Inclusion of this layer does not necessarily introduce the additional
hardware support.  See the bbappends for specific rules on enabling VCU, VDU,
VCU2 and ISP specific software, usually through MACHINE_FEATURES.

## Dependencies

This layer depends on:

	URI: https://git.openembedded.org/openembedded-core
	layers: meta
	branch: wrynose

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: wrynose

	URI:
	 https://git.yoctoproject.org/meta-xilinx (official version)
	 https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core
	branch: wrynose or AMD release version (e.g. rel-v2026.2)

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: wrynose
