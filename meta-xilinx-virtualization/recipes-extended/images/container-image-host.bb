DESCRIPTION = "A configurable container host image"
LICENSE = "MIT"

# This image is a reference implementation to create a target platform
# capable of running containers. This includes kernel configuration,
# container runtimes, tools and other support applications.
#
# The wpackages to install are largely described in the packagegroups
# that are part of this layer. packagegroups are preferred as they can
# easily be used to create similar images of different composition.
# The recipes for the packages have their list of build and runtime
# dependencies, as such, those dependencies are not part of the image
# install or listed explicitly in the packgroups.
#
# CNCF areas that have choices are described by VIRTUAL-RUNTIME
# variables. These variables can be set individually (in a distro,
# layer or local configuration file), or can be set by the setting of
# a "CONTAINER_PROFILE". It is possible to select incompatible
# packages if setting the VIRTUAL-RUNTIME variables individually.
# container profiles have been created as valid / tested stacks of the
# components in meta-virtualization.
#
# The contents of the image are selected by testing the VIRTUAL-RUNTIME
# values and mapping them to packagegroups.
#
# The possible VIRTUAL-RUNTIME variables (and their values) are
# currently:
#
## engines: docker/docker-moby, virtual-containerd, cri-o, podman, lxc
##    VIRTUAL-RUNTIME_container_engine ??= "podman"
## runtime: runc, crun, runv, runx
##    VIRTUAL-RUNTIME_container_runtime ??= "virtual-runc"
## networking: cni, netavark
##    VIRTUAL-RUNTIME_container_networking ??= "cni"
## dns: cni, aardvark-dns
##    VIRTUAL-RUNTIME_container_dns ??= "cni"
## orchestration: k8s, k3s
##    VIRTUAL-RUNTIME_container_orchestration ??= "k3s"
## Kubernetes terminology "components"
##   VIRTUAL-RUNTIME_cri ??= "virtual-containerd"
##   VIRTUAL-RUNTIME_cni ??= "cni"
#
# To select a CONTAINER_PROFILE, set the variable in your local,
# distro or layer configuration:
#
#   CONTAINER_PROFILE="<your value>"
#
# The possible values for CONTAINER_PROFILE can be found in
# conf/distro/include in the format of: meta-virt-container-<profile>.inc
#
##    default (docker)
##    containerd
##    podman
##    docker
##    k3s-host
##    k3s-node

inherit features_check

# minimum features tested to have a working container host
# image. These will be enforced by the features_check inherit
REQUIRED_DISTRO_FEATURES ?= " virtualization \
                              systemd \
                              seccomp \
			    "

# features that are typically enabled. Note, these are not
# enforced, but maybe added to the required distro feature
# definition in the future.
RECOMMENDED_DISTRO_FEATURES ?= " pam \
				 usrmerge \
                               "
# features that are enabled for specific wworkloads. These
# are not enforced, except for specific configurations.
OPTIONAL_DISTRO_FEATURES ?= " vmsep \
			      k3s \
			      k8s \
                             "

REQUIRED_DISTRO_FEATURES:append = " ${@bb.utils.contains('VIRTUAL-RUNTIME_container_orchestration','k3s-node','k3s','',d)}"
REQUIRED_DISTRO_FEATURES:append = " ${@bb.utils.contains('VIRTUAL-RUNTIME_container_orchestration','k3s-host','k3s','',d)}"

# If the image is going to be placed into a cluster, we need some randomization
# of the host name to make it unique
IMAGE_FEATURES[validitems] += "virt-unique-hostname"
IMAGE_FEATURES[validitems] += "container-tools"

IMAGE_FEATURES += "ssh-server-openssh"
IMAGE_FEATURES += "package-management"
IMAGE_FEATURES += "virt-unique-hostname"
# This may be automatically enabled in the future via a toold or debug flag
# IMAGE_FEATURES += "container-tools"

IMAGE_LINGUAS = " "

# additional packages to install
CONTAINER_IMAGE_HOST_EXTRA_INSTALL ?= ""

# values can be: "all", "split" or ""
CONTAINER_IMAGE_KERNEL_MODULES ?= "all"

# These could be done via a mapping to allow a single selection line
# per type of virtul runtime, but right now the format of the
# virtual-runtime to packagegroup name is not mandated, so we keep
# them separate to allow the mapping in the individual items.
IMAGE_INSTALL = " \
    packagegroup-core-boot \
    packagegroup-oci \
    container-host-config \
    ${@bb.utils.contains('CONTAINER_IMAGE_KERNEL_MODULES','split','','kernel-modules',d)}  \
    ${@bb.utils.contains_any('VIRTUAL-RUNTIME_container_engine','docker docker-moby','packagegroup-docker','',d)}  \
    ${@bb.utils.contains_any('VIRTUAL-RUNTIME_container_engine','podman','packagegroup-podman','',d)}  \
    ${@bb.utils.contains_any('VIRTUAL-RUNTIME_container_engine','containerd','packagegroup-containerd','',d)}  \
    ${@bb.utils.contains('VIRTUAL-RUNTIME_container_networking','cni','packagegroup-cni','',d)}  \
    ${@bb.utils.contains('VIRTUAL-RUNTIME_container_networking','netavark','packagegroup-netavark','',d)}  \
    ${@bb.utils.contains('IMAGE_FEATURES','container-tools','packagegroup-container-tools','',d)}  \
    ${@bb.utils.contains('VIRTUAL-RUNTIME_container_orchestration','k3s-host','packagegroup-k3s-host','',d)}  \
    ${@bb.utils.contains('VIRTUAL-RUNTIME_container_orchestration','k3s-node','packagegroup-k3s-node','',d)}  \
    ${CONTAINER_IMAGE_HOST_EXTRA_INSTALL} \
    "

# inherit the basics of a booting image
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"

# we always need extra space to install container images
IMAGE_ROOTFS_EXTRA_SPACE = "41943040"
