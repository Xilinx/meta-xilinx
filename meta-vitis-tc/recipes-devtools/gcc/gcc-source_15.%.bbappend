FILESEXTRAPATHS:append := ":${THISDIR}/gcc-15"
SRC_URI += " \
        file://additional-microblaze-multilibs.patch \
        file://riscv-multilib-generator-python.patch \
"
