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
REQUIRED_DISTRO_FEATURES = "${DISTRO_FEATURES}"
PACKAGECONFIG ?= "${DISTRO_FEATURES}"

do_configure:prepend() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper ${DTS_FILE} -- baremetal_xparameters_xlnx.py ${ESW_MACHINE} ${S}
    install -m 0755 xparameters.h ${S}/${ESW_COMPONENT_SRC}/
}

do_compile() {
   # Combines the .a archives produced by all of the dependent items
   cd ${RECIPE_SYSROOT}/usr/lib/
   echo create libxil.a > libxil.mri
   for each in ${REQUIRED_DISTRO_FEATURES}; do
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
