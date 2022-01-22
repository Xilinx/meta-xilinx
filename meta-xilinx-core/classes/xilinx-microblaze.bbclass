# Class to add a deprecated warning from various configuration files.

# Immediately after the ConfigParsed event handler, warn the user of any
# deprecated files the user has used.
addhandler xilinx_microblaze_config_eventhandler
xilinx_microblaze_config_eventhandler[eventmask] = "bb.event.ConfigParsed"
python xilinx_microblaze_config_eventhandler () {
    if d.getVar('DEFAULTTUNE').startswith('microblaze'):
        if 'xilinx-microblaze' not in d.getVar('BBFILE_COLLECTIONS').split():
            bb.fatal('You must include the meta-microblaze layer to build for this configuration.')
}
