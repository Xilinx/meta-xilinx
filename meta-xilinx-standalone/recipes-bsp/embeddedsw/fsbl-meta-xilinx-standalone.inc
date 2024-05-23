# Include the fsbl-fw setting, if it's enabled
FSBL_INC = "${@bb.utils.contains('BBMULTICONFIG', 'fsbl-fw', 'fsbl-fw-cfg.inc', '', d)}"
require ${FSBL_INC}

def check_fsbl_variables(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('FSBL_DEPENDS') and not d.getVar('FSBL_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        if not os.path.exists(d.getVar('FSBL_FILE') + ".elf"):
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("The expect file %s.elf is not available.\nSet FSBL_FILE to the path with a precompiled FSBL binary or you may need to enable BBMULTICONFIG += 'fsbl-fw' to generate it." % d.getVar('FSBL_FILE'))
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${FSBL_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${FSBL_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${FSBL_FILE}.elf:True')
