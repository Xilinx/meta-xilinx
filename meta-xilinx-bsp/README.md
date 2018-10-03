meta-xilinx
===========

This layer provides support for MicroBlaze, Zynq and ZynqMP.

Additional documentation:

* [Building](README.building.md)
* [Booting](README.booting.md)

Supported Boards/Machines
=========================

Boards/Machines supported by this branch:

virt-versal

Additional information on Xilinx architectures can be found at:
	http://www.xilinx.com/support/index.htm


Maintainers, Mailing list, Patches
==================================

Please send any patches, pull requests, comments or questions for this layer to
the [meta-xilinx mailing list](https://lists.yoctoproject.org/listinfo/meta-xilinx):

	meta-xilinx@lists.yoctoproject.org

Maintainers:

	Manjukumar Harthikote Matha <manjukumar.harthikote-matha@xilinx.com>

Dependencies
============

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta

Recipe Licenses
===============

Due to licensing restrictions some recipes in this layer rely on closed source
or restricted content provided by Xilinx. In order to use these recipes you must
accept or agree to the licensing terms (e.g. EULA, Export Compliance, NDA,
Redistribution, etc). This layer **does not enforce** any legal requirement, it
is the **responsibility of the user** the ensure that they are in compliance
with any licenses or legal requirements for content used.

In order to use recipes that rely on restricted content the `xilinx` license
flag must be white-listed in the build configuration (e.g. `local.conf`). This
can be done on a per package basis:

	LICENSE_FLAGS_WHITELIST += "xilinx_pmu-rom"

or generally:

	LICENSE_FLAGS_WHITELIST += "xilinx"

Generally speaking Xilinx content that is provided as a restricted download
cannot be obtained without a Xilinx account, in order to use this content you
must first download it with your Xilinx account and place the downloaded content
in the `downloads/` directory of your build or on a `PREMIRROR`. Attempting to
fetch the content using bitbake will fail, indicating the URL from which to
acquire the content.

