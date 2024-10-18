# meta-vitis-tc

This layer is used to build various toolchains that may be embedded into
the AMD Vitis (and Vivado) products.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-standalone
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: scarthgap

optionally, you may alwys want to include:

	URI: https://git.yoctoproject.org/meta-mingw
	layers: meta-mingw
	branch: scarthgap

---

## Configuring Machines

Baremetal toolchains can be built using:

```
$ MACHINE=<toolchain> DISTRO=xilinx-standalone bitbake meta-xilinx-toolchain
```

The <toolchain> value should be one of:
  aarch32-tc    - 32-bit Cortex-A toolchains
  aarch64-tc    - 64-bit Cortex-A toolchains
  arm-rm-tc     - ARM Cortex-R and M toolchains
  microblaze-tc - Microblaze toolchains
  riscv-tc      - Risc-V toolchains

Also there is a standalone QEMU SDK:

```
$ MACHINE=zynqmp-generic bitbake meta-qemu-xilinx
```
