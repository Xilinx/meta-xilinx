require qemu-tpm.inc
require qemu-xen_8.2.inc

# We do not want QEMU, on the target to be configured with OpenGL
PACKAGECONFIG:remove:class-target:petalinux = "virglrenderer epoxy gtk+"

SKIP_RECIPE[qemu-xilinx] = "QEMU upstream has support for Xen paravirtualization. \
poky uses QEMU 8.2.7 so we carry patches to backport features from QEMU mainline. \
This is the tested and widely used upstream QEMU baseline for Xen flows. \
qemu-xilinx is a heavily modified QEMU for AMD board emulation. It differs from \
the tested and widely used upstream QEMU, so using it for Xen could cause problems \
not seen with upstream QEMU."
