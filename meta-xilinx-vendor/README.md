# meta-xilinx-vendor

This layer provides support for MicroBlaze, Zynq, ZynqMP and Versal architectures vendor boards.

## Supported Boards/Machines

**Boards/Machines supported by this layer:**


| Platform | Vendor Board Variant | Machine Configuration file | Board Device tree |
| ---| --- | ---| ---------- |
|MicroBlaze|[Xilinx S3A DSP 1800](https://shop.trenz-electronic.de/en/TE0320-00-EV02I-FPGA-Module-with-Spartan-3A-DSP-1800K-EV02I-1-Gbit-DDR-RAM)|[s3adsp1800-qemu-microblazeeb](conf/machine/s3adsp1800-qemu-microblazeeb.conf)|NA|
|Zynq-7000|[Avent Microzed](https://www.xilinx.com/products/boards-and-kits/1-5lakcu.html)|[microzed-zynq7](conf/machine/microzed-zynq7.conf)|`zynq-microzed.dtb`|
||[Avnet Picozed](https://www.xilinx.com/products/boards-and-kits/1-58nuel.html)|[picozed-zynq7](conf/machine/picozed-zynq7.conf)|NA|
||[Avnet Minized](https://www.xilinx.com/products/boards-and-kits/1-odbhjd.html)|[minized-zynq7](conf/machine/minized-zynq7.conf)|NA|
||[Avnet/Digilent ZedBoard](https://www.xilinx.com/products/boards-and-kits/1-8dyf-11.html)|[zedboard-zynq7](conf/machine/zedboard-zynq7.conf)|NA|
||[Digilent Zybo](https://www.xilinx.com/support/university/boards-portfolio/xup-boards/DigilentZYBO.html)|[zybo-zynq7](conf/machine/zybo-zynq7.conf)|`zynq-zybo.dtb`|
||[Digilent Zybo Linux BD](https://www.xilinx.com/support/university/boards-portfolio/xup-boards/DigilentZYBO.html)|[zybo-linux-bd-zynq7](conf/machine/zybo-linux-bd-zynq7.conf)|NA|
|ZynqMP|[Avent Ultra96 v1](https://www.xilinx.com/products/boards-and-kits/1-vad4rl.html)|[ultra96-zynqmp](conf/machine/ultra96-zynqmp.conf)|`avnet-ultra96-rev1`|
|Versal|NA|NA|NA|

> **Note:** 
```
1. For Zybo Linux BD reference design refer meta-xilinx-contrib layer.
2. Ultra96 Machine configuration file is unsupported and is compatible with v1 board only. Refer to meta-avnet for v2 board.
```


## Dependencies

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: master or xilinx current release version (e.g. hosister)

	URI: git://git.yoctoproject.org/meta-xilinx.git
	layers: meta-xilinx-microblaze, meta-xilinx-core
	branch: master or xilinx current release version (e.g. hosister)
