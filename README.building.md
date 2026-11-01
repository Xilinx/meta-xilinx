# Build Instructions

This section describes how to get your build host ready to work with meta-xilinx
layers.

The following instructions require OE-Core, meta-openembedded and BitBake.

> **Pre-requisites:** Refer [Preparing Build Host](https://docs.yoctoproject.org/singleindex.html#preparing-the-build-host) documentation.

1. Create a project directory.
```
$ mkdir -p yp/sources
$ cd yp/sources
```

2. Clone the OpenEmbedded, Yocto and AMD repository.
> **Note:**
> * *<release-branch>:* refers to upstream stable release branch.
> * *<rel-version>:* refers to AMD release version.
> * *README:* By default README file link will be pointing to master branch so make
> sure to checkout the <release-branch> or <rel-version> branch.
> * *Upstream Pending Patches:* It is intended to resync Wrynose (upstream) for
> both meta-virtualization and meta-openamp, but currently there are some patches
> in there that have not yet been accepted by upstream. So using the fork from
> https://github.com/Xilinx is recommended.

```
$ git clone -b <release-branch> https://git.openembedded.org/openembedded-core
$ git clone -b <release-branch> https://git.openembedded.org/bitbake
$ git clone -b <release-branch> https://git.yoctoproject.org/meta-yocto
$ git clone -b <release-branch> https://git.openembedded.org/meta-openembedded
$ git clone -b <release-branch> https://git.yoctoproject.org/meta-security
$ git clone -b <release-branch> https://git.yoctoproject.org/meta-arm
$ git clone -b <rel-version> https://github.com/Xilinx/meta-virtualization
$ git clone -b <rel-version> https://github.com/Xilinx/meta-openamp
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx
$ git clone -b <rel-version> https://github.com/Xilinx/meta-amd-adaptive-socs
$ git clone -b <rel-version> https://github.com/Xilinx/gen-machine-conf
$ cd ../
```
> **Note:** gen-machine-conf above is a tool to generate machines, while
> optional, it is recommended.

3. Initialize a build environment using the `oe-init-build-env` script.
```
$ source sources/openembedded-core/oe-init-build-env
```

4. Once initialized configure `bblayers.conf` by adding dependency layers as shown
   below using `bitbake-layers` command.

```
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-oe
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-python
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-networking
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-filesystems
$ bitbake-layers add-layer ./<path-to-layer>/meta-arm/meta-arm-toolchain
$ bitbake-layers add-layer ./<path-to-layer>/meta-arm/meta-arm
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-microblaze
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-core
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-standalone
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-standalone-sdt
$ bitbake-layers add-layer ./<path-to-layer>/meta-amd-adaptive-socs/meta-amd-adaptive-socs-bsp
```
> **Note:**
> 1. If any of the layers depends on other layers then add those layers first.
> 2. If user wants to use MALI400 or MALIG78AE GPU or Multimedia or PL firmware
>    demos or Virtualization then add these respective layers. For more details
>    see layer README files.
> ```
> $ bitbake-layers add-layer ./<path-to-layer>/meta-amd-adaptive-socs/meta-amd-adaptive-socs-core
> $ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-mali400
> $ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-demos
> $ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-multimedia
> $ bitbake-layers add-layer ./<path-to-layer>/meta-virtualization
> $ bitbake-layers add-layer ./<path-to-layer>/meta-security
> $ bitbake-layers add-layer ./<path-to-layer>/meta-security/meta-tpm
> $ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization
> $ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-gpu-malig78ae
> $ bitbake-layers add-layer ./<path-to-layer>/meta-openamp
> ```

5. Add the gen-machine-conf tool to PATH so the gen-machine-conf command is
   available in the current shell. This is only needed if you are generating
   SDT machine files, see step 9.
```
$ export PATH=<path-to-layer>/gen-machine-conf:$PATH
```
> **Note:** The gen-machine-conf executable lives in the root of the cloned
> gen-machine-conf repository, so add the clone directory itself to PATH and not
> the executable. For example, if the repository was cloned to
> /home/user/yp/sources/gen-machine-conf then use:
> ```
> $ export PATH=/home/user/yp/sources/gen-machine-conf:$PATH
> ```
> Verify the tool is picked up by running `which gen-machine-conf`.

6. Create a new layer for the SDT machine files generated using the
   gen-machine-conf tool. If user already has a custom-bsp layer then you can
   skip this step.
```
$ bitbake-layers create-layer --add-layer --layerid <layerid> <layername>
```

7. For NFS build host system modify the build/conf/local.conf and add TMPDIR
   path as shown below. On local storage $TMPDIR will be set to build/tmp
```
TMPDIR = "/tmp/$USER/yocto/release_version/build"
```
8. The current configuration does not allow the root user to login. As this
   can be useful for development, you can enable the
   core/yocto/root-login-with-empty-password fragment:

```
$ bitbake-config-build enable-fragment core/yocto/root-login-with-empty-password
```

9. Follow generating SDT machine configuration file instructions using
   gen-machine-conf tool. SDT machine files are generated using sdtgen
   output.
   * [SDT](meta-xilinx-standalone-sdt/README.sdt.bsp.md)

10. Once machine files are generated in <conf-directory>/machine/<soc-family>-<board-name>-<sdt>-<design-name>.conf,
    include the QEMU DT files, See [QEMU Configurations](#qemu-configurations)
    section for more details. This step can be skipped if you are using pre-built
    target machines files.

11. Modify the build/conf/local.conf file to add wic image to default target
    image as shown below.
```
IMAGE_FSTYPES += "wic wic.xz wic.bmap"
```

12. By default images boot from distro boot. If user would like to boot from
    standalone EFI bootloader then set the EFI_PROVIDER variable to `systemd-boot`
    to use this class in build/conf/local.conf file else skip this step.
```
DISTRO_FEATURES:append = " efi"
EFI_PROVIDER ?= "systemd-boot"
```

13. Build the qemu-helper-native package to setup QEMU network tap devices.
```
$ MACHINE=<target_machine_name> bitbake qemu-helper-native
```

14. Manually configure a tap interface for your build system. As root run
    ./<path-to-layer>/openembedded-core/scripts/runqemu-gen-tapdevs, which should
    generate a list of tap devices. Once tap interfaces are successfully created
    you should be able to see all the interfaces by running ifconfig command.

```
$ sudo ./<path-to-layer>/openembedded-core/scripts/runqemu-gen-tapdevs $(id -g $USER) 4
```

15. Build an OS image for the target using `bitbake` command.
> **Note:** Refer ./<path-to-distro-layer>/conf/templates/default/conf-notes.txt
> for available target image-name. e.g. core-image-minimal or core-image-full-cmdline
```
$ MACHINE=<target_machine_name> bitbake <target-image>
```

16. Once complete the images for the target machine will be available in the output
    directory `${TMPDIR}/deploy/images/${MACHINE}/`.

17. Follow [Booting Instructions](README.booting.md)

## QEMU Configurations

This section describes the QEMU settings which must be added to the generated
machine configuration file in order to use the runqemu command. The following
board settings need to be added in sdt machine configuration file to define
which QEMU device trees should be used.

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
> QEMU Memory: Memory configurations differ per board, hence we need to adjust
> QB_MEM to match the board dtsi files. ZynqMP boards use a flat allocation,
> while Versal and Versal Gen 2 boards describe each DDR region separately.
> Below are some examples.
> * ZynqMP ZCU102 `QB_MEM = "-m 4G"`
> * Versal VCK190 `QB_MEM = "-machine dynamic-mem=on -device amd-ddr-memory,address=0x800000000,size=0x180000000 -device amd-ddr-memory,address=0x50000000000,size=0x100000000 -device amd-ddr-memory,address=0x60000000000,size=0x100000000"`
> * Versal Gen 2 VEK385 `QB_MEM = "-machine dynamic-mem=on -device amd-ddr-memory,address=0x800000000,size=0x80000000 -device amd-ddr-memory,address=0x50000000000,size=0x100000000 -device amd-ddr-memory,address=0x60000000000,size=0x80000000"`

> **Note:** QEMU_HW_DTB_PS files are based on eval board schematics. If you are
> using a custom board then user has to create a QEMU_HW_DTB_PS to match their
> custom boards. Refer https://github.com/Xilinx/qemu-devicetrees/blob/master/board-versal-ps-vek280.dts
> as an example.

The table below lists the values used by the reference machines shipped in
meta-amd-adaptive-socs. Versal Gen 2 2VE/2VM boards do not set a PMC device
tree.

| Devices       | Evaluation Board                                                              |   QEMU PMC or PMU DTB file   |        QEMU PS DTB file            |
|---------------|-------------------------------------------------------------------------------|------------------------------|------------------------------------|
| ZynqMP        | [ZCU102](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/ek-u1-zcu102-g.html) | `zynqmp-pmu.dtb`             | `board-zynqmp-zcu102.dtb`          |
|               | [ZCU104](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/zcu104.html)         | `zynqmp-pmu.dtb`             | `board-zynqmp-zcu104.dtb`          |
|               | [ZCU106](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/zcu106.html)         | `zynqmp-pmu.dtb`             | `board-zynqmp-zcu102.dtb`          |
|               | [ZCU111](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/zcu111.html)         | `zynqmp-pmu.dtb`             | `board-zynqmp-zcu102.dtb`          |
|               | [ZCU208](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/zcu208.html)         | `zynqmp-pmu.dtb`             | `board-zynqmp-zcu102.dtb`          |
|               | [ZCU216](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/zcu216.html)         | `zynqmp-pmu.dtb`             | `board-zynqmp-zcu102.dtb`          |
| Versal        | [VCK190](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vck190.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vck190.dtb`       |
|               | [VMK180](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vmk180.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vmk180.dtb`       |
|               | [VPK120](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vpk120.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vpk120.dtb`       |
|               | [VPK180](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vpk180.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vpk180.dtb`       |
|               | [VEK280](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vek280.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vek280.dtb`       |
|               | [VHK158](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vhk158.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vhk158.dtb`       |
|               | [VRK160](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vrk160.html)         | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vrk160.dtb`       |
|               | VRK165                                                                        | `board-versal-pmc-virt.dtb`  | `board-versal-ps-vrk165.dtb`       |
| Versal Gen 2  | [VEK385](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vek385.html)         | n/a                          | `board-versal2-psxc-vek385.dtb`    |
|               | VEK386                                                                        | n/a                          | `board-versal2-psxc-vek386.dtb`    |
|               | VMK365                                                                        | n/a                          | `board-versal2-psxc-vmk365.dtb`    |
|               | [VPK360](https://www.amd.com/en/products/adaptive-socs-and-fpgas/evaluation-boards/vpk360.html)         | `board-versal2-pmc-vpk360.dtb` | `board-versal2-ps-vpk360.dtb`    |

> **Note:** Additional information on AMD Adaptive SoC's and FPGA's can be found at:
> https://www.amd.com/en/products/adaptive-socs-and-fpgas.html
