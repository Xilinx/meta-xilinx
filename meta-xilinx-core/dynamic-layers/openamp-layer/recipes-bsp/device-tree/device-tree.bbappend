FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://zynq-openamp.dtsi \
    file://zynqmp-openamp.dtsi \
    file://versal-openamp.dtsi \
    file://versal-net-openamp.dtsi \
"

# openamp.dtsi is in the WORKDIR
DT_INCLUDE:append = " ${WORKDIR}"

do_configure[vardeps] += "ENABLE_OPENAMP_DTSI OPENAMP_EXTRA_OVERLAYS"

OPENAMP_EXTRA_OVERLAYS:zynq = "zynq-openamp.dtsi"
OPENAMP_EXTRA_OVERLAYS:zynqmp = "zynqmp-openamp.dtsi"
OPENAMP_EXTRA_OVERLAYS:versal = "versal-openamp.dtsi"
OPENAMP_EXTRA_OVERLAYS:versal-net = "versal-net-openamp.dtsi"

def set_openamp_extra_overlays(d):
    distro_features = d.getVar('DISTRO_FEATURES', True)
    enable_openamp_dtsi = d.getVar('ENABLE_OPENAMP_DTSI')
    if 'openamp' in distro_features and enable_openamp_dtsi == '1':
        return d.getVar('OPENAMP_EXTRA_OVERLAYS', True)
    else:
        return ''

EXTRA_OVERLAYS:append = "${@set_openamp_extra_overlays(d)}"
