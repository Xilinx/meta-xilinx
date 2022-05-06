# meta-xilinx

This layer provides support for MicroBlaze, Zynq, ZynqMP and Versal architectures Xilinx evaluation boards.

## Additional documentation:

* [Building](../README.building.md)
* [Booting](../README.booting.md)

## Supported Boards/Machines

**Boards/Machines supported by this layer:**

| Platform | Xilinx Board Variant | Machine Configuration file | Board Device tree |
| ---| --- | ---| ---------- |
|MicroBlaze|[Xilinx KC705](https://www.xilinx.com/products/boards-and-kits/ek-k7-kc705-g.html)|[kc705-microblazeel](conf/machine/kc705-microblazeel.conf)|`kc705-full`|
|Zynq-7000|Zynq (QEMU)|[qemu-zynq7](conf/machine/qemu-zynq7.conf)|NA|
||[Xilinx ZC702](https://www.xilinx.com/products/boards-and-kits/ek-z7-zc702-g.html)|[zc702-zynq7](conf/machine/zc702-zynq7.conf)|`zc702`|
||[Xilinx ZC706](https://www.xilinx.com/products/boards-and-kits/ek-z7-zc706-g.html)|[zc706-zynq7](conf/machine/zc706-zynq7.conf)|`zc706`|
|ZynqMP|[Xilinx ZCU102](https://www.xilinx.com/products/boards-and-kits/ek-u1-zcu102-g.html)|[zcu102-zynqmp](conf/machine/zcu102-zynqmp.conf)|`zcu102-rev1.0`|
||[Xilinx ZCU104](https://www.xilinx.com/products/boards-and-kits/zcu104.html)|[zcu104-zynqmp](conf/machine/zcu104-zynqmp.conf)|`zcu104-revc`|
||[Xilinx ZCU106](https://www.xilinx.com/products/boards-and-kits/zcu106.html)|[zcu106-zynqmp](conf/machine/zcu106-zynqmp.conf)|`zcu106-reva`|
||[Xilinx ZCU111](https://www.xilinx.com/products/boards-and-kits/zcu111.html)|[zcu111-zynqmp](conf/machine/zcu111-zynqmp.conf)|`zcu111-reva`|
||[Xilinx ZCU1275](https://www.xilinx.com/products/boards-and-kits/zcu1275.html)|[zcu1275-zynqmp](conf/machine/zcu1275-zynqmp.conf)|`zcu1275-revb`|
||[Xilinx ZCU1285](https://www.xilinx.com/products/boards-and-kits/zcu1285.html)|[zcu1285-zynqmp](conf/machine/zcu1285-zynqmp.conf)|`zcu1285-reva`|
||[Xilinx ZCU208](https://www.xilinx.com/products/boards-and-kits/zcu208.html)|[zcu208-zynqmp](conf/machine/zcu208-zynqmp.conf)|`zcu208-reva`|
||[Xilinx ZCU216](https://www.xilinx.com/products/boards-and-kits/zcu216.html)|[zcu216-zynqmp](conf/machine/zcu216-zynqmp.conf)|`zcu216-reva`|
|Versal|[Xilinx VCK190](https://www.xilinx.com/products/boards-and-kits/vck190.html)|[vck190-versal](conf/machine/vck190-versal.conf)|`versal-vck190-reva-x-ebm-01-reva`|
||[Xilinx VMK180](https://www.xilinx.com/products/boards-and-kits/vmk180.html)|[vmk180-versal](conf/machine/vmk180-versal.conf)|`versal-vmk180-reva-x-ebm-01-reva`|
||[Xilinx VCK5000](https://www.xilinx.com/products/boards-and-kits/vck5000.html)|[vck5000-versal](conf/machine/vck5000-versal.conf)|`versal-vck5000-reva-x-ebm-01-reva`|

> **Note:** Additional information on Xilinx architectures can be found at:
	https://www.xilinx.com/products/silicon-devices.html

## Dependencies

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: master or xilinx current release version (e.g. hosister)

	URI: git://git.yoctoproject.org/meta-xilinx.git
	layers: meta-xilinx-microblaze, meta-xilinx-core
	branch: master or xilinx current release version (e.g. hosister)

