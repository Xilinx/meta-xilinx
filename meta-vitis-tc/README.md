# meta-vitis-tc

This layer is used to build various toolchains that may be embedded into
the AMD Vitis (and Vivado) products.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: master

	URI: https://git.openembedded.org/meta-xilinx
	layers: meta-xilinx-core, meta-microblaze, meta-xilinx-standalone
	branch: master

optionally, you may alwys want to include:

	URI: https://git.yoctoproject.org/meta-mingw
	layers: meta-mingw
	branch: master

---

## Configuring Machines

Baremetal toolchains can be built using:

MACHINE=<toolchain> DISTRO=xilinx-standalone bitbake meta-xilinx-toolchain

The <toolchain> value should be one of:
  aarch32-tc    - 32-bit Cortex-A toolchains
  aarch64-tc    - 64-bit Cortex-A toolchains
  arm-rm-tc     - ARM Cortex-R and M toolchains
  microblaze-tc - Microblaze toolchains
  riscv-tc      - Risc-V toolchains


Also there is a standalone QEMU SDK:

MACHINE=zynqmp-generic bitbake meta-qemu-xilinx
