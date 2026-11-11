SUMMARY = "AMD Xilinx ESW configuration helper recipe (Lopper, SDT \
fragments and helper metadata)."
# Can't depend on esw since this is needed for setup!
DESCRIPTION = "AMD Xilinx ESW configuration helper recipe: deploys the \
Lopper tools, system-device-tree fragments and helper YAML metadata \
that the rest of the embeddedsw recipes consume during configure."
inherit xlnx-embeddedsw

INHIBIT_DEFAULT_DEPS = "1"

# Installing this recipe should install the lopper tools and such
DEPENDS = "lopper xilinx-lops"

COMPATIBLE_HOST:forcevariable = ".*"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/"

do_configure[noexec] = '1'
do_compile[noexec] = '1'

do_install() {
    # The configuration step requires only the yaml files, make them
    # available to the SDK
    cd ${S}${ESW_COMPONENT_SRC}
    for each in `find . -name *.yaml` ; do
        mkdir -p $(dirname ${D}/${datadir}/embeddedsw${ESW_COMPONENT_SRC}$each)
        install -m 0644 $each ${D}/${datadir}/embeddedsw${ESW_COMPONENT_SRC}$each
    done
}

FILES:${PN} += "${datadir}/embeddedsw"

BBCLASSEXTEND = "native nativesdk"
