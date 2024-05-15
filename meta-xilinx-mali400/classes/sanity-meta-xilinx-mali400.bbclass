addhandler mali400_bbappend_distrocheck
mali400_bbappend_distrocheck[eventmask] = "bb.event.SanityCheck"
python mali400_bbappend_distrocheck() {
    skip_check = e.data.getVar('SKIP_META_XILINX_MALI400_SANITY_CHECK') == "1"
    if 'libmali' not in e.data.getVar('DISTRO_FEATURES').split() and not skip_check:
        bb.warn("You have included the meta-xilinx-mali400 layer, but \
'libmali' has not been enabled in your DISTRO_FEATURES. Some bbappend files \
may not take effect. See the meta-xilinx-mali400 README for details on enabling \
libmali - mali400 support.")
}
