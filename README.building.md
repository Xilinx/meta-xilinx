# Build Instructions

This section describes how to get your build host ready to work with meta-xilinx
layers.

The following instructions require OE-Core meta and BitBake. Poky provides these
components, however they can be acquired separately.

> **Pre-requisites:** Refer [Preparing Build Host](https://docs.yoctoproject.org/5.0.4/singleindex.html#preparing-the-build-host) documentation.

1. Create a project directory.
```
$ mkdir sources
$ cd sources
```

2. Clone the poky, openembedded and AMD repository.
> **Note:**
> * *release_branch:* refers to upstream stable release branch.
> * *rel-version:* refers to AMD release version.
> * *README:* By default README file link will be pointing to master branch so make
> sure to checkout the release_branch or rel-version branch.
> * *Upstream Pending Patches:* It is intended to resync Scarthgap (upstream) for
> both meta-virtualization and meta-openamp, but currently there are some patches
> in there that have not yet been accepted by upstream. So using the fork from
> https://github.com/Xilinx is recommended.

```
$ mkdir sources
$ git clone -b <release-branch> https://git.yoctoproject.org/poky
$ git clone -b <release-branch> https://git.openembedded.org/meta-openembedded
$ git clone -b <release-branch> https://git.yoctoproject.org/meta-virtualization
$ git clone -b <release-branch> https://git.yoctoproject.org/meta-arm
$ git clone -b <release-branch> https://github.com/OpenAMP/meta-openamp
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx --recurse-submodules
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx-tools
```
> **Note:**
> * When meta-xilinx layer is cloned using git tool by default it will clone
> [gen-machine-conf](https://github.com/Xilinx/gen-machine-conf.git) repo as
> submodules, If you don't need to clone gen-machine-conf repo then remove
> `--recurse-submodules` option.
> * Skip this step if you are using yocto-manifests https://github.com/Xilinx/yocto-manifests

3. Initialize a build environment using the `oe-init-build-env` script. 
```
$ source poky/oe-init-build-env
```

4. Once initialized configure `bblayers.conf` by adding dependency layers as shown
   below using `bitbake-layers` command.
> **Note:** From step 3 by default `meta-yocto-bsp` will be included in bblayers.conf
> file and this can be removed using `$ bitbake-layers remove-layer meta-yocto-bsp`
> command.

```
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-oe
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-python
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-networking
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-filesystems
$ bitbake-layers add-layer ./<path-to-layer>/meta-virtualization
$ bitbake-layers add-layer ./<path-to-layer>/meta-arm/meta-arm-toolchain
$ bitbake-layers add-layer ./<path-to-layer>/meta-arm/meta-arm
$ bitbake-layers add-layer ./<path-to-layer>/meta-openamp
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-microblaze
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-core
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-standalone
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-standalone-sdt
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-bsp
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx-tools
```
> **Note:**
> 1. For SDT build flow user can remove meta-xilinx-tools as this layer is
> optional.
> 2. If user wants to build machine files supported by meta-xilinx-vendor or
> met-xilinx-contrib layer then include these layer running following commands.
```
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx-vendor
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx-contrib
```

5. Create a new layer to for SDT or XSCT machine files geneated using gen-machineconf
   tool. If user already has a custom-bsp layer then you can skip this step.
```
$ bitbake-layers create-layer --add-layer --layerid <layerid> <layername>
```

6. For NFS build host system modify the build/conf/local.conf and add TMPDIR
   path as shown below. On local storage $TMPDIR will be set to build/tmp
```
TMPDIR = "/tmp/$USER/yocto/release_version/build"
```

7. Follow generating SDT or XSCT machine configuration file instructions using
   gen-machineconf tool. SDT or XSCT machine files are generated using sdtgen
   output or xsa.
   * [SDT](https://github.com/Xilinx/meta-xilinx/blob/master/meta-xilinx-standalone-sdt/README.sdt.bsp.md)
   * [XSCT](https://github.com/Xilinx/meta-xilinx-tools/blob/master/README.xsct.bsp.md)

8. Set hardware `MACHINE` configuration variable in <proj-dir>/build/conf/local.conf
   file for a specific target which can boot and run the in the board or QEMU.
```
MACHINE = "<target_machine_name>"
```
* For list of available pre-built target machines see meta layer README files.

  * [meta-amd-adaptive-socs-bsp README](https://github.com/Xilinx/meta-amd-adaptive-socs/blob/master/meta-amd-adaptive-socs-bsp/README.asoc.bsp.md)
  * [meta-xilinx-tools README](https://github.com/Xilinx/meta-xilinx-tools/blob/master/README.xsct.bsp.md)
  * [meta-kria README](https://github.com/Xilinx/meta-kria/blob/master/README.kria.bsp.md)

9. Once machine files are generated in <conf-directory>/machine/<soc-family>-<board-name>-<sdt-or-xsct>-<design-name>.conf,
   include the QEMU DT files, See [QEMU Configurations](#qemu-configurations)
   section for more details. This step can be skipped if you are using pre-built
   target machines files.

10. Modify the build/conf/local.conf file to add wic image to default target
   image as shown below.
```
IMAGE_FSTYPES += "wic"
WKS_FILES = "xilinx-default-sd.wks"
```

11.  Build the qemu-helper-native package to setup QEMU network tap devices.
```
$ bitbake qemu-helper-native
```

12.  Manually configure a tap interface for your build system. As root run
   <path-to>/sources/poky/scripts/runqemu-gen-tapdevs, which should generate a
   list of tap devices. Once tap interfaces are successfully create you should
   be able to see all the interfaces by running ifconfig command.

```
$ sudo ./<path-to-layer>/poky/scripts/runqemu-gen-tapdevs $(id -u $USER) $(id -g $USER) 4 tmp/sysroots-components/x86_64/qemu-helper-native/usr/bin
```

13. Build an OS image for the target using `bitbake` command.
> **Note:** Refer ./<path-to-distro-layer>/conf/templates/default/conf-notes.txt
> for available target image-name. e.g. core-image-minimal or petalinux-image-minimal
```
$ bitbake <target-image>
```

14. Once complete the images for the target machine will be available in the output
   directory `${TMPDIR}/deploy/images/${MACHINE}/`.

15. Follow [Booting Instructions](https://github.com/Xilinx/meta-xilinx/blob/master/README.booting.md)

## QEMU Configurations

This section describes the QEMU settings which must be added to the generated
machine configuration file in order to use the runqemu command. The following
board settings need to be added in sdt or xsct machine configuration file to
define which QEMU device trees should be used.

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

> **Note:** QEMU_HW_DTB_PS files are based on eval board schematics. If you are
> using a custom board then user has to create a QEMU_HW_DTB_PS to match their
> custom boards. Refer https://github.com/Xilinx/qemu-devicetrees/blob/master/board-versal-ps-vek280.dts
> as an example.

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

> **Note:** Additional information on AMD Adaptive SoC's and FPGA's can be found at:
	https://www.amd.com/en/products/adaptive-socs-and-fpgas.html
