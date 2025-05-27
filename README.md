# meta-xilinx

Collection of layers to enable AMD Xilinx products.

* **meta-microblaze**: layer containing the AMD Xilinx MicroBlaze architecture
specific implementation, such as microblaze gcc tool and other tools.

* **meta-xilinx-bsp**: layer containing the AMD Xilinx evaluation boards metadata
such as eval boards machine configurations files, kernel configuration fragments,
series configuration compiler(.scc) files etc.

* **meta-xilinx-contrib**: layer containing contribution from open source developers
for vendor specific boards which has AMD Xilinx devices or SoM's.

* **meta-xilinx-core**: layer containing the AMD Xilinx hardware devices metadata
such as tune files, generic, soc variant machine configurations, boot firmware
components, kernel etc.

* **meta-xilinx-standalone**: layer containing the AMD Xilinx Baremetal or
Standalone Toolchains metadata to build baremetal firmware and applications.

* **meta-xilinx-standalone-sdt**: layer containing metadata to build
all the boot images using lopper and system device tree without using the
meta-xilinx-tools layer.

* **meta-xilinx-vendor**: layer containing 3rd party vendor boards machine
configurations files, boot firmware, kernel configuration fragments, .scc files,
device tree etc.

* **meta-xilinx-multimedia**: layer contains AMD Xilinx specific multimedia packages
including recipes for the fork of GStreamer.

* **meta-xilinx-mali400**: layer contains AMD Xilinx MALI400 GPU drivers and
library packages including recipes for the fork of wayland.

* **meta-xilinx-gpu-malig78ae**: layer contains AMD Xilinx MALIG78 GPU drivers and
library packages.

* **meta-xilinx-demos**: layer contains PL firmware, gpio demo apps packages.

* **meta-xilinx-imgrcvry**: layer containing AMD Xilinx image recovery features
metadata and packages.

* **meta-vitis-tc**: layer contains toolchain build that are embedded into AMD
Vivado and Vitis tools.

* **meta-xilinx-virtualization**: layer containing metadata to build Xen target
images.

> **See:** AMD Xilinx devices:
	https://www.xilinx.com/products/silicon-devices.html

> **Note:** For AMD Ryzen, EPYC and Opteron A1100 architectures see:
    https://git.yoctoproject.org/meta-amd/tree/

Please see the respective READMEs and docs in the layer subdirectories

## Release Information

Refer [AMD Xilinx Yocto wiki](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/2613018625)
page for release features, known issue and limitations.

## Additional Documentation

For more information about [Yocto Project](https://www.yoctoproject.org) see Yocto Project docs which can be found at:

 * https://docs.yoctoproject.org/singleindex.html
