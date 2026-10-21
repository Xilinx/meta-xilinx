# Xen Boot Script Instructions

This readme describes on how to generate boot script for xen boot using imagebuilder
tool and boot using QEMU or Hardware.

1. The following variable needs to be added to the end of the conf/local.conf file.
   ```
   IMAGE_INSTALL:append = " u-boot-xen-scr"
   ```

2. [u-boot-xen-scr recipe](./recipes-bsp/u-boot/u-boot-xen-scr.bb) provides xen
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
   > **Note:** In below example xen_boot.scr is located in the /boot directory of
   >           mmc device 0 ext partition number 3. ZynqMP and Versal boot from
   >           SD by default, whereas Versal-2ve-2vm boots from UFS and so uses
   >           the scsi interface. The load command varies depending on boot
   >           device and partition number or tftp boot path.
    ```
    Versal> mmc dev 0; mmc part; ext4ls mmc 0:3 /boot
    switch to partitions #0, OK
    mmc0 is current device

    Partition Map for mmc device 0  --   Partition Type: DOS

    Part	Start Sector	Num Sectors	    UUID		    Type
      1	    8         	    2097152   	    076c4a2a-01	    0c Boot
      2	    2097160   	    4194304   	    076c4a2a-02	    0c
      3	    6291464   	    7870604   	    076c4a2a-03	    83
                ./
                ../
        <SYM>   Image
     31750656   Image-6.18.40-xilinx-g59d213656d69
        <SYM>   xen
        <SYM>   xen-4
        <SYM>   xen-4.21
      1179680   xen-4.21.0
         5127   xen-4.21.0.config
         1509   xen_boot.scr
        61752   zephyr-synchronization-xenvmgicv3.bin

    9 file(s), 2 dir(s)

    Versal>
   ```

   The load command differs per SoC, as the boot medium and the load address
   depend on the platform. Use the command matching your target.

   **ZynqMP:**
    ```
    ZynqMP> setenv bootcmd_xen 'mmc dev 0; ext4load mmc 0:3 0xC00000 /boot/xen_boot.scr; source 0xC00000'; pri bootcmd_xen; run bootcmd_xen
    ```

   **Versal:**
    ```
    Versal> setenv bootcmd_xen 'mmc dev 0; ext4load mmc 0:3 0xC00000 /boot/xen_boot.scr; source 0xC00000'; pri bootcmd_xen; run bootcmd_xen
    ```

   **Versal-2ve-2vm:**
    ```
    versal2> setenv bootcmd_xen 'scsi dev 0; ext4load scsi 0:3 0x20200000 /boot/xen_boot.scr; source 0x20200000'; pri bootcmd_xen; run bootcmd_xen
    ```

3. Verify Xen Dom0 is up and running on QEMU or Hardware.

## References

* https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842530/Xen+Hypervisor
