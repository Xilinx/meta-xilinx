RPU1_PROC ?= ""
RPU1_PROC:zynqmp = "r5-1"
RPU1_PROC:versal = "r5-1"
RPU1_PROC:versal-net = "r52-1"
RPU1_PROC:versal-2ve-2vm = "r52-1"

RPU1_OS ?= "baremetal"
RPU1_MC ?= "${MACHINE}-cortex${RPU1_PROC}-${RPU1_OS}"

def rpu1_mc_name(d):
    proc = d.getVar('RPU1_PROC')
    if not proc:
        return ''
    return '%s-cortex%s-%s' % (d.getVar('MACHINE'), proc, d.getVar('RPU1_OS'))

RPU1_CFG_INC = "${@'rpu1-cfg.inc' if rpu1_mc_name(d) and rpu1_mc_name(d) in (d.getVar('BBMULTICONFIG') or '').split() else ''}"
require ${RPU1_CFG_INC}

def check_rpu1_vars(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('RPU1_DEPENDS') and not d.getVar('RPU1_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        msg = ""
        if not os.path.exists(d.getVar('RPU1_FILE') + ".elf"):
            msg = msg + "The expected file %s.elf is not available.  " % d.getVar('RPU1_FILE')
            if not d.getVar('WITHIN_EXT_SDK'):
                raise bb.parse.SkipRecipe("%s\nEither specify RPU1_FILE, or you may need to enable the appropriate RPU1 baremetal multiconfig." % msg)
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${RPU1_FILE}.elf')
            d.setVarFlag('do_install', 'file-checksums', '${RPU1_FILE}.elf:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${RPU1_FILE}.elf:True')
