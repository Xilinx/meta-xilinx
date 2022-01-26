# Include the zynqmp-pmufw setting, if it's enabled
PMUFW_INC = "${@bb.utils.contains('BBMULTICONFIG', 'zynqmp-pmufw', 'zynqmp-pmufw-cfg.inc', '', d)}"
require ${PMUFW_INC}

def check_pmu_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('PMU_DEPENDS') and not d.getVar('PMU_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        fail = False
        if not os.path.exists(d.getVar('PMU_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PMU_FILE')
            fail = True
        if not os.path.exists(d.getVar('PMU_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PMU_FILE')
            fail = True
        if fail:
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nEither specify PMU_FILE, or you may need to enable BBMULTICONFIG += 'zynqmp-pmufw' to generate it." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PMU_FILE}.elf file://${PMU_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${PMU_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PMU_FILE}.elf:True ${PMU_FILE}.bin:True')
