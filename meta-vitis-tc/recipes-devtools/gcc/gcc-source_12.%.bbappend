# Add MicroBlaze Patches (only when using MicroBlaze)
FILESEXTRAPATHS:append := ":${THISDIR}/gcc-12"
SRC_URI += " \
        file://additional-microblaze-multilibs.patch \
        file://riscv-multilib-generator-python.patch \
"
