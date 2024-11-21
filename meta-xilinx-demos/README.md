# meta-xilinx-demos

This layer contains demos recipes and packagegroup for PL firmware, openamp,
jupyter notebook, ros, qt5, multimedia, sdfec etc for AMD Adaptive SoC's and
FPGA's target images.

> **Note:** Some of the demos recipes and packagegroup are moved from meta-petalinux
> layer to meta-xilinx-demos layer and these packagegroup are renamed.

## How to enable demos for target image

1. Follow [Building Instructions](../README.building.md) upto step 4.

2. Add meta-xilinx-demos to bblayers.conf as shown below.
```
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-demos
```

3. Add required demos recipes or packagegroup to target image using IMAGE_INSTALL
   variable to the end of the conf/local.conf file as shown below. For example
   include gpio-demo application.
```
IMAGE_INSTALL:append = " gpio-demo"
```

4. Continue [Building Instructions](../README.building.md) from step 5.

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-perl, meta-python, meta-filesystems, meta-gnome,
            meta-multimedia, meta-networking, meta-webserver, meta-xfce,
            meta-initramfs.
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core, meta-xilinx-microblaze, meta-xilinx-bsp,
            meta-xilinx-standalone.
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)

	URI: https://github.com/Xilinx/meta-jupyter
	branch: scarthgap or AMD release version (e.g. rel-v2024.2)

	URI: https://github.com/OpenAMP/meta-openamp
	branch: scarthgap

	URI: https://github.com/meta-qt5/meta-qt5
	branch: scarthgap

	URI: https://github.com/Xilinx/meta-ros
	layers: meta-ros-common, meta-ros2, meta-ros2-jazzy
	branch: AMD release version (e.g. rel-v2024.2)

	URI: https://git.yoctoproject.org/meta-arm
	layers: meta-arm, meta-arm-toolchain
	branch: scarthgap
