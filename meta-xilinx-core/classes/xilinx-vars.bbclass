# Check variable usage to make sure that the machine is probably configured
# properly.
addhandler xilinx_variables_config_eventhandler
xilinx_variables_config_eventhandler[eventmask] = "bb.event.ConfigParsed"

# It's up to the base sytem to define the variables being used here, we're
# only going to check them.
python xilinx_variables_config_eventhandler () {
    # Verify DEFAULTTUNE
    tune_prior = d.getVar('DEFAULTTUNE_PRIOR')
    tune_final = d.getVar('DEFAULTTUNE')

    if tune_prior and tune_prior != tune_final:
        bb.fatal("The loaded DEFAULTTUNE is %s, but it appears you intended %s. " \
                 "This is usually as a result of specifying it after the 'require' in the machine .conf file. " \
                 "See meta-xilinx-core/conf/machine/README." % (tune_prior, tune_final))

    # Verify 'xilinx' is in LICENSE_FLAGS_ACCEPTED
    license_flags = d.getVar('LICENSE_FLAGS_ACCEPTED') or ""
    if all(license not in license_flags.split() for license in ['xilinx', 'xilinx_pmu-rom-native']) :
        bb.warn("The ZynqMP pmu-rom is not enabled, qemu may not be able to emulate a ZynqMP system without it. " \
                "To enable this you must add 'xilinx' to the LICENSE_FLAGS_ACCEPTED to indicate you accept the software license.")
}
