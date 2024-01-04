# meta-xilinx-vendor

This layer enables AMD Xilinx Xen configurations and features for ZynqMP and
Versal devices and also provides related metadata.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: langdale

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-python, meta-filesystems, meta-networking.
	branch: langdale

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and amd xilinx release)
	layers: meta-xilinx-core, meta-xilinx-standalone
	branch: langdale or amd xilinx release version (e.g. rel-v2024.1)

	URI: https://git.yoctoproject.org/meta-virtualization
	branch: langdale

	URI: https://git.yoctoproject.org/meta-security
	layers: meta-tpm
	branch: langdale