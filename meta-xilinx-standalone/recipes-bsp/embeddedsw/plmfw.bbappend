# Include the versal-fw setting, if it's enabled
PLMFW_INC = "${@bb.utils.contains('BBMULTICONFIG', 'versal-fw', 'versal-fw-cfg.inc', '', d)}"
require ${PLMFW_INC}

def check_plm_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('PLM_DEPENDS') and not d.getVar('PLM_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        fail = False
        if not os.path.exists(d.getVar('PLM_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('PLM_FILE')
            fail = True
        if not os.path.exists(d.getVar('PLM_FILE') + ".bin"):
            msg = msg + "The expected file %s.bin is not available.  " % d.getVar('PLM_FILE')
            fail = True
        if fail:
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nEither specify PLM_FILE, or you may need to enable BBMULTICONFIG += 'versal-fw' to generate it." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${PLM_FILE}.elf file://${PLM_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${PLM_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${PLM_FILE}.elf:True ${PLM_FILE}.bin:True')
