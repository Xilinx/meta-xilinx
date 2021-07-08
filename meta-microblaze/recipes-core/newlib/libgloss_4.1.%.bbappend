require microblaze-newlib.inc

do_configure_prepend_microblaze() {
    # hack for microblaze, which needs xilinx.ld to literally do any linking (its hard coded in its LINK_SPEC)
    export CC="${CC} -L${S}/libgloss/microblaze"
}

# Libgloss provides various .o files in libdir
# These must NOT be stripped, but for some reason they are installed +x
# which triggers them to be stripped.
do_install_append_microblaze() {
    chmod 0644 ${D}${libdir}/*.o
}
