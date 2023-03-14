# meta-xilinx-bsp

This layer enables AMD Xilinx MicroBlaze, Zynq, ZynqMP and Versal device
evaluation boards and provides related metadata.

## Additional documentation

* [Building Image Instructions](../README.building.md)
* [Booting Image Instructions](../README.booting.md)
---

## AMD Xilinx Evaluation Boards BSP Machines files

The following boards are supported by the meta-xilinx-bsp layer:

> **Variable usage examples:**
>
> Machine Configuration file: `MACHINE = "zcu102-zynqmp"`
>
> Reference XSA: `HDF_MACHINE = "zcu102-zynqmp"`
>
> HW Board Device tree: `YAML_DT_BOARD_FLAGS = "{BOARD zcu102-rev1.0}"`

| Devices    | Evaluation Board                                                              | Machine Configuration file                                   | Reference XSA         | HW Board Device tree                | QEMU tested | HW tested |
|------------|-------------------------------------------------------------------------------|--------------------------------------------------------------|-----------------------|-------------------------------------|-------------|-----------|
| MicroBlaze | [KC705](https://www.xilinx.com/products/boards-and-kits/ek-k7-kc705-g.html)   | [kc705-microblazeel](conf/machine/kc705-microblazeel.conf)   | `kc705-microblazeel`  | `kc705-full`                        | Yes         | Yes       |
| Zynq-7000  | [ZC702](https://www.xilinx.com/products/boards-and-kits/ek-z7-zc702-g.html)   | [zc702-zynq7](conf/machine/zc702-zynq7.conf)                 | `zc702-zynq7`         | `zc702`                             | Yes         | Yes       |
|            | [ZC706](https://www.xilinx.com/products/boards-and-kits/ek-z7-zc706-g.html)   | [zc706-zynq7](conf/machine/zc706-zynq7.conf)                 | `zc706-zynq7`         | `zc706`                             | Yes         | Yes       |
| ZynqMP     | [ZCU102](https://www.xilinx.com/products/boards-and-kits/ek-u1-zcu102-g.html) | [zcu102-zynqmp](conf/machine/zcu102-zynqmp.conf)             | `zcu102-zynqmp`       | `zcu102-rev1.0`                     | Yes         | Yes       |
|            | [ZCU104](https://www.xilinx.com/products/boards-and-kits/zcu104.html)         | [zcu104-zynqmp](conf/machine/zcu104-zynqmp.conf)             | `zcu104-zynqmp`       | `zcu104-revc`                       | Yes         | Yes       |
|            | [ZCU106](https://www.xilinx.com/products/boards-and-kits/zcu106.html)         | [zcu106-zynqmp](conf/machine/zcu106-zynqmp.conf)             | `zcu106-zynqmp`       | `zcu106-reva`                       | Yes         | Yes       |
|            | [ZCU111](https://www.xilinx.com/products/boards-and-kits/zcu111.html)         | [zcu111-zynqmp](conf/machine/zcu111-zynqmp.conf)             | `zcu111-zynqmp`       | `zcu111-reva`                       | Yes         | Yes       |
|            | [ZCU1275](https://www.xilinx.com/products/boards-and-kits/zcu1275.html)       | [zcu1275-zynqmp](conf/machine/zcu1275-zynqmp.conf)           | `zcu1275-zynqmp`      | `zcu1275-revb`                      | Yes         | Yes       |
|            | [ZCU1285](https://www.xilinx.com/products/boards-and-kits/zcu1285.html)       | [zcu1285-zynqmp](conf/machine/zcu1285-zynqmp.conf)           | `zcu1285-zynqmp`      | `zcu1285-reva`                      | Yes         | Yes       |
|            | [ZCU216](https://www.xilinx.com/products/boards-and-kits/zcu216.html)         | [zcu216-zynqmp](conf/machine/zcu216-zynqmp.conf)             | `zcu216-zynqmp`       | `zcu216-reva`                       | Yes         | Yes       |
| Versal     | [VCK190](https://www.xilinx.com/products/boards-and-kits/vck190.html)         | [vck190-versal](conf/machine/vck190-versal.conf)             | `vck190-versal`       | `versal-vck190-reva-x-ebm-01-reva`  | Yes         | Yes       |
|            | [VMK180](https://www.xilinx.com/products/boards-and-kits/vmk180.html)         | [vmk180-versal](conf/machine/vmk180-versal.conf)             | `vmk180-versal`       | `versal-vmk180-reva-x-ebm-01-reva`  | Yes         | Yes       |
|            | [VCK5000](https://www.xilinx.com/products/boards-and-kits/vck5000.html)       | [vck5000-versal](conf/machine/vck5000-versal.conf)           | `vck5000-versal`      | `versal-vck5000-reva-x-ebm-01-reva` | No          | No        |
|            | [VPK120](https://www.xilinx.com/products/boards-and-kits/vpk120.html)         | [vpk120-versal](conf/machine/vpk120-versal.conf)             | `vpk120-versal`       | `versal-vpk120-reva`                | Yes         | Yes       |

> **Note:** Additional information on Xilinx architectures can be found at:
	https://www.xilinx.com/products/silicon-devices.html
---
## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: langdale

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: langdale

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and amd xilinx release)
	layers: meta-xilinx-microblaze, meta-xilinx-core
	branch: langdale or amd xilinx release version (e.g. rel-v2022.2)
