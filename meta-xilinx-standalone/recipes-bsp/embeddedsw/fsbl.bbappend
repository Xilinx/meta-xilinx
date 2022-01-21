# Include the fsbl-fw setting, if it's enabled
FSBL_INC = "${@bb.utils.contains('BBMULTICONFIG', 'fsbl-fw', 'fsbl-fw-cfg.inc', '', d)}"
require ${FSBL_INC}

def check_fsbl_variables(d):
    if not d.getVar('FSBL_DEPENDS') and not d.getVar('FSBL_MCDEPENDS') and not (d.getVar('BBMULTICONFIG') and 'fsbl-fw' in d.getVar('BBMULTICONFIG').split()):
        if not os.path.exists(d.getVar('FSBL_FILE') + ".elf"):
            d.setVar('BB_DONT_CACHE', '1')
            raise bb.parse.SkipRecipe("The expect file %s.elf is not available.\nEither specify FSBL_FILE, or you may need to enable BBMULTICONFIG += 'fsbl-fw' to generate it." % d.getVar('FSBL_FILE'))
