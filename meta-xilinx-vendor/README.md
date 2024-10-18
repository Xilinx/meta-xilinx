# meta-xilinx-vendor

This layer enables third party vendor boards for AMD MicroBlaze, Zynq, ZynqMP and
Versal devices and provides related metadata.

## Supported Boards/Machines

**Boards/Machines supported by this layer:**

> **Variable usage examples:**
>
> Machine Configuration file: `MACHINE = "microzed-zynq7"`
>

| Devices    | Vendor Evaluation Board                                                                                           | Machine Configuration file                                   | Reference XSA    | HW Board Device tree | QEMU tested | HW tested |
|------------|-------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------|------------------|----------------------|-------------|-----------|
| MicroBlaze | NA                                                                                                                | NA                                                           | NA               | NA                   | No          | No        |
| Zynq-7000  | [Avent Microzed](https://www.xilinx.com/products/boards-and-kits/1-5lakcu.html)                                   | [microzed-zynq7](conf/machine/microzed-zynq7.conf)           | NA               | `zynq-microzed.dtb`  | No          | No        |
|            | [Avnet Picozed](https://www.xilinx.com/products/boards-and-kits/1-58nuel.html)                                    | [picozed-zynq7](conf/machine/picozed-zynq7.conf)             | NA               | NA                   | No          | No        |
|            | [Avnet Minized](https://www.xilinx.com/products/boards-and-kits/1-odbhjd.html)                                    | [minized-zynq7](conf/machine/minized-zynq7.conf)             | NA               | NA                   | No          | No        |
|            | [Avnet/Digilent ZedBoard](https://www.xilinx.com/products/boards-and-kits/1-8dyf-11.html)                         | [zedboard-zynq7](conf/machine/zedboard-zynq7.conf)           | NA               | NA                   | No          | No        |
|            | [Digilent Zybo](https://www.xilinx.com/support/university/boards-portfolio/xup-boards/DigilentZYBO.html)          | [zybo-zynq7](conf/machine/zybo-zynq7.conf)                   | NA               | `zynq-zybo.dtb`      | No          | No        |
|            | [Digilent Zybo Linux BD](https://www.xilinx.com/support/university/boards-portfolio/xup-boards/DigilentZYBO.html) | [zybo-linux-bd-zynq7](conf/machine/zybo-linux-bd-zynq7.conf) | NA               | NA                   | No          | No        |
| Versal     | NA                                                                                                                | NA                                                           | NA               | NA                   | NA          | NA        |

> **Note:** 
```
1. For Zybo Linux BD reference design refer meta-xilinx-contrib layer.
2. Ultra96 v1 is no longer supported. Refer to https://github.com/Avnet/meta-avnet for v2 board.
```

## AMD Vendor board XSCT Build Instructions

Follow [XSCT Build Instructions](https://github.com/Xilinx/meta-xilinx-tools/blob/master/README.xsct.bsp.md)

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: scarthgap

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-microblaze, meta-xilinx-core
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)
