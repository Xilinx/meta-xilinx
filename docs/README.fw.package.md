# How to package and deploy firmware elf or bin to linux root filesystem

* [Introduction](#introduction)
* [How to create and install firmware package recipe](#how-to-create-and-install-firmware-package-recipe)

## Introduction
This readme describes how to package and deploy firmware baremetal or freertos
application elf or bin files to linux root filesystem under /lib/firmware directory.
Packaging an firmware baremetal or freertos application elf or bin files to linux
root filesystem is not automated from multiconfig baremetal or freertos recipes.
Hence user has to create a linux recipe and inherit fw-package which provides
infrastructure to package and deploy firmware elf or bin files to linux root
filesystem.

## How to create and install firmware package recipe

1. Follow [SDT Mulitconfig Building Instructions](../meta-xilinx-standalone-sdt/README.sdt.mc.build.md)
   upto step 3.

2. Create recipes-firmware directory in distribution meta layer.
```
$ mkdir -p <meta-layer>/recipes-firmware/<firmware-package-name>/
```

3. Now create the recipes firmware package using recipetool.
```
$ recipetool create -o <meta-layer>/recipes-firmware/<firmware-package-name>/firmware-package-name.bb 
```

4. Modify the recipe and inherit fw-package bbclass as shown below.

> **Note:** 
> * **DESTDIR:** Variable to specify elf or bin directory path. Default is set to
>                /lib/firmware directory from fw-package,bbclass and user can
>                change this value to /boot directory depending on requirement.
> * **FW_NAME:** Variable to define firmware baremetal or freertos application
>                recipe name.
> * **TARGET_MC:** Variable to define one of the multiconfig target name
>                  (ex: <soc-family>-<board-name>-sdt-<design-name>-cortexr5-0-baremetal)
>                  from the BBMULTICONFIG list generated at [SDT Mulitconfig Building Instructions](../meta-xilinx-standalone-sdt/README.sdt.mc.build.md)
>                  step 2.

```
SUMMARY = "Recipe to package and deploy baremetal or freertos elf or bin to linux rootfs"
LICENSE = "CLOSED"

inherit fw-package

FW_NAME = "hello-world"

TARGET_MC = "<soc-family>-<board-name>-sdt-<design-name>-cortexr5-0-baremetal"

FW_MCDEPENDS := "mc::${TARGET_MC}:${FW_NAME}:do_deploy"
FW_DEPLOY_DIR := "${TOPDIR}/tmp-${TARGET_MC}/deploy/images/${MACHINE}"
```
5. Add firmware-package recipe to image to local.conf as shown below.

```
IMAGE_INSTALL:append = " \
    firmware-package-name \
    "
```
6. Follow [SDT Building Instructions](../meta-xilinx-standalone-sdt/README.sdt.bsp.md) and continue from
   step 5.
