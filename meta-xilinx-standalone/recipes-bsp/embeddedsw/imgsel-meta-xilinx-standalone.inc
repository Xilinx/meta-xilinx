# Default imgsel configuration, using fsbl multiconfig
IMGSEL_DEPENDS ?= ""
IMGSEL_MCDEPENDS ?= "mc::${MACHINE}-cortexa53-fsbl:image-selector:do_deploy"
IMGSEL_DEPLOY_DIR ?= "${TOPDIR}/tmp-${MACHINE}-cortexa53-fsbl/deploy/images/${MACHINE}/"
IMGSEL_IMAGE_NAME ?= "image-selector-${MACHINE}"

def check_imgsel_variables(d):
    # If both are blank, the user MUST pass in the path to the firmware!
    if not d.getVar('IMGSEL_DEPENDS') and not d.getVar('IMGSEL_MCDEPENDS'):
        # Don't cache this, as the items on disk can change!
        d.setVar('BB_DONT_CACHE', '1')

        if not os.path.exists(d.getVar('IMGSEL_FILE') + ".bin"):
            raise bb.parse.SkipRecipe("Could not open image selector file: %s.bin" % d.getVar('IMGSEL_FILE'))
        else:
            # We found the file, so be sure to track it
            d.setVar('SRC_URI', 'file://${IMGSEL_FILE}.bin')
            d.setVarFlag('do_install', 'file-checksums', '${IMGSEL_FILE}.bin:True')
            d.setVarFlag('do_deploy', 'file-checksums', '${IMGSEL_FILE}.bin:True')
