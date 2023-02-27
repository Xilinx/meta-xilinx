# meta-xilinx-bsp

This layer enables AMD Xilinx MicroBlaze, Zynq, ZynqMP and Versal device
evaluation boards and provides related metadata.

## Additional documentation:

* [Building Image Instructions](../README.building.md)
* [Booting Image Instructions](../README.booting.md)

## Supported Boards/Machines

**Boards/Machines supported by this layer:**

| Devices    | Board Variant                                                                 | Machine Configuration file                                   | HW Board Device tree                | QEMU tested | HW tested |
|------------|-------------------------------------------------------------------------------|--------------------------------------------------------------|-------------------------------------|-------------|-----------|
| MicroBlaze | [KC705](https://www.xilinx.com/products/boards-and-kits/ek-k7-kc705-g.html)   | [kc705-microblazeel](conf/machine/kc705-microblazeel.conf)   | `kc705-full`                        | Yes         | Yes       |
|            | [AC701](https://www.xilinx.com/products/boards-and-kits/ek-a7-ac701-g.html)   | [ac701-microblazeel](conf/machine/ac701-microblazeel.conf)   | `ac701-full`                        | Yes         | Yes       |
|            | [KCU105](https://www.xilinx.com/products/boards-and-kits/kcu105.html)         | [kcu105-microblazeel](conf/machine/kcu105-microblazeel.conf) | `kcu105`                            | Yes         | Yes       |
|            | [VCU118](https://www.xilinx.com/products/boards-and-kits/vcu118.html)         | [vcu118-microblazeel](conf/machine/vcu118-microblazeel.conf) | `vcu118-rev2.0`                     | Yes         | Yes       |
| Zynq-7000  | Zynq (QEMU)                                                                   | [qemu-zynq7](conf/machine/qemu-zynq7.conf)                   | NA                                  | Yes         | NA        |
|            | [ZC702](https://www.xilinx.com/products/boards-and-kits/ek-z7-zc702-g.html)   | [zc702-zynq7](conf/machine/zc702-zynq7.conf)                 | `zc702`                             | Yes         | Yes       |
|            | [ZC706](https://www.xilinx.com/products/boards-and-kits/ek-z7-zc706-g.html)   | [zc706-zynq7](conf/machine/zc706-zynq7.conf)                 | `zc706`                             | Yes         | Yes       |
| ZynqMP     | [ZCU102](https://www.xilinx.com/products/boards-and-kits/ek-u1-zcu102-g.html) | [zcu102-zynqmp](conf/machine/zcu102-zynqmp.conf)             | `zcu102-rev1.0`                     | Yes         | Yes       |
|            | [ZCU104](https://www.xilinx.com/products/boards-and-kits/zcu104.html)         | [zcu104-zynqmp](conf/machine/zcu104-zynqmp.conf)             | `zcu104-revc`                       | Yes         | Yes       |
|            | [ZCU106](https://www.xilinx.com/products/boards-and-kits/zcu106.html)         | [zcu106-zynqmp](conf/machine/zcu106-zynqmp.conf)             | `zcu106-reva`                       | Yes         | Yes       |
|            | [ZCU111](https://www.xilinx.com/products/boards-and-kits/zcu111.html)         | [zcu111-zynqmp](conf/machine/zcu111-zynqmp.conf)             | `zcu111-reva`                       | Yes         | Yes       |
|            | [ZCU1275](https://www.xilinx.com/products/boards-and-kits/zcu1275.html)       | [zcu1275-zynqmp](conf/machine/zcu1275-zynqmp.conf)           | `zcu1275-revb`                      | Yes         | Yes       |
|            | [ZCU1285](https://www.xilinx.com/products/boards-and-kits/zcu1285.html)       | [zcu1285-zynqmp](conf/machine/zcu1285-zynqmp.conf)           | `zcu1285-reva`                      | Yes         | Yes       |
|            | [ZCU208](https://www.xilinx.com/products/boards-and-kits/zcu208.html)         | [zcu208-zynqmp](conf/machine/zcu208-zynqmp.conf)             | `zcu208-reva`                       | Yes         | Yes       |
|            | [ZCU216](https://www.xilinx.com/products/boards-and-kits/zcu216.html)         | [zcu216-zynqmp](conf/machine/zcu216-zynqmp.conf)             | `zcu216-reva`                       | Yes         | Yes       |
|            | [ZCU670](https://www.xilinx.com/products/boards-and-kits/zcu670.html)         | [zcu216-zynqmp](conf/machine/zcu670-zynqmp.conf)             | `zcu670-revb`                       | Yes         | Yes       |
| Versal     | [VCK190](https://www.xilinx.com/products/boards-and-kits/vck190.html)         | [vck190-versal](conf/machine/vck190-versal.conf)             | `versal-vck190-reva-x-ebm-01-reva`  | Yes         | Yes       |
|            | [VMK180](https://www.xilinx.com/products/boards-and-kits/vmk180.html)         | [vmk180-versal](conf/machine/vmk180-versal.conf)             | `versal-vmk180-reva-x-ebm-01-reva`  | Yes         | Yes       |
|            | [VCK5000](https://www.xilinx.com/products/boards-and-kits/vck5000.html)       | [vck5000-versal](conf/machine/vck5000-versal.conf)           | `versal-vck5000-reva-x-ebm-01-reva` | No          | No        |
|            | [VPK120](https://www.xilinx.com/products/boards-and-kits/vpk120.html)         | [vpk120-versal](conf/machine/vpk120-versal.conf)             | `versal-vpk120-reva`                | Yes         | Yes       |
|            | [VPK180](https://www.xilinx.com/products/boards-and-kits/vpk180.html)         | [vpk180-versal](conf/machine/vpk180-versal.conf)             | `versal-vmk180-reva-x-ebm-01-reva`  | Yes         | Yes       |
|            | [VEK280](https://www.xilinx.com/products/boards-and-kits/vek280.html)         | [vek280-versal](conf/machine/vek280-versal.conf)             | `versal-vek280-reva`                | Yes         | Yes       |
|            | [VHK158](https://www.xilinx.com/products/boards-and-kits/vhk158.html)         | [vhk158-versal](conf/machine/vhk158-versal.conf)             | `versal-vhk158-reva`                | Yes         | Yes       |

> **Note:** Additional information on Xilinx architectures can be found at:
	https://www.xilinx.com/products/silicon-devices.html

## Dependencies

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: langdale

	URI: git://git.yoctoproject.org/meta-xilinx.git
	layers: meta-xilinx-microblaze, meta-xilinx-core
	branch: langdale

