SUMMARY = "Basic container image with development tools"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

CONTAINER_SHELL="bash"

CORE_DEV_IMAGE_EXTRA_INSTALL ?= ""

include container-base.bb
inherit core-image

CORE_DEV_IMAGE_EDITOR ?= "vim-tiny"
# base-utils is required for post-install scriptlets in most packages,
# coreutils or busybox can do the job
CORE_DEV_IMAGE_CORE_UTILS ?= "${VIRTUAL-RUNTIME_base-utils}"

IMAGE_INSTALL += " \
   ${CORE_DEV_IMAGE_EXTRA_INSTALL} \
   ${CORE_DEV_IMAGE_CORE_UTILS} \
   ${CORE_DEV_IMAGE_EDITOR} \
   "

OCI_IMAGE_ENTRYPOINT = ""

# development headers, tools and package management to update
# the container.
IMAGE_FEATURES += "dev-pkgs"
IMAGE_FEATURES += "tools-sdk"
IMAGE_FEATURES += "package-management"