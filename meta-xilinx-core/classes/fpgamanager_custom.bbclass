# This class inherits dfx_user_dts.bbclass for below use cases.
# Zynq-7000 and ZynqMP: Full bitstream loading.
# ZynqMP: DFx Static and Partial bitstream loading.
# Versal: DFx Static and Parial pdi loading.
# Versal: Full PDI loading.

inherit dfx_user_dts

python fpgamanager_warn_msg () {
    if not d.getVar("FPGAMANAGER_NO_WARN"):
        arch = d.getVar('SOC_FAMILY')
        pn = d.getVar('PN')        
        warn_msg = 'Users should start using dfx_user_dts bbclass for '
        if arch == 'zynq':
            warn_msg += 'Zynq-7000 Full bitstream loading use case.'
        elif arch == 'zynqmp':
            warn_msg += 'ZynqMP Full or DFx Static or DFx Partial bitstream loading use case.'
        elif arch == 'versal':
            warn_msg += 'Versal DFx Static or DFx Partial or Full PDI loading use case.'

        bb.warn("Recipe %s has inherited fpgamanager_custom bbclass which will be deprecated in 2024.1 release. \n%s" % (pn, warn_msg))
}

do_install[postfuncs] += "fpgamanager_warn_msg"