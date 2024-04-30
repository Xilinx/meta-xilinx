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
$ git clone -b <release-branch> https://git.yoctoproject.org/git/meta-virtualization
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx.git --recurse-submodules
$ git clone -b <rel-version> https://github.com/Xilinx/meta-xilinx-tools.git
```
> **Note:**
> * When meta-xilinx layer is cloned using git tool by default it will clone
> [gen-machine-conf](https://github.com/Xilinx/gen-machine-conf.git) repo as
> submodules, If you don't need to clone gen-machine-conf repo then remove
> `--recurse-submodules` option.
> * Skip this step if you are using yocto-manifests https://github.com/Xilinx/yocto-manifests

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
$ bitbake-layers add-layer ./<path-to-layer>/meta-virtualization
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
* For list of available target machines see meta layer README files.

 * [meta-xilinx-bsp README](https://github.com/Xilinx/meta-xilinx/tree/master/meta-xilinx-bsp#amd-xilinx-evaluation-boards-bsp-machines-files)
 * [meta-kria README](https://github.com/Xilinx/meta-xilinx/tree/master/meta-xilinx-bsp#amd-xilinx-evaluation-boards-bsp-machines-files)

6. For NFS build host system modify the build/conf/local.conf and add TMPDIR
   path as shown below. On local storage $TMPDIR will be set to build/tmp
```
TMPDIR = "/tmp/$USER/yocto/release_version/build"
```

7. Modify the build/conf/local.conf file to add wic image to default target
   image as shown below.
```
IMAGE_FSTYPES += "wic"
WKS_FILES = "xilinx-default-sd.wks"
```

8. Build the qemu-helper-native package to setup QEMU network tap devices.
```
$ bitbake qemu-helper-native
```

9. Manually configure a tap interface for your build system. As root run
   <path-to>/sources/poky/scripts/runqemu-gen-tapdevs, which should generate a
   list of tap devices. Once tap interfaces are successfully create you should
   be able to see all the interfaces by running ifconfig command.

```
$ sudo ./<path-to-layer>/poky/scripts/runqemu-gen-tapdevs $(id -u $USER) $(id -g $USER) 4 tmp/sysroots-components/x86_64/qemu-helper-native/usr/bin
```

10. Build an OS image for the target using `bitbake` command.
> **Note:** Refer ./<path-to-distro-layer>/conf/templates/default/conf-notes.txt
> for available target image-name. e.g. core-image-minimal or petalinux-image-minimal

```
$ bitbake <target-image>
```

7. Once complete the images for the target machine will be available in the output
   directory `${TMPDIR}/deploy/images/${MACHINE}/`.

8. Follow [Booting Instructions](https://github.com/Xilinx/meta-xilinx/blob/master/README.booting.md)
