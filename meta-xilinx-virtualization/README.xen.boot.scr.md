# Xen Boot Script Instructions

This readme describes on how to generate boot script for xen boot using imagebuilder
tool and boot using QEMU or Hardware.

1. The following variable needs to be added to the end of the conf/local.conf file.
   ```
   UBOOT_BOOT_SCRIPT:append = " u-boot-xen-scr"
   ```

2. [u-boot-xen-scr reciep](./recipes-bsp/u-boot/u-boot-xen-scr.bb) provides xen
   dom0 and dom0less template configs. User can use these template configs to
   generate the xen_boot.scr file or create your own config. Default config is
   <soc>-xen-scr-dom0-template-cfg.
   See [Image Builder README](https://gitlab.com/xen-project/imagebuilder/-/blob/master/README.md?ref_type=heads)
   for variable usage.

3. Create a custom xen image builder config, add variables and copy to one of the
   template as shown below.
    ```
    $ touch ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization/recipes-bsp/u-boot/u-boot-xen-scr/custom-cfg
    $ cp -r ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization/recipes-bsp/u-boot/u-boot-xen-scr/custom-cfg ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization/recipes-bsp/u-boot/u-boot-xen-scr/<soc>-xen-scr-dom0-template-cfg

    # versal-2ve-2vm example
    $ cp -r ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization/recipes-bsp/u-boot/u-boot-xen-scr/custom-cfg ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization/recipes-bsp/u-boot/u-boot-xen-scr/versal-2ve-2vm-xen-scr-dom0-template-cfg
    ```
    > **Note:** If you are using xen dom0less boot then copy your custom-cfg to
    > <soc>-xen-scr-dom0less-template-cfg and update XEN_TEMPLATE_CONFIG_DEFAULT variable
    > in ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization/recipes-bsp/u-boot/u-boot-xen-scr.bb
    > file as shown below.

    ```
    # versal-2ve-2vm example
    XEN_TEMPLATE_CONFIG_DEFAULT:versal-2ve-2vm = "versal-2ve-2vm-xen-scr-dom0less-template-cfg"
    ```

4. Now build the target image to deploy the xen_boot.scr to target image.
    ```
    $ MACHINE=<machine-name> bitbake <target-image-recipe>
    ```

## Xen Boot Instructions

1. Follow [Booting Instructions](../README.booting.md) and halt at u-boot.

2. Now source the xen_boot.scr as shown below, with this it will boot xen dom0
   or dom0less depending on config file.
   > **Note:** In below example xen_boot.scr is located in mmc device 0 ext partition
   >           number 2. The load command varies depending on boot device and
   >           partition number or tftp boot path.
    ```
    Versal> mmc dev 0; mmc part; ext4ls mmc 0:2; fdt addr $fdtcontroladdr;
    switch to partitions #0, OK
    mmc0 is current device

    Partition Map for mmc device 0  --   Partition Type: EFI

    Part    Start LBA       End LBA         Name
            Attributes
            Type GUID
            Partition GUID
      1     0x00000040      0x0010003f      "ESP"
            attrs:  0x0000000000000000
            type:   ebd0a0a2-b9e5-4433-87c0-68b6b72699c7
            guid:   4d0ff74a-d389-4aa1-b381-c7249390b306
      2     0x00100040      0x0020003f      "boot"
            attrs:  0x0000000000000004
            type:   0fc63daf-8483-4772-8e79-3d69d8477de4
            guid:   418a8cb3-c534-4e1a-b2fc-861e4c801e1e
      3     0x00200040      0x0100003f      "root"
            attrs:  0x0000000000000000
            type:   0fc63daf-8483-4772-8e79-3d69d8477de4
            guid:   cdd630e3-483c-42ea-8c23-06efa38438c6
      4     0x01000040      0x01a0003f      "xen"
            attrs:  0x0000000000000000
            type:   0fc63daf-8483-4772-8e79-3d69d8477de4
            guid:   9c691d05-45cc-4895-9395-7b6d7b6b495d
      5     0x01a00040      0x01a0603f      "other-os"
            attrs:  0x0000000000000000
            type:   0fc63daf-8483-4772-8e79-3d69d8477de4
            guid:   d22ea0ba-ac99-4d71-81ab-d9786bbf9431
      6     0x01a06040      0x01c0603f      "storage"
            attrs:  0x0000000000000000
            type:   ebd0a0a2-b9e5-4433-87c0-68b6b72699c7
            guid:   f806f104-8c4c-4d84-9b74-ada2dcb6e763
                ./
                ../
                lost+found/
            <SYM>   Image
     31965696   Image-6.12.10-xilinx-g21a247fc054c
          977   boot.scr
        <SYM>   xen
        <SYM>   xen-4
        <SYM>   xen-4.20
      1146904   xen-4.20.0-rc5
         3974   xen-4.20.0-rc5.config
         1451   xen_boot.scr

    9 file(s), 3 dir(s)

    Working FDT set to 7be94940
    Versal> setenv bootcmd_xen 'mmc dev 0; ext4load mmc 0:2 0xC00000 xen_boot.scr; source 0xC00000'; pri bootcmd_xen; run bootcmd_xen
   ```

3. Verify Xen Dom0 is up and running on QEMU or Hardware.

## References

* https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842530/Xen+Hypervisor
