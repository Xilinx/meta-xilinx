# Class to add a deprecated warning from various configuration files.

# Immediately after the ConfigParsed event handler, warn the user of any
# deprecated files the user has used.
addhandler xilinx_deprecated_config_eventhandler
xilinx_deprecated_config_eventhandler[eventmask] = "bb.event.ConfigParsed"
python xilinx_deprecated_config_eventhandler () {
    # Check for BOARD & BOARD_VARIANT usage
    if d.getVar('BOARD') or d.getVar('BOARD_VARIANT'):
        bb.warn("Deprecated BOARD (%s) or BOARD_VARIANT (%s) is being used." % (d.getVar('BOARD'), d.getVar('BOARD_VARIANT')))

        if d.getVar('MACHINE') != d.getVar('ORIG_MACHINE'):
            if d.getVar('BOARD_VARIANT') or d.getVar('BOARD') == d.getVar('MACHINE'):
                if not check_conf_exists("conf/machine/${MACHINE}.conf", d):
                    mach_path = os.path.join(d.getVar('TOPDIR'), "conf/machine", d.getVar('MACHINE') + '.conf')
                    bb.utils.mkdirhier(os.path.dirname(mach_path))
                    bb.warn('Generating (board_variant) MACHINE file: %s' % mach_path)
                    with open(mach_path, "w") as f:
                        f.write('#@TYPE: Machine\n')
                        f.write('#@NAME: %s\n' % d.getVar('MACHINE'))
                        f.write('#@DESCRIPTION: Generated %s machine\n' % d.getVar('MACHINE'))
                        f.write('\n')
                        f.write('#### Preamble\n')
                        f.write('''MACHINEOVERRIDES =. "${@['', '%s:']['%s' != '${MACHINE}']}"\n''' % (d.getVar('MACHINE'), d.getVar('MACHINE')))
                        f.write('#### Regular settings follow\n')
                        f.write('\n')
                        f.write('unset BOARD\n')
                        f.write('unset BOARD_VARIANT\n')
                        f.write('\n')
                        f.write('DEFAULTTUNE ?= "%s"\n' % d.getVar('DEFAULTTUNE'))
                        if d.getVar('TUNE_FEATURES:tune-microblaze'):
                            f.write('TUNE_FEATURES:tune-microblaze ?= "%s"\n' % d.getVar('TUNE_FEATURES'))
                        if d.getVar('SOC_VARIANT'):
                            f.write('SOC_VARIANT ?= "%s"\n' % d.getVar('SOC_VARIANT'))
                        f.write('\n')
                        f.write('require conf/machine/%s.conf\n' % [d.getVar('ORIG_MACHINE'), d.getVar('BOARD')][bool(d.getVar('BOARD_VARIANT'))])
                        f.write('\n')
                        f.write('#### No additional settings should be after the Postamble\n')
                        f.write('#### Postamble\n')
                        f.write('''PACKAGE_EXTRA_ARCHS:append = "${@['', ' %s']['%s' != "${MACHINE}"]}"\n''' % ((d.getVar('MACHINE_ARCH'), d.getVar('MACHINE'))))
                    bb.warn('Note: The generated machine conf file may be incomplete.  If so copy the missing settings from the original conf files.')
                bb.warn('In the future use: MACHINE = "%s"' % d.getVar('MACHINE'))
            if d.getVar('BOARD') and d.getVar('BOARD') != d.getVar('MACHINE'):
                if not check_conf_exists("conf/machine/${BOARD}.conf", d):
                    mach_path = os.path.join(d.getVar('TOPDIR'), "conf/machine", d.getVar('BOARD') + '.conf')
                    bb.utils.mkdirhier(os.path.dirname(mach_path))
                    bb.warn('Generating (board) MACHINE file: %s' % mach_path)
                    with open(mach_path, "w") as f:
                        f.write('#@TYPE: Machine\n')
                        f.write('#@NAME: %s\n' % d.getVar('BOARD'))
                        f.write('#@DESCRIPTION: Generated %s machine\n' % d.getVar('BOARD'))
                        f.write('\n')
                        f.write('#### Preamble\n')
                        f.write('''MACHINEOVERRIDES =. "${@['', '%s:']['%s' != '${MACHINE}']}"\n''' % (d.getVar('BOARD'), d.getVar('BOARD')))
                        f.write('#### Regular settings follow\n')
                        f.write('\n')
                        f.write('unset BOARD\n')
                        f.write('unset BOARD_VARIANT\n')
                        f.write('require conf/machine/%s.conf\n' % d.getVar('ORIG_MACHINE'))
                        f.write('\n')
                        f.write('#### No additional settings should be after the Postamble\n')
                        f.write('#### Postamble\n')
                        f.write('''PACKAGE_EXTRA_ARCHS:append = "${@['', ' %s']['%s' != "${MACHINE}"]}"\n''' % ((d.getVar('BOARD_ARCH'), d.getVar('BOARD'))))
                    bb.warn('Note: The generated machine conf file may be incomplete.  If so copy the missing settings from the original conf files.')
            d.delVar('ORIG_MACHINE')

    msg_list = d.getVarFlags('XILINX_DEPRECATED') or []
    for msg_source in msg_list:
        if msg_source == "doc":
            continue
        msg = d.getVarFlag('XILINX_DEPRECATED', msg_source) or ""
        bb.warn('%s: %s' % (msg_source, msg))
}
