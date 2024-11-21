inherit esw esw_apps_common

COMPATIBLE_MACHINE = ".*"
COMPATIBLE_HOST = ".*"

python () {

    raise bb.parse.SkipRecipe(" \n \
image-recovery is not currently supported in the SDT build flow\n \
\n \
Please set the following variables to provide an image-recovery binary: \n \
IMGRCRY_MCDEPENDS = \"\" \n \
IMGRCRY_DEPLOY_DIR = \"/path/to/image-recovery/\" \n \
IMGRCRY_IMAGE_NAME = \"image-recovery-file.bin\" \n \
NOTE: The IMGRCRY_DEPLOY_DIR should contain both .bin and .elf versions of the file \n \
")

}
