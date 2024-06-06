inherit esw python3native

ESW_COMPONENT_SRC = "/ThirdParty/sw_services/lwip220/src/"
ESW_COMPONENT_NAME = "liblwip220.a"

DEPENDS += "libxil"
DEPENDS:append:xilinx-freertos = "freertos10-xilinx"

EXTRA_OECMAKE += "-Dlwip220_api_mode=RAW_API"
EXTRA_OECMAKE += "-Dlwip220_dhcp_does_arp_check=ON"
EXTRA_OECMAKE += "-Dlwip220_dhcp=ON"
EXTRA_OECMAKE += "-Dlwip220_pbuf_pool_size=2048"
EXTRA_OECMAKE += "-Dlwip220_ipv6_enable=OFF"
EXTRA_OECMAKE:append:xilinx-freertos = " -Dlwip220_api_mode=SOCKET_API"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 xtopology_g.c ${S}/${ESW_COMPONENT_SRC}/
    )
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${B}/${ESW_COMPONENT_NAME} ${D}${libdir}
    install -m 0644  ${B}/include/*.h ${D}${includedir}
    cp -r ${B}/include/arch/ ${D}${includedir}
    cp -r ${B}/include/include/lwip/ ${D}${includedir}
    cp -r ${B}/include/netif/ ${D}${includedir}
}
