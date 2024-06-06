# See meta-virtualization recipes-graphics/xorg-xserver/xserver-xorg_xen.inc

# We want the configuration to remain optimized, if a user wants the removal
# behavior, then they can set one of the below using 'glamor' as in the
# xserver-xorg_xen.inc file.
XEN_REMOVED_OPENGL_PKGCONFIGS:zynqmp ?= ""
XEN_REMOVED_OPENGL_PKGCONFIGS:versal ?= ""

