SUMMARY = "AMD Xilinx Versal PLM infrastructure library (xilplmi)."
DESCRIPTION = "Infrastructure subsystem of the Versal Platform Loader \
and Manager (PLM) firmware (event manager, error handling, IPI), part \
of the AMD Xilinx embeddedsw stack."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilplmi/src/"
ESW_COMPONENT_NAME = "libxilplmi.a"

DEPENDS += " \
    xilstandalone \
    libxil \
    cfupmc \
    xiltimer \
    ${@'xilmailbox' if d.getVar('ESW_MACHINE') in ['psv_cortexa72_0', 'psv_cortexr5_0','psx_cortexa78_0', 'psx_cortexr52_0', 'pmc_0', 'psx_pmc_0', 'psx_psm_0'] else ''} "
