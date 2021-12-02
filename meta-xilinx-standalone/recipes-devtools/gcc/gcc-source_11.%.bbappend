COMPATIBLE_HOST = "${HOST_SYS}"

# Add MicroBlaze Patches (only when using MicroBlaze)
FILESEXTRAPATHS:append:microblaze:xilinx-standalone := ":${THISDIR}/gcc-11"
SRC_URI:append:microblaze:xilinx-standalone = " \
        file://additional-microblaze-multilibs.patch \
"

CHECK_FOR_MICROBLAZE:microblaze = "1"

python() {
    if d.getVar('CHECK_FOR_MICROBLAZE') == '1':
        if 'xilinx-microblaze' not in d.getVar('BBFILE_COLLECTIONS').split():
            bb.fatal('You must include the meta-microblaze layer to build for this configuration.')
}
