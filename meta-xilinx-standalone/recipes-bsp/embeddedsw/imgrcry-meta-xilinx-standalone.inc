# Default imgrcry configuration, using fsbl multiconfig
IMGRCRY_DEPENDS ?= ""
IMGRCRY_MCDEPENDS ?= "mc::${MACHINE}-cortexa53-fsbl:image-recovery:do_deploy"
IMGRCRY_DEPLOY_DIR ?= "${TOPDIR}/tmp-${MACHINE}-cortexa53-fsbl/deploy/images/${MACHINE}/"
IMGRCRY_IMAGE_NAME ?= "image-recovery-${MACHINE}"

def check_imgrcry_variables(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('IMGRCRY_DEPENDS') and not d.getVar('IMGRCRY_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        if not os.path.exists(d.getVar('IMGRCRY_FILE') + ".bin"):
            raise bb.parse.SkipRecipe("Could not open image recovery file: %s.bin" % d.getVar('IMGRCRY_FILE'))
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${IMGRCRY_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${IMGRCRY_FILE}.bin:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${IMGRCRY_FILE}.bin:True')
