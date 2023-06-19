
ZynqMP - PMU ROM
----------------

Since Xilinx tool release v2017.1 multiple components (arm-trusted-firmware,
linux, u-boot, etc.) require the PMU firmware to be loaded. For QEMU this also
means that the PMU ROM must be loaded so that the PMU firmware can be used.

The PMU ROM is available for download, and a specific recipe has been created
to make this available: pmu-rom-native.

The license on the software is Xilinx proprietary, so you may be required to
enable the approprate LICENSE_FLAGS_ACCEPTED to trigger the download.
The license itself is available within the download at the URL referred to in
meta-xilinx-core/recipes-bsp/pmu-firmware/pmu-rom-native_2022.2.bb.

Add the following to your local.conf to acknowledge you accept the proprietary
xilinx license.

   LICENSE_FLAGS_ACCEPTED:append = " xilinx"
