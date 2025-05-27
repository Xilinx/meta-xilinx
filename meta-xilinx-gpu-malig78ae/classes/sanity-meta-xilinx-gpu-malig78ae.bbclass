addhandler malig78ae_bbappend_distrocheck
malig78ae_bbappend_distrocheck[eventmask] = "bb.event.SanityCheck"
python malig78ae_bbappend_distrocheck() {
    skip_check = e.data.getVar('SKIP_META_XILINX_MALIG78AE_SANITY_CHECK') == "1"
    if 'libmali' not in e.data.getVar('DISTRO_FEATURES').split() and not skip_check:
        bb.warn("You have included the meta-xilinx-gpu-malig78ae layer, but \
'libmali' has not been enabled in your DISTRO_FEATURES. Some bbappend files \
may not take effect. See the meta-xilinx-gpu-malig78ae README for details on enabling \
libmali - malig78ae support.")
}
