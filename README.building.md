# Build Instructions

This section describes how to get your build host ready to work with meta-xilinx
layers.

The following instructions require OE-Core meta and BitBake. Poky provides these
components, however they can be acquired separately.

> **Pre-requisites:** Refer [Preparing Build Host](https://docs.yoctoproject.org/4.1.2/singleindex.html#preparing-the-build-host) documentation.

1. Create a project directory.
```
$ mkdir sources
$ cd sources
```
2. Clone the poky, openembedded and amd xilinx repository.
> **Note:**
> * *release_branch:* refers to upstream stable release branch.
> * *rel-version:* refers to amd xilinx release version.
```
$ mkdir sources
$ git clone -b <release-branch> https://git.yoctoproject.org/poky.git
$ git clone -b <release-branch> https://git.openembedded.org/meta-openembedded.git
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx.git
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx-tools.git
```
3. Initialize a build environment using the `oe-init-build-env` script. 
```
$ source poky/oe-init-build-env
```
4. Once initialized configure `bblayers.conf` by adding dependency layers as shown
   below using `bitbake-layers` command.
> **Note:** From step 3 by default `meta-yocto-bsp` will be included in bblayers.conf
> file and this can be removed using `$ bitbake-layers remove-layer meta-yocto-bsp`
> command.

```
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-oe
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-python
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-filesystems
$ bitbake-layers add-layer ./<path-to-layer>/meta-openembedded/meta-networking
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-microblaze
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-core
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-standalone
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-bsp
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-vendor
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx/meta-xilinx-contrib
$ bitbake-layers add-layer ./<path-to-layer>/meta-xilinx-tools
```
> **Note:** We recommend using meta-xilinx-tools, the version that is built as
> standalone may not work on many boards as it does not know the board configuration.

5. Set hardware `MACHINE` configuration variable in <proj-dir>/build/conf/local.conf
   file for a specific target which can boot and run the in the board or QEMU.
```
MACHINE = "<target_machine_name>"
```
Available target machines are:

| Device     | target machines     |
|------------|---------------------|
| MicroBlaze | microblaze-generic  |
|            | ac701-microblazeel  |
|            | kc705-microblazeel  |
|            | kcu105-microblazeel |
|            | vcu118-microblazeel |
| Zynq-7000  | zynq-generic        |
|            | zc702-zynq7         |
|            | zc706-zynq7         |
| ZynqMP     | zynqmp-generic      |
|            | zcu102-zynqmp       |
|            | zcu104-zynqmp       |
|            | zcu106-zynqmp       |
|            | zcu111-zynqmp       |
|            | zcu208-zynqmp       |
|            | zcu216-zynqmp       |
|            | zcu670-zynqmp       |
|            | zcu1275-zynqmp      |
|            | zcu1285-zynqmp      |
|            | ultra96-zynqmp      |
| Versal     | versal-generic      |
|            | versal-net-generic  |
|            | vck190-versal       |
|            | vmk180-versal       |
|            | vek280-versal       |
|            | vpk120-versal       |
|            | vpk180-versal       |
|            | vhk158-versal       |

6. Build an OS image for the target using `bitbake` command.
> **Note:** Refer ./<path-to-distro-layer>/conf/templates/default/conf-notes.txt
> for available target image-name. e.g. core-image-minimal

```
$ bitbake <image-name>
```

7. Once complete the images for the target machine will be available in the output
   directory `${TMPDIR}/deploy/images/${MACHINE}/`.
