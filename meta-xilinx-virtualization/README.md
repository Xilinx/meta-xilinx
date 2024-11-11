# meta-xilinx-virtualization

This layer enables AMD Xen configurations and features for ZynqMP and
Versal devices and also provides related metadata.

See [Xen Build Instructions](README.build.xen.md) to configure and build xen
images.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-python, meta-filesystems, meta-networking.
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core, meta-xilinx-standalone
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)

	URI: https://git.yoctoproject.org/meta-virtualization
	branch: scarthgap

	URI: https://git.yoctoproject.org/meta-security
	layers: meta-tpm
	branch: scarthgap

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: scarthgap
