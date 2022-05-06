# meta-xilinx-contrib

This layer is a contribution layer to support for MicroBlaze, Zynq and ZynqMP architectures.
Any patches from open source contributors for vendor board can be added here.

## Supported Boards/Machines

**Boards/Machines supported by this layer:**


| Platform | Vendor Board Variant | Machine Configuration file | Board Device tree |
| ---| --- | ---| ---------- |
|MicroBlaze|[Xilinx ML605 (QEMU)](https://www.digikey.com/en/products/detail/amd-xilinx/EK-V6-ML605-G/2175174)|[ml605-qemu-microblazeel](conf/machine/ml605-qemu-microblazeel.conf)|NA|
|Zynq-7000|NA|NA|NA|
|ZynqMP|NA|NA|NA|
|Versal|NA|NA|NA|

## Dependencies

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: master or xilinx current release version (e.g. hosister)

	URI: git://git.yoctoproject.org/meta-xilinx.git
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-vendor
	branch: master or xilinx current release version (e.g. hosister)

