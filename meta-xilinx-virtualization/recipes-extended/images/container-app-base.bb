SUMMARY = "Basic Application container image"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

include container-base.bb

# CONTAINER_APP_CMD is the executable to run as the entrypoint of the
# container. What we have below is a placholder. if you run this
# container, you'll get the date echo'd
CONTAINER_APP_CMD ?= "date"

# The container app is the package(s) to install into the container.
# They must provide the command specified in CONTAINER_APP_CMD
CONTAINER_APP ?= ""

OCI_IMAGE_ENTRYPOINT = "${CONTAINER_APP_CMD}"
IMAGE_INSTALL:append = "${CONTAINER_APP}"
