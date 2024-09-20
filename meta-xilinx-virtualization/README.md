# meta-xilinx-vendor

This layer enables AMD Xilinx Xen configurations and features for ZynqMP and
Versal devices and also provides related metadata.

## Xen Build Instructions

The Yocto Project setup for AMD Xilinx Xen configurations workflow is as follows.
Be sure to read everything below.

1. Follow [Building Instructions](../README.building.md) upto step 2.

2. Clone the meta-security repository.

```
$ git clone -b <release-branch> https://git.yoctoproject.org/meta-security
```

3. Continue [Building Instructions](../README.building.md) from step 4.

> **Note:**
> * For System Device Tree(SDT) workflow see [SDT Building Instructions](../meta-xilinx-standalone-sdt/README.md)

4. Add meta-xilinx-virtualization layer to bblayers.conf as shown below.

```
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-virtualization
```

5. The following variables needs to be added to the end of the conf/local.conf file.

```
# Xen variables
BOOTMODE = "xen"
ENABLE_XEN_UBOOT_SCR = "1"
ENABLE_XEN_DTSI = "1"
ENABLE_XEN_QEMU_DTSI = "1"

# Default Xen Serial Console is serial0, if you are using serial1 then set as show below.
XEN_SERIAL_CONSOLES = "serial1"

# Variables for Xen JTAG or SD INITRD boot modes but this is not required for SD WIC image.
IMAGE_FSTYPES += "cpio.gz"
RAMDISK_IMAGE = "rootfs.cpio.gz"

# Variables for Xen SD WIC image boot flow.
IMAGE_FSTYPES += "wic"
WKS_FILES = "xilinx-default-sd.wks"

DISTRO_FEATURES:append = " multiarch security tpm virtualization vmsep xen"

IMAGE_FEATURES += "ssh-server-openssh"

DISTRO_FEATURES:append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED = "sysvinit"

IMAGE_INSTALL:append = " \
    kernel-module-xen-blkback \
    kernel-module-xen-gntalloc \
    kernel-module-xen-gntdev \
    kernel-module-xen-netback \
    kernel-module-xen-wdt \
    xen \
    xen-tools \
    xen-tools-xenstat \
    ${@bb.utils.contains('DISTRO_FEATURES', 'vmsep', 'qemu-aarch64 qemu-keymaps', 'qemu', d)} \
    "
```

6. Continue [Building Instructions](../README.building.md) from step 5.

## Xen Boot Instructions

> **Note:**
> * This README provides instructions for Xen Dom0 only.

1. Follow [Booting Instructions](../README.booting.md) upto step 2.

2. Verify Xen Dom0 is up and running on QEMU or target as shown below.

```
Poky (Yocto Project Reference Distro) 4.1.4 zynqmp-generic hvc0

zynqmp-generic login: root
root@zynqmp-generic:~# xl list
Name                                        ID   Mem VCPUs      State   Time(s)
Domain-0                                     0  1500     1     r-----     123.5
root@zynqmp-generic:~# xl info
host                   : zynqmp-generic
release                : 6.1.0-xilinx-v2024.1
version                : #1 SMP Thu Dec 21 07:00:11 UTC 2023
machine                : aarch64
nr_cpus                : 4
max_cpu_id             : 3
nr_nodes               : 1
cores_per_socket       : 1
threads_per_core       : 1
cpu_mhz                : 99.990
hw_caps                : 00000000:00000000:00000000:00000000:00000000:00000000:00000000:00000000
virt_caps              : hvm hvm_directio hap iommu_hap_pt_share vpmu gnttab-v1
total_memory           : 4095
free_memory            : 2529
sharing_freed_memory   : 0
sharing_used_memory    : 0
outstanding_claims     : 0
free_cpus              : 0
xen_major              : 4
xen_minor              : 17
xen_extra              : .0
xen_version            : 4.17.0
xen_caps               : xen-3.0-aarch64 xen-3.0-armv7l
xen_scheduler          : credit2
xen_pagesize           : 4096
platform_params        : virt_start=0x200000
xen_changeset          : Tue Dec 12 10:08:40 2023 +0100 git:38eebc6e5c-dirty
xen_commandline        : console=dtuart dtuart=serial0 dom0_mem=1500M dom0_max_vcpus=1 bootscrub=0 vwfi=native
cc_compiler            : aarch64-poky-linux-gcc (GCC) 12.2.0
cc_compile_by          : santraju
cc_compile_domain      :
cc_compile_date        : 2023-12-12
build_id               : 5e2952e1dd06c52a2a09ada7476333c48d88a285
xend_config_format     : 4
root@zynqmp-generic:~#
```

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta, meta-poky
	branch: langdale

	URI: https://git.openembedded.org/meta-openembedded
	layers: meta-oe, meta-python, meta-filesystems, meta-networking.
	branch: langdale

	URI:
        https://git.yoctoproject.org/meta-xilinx (official version)
        https://github.com/Xilinx/meta-xilinx (development and amd xilinx release)
	layers: meta-xilinx-core, meta-xilinx-standalone
	branch: langdale or amd xilinx release version (e.g. rel-v2024.1)

	URI: https://git.yoctoproject.org/meta-virtualization
	branch: langdale

	URI: https://git.yoctoproject.org/meta-security
	layers: meta-tpm
	branch: langdale

## References

* https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842530/Xen+Hypervisor
