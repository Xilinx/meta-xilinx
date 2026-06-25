SUMMARY = "AMD Xilinx RSA signature verification library (xilrsa)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
performs RSA signature verification for the Zynq UltraScale+ MPSoC \
secure-boot flow."
inherit esw python3native

ESW_COMPONENT_SRC = "/lib/sw_services/xilrsa/src/"
ESW_COMPONENT_NAME = "libxilrsa.a"

DEPENDS += "libxil"

do_configure:prepend() {
    # This script should also not rely on relative paths and such
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}
