meta-xilinx
===========

This layer provides support for MicroBlaze, Zynq and ZynqMP.

Additional documentation:
 * [Building](README.building.md)
 * [Booting](README.booting.md)

Supported Boards/Machines
=========================

Boards/Machines supported by this layer:
 * MicroBlaze:
  * [qemumicroblaze](conf/machine/qemumicroblaze.conf) - `qemumicroblaze`
  * [qemumicroblaze-s3adsp1800](conf/machine/qemumicroblaze-s3adsp1800.conf) - `qemumicroblaze-s3adsp1800`
  * [Xilinx KC705](conf/machine/kc705-microblazeel.conf) - `kc705-microblazeel`
 * Zynq:
  * [qemuzynq](conf/machine/qemuzynq.conf) - `qemuzynq`
  * [Xilinx ZC702](conf/machine/zc702-zynq7.conf) - `zc702-zynq7`
  * [Xilinx ZC706](conf/machine/zc706-zynq7.conf) - `zc706-zynq7`
  * [Avnet MicroZed](conf/machine/microzed-zynq7.conf) - `microzed-zynq7`
  * [Avnet PicoZed](conf/machine/picozed-zynq7.conf) - `picozed-zynq7`
  * [Avnet/Digilent ZedBoard](conf/machine/zedboard-zynq7.conf) - `zedboard-zynq7`
  * [Digilent Zybo](conf/machine/zybo-zynq7.conf) - `zybo-zynq7`
  * [Digilent Zybo Linux BD](conf/machine/zybo-linux-bd-zynq7.conf) - `zybo-linux-bd-zynq7`
 * ZynqMP:
  * [Xilinx EP108](conf/machine/ep108-zynqmp.conf) - `ep108-zynqmp` (QEMU support)
  * [Xilinx ZCU102](conf/machine/zcu102-zynqmp.conf) - `zcu102-zynqmp`

Additional information on Xilinx architectures can be found at:
	http://www.xilinx.com/support/index.htm

Maintainers, Mailing list, Patches
==================================

Please send any patches, pull requests, comments or questions for this layer to
the [meta-xilinx mailing list](https://lists.yoctoproject.org/listinfo/meta-xilinx):

	meta-xilinx@lists.yoctoproject.org

Maintainers:

	Nathan Rossi <nathan@nathanrossi.com>
	Manjukumar Harthikote Matha <manjukumar.harthikote-matha@xilinx.com>

Dependencies
============

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta

