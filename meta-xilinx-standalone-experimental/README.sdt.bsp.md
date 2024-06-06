# SDT BSP

This section describes the SDT BSP settings which must be added to the generated
machine configuration file, following [Build Instructions](README.md) step 4, in
order to use the runqemu command.

## SDT BSP settings

The following board settings need to be added in sdt machine configuration file
to define which QEMU device trees should be used.

> **Variable usage examples:**
>
> QEMU Device tree deploy directory: `QEMU_HW_DTB_PATH = "${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees/multiarch"`
> 
> QEMU PMU Device tree: `QEMU_HW_DTB_PMU = "${QEMU_HW_DTB_PATH}/zynqmp-pmu.dtb"`
> 
> QEMU PS Device tree: `QEMU_HW_DTB_PS = "${QEMU_HW_DTB_PATH}/board-versal-ps-vck190.dtb"`
>
> QEMU PMC Board Device tree: `QEMU_HW_DTB_PMC = "${QEMU_HW_DTB_PATH}/board-versal-pmc-virt.dtb"`
>
> QEMU Memory: Some boards for example VEK280 and VH158 memory configurations are
> different, Hence we need to adjust the same in QB_MEM to match board dtsi files.
> Below are some examples.
> * ZynqMP `QB_MEM = "-m 4096"`
> * Versal VEK280 `QB_MEM = "-m 12G"`


| Devices | Evaluation Board                                                              |   QEMU PMC or PMU DTB file  |        QEMU PS DTB file       | QB Mem |
|---------|-------------------------------------------------------------------------------|-----------------------------|-------------------------------|--------|
| ZynqMP  | [ZCU102](https://www.xilinx.com/products/boards-and-kits/ek-u1-zcu102-g.html) | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
|         | [ZCU104](https://www.xilinx.com/products/boards-and-kits/zcu104.html)         | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
|         | [ZCU106](https://www.xilinx.com/products/boards-and-kits/zcu106.html)         | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
|         | [ZCU111](https://www.xilinx.com/products/boards-and-kits/zcu111.html)         | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
|         | [ZCU208](https://www.xilinx.com/products/boards-and-kits/zcu208.html)         | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
|         | [ZCU216](https://www.xilinx.com/products/boards-and-kits/zcu216.html)         | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
|         | [ZCU670](https://www.xilinx.com/products/boards-and-kits/zcu670.html)         | `zynqmp-pmu.dtb`            | `zcu102-arm.dtb`              |  4096  |
| Versal  | [VCK190](https://www.xilinx.com/products/boards-and-kits/vck190.html)         | `board-versal-pmc-virt.dtb` | `board-versal-ps-vck190.dtb`  |   8G   |
|         | [VMK180](https://www.xilinx.com/products/boards-and-kits/vmk180.html)         | `board-versal-pmc-virt.dtb` | `board-versal-ps-vmk180.dtb`  |   8G   |
|         | [VPK120](https://www.xilinx.com/products/boards-and-kits/vpk120.html)         | `board-versal-pmc-virt.dtb` | `board-versal-ps-vpk120.dtb`  |   8G   |
|         | [VPK180](https://www.xilinx.com/products/boards-and-kits/vpk180.html)         | `board-versal-pmc-virt.dtb` | `board-versal-ps-vpk180.dtb`  |   8G   |
|         | [VEK280](https://www.xilinx.com/products/boards-and-kits/vek280.html)         | `board-versal-pmc-virt.dtb` | `board-versal-ps-vek280.dtb`  |  12G   |
|         | [VHK158](https://www.xilinx.com/products/boards-and-kits/vhk158.html)         | `board-versal-pmc-virt.dtb` | `board-versal-ps-vhk158.dtb`  |  32G   |

> **Note:** Additional information on Xilinx architectures can be found at:
	https://www.xilinx.com/products/silicon-devices.html
