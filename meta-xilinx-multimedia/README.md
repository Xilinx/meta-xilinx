# meta-xilinx-multimedia

This layer contains multimedia related recipes and bbappends for AMD Adaptive
SoC's and specific FPGA targets.

Inclusion of this layer does not necessarily introduce the additional
hardware support.  See the bbappends for specific rules on enabling VCU, VDU,
VCU2 and ISP specific software, usually through MACHINE_FEATURES.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-perl, meta-python, meta-filesystems, meta-gnome,
            meta-multimedia, meta-networking, meta-webserver, meta-xfce,
            meta-initramfs.
	branch: scarthgap
