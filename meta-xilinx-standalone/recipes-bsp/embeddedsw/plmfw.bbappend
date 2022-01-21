# Include the versal-fw setting, if it's enabled
PLMFW_INC = "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'versal-fw-cfg.inc', '', d)}"
require ${PLMFW_INC}

def check_plm_vars(d):
    if not d.getVar('PLM_DEPENDS') and not d.getVar('PLM_MCDEPENDS') and not (d.getVar('BBMULTICONFIG') and 'versal-fw' in d.getVar('BBMULTICONFIG').split()):
        msg = ""
        fail = False
        if not os.path.exists(d.getVar('PLM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PLM_FILE')
            fail = True
        if not os.path.exists(d.getVar('PLM_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PLM_FILE')
            fail = True
        if fail:
            d.setVar('BB_DONT_CACHE', '1')
            raise bb.parse.SkipRecipe("%s\nEither specify PLM_FILE, or you may need to enable BBMULTICONFIG += 'versal-fw' to generate it." % msg)
