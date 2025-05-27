# meta-xilinx-imgrcvry

This layer enables Image Recovery development metadata for poky, oe, meta-xilinx and
other layers.

# Building Instructions
This section describes how to build Image Recovery bin file with meta-xilinx-imgrcvry layer.

1. Set hardware MACHINE configuration variable in build/conf/local.conf file for a specific 
   target which can boot and run in the board.
```
MACHINE = "versal-vek280-sdt-seg"
```

2. Set DISTRO configuration variable as xilinx-image-recovery in build/conf/local.conf file
   to build image recovery .bin file.
```
DISTRO = "xilinx-image-recovery"
```

3. Build the tiny initramfs image using `bitbake` command.
```
bitbake image-recovery-tiny-initramfs
```

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: scarthgap

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-python, meta-filesystems, meta-networking.
	branch: scarthgap

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and AMD release)
	layers: meta-xilinx-core, meta-xilinx-standalone, meta-xilinx-standalone-sdt.
	branch: scarthgap or AMD release version (e.g. rel-v2025.1)

	URI:
        https://github.com/Xilinx/meta-amd-adaptive-socs (development and AMD release)
	layers: meta-amd-adaptive-socs-core, meta-amd-adaptive-socs-bsp
	branch: scarthgap or AMD release version (e.g. rel-v2025.1)
