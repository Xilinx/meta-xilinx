inherit esw python3native features_check

LIBXIL_CONFIG ??= ""
include ${LIBXIL_CONFIG}

# The python code allows us to use an include above, instead of require
# as it enforces that the file will be available for inclusion.  It also
# gives the user feedback if something isn't configured properly.
python () {
    libxil_cfg = d.getVar("LIBXIL_CONFIG")
    if libxil_cfg:
        bbpath = d.getVar('BBPATH')
        libxil_path = bb.utils.which(bbpath, libxil_cfg)
        if libxil_path:
            return
        else:
            d.setVar('BB_DONT_CACHE', '1')
            bb.parse.SkipRecipe("LIBXIL_CONFIG (%s) was not found." % libxil_cfg)
    else:
        d.setVar('BB_DONT_CACHE', '1')
        raise bb.parse.SkipRecipe("No LIBXIL_CONFIG set.")
}

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/"
ESW_COMPONENT_NAME = "libxil.a"

DEPENDS += "xilstandalone "
MACHINE_FEATURES:remove = "aiengine dfeccf dfeequ dfemix dfeprach rfdc dfeofdm"
REQUIRED_MACHINE_FEATURES = "${MACHINE_FEATURES}"
PACKAGECONFIG ?= "${MACHINE_FEATURES}"

do_compile() {
   # Combines the .a archives produced by all of the dependent items
   cd ${RECIPE_SYSROOT}/usr/lib/
   echo create libxil.a > libxil.mri
   for each in ${REQUIRED_MACHINE_FEATURES}; do
     each=$(echo $each | sed 's/-/_/g')
     if [ -e lib$each.a ]; then
       echo addlib lib$each.a >> libxil.mri
     fi
   done
   echo “save” >> libxil.mri
   echo “end” >> libxil.mri
   ${AR} -M <libxil.mri
   cp libxil.a ${B}
}

do_install() {
    install -d ${D}${libdir}
    install -m 0755  ${B}/${ESW_COMPONENT_NAME} ${D}${libdir}

    # Install Spec files for consumers of BSP
    install -d ${D}${includedir}
    cp -rf  ${SPECFILE_PATH} ${D}${includedir}
}
