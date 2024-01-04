require qemu-tpm.inc
require qemu-xen.inc

# We do not want QEMU, on the target to be configured with OpenGL
PACKAGECONFIG:remove:class-target:petalinux = "virglrenderer epoxy gtk+"
