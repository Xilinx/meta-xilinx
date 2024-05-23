addhandler security_bbappend_distrocheck
security_bbappend_distrocheck[eventmask] = "bb.event.SanityCheck"
python security_bbappend_distrocheck() {
    skip_check = e.data.getVar('SKIP_META_XILINX_STANDALONE_SANITY_CHECK') == "1"
    if not e.data.getVar('XILINX_WITH_ESW') and not skip_check:
        bb.warn("You have included the meta-xilinx-standalone layer, but \
it has not been enabled using XILINX_WITH_ESW in your configuration. Some \
bbappend files and preferred version setting may not take effect. See the \
meta-security README for details on enabling security support.")
}
