# meta-xilinx-virtualization

This layer enables AMD Xen configurations and features for ZynqMP and
Versal devices and also provides related metadata.

See [Xen Build Instructions](README.build.xen.md) to configure and build xen
images.

## Dependencies

This layer depends on:

	URI: https://git.openembedded.org/openembedded-core
	layers: meta
	branch: wrynose

	URI:
	 https://git.yoctoproject.org/meta-xilinx (official version)
	 https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core
	branch: wrynose or AMD release version (e.g. rel-v2026.2)

	URI:
	 https://git.yoctoproject.org/meta-virtualization (official version)
	 https://github.com/Xilinx/meta-virtualization (development and AMD release)
	branch: wrynose or AMD release version (e.g. rel-v2026.2)

	URI: https://git.yoctoproject.org/meta-security
	layers: meta-security, meta-tpm
	branch: wrynose

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: wrynose
