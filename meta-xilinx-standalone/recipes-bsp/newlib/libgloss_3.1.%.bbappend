do_configure_prepend_microblaze() {
    # hack for microblaze, which needs xilinx.ld to literally do any linking (its hard coded in its LINK_SPEC)
    export CC="${CC} -L${S}/libgloss/microblaze"
}


# We use libgloss as if it was libxil, to avoid linking issues
do_install_append_microblaze-pmu(){
  cp ${D}/${libdir}/libgloss.a ${D}/${libdir}/libxil.a
}

# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append = " \
        file://0001-Patch-microblaze-Add-config-microblaze.mt-for-target.patch \
        file://0002-Patch-microblaze-Modified-_exceptional_handler.patch \
        file://0003-LOCAL-Add-missing-declarations-for-xil_printf-to-std.patch \
        file://0004-Local-deleting-the-xil_printf.c-file-as-now-it-part-.patch \
        file://0005-Local-deleting-the-xil_printf.o-from-MAKEFILE.patch \
        file://0006-MB-X-intial-commit.patch \
        file://0007-Patch-Microblaze-newlib-port-for-microblaze-m64-flag.patch \
        file://0008-fixing-the-bug-in-crt-files-added-addlik-instead-of-.patch \
        file://0009-Added-MB-64-support-to-strcmp-strcpy-strlen-files.patch \
        file://0010-Patch-MicroBlaze-typos-in-string-functions-microblaz.patch \
        file://0011-Removing-the-Assembly-implementation-of-64bit-string.patch \
        "
