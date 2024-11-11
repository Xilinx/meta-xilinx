FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# openamp.dtsi is in the WORKDIR
DT_INCLUDE:append = " ${WORKDIR}"

do_configure[vardeps] += "ENABLE_OPENAMP_DTSI OPENAMP_EXTRA_DT_INCLUDE_FILES"

OPENAMP_EXTRA_DT_INCLUDE_FILES ?= ""
OPENAMP_EXTRA_DT_INCLUDE_FILES:zynqmp = "zynqmp-openamp.dtsi"
OPENAMP_EXTRA_DT_INCLUDE_FILES:versal = "versal-openamp.dtsi"
OPENAMP_EXTRA_DT_INCLUDE_FILES:versal-net = "versal-net-openamp.dtsi"

def set_openamp_extra_dt_include_files(d):
    distro_features = d.getVar('DISTRO_FEATURES', True)
    enable_openamp_dtsi = d.getVar('ENABLE_OPENAMP_DTSI')
    if 'openamp' in distro_features and enable_openamp_dtsi == '1':
        return ' ${OPENAMP_EXTRA_DT_INCLUDE_FILES}'
    else:
        return ''

EXTRA_DT_INCLUDE_FILES:append = "${@set_openamp_extra_dt_include_files(d)}"
