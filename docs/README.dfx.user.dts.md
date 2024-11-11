# Build Instructions to create firmware recipes using dfx_user_dts bbclass

* [Introduction](#introduction)
* [How to create a firmware recipe app](#how-to-create-a-firmware-recipe-app)
* [Test Procedure on Target](#test-procedure-on-target)
  * [Loading PL bitstream or pdi and dt overlay](#loading-pl-bitstream-or-pdi-and-dt-overlay)
  * [Testing PL functionality](#testing-pl-functionality)
  * [Unloading PL bitstream or pdi and dt overlay](#unloading-pl-bitstream-or-pdi-and-dt-overlay)
* [References](#references)

## Introduction
This readme describes the build instructions to create firmware recipes using
dfx_user_dts.bbclass for dfx configuration. This bitbake class supports
following use cases.

> **Note:** Refer https://github.com/Xilinx/dfx-mgr/blob/master/README.md for
> shell.json and accel.json file content.

* **Zynq 7000, ZynqMP and Versal**:
  * Design: Vivado flat or Segmented Configuration design.
    * Input files to firmware recipes: .bit or .bin or _pld.pdi, .dtsi or dtbo and shell.json (optional)
    * Usage Examples:
```
# Zynq 7000 or ZynqMP flat design
SRC_URI = " \
    file://<flat_design_pl>.bit \
    file://<flat_design_pl>.dtsi \
    file://shell.json \
    "
```

```
# Zynq 7000 or ZynqMP flat design
SRC_URI = " \
    file://<flat_design_pl>.bit \
    file://<flat_design_pl>.dtbo \
    file://shell.json \
    "
```

```
# Zynq 7000 or ZynqMP flat design
SRC_URI = " \
    file://<flat_design_pl>.bin \
    file://<flat_design_pl>.dtsi \
    file://shell.json \
    "
```

```
# Zynq 7000 or ZynqMP flat design
SRC_URI = " \
    file://<flat_design_pl>.bin \
    file://<flat_design_pl>.dtbo \
    file://shell.json \
    "
```

```
# Zynq 7000 or ZynqMP flat design
SRC_URI = " \
    file://<flat_design_pl>.bit \
    file://shell.json \
    "
```

```
# Zynq 7000 or ZynqMP flat design
SRC_URI = " \
    file://<flat_design_pl>.bin \
    file://shell.json \
    "
```

```
# Versal Segmented Configuration design
SRC_URI = " \
    file://<flat_design>_pld.pdi \
    file://<flat_design>_pld.dtsi \
    file://shell.json \
    "
```

```
# Versal Segmented Configuration design
SRC_URI = " \
    file://<flat_design>_pld.pdi \
    file://<flat_design>_pld.dtbo \
    file://shell.json \
    "
```

```
# Versal Segmented Configuration design
SRC_URI = " \
    file://<flat_design>_pld.pdi \
    file://shell.json \
    "
```

* **ZynqMP and Versal**:
  * Design: Vivado DFx design.
    * Input files to firmware recipes: .bit/bin(ZynqMP) or .pdi(Versal), .dtsi or dtbo
      shell.json or accel.json (optional) and .xclbin (optional).
    * Usage Examples:

```
# ZynqMP DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.bit \
    file://<dfx_design_static_pl>.dtsi \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# ZynqMP DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.bit \
    file://<dfx_design_static_pl>.dtbo \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# ZynqMP DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.bin \
    file://<dfx_design_static_pl>.dtsi \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# ZynqMP DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.bin \
    file://<dfx_design_static_pl>.dtbo \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# ZynqMP DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.bit \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# ZynqMP DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.bin \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# ZynqMP DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.bit \
    file://<dfx_design_rp_rm_pl>.dtsi \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# ZynqMP DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.bit \
    file://<dfx_design_rp_rm_pl>.dtbo \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# ZynqMP DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.bin \
    file://<dfx_design_rp_rm_pl>.dtsi \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# ZynqMP DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.bin \
    file://<dfx_design_rp_rm_pl>.dtbo \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# ZynqMP DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.bit \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# ZynqMP DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.bin \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# Versal DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.pdi \
    file://<dfx_design_static_pl>.dtsi \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# Versal DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.pdi \
    file://<dfx_design_static_pl>.dtbo \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# Versal DFx Static
SRC_URI = " \
    file://<dfx_design_static_pl>.pdi \
    file://shell.json \
    file://<dfx_design_static_pl>.xclbin \
    "
```

```
# Versal DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.pdi \
    file://<dfx_design_rp_rm_pl>.dtsi \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# Versal DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.pdi \
    file://<dfx_design_rp_rm_pl>.dtbo \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```

```
# Versal DFx RP
SRC_URI = " \
    file://<dfx_design_rp_rm_pl>.pdi \
    file://accel.json \
    file://<dfx_design_rp_rm_pl>.xclbin \
    "
```
---

## How to create a firmware recipe app

1. Follow SDT or XSCT Build instructions whichever build method is used but not
   both.
    a. [SDT Building Instructions](../meta-xilinx-standalone-sdt/README.md) upto step 4.
    b. [XSCT Building Instructions](../README.building.md)
       upto step 4.b (With SDT overlay).
2. Create recipes-firmware directory in meta layer and copy the .bit/bin/pdi,
   .dtsi/dtbo, .json and .xclbin file to these directories.
```
$ mkdir -p <meta-layer>/recipes-firmware/<recipes-firmware-app>/files
$ cp -r <path-to-files>/*.{bit or bin or pdi, dtsi or dtbo, shell.json or accel.json and .xclbin} <meta-layer>/recipes-firmware/<firmware-app-name>/files
```
3. Now create the recipes for flat or static or partial firmware using recipetool.
```
$ recipetool create -o <meta-layer>/recipes-firmware/<firmware-app-name>/firmware-app-name.bb file:///<meta-layer>/recipes-firmware/<firmware-app-name>/files
```
4. Modify the recipe and inherit dfx_user_dts bbclass as shown below.
```
SUMMARY = "Full Bitstream loading app firmware using dfx_user_dts bbclass"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit dfx_user_dts

SRC_URI = "\
    file://shell.json \
    file://zcu111-pl-demo-user-dts.bit \
    file://zcu111-pl-demo-user-dts.dtsi \
    "

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
```
5. Add firmware-recipe app to image and enable fpga-overlay machine features to
   local.conf as shown below.
> **Note:** fpga-manager-script provides fpgautil tool to load .bin/pdi and dtbo
> at runtime linux.
```
MACHINE_FEATURES += "fpga-overlay"
IMAGE_INSTALL:append = " \
  firmware-app-name \
  fpga-manager-script \
  "
```
6. Follow SDT or XSCT Build instructions whichever build method is used but not
   both.
    a. [SDT Building Instructions](../meta-xilinx-standalone-sdt/README.md ) and continue from step 5.
    b. [XSCT Building Instructions](../README.building.md)
       and continue from step 5.
7. Once images are built firmware app files will be installed on target_rootfs.
```
# <target_rootfs>/lib/firmware/xilinx/firmware-app-name
```
---

## Test Procedure on Target
* Once Linux boots on target, use fpgautil command to load .bin or .pdi and
  corresponding dt overlay as shown below.
> **Note:** firmware can be loaded only with sudo or root permissions.
---

### Loading PL bitstream or pdi and dt overlay

* ZynqMP
```
yocto-zynqmp-generic:~$ cd /
yocto-zynqmp-generic:/$ sudo su
yocto-zynqmp-generic:/# cat /proc/interrupts
           CPU0       CPU1       CPU2       CPU3
 11:       3399       4404       3273       3113     GICv2  30 Level     arch_timer
 14:          0          0          0          0     GICv2  67 Level     zynqmp-ipi
 15:          0          0          0          0     GICv2  58 Level     ffa60000.rtc
 16:          0          0          0          0     GICv2  59 Level     ffa60000.rtc
 17:          0          0          0          0     GICv2  88 Level     ams-irq
 18:          0          0          0          0     GICv2 155 Level     axi-pmon, axi-pmon
 19:          0          0          0          0     GICv2 175 Level     arm-pmu
 20:          0          0          0          0     GICv2 176 Level     arm-pmu
 21:          0          0          0          0     GICv2 177 Level     arm-pmu
 22:          0          0          0          0     GICv2 178 Level     arm-pmu
 23:        379          0          0          0     GICv2  53 Level     xuartps
 26:          0          0          0          0     GICv2 156 Level     zynqmp-dma
 27:          0          0          0          0     GICv2 157 Level     zynqmp-dma
 28:          0          0          0          0     GICv2 158 Level     zynqmp-dma
 29:          0          0          0          0     GICv2 159 Level     zynqmp-dma
 30:          0          0          0          0     GICv2 160 Level     zynqmp-dma
 31:          0          0          0          0     GICv2 161 Level     zynqmp-dma
 32:          0          0          0          0     GICv2 162 Level     zynqmp-dma
 33:          0          0          0          0     GICv2 163 Level     zynqmp-dma
 34:          0          0          0          0     GICv2 109 Level     zynqmp-dma
 35:          0          0          0          0     GICv2 110 Level     zynqmp-dma
 36:          0          0          0          0     GICv2 111 Level     zynqmp-dma
 37:          0          0          0          0     GICv2 112 Level     zynqmp-dma
 38:          0          0          0          0     GICv2 113 Level     zynqmp-dma
 39:          0          0          0          0     GICv2 114 Level     zynqmp-dma
 40:          0          0          0          0     GICv2 115 Level     zynqmp-dma
 41:          0          0          0          0     GICv2 116 Level     zynqmp-dma
 42:          0          0          0          0     GICv2 154 Level     fd4c0000.dma-controller
 43:      11183          0          0          0     GICv2  47 Level     ff0f0000.spi
 44:         77          0          0          0     GICv2  95 Level     eth0, eth0
 45:          0          0          0          0     GICv2  57 Level     axi-pmon, axi-pmon
 46:       2365          0          0          0     GICv2  49 Level     cdns-i2c
 47:        326          0          0          0     GICv2  50 Level     cdns-i2c
 49:          0          0          0          0     GICv2  84 Edge      ff150000.watchdog
 50:          0          0          0          0     GICv2 151 Level     fd4a0000.display
 51:        551          0          0          0     GICv2  81 Level     mmc0
 52:          0          0          0          0     GICv2 165 Level     ahci-ceva[fd0c0000.ahci]
 53:          0          0          0          0     GICv2  97 Level     xhci-hcd:usb1
 54:          0          0          0          0  zynq-gpio  22 Edge      sw19
IPI0:        73         69        133        115       Rescheduling interrupts
IPI1:      2590       1426       1711      13134       Function call interrupts
IPI2:         0          0          0          0       CPU stop interrupts
IPI3:         0          0          0          0       CPU stop (for crash dump) interrupts
IPI4:         0          0          0          0       Timer broadcast interrupts
IPI5:         0          0          0          0       IRQ work interrupts
IPI6:         0          0          0          0       CPU wake-up interrupts
Err:          0
yocto-zynqmp-generic:/#
yocto-zynqmp-generic:/# tree /lib/firmware/
/lib/firmware/
`-- xilinx
    `-- zcu111-pl-demo-user-dts
        |-- shell.json
        |-- zcu111-pl-demo-user-dts.bin 
        `-- zcu111-pl-demo-user-dts.dtbo

2 directories, 3 files
yocto-zynqmp-generic:/#
yocto-zynqmp-generic:/# fpgautil -b /lib/firmware/xilinx/zcu111-pl-demo-user-dts/zcu111-pl-demo-user-dts.bin -o /lib/firmware/xilinx/zcu111-pl-demo-user-dts/zcu111-pl-demo-user-dts.dtbo
[   86.077583] fpga_manager fpga0: writing zcu111-pl-demo-user-dts.bin to Xilinx ZynqMP FPGA Manager
[   86.300854] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga-region/firmware-name
[   86.311158] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga-region/pid
[   86.320571] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga-region/resets
[   86.330230] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga-region/uid
[   86.340074] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/afi0
[   86.349574] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/clocking0
[   86.359510] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_gpio_0
[   86.369526] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/misc_clk_0
[   86.379544] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_gpio_1
[   86.389561] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_gpio_2
[   86.399588] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_uartlite_0
[   86.409951] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/ddr4_0
[   86.439309] a0030000.serial: ttyUL0 at MMIO 0xa0030000 (irq = 57, base_baud = 0) is a uartlite
[   86.456365] uartlite a0030000.serial: Runtime PM usage count underflow!
[   86.466353] input: axi:pl-gpio-keys as /devices/platform/axi/axi:pl-gpio-keys/input/input1
Time taken to load BIN is 402.000000 Milli Seconds
BIN FILE loaded through FPGA manager successfully
yocto-zynqmp-generic:/#
```
* Versal (DFx Static)
```
yocto-vck190-versal:/$ sudo su
yocto-vck190-versal:/# fpgautil -b /lib/firmware/xilinx/vck190-dfx-static/vck190-dfx-static.pdi -o /lib/firmware/xilinx/vck190-dfx-static/vck190-dfx-static.dtbo
[  110.575263] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga/external-fpga-config
[  110.585557] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga/pid
[  110.594365] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga/uid
[  110.603307] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/fpga_PR0
[  110.613152] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/fpga_PR1
[  110.623007] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/fpga_PR2
[  110.632849] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/static_region_axi_bram_ctrl_0
[  110.644516] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/static_region_dfx_decoupler_rp1
[  110.656351] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/static_region_dfx_decoupler_rp2
[  110.668188] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/static_region_dfx_decoupler_rp3
[  110.682762] of-fpga-region fpga:fpga-PR0: FPGA Region probed
[  110.689956] of-fpga-region fpga:fpga-PR1: FPGA Region probed
[  110.695890] of-fpga-region fpga:fpga-PR2: FPGA Region probed
Time taken to load BIN is 133.000000 Milli Seconds
BIN FILE loaded through FPGA manager successfully
yocto-vck190-versal:/#
```
* Versal (DFx RP)
```
yocto-vck190-versal:/$ sudo su
yocto-vck190-versal:/# fpgautil -b /lib/firmware/xilinx/vck190-dfx-rp1rm1-dipsw/vck190-dfx-rp1rm1-dipsw.pdi -o /lib/firmware/xilinx/vck190-dfx-rp1rm1-dipsw/vck190-dfx-rp1rm1-dipsw.dtbo -f Partial -n PR0
[  154.155127] fpga_manager fpga0: writing vck190-dfx-rp1rm1-dipsw.pdi to Xilinx Versal FPGA Manager
[173465.709]Loading PDI from DDR
[173465.800]Monolithic/Master Device
[173469.235]3.520 ms: PDI initialization time
[173473.045]+++Loading Image#: 0x0, Name: pl_cfi, Id: 0x18700002
[173478.669]---Loading Partition#: 0x0, Id: 0x103
[173483.052] 0.032 ms for Partition#: 0x0, Size: 1264 Bytes
[173488.219]---Loading Partition#: 0x1, Id: 0x203
[173492.599] 0.030 ms for Partition#: 0x1, Size: 672 Bytes
[173497.682]---Loading Partition#: 0x2, Id: 0x303
[173503.193] 1.159 ms for Partition#: 0x2, Size: 204960 Bytes
[173507.400]---Loading Partition#: 0x3, Id: 0x403
[173511.805] 0.054 ms for Partition#: 0x3, Size: 8400 Bytes
[173516.979]Subsystem PDI Load: Done
[  154.220425] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/rp1_axi_gpio_0
[  154.239592] input: axi:pl-gpio-keys as /devices/platform/axi/axi:pl-gpio-keys/input/input1
Time taken to load BIN is 99.000000 Milli Seconds
BIN FILE loaded through FPGA manager successfully
yocto-vck190-versal:/#
```
* Versal (Segmented Configuration)
```
yocto-vck190-versal:/$ sudo su
yocto-vck190-versal:/# fpgautil -b /lib/firmware/xilinx/vck190-dfx-full/vck190-dfx-full.pdi -o /lib/firmware/xilinx/vck190-dfx-full/vck190-dfx-full.dtbo
[  642.857986] fpga_manager fpga0: writing vck190-dfx-full.pdi to Xilinx Versal FPGA Manager
[653673.622]Loading PDI from DDR
[653673.713]Monolithic/Master Device
[653677.159]3.531 ms: PDI initialization time
[653680.973]+++Loading Image#: 0x0, Name: pl_cfi, Id: 0x18700001
[653686.608]---Loading Partition#: 0x0, Id: 0x103
[653705.058] 14.091 ms for Partition#: 0x0, Size: 22176 Bytes
[653707.626]---Loading Partition#: 0x1, Id: 0x105
[653712.243] 0.264 ms for Partition#: 0x1, Size: 4784 Bytes
[653717.183]---Loading Partition#: 0x2, Id: 0x205
[653725.148] 3.608 ms for Partition#: 0x2, Size: 64368 Bytes
[653727.632]---Loading Partition#: 0x3, Id: 0x203
[653732.018] 0.030 ms for Partition#: 0x3, Size: 672 Bytes
[653737.107]---Loading Partition#: 0x4, Id: 0x303
[653768.983] 27.516 ms for Partition#: 0x4, Size: 1115456 Bytes
[653771.723]---Loading Partition#: 0x5, Id: 0x305
[653777.150] 1.068 ms for Partition#: 0x5, Size: 69056 Bytes
[653781.371]---Loading Partition#: 0x6, Id: 0x403
[653785.892] 0.166 ms for Partition#: 0x6, Size: 242320 Bytes
[653791.103]---Loading Partition#: 0x7, Id: 0x405
ERR PldMemCtrlrMap: 0x490E
ERR PldInitNode: 0xFFFF
ERR XPm_InitNode: 0xFFFF
ALERT XPm_ProcessCmd: Error 0x15 while processing command 0xC023E
ALERT XPm_ProcessCmd: Err Code: 0x15
[653811.158]CMD: 0x000C023E execute failed, Processed Cdo Length 0x129C
[653817.390]CMD Payload START, Len:0x00000008
 0x00000000F20012C0: 0x18700001 0x0000000A 0xF6110000 0x00000002
 0x00000000F20012CC: 0x00000000 0x00000000 0x80000000 0x00000000
 0x00000000F20012DC:
[653834.800]CMD Payload END
[653837.277]Error loading PL data:
CFU_ISR: 0x00000000, CFU_STATUS: 0x00002A8C
PMC ERR1: 0x00000000, PMC ERR2: 0x00000000
[653848.127]PLM Error Status: 0x223E0015
[65 851.704]XPlm _IpiDispatehHandl0:: Error:hIPI crmmand faileddfor tommanA ID: 0x1000701
[653859.465]PLM Error Status: 0x27010015
[  643.063905] fpga_region region0: failed to load FPGA image
[  643.069420] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga/firmware-name
[  643.079075] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga/pid
[  643.087857] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /fpga/uid
[  643.096849] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_bram_ctrl_0
[  643.107288] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_gpio_dip_sw
[  643.117729] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_gpio_led
[  643.127906] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_gpio_pb
[  643.137996] OF: overlay: WARNING: memory leak will occur if overlay removed, property: /__symbols__/axi_uartlite_0
[  643.178340] 20100000000.serial: ttyUL0 at MMIO 0x20100000000 (irq = 41, base_baud = 0) is a uartlite
[  643.189536] uartlite 20100000000.serial: Runtime PM usage count underflow!
[  643.198059] input: axi:pl-gpio-keys as /devices/platform/axi/axi:pl-gpio-keys/input/input0
yocto-vck190-versal:/#
```
---

### Testing PL functionality 

* This examples uses PL GPIO DIP switches and Push buttons to capture interrupts.
* Verify PL GPIO DIP switches and Push buttons are registered.
* Move the DIP Switches ON/OFF and verify the interrupt counts.
```
yocto-zynqmp-generic:/# cat /proc/interrupts
           CPU0       CPU1       CPU2       CPU3
 11:       4254       6509       4214       4236     GICv2  30 Level     arch_timer
 14:          0          0          0          0     GICv2  67 Level     zynqmp-ipi
 15:          0          0          0          0     GICv2  58 Level     ffa60000.rtc
 16:          0          0          0          0     GICv2  59 Level     ffa60000.rtc
 17:          0          0          0          0     GICv2  88 Level     ams-irq
 18:          0          0          0          0     GICv2 155 Level     axi-pmon, axi-pmon
 19:          0          0          0          0     GICv2 175 Level     arm-pmu
 20:          0          0          0          0     GICv2 176 Level     arm-pmu
 21:          0          0          0          0     GICv2 177 Level     arm-pmu
 22:          0          0          0          0     GICv2 178 Level     arm-pmu
 23:        579          0          0          0     GICv2  53 Level     xuartps
 26:          0          0          0          0     GICv2 156 Level     zynqmp-dma
 27:          0          0          0          0     GICv2 157 Level     zynqmp-dma
 28:          0          0          0          0     GICv2 158 Level     zynqmp-dma
 29:          0          0          0          0     GICv2 159 Level     zynqmp-dma
 30:          0          0          0          0     GICv2 160 Level     zynqmp-dma
 31:          0          0          0          0     GICv2 161 Level     zynqmp-dma
 32:          0          0          0          0     GICv2 162 Level     zynqmp-dma
 33:          0          0          0          0     GICv2 163 Level     zynqmp-dma
 34:          0          0          0          0     GICv2 109 Level     zynqmp-dma
 35:          0          0          0          0     GICv2 110 Level     zynqmp-dma
 36:          0          0          0          0     GICv2 111 Level     zynqmp-dma
 37:          0          0          0          0     GICv2 112 Level     zynqmp-dma
 38:          0          0          0          0     GICv2 113 Level     zynqmp-dma
 39:          0          0          0          0     GICv2 114 Level     zynqmp-dma
 40:          0          0          0          0     GICv2 115 Level     zynqmp-dma
 41:          0          0          0          0     GICv2 116 Level     zynqmp-dma
 42:          0          0          0          0     GICv2 154 Level     fd4c0000.dma-controller
 43:      11183          0          0          0     GICv2  47 Level     ff0f0000.spi
 44:        146          0          0          0     GICv2  95 Level     eth0, eth0
 45:          0          0          0          0     GICv2  57 Level     axi-pmon, axi-pmon
 46:       2365          0          0          0     GICv2  49 Level     cdns-i2c
 47:        326          0          0          0     GICv2  50 Level     cdns-i2c
 49:          0          0          0          0     GICv2  84 Edge      ff150000.watchdog
 50:          0          0          0          0     GICv2 151 Level     fd4a0000.display
 51:        551          0          0          0     GICv2  81 Level     mmc0
 52:          0          0          0          0     GICv2 165 Level     ahci-ceva[fd0c0000.ahci]
 53:          0          0          0          0     GICv2  97 Level     xhci-hcd:usb1
 54:          0          0          0          0  zynq-gpio  22 Edge      sw19
 58:          0          0          0          0  gpio-xilinx   4 Edge      PL_GPIO_PB_SW9_N
 59:          0          0          0          0  gpio-xilinx   3 Edge      PL_GPIO_PB_SW12_E
 60:          0          0          0          0  gpio-xilinx   2 Edge      PL_GPIO_PB_SW13_S
 61:          0          0          0          0  gpio-xilinx   1 Edge      PL_GPIO_PB_SW10_W
 62:          0          0          0          0  gpio-xilinx   0 Edge      PL_GPIO_PB_SW11_C
 63:          0          0          0          0  gpio-xilinx   7 Edge      PL_GPIO_DIP_SW7
 64:          0          0          0          0  gpio-xilinx   6 Edge      PL_GPIO_DIP_SW6
 65:          0          0          0          0  gpio-xilinx   5 Edge      PL_GPIO_DIP_SW5
 66:          0          0          0          0  gpio-xilinx   4 Edge      PL_GPIO_DIP_SW4
 67:          0          0          0          0  gpio-xilinx   3 Edge      PL_GPIO_DIP_SW3
 68:          0          0          0          0  gpio-xilinx   2 Edge      PL_GPIO_DIP_SW2
 69:          0          0          0          0  gpio-xilinx   1 Edge      PL_GPIO_DIP_SW1
 70:          0          0          0          0  gpio-xilinx   0 Edge      PL_GPIO_DIP_SW0
IPI0:        77         79        141        123       Rescheduling interrupts
IPI1:      2621       1536       1782      13236       Function call interrupts
IPI2:         0          0          0          0       CPU stop interrupts
IPI3:         0          0          0          0       CPU stop (for crash dump) interrupts
IPI4:         0          0          0          0       Timer broadcast interrupts
IPI5:         0          0          0          0       IRQ work interrupts
IPI6:         0          0          0          0       CPU wake-up interrupts
Err:          0
yocto-zynqmp-generic:/#
yocto-zynqmp-generic:/#
yocto-zynqmp-generic:/# cat /proc/interrupts
           CPU0       CPU1       CPU2       CPU3
 11:       4972       7894       4568       4673     GICv2  30 Level     arch_timer
 14:          0          0          0          0     GICv2  67 Level     zynqmp-ipi
 15:          0          0          0          0     GICv2  58 Level     ffa60000.rtc
 16:          0          0          0          0     GICv2  59 Level     ffa60000.rtc
 17:          0          0          0          0     GICv2  88 Level     ams-irq
 18:          0          0          0          0     GICv2 155 Level     axi-pmon, axi-pmon
 19:          0          0          0          0     GICv2 175 Level     arm-pmu
 20:          0          0          0          0     GICv2 176 Level     arm-pmu
 21:          0          0          0          0     GICv2 177 Level     arm-pmu
 22:          0          0          0          0     GICv2 178 Level     arm-pmu
 23:        685          0          0          0     GICv2  53 Level     xuartps
 26:          0          0          0          0     GICv2 156 Level     zynqmp-dma
 27:          0          0          0          0     GICv2 157 Level     zynqmp-dma
 28:          0          0          0          0     GICv2 158 Level     zynqmp-dma
 29:          0          0          0          0     GICv2 159 Level     zynqmp-dma
 30:          0          0          0          0     GICv2 160 Level     zynqmp-dma
 31:          0          0          0          0     GICv2 161 Level     zynqmp-dma
 32:          0          0          0          0     GICv2 162 Level     zynqmp-dma
 33:          0          0          0          0     GICv2 163 Level     zynqmp-dma
 34:          0          0          0          0     GICv2 109 Level     zynqmp-dma
 35:          0          0          0          0     GICv2 110 Level     zynqmp-dma
 36:          0          0          0          0     GICv2 111 Level     zynqmp-dma
 37:          0          0          0          0     GICv2 112 Level     zynqmp-dma
 38:          0          0          0          0     GICv2 113 Level     zynqmp-dma
 39:          0          0          0          0     GICv2 114 Level     zynqmp-dma
 40:          0          0          0          0     GICv2 115 Level     zynqmp-dma
 41:          0          0          0          0     GICv2 116 Level     zynqmp-dma
 42:          0          0          0          0     GICv2 154 Level     fd4c0000.dma-controller
 43:      11183          0          0          0     GICv2  47 Level     ff0f0000.spi
 44:        265          0          0          0     GICv2  95 Level     eth0, eth0
 45:          0          0          0          0     GICv2  57 Level     axi-pmon, axi-pmon
 46:       2365          0          0          0     GICv2  49 Level     cdns-i2c
 47:        326          0          0          0     GICv2  50 Level     cdns-i2c
 49:          0          0          0          0     GICv2  84 Edge      ff150000.watchdog
 50:          0          0          0          0     GICv2 151 Level     fd4a0000.display
 51:        551          0          0          0     GICv2  81 Level     mmc0
 52:          0          0          0          0     GICv2 165 Level     ahci-ceva[fd0c0000.ahci]
 53:          0          0          0          0     GICv2  97 Level     xhci-hcd:usb1
 54:          0          0          0          0  zynq-gpio  22 Edge      sw19
 58:         12          0          0          0  gpio-xilinx   4 Edge      PL_GPIO_PB_SW9_N
 59:          8          0          0          0  gpio-xilinx   3 Edge      PL_GPIO_PB_SW12_E
 60:          8          0          0          0  gpio-xilinx   2 Edge      PL_GPIO_PB_SW13_S
 61:          8          0          0          0  gpio-xilinx   1 Edge      PL_GPIO_PB_SW10_W
 62:         10          0          0          0  gpio-xilinx   0 Edge      PL_GPIO_PB_SW11_C
 63:          2          0          0          0  gpio-xilinx   7 Edge      PL_GPIO_DIP_SW7
 64:          4          0          0          0  gpio-xilinx   6 Edge      PL_GPIO_DIP_SW6
 65:          2          0          0          0  gpio-xilinx   5 Edge      PL_GPIO_DIP_SW5
 66:          2          0          0          0  gpio-xilinx   4 Edge      PL_GPIO_DIP_SW4
 67:          2          0          0          0  gpio-xilinx   3 Edge      PL_GPIO_DIP_SW3
 68:          2          0          0          0  gpio-xilinx   2 Edge      PL_GPIO_DIP_SW2
 69:          2          0          0          0  gpio-xilinx   1 Edge      PL_GPIO_DIP_SW1
 70:          4          0          0          0  gpio-xilinx   0 Edge      PL_GPIO_DIP_SW0
IPI0:        77         79        142        123       Rescheduling interrupts
IPI1:      2641       1596       2011      13239       Function call interrupts
IPI2:         0          0          0          0       CPU stop interrupts
IPI3:         0          0          0          0       CPU stop (for crash dump) interrupts
IPI4:         0          0          0          0       Timer broadcast interrupts
IPI5:         0          0          0          0       IRQ work interrupts
IPI6:         0          0          0          0       CPU wake-up interrupts
Err:          0
yocto-zynqmp-generic:/#
```
---

### Unloading PL bitstream or pdi and dt overlay
* Zynq or ZynqMP
```
yocto-zynqmp-generic:/home/petalinux# fpgautil -R
```
* Versal (DFx RP)
```
yocto-vck190-versal:/# fpgautil -R -n PR0
```
* Versal (DFx Static)
```
yocto-vck190-versal:/# fpgautil -R -n Full
```
---

## References
* https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18841645/Solution+Zynq+PL+Programming+With+FPGA+Manager
* https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18841847/Solution+ZynqMP+PL+Programming
* https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/1188397412/Solution+Versal+PL+Programming
