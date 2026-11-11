SUMMARY = "AMD Xilinx Power Management library (xilpm, next-gen)."
DESCRIPTION = "Next-generation rewrite of the AMD Xilinx embeddedsw \
power-management library used on newer Versal generations."
inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilpm_ng/src/"
ESW_COMPONENT_NAME = "libxilpm_ng.a"

EXTRA_OECMAKE += "-DXILPM_NG_EEMI_ENABLE=ON"

DEPENDS += " \
    libxil \
    xilstandalone \
    ${@'xilplmi cframe' if d.getVar('ESW_MACHINE') == 'pmc_0' else ''} \
    "

FILES:${PN} += "${libdir}/xpm_memory_pools.ld"

do_install:append() {
    if [ -f ${B}/xpm_memory_pools.ld ]; then
        install -Dm0644 ${B}/xpm_memory_pools.ld ${D}${libdir}/xpm_memory_pools.ld
    fi
}
