# Class to add a deprecated warning from various configuration files.

# Immediately after the ConfigParsed event handler, warn the user of any
# deprecated files the user has used.
addhandler xilinx_deprecated_config_eventhandler
xilinx_deprecated_config_eventhandler[eventmask] = "bb.event.ConfigParsed"
python xilinx_deprecated_config_eventhandler () {
    # Check for BOARD & BOARD_VARIANT usage
    if d.getVar('BOARD') or d.getVar('BOARD_VARIANT'):
        bb.error("Deprecated BOARD (%s) or BOARD_VARIANT (%s) is being used, they are no longer supported and are ignored." % (d.getVar('BOARD'), d.getVar('BOARD_VARIANT')))

    msg_list = d.getVarFlags('XILINX_DEPRECATED') or []
    for msg_source in msg_list:
        if msg_source == "doc":
            continue
        msg = d.getVarFlag('XILINX_DEPRECATED', msg_source) or ""
        if msg:
            bb.warn('%s: %s' % (msg_source, msg))
}
