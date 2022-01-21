# Include the versal-fw setting, if it's enabled
PSMFW_INC = "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'versal-fw-cfg.inc', '', d)}"
require ${PSMFW_INC}

def check_psm_vars(d):
    if not d.getVar('PSM_DEPENDS') and not d.getVar('PSM_MCDEPENDS') and not (d.getVar('BBMULTICONFIG') and 'versal-fw' in d.getVar('BBMULTICONFIG').split()):
        msg = ""
        fail = False
        if not os.path.exists(d.getVar('PSM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PSM_FILE')
            fail = True
        if not os.path.exists(d.getVar('PSM_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PSM_FILE')
            fail = True
        if fail:
            d.setVar('BB_DONT_CACHE', '1')
            raise bb.parse.SkipRecipe("%s\nEither specify PSM_FILE, or you may need to enable BBMULTICONFIG += 'versal-fw' to generate it." % msg)
