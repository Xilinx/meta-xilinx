require qemu-tpm.inc
require qemu-xen_8.1.inc

# We do not want QEMU, on the target to be configured with OpenGL
PACKAGECONFIG:remove:class-target = "virglrenderer epoxy gtk+"
