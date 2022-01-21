# Include the zynqmp-pmufw setting, if it's enabled
PMUFW_INC = "${@bb.utils.contains('BBMULTICONFIG', 'zynqmp-pmufw', 'zynqmp-pmufw-cfg.inc', '', d)}"
require ${PMUFW_INC}

def check_pmu_vars(d):
    if not d.getVar('PMU_DEPENDS') and not d.getVar('PMU_MCDEPENDS') and not (d.getVar('BBMULTICONFIG') and 'zynqmp-pmufw' in d.getVar('BBMULTICONFIG').split()):
        msg = ""
        fail = False
        if not os.path.exists(d.getVar('PMU_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PMU_FILE')
            fail = True
        if not os.path.exists(d.getVar('PMU_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PMU_FILE')
            fail = True
        if fail:
            d.setVar('BB_DONT_CACHE', '1')
            raise bb.parse.SkipRecipe("%s\nEither specify PMU_FILE, or you may need to enable BBMULTICONFIG += 'zynqmp-pmufw' to generate it." % msg)
