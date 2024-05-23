# Include the versal-fw setting, if it's enabled
PSMFW_INC = "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'versal-fw-cfg.inc', '', d)}"
require ${PSMFW_INC}

def check_psm_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('PSM_DEPENDS') and not d.getVar('PSM_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        fail = False
        if not os.path.exists(d.getVar('PSM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PSM_FILE')
            fail = True
        if not os.path.exists(d.getVar('PSM_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PSM_FILE')
            fail = True

        if fail:
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nEither specify PSM_FILE, or you may need to enable BBMULTICONFIG += 'versal-fw' to generate it." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PSM_FILE}.elf file://${PSM_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${PSM_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PSM_FILE}.elf:True ${PSM_FILE}.bin:True')
