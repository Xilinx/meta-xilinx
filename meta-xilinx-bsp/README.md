# meta-xilinx-bsp

This layer is deprecated.  All BSP components have been moved to
other layers.

XSCT based machine are in generally in meta-xilinx-tools, while
SDT based machines are in meta-amd-adaptive-socs-bsps.

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-standalone
	branch: scarthgap or AMD release version (e.g. rel-v2026.1)
