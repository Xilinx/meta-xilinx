# meta-xilinx-contrib

This layer is a contribution layer enables AMD Xilinx MicroBlaze, Zynq, ZynqMP
and Versal devices and provides related metadata.
Any patches from open source contributors for vendor board can be added here.

## Vendor Evaluation Boards BSP Machines files

The following boards are supported by the meta-xilinx-contrib layer:

| Devices    | Vendor Board Variant                                                                               | Machine Configuration file                                           | HW Board Device tree | QEMU tested | HW tested |
|------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------|----------------------|-------------|-----------|
| MicroBlaze | [Xilinx ML605 (QEMU)](https://www.digikey.com/en/products/detail/amd-xilinx/EK-V6-ML605-G/2175174) | [ml605-qemu-microblazeel](conf/machine/ml605-qemu-microblazeel.conf) | NA                   | No          | NA        |
| Zynq-7000  | NA                                                                                                 | NA                                                                   | NA                   |             |           |
| ZynqMP     | NA                                                                                                 | NA                                                                   | NA                   |             |           |
| Versal     | NA                                                                                                 | NA                                                                   | NA                   |             |           |
---
## Dependencies

This layer depends on:

	URI: https:///git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: langdale

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: langdale

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and amd xilinx release)
	layers: meta-xilinx-microblaze, meta-xilinx-core, meta-xilinx-vendor
	branch: langdale or amd xilinx release version (e.g. rel-v2022.2)
