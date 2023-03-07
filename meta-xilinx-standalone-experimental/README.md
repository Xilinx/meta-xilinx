# meta-xilinx-standalone-experimental

This layer contains experimental items that may eventually be added
to the meta-xilinx-standalone layer.  The components in this layer
may or may not be buildable as they may require unreleased code.

The non-Linux software components are still in development and
this should be considered to be a preview release only.  For instance,
some components may not be buildable, expect APIs to change on various
parts and pieces.

## Build Instructions

**Note:** to use this layer you must REMOVE meta-xilinx-tools from your
project.  meta-xilinx-tools is not compatible with this experimental
approach.  You may also have to remove other layers that depend
on meta-xilinx-tools, such as meta-kria and meta-system-controller.

To use the experimental version of the embedded software (firmware)
as well as system configuration, you must build the 'meta-xilinx-setup'
SDK.  This SDK is passed a device tree, constructed from System Device tree and
produces a number of configuration files.

1. Remove meta-xilinx-tools, meta-kria and meta-system-controller, then add the decoupling layer
```
$ bitbake-layers remove-layer meta-xilinx-tools
$ bitbake-layers remove-layer meta-kria
$ bitbake-layers remove-layer meta-system-controller
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-standalone-experimental
```
2. Build the setup SDK
```
$ bitbake meta-xilinx-setup
```
3. Install the setup SDK:
```
$ .${TMPDIR}/tmp/deploy/sdk/x86_64-xilinx-nativesdk-prestep-2023.2....sh -d prestep -y
```

Then follow the instructions in the 'prestep/README-setup' file.


## Dependencies

This layer depends on:

	URI: git://git.openembedded.org/bitbake

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: langdale

	URI: git://git.yoctoproject.org/meta-xilinx.git
	layers: meta-xilinx-core, meta-xilinx-bsp, meta-xilinx-standalone
	branch: langdale

