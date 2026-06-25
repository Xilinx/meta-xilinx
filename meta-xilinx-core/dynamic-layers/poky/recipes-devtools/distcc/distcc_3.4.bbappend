# The fixup_distcc_mirror_tarball postfunc in the upstream distcc_3.4.bb
# attempts to extract and patch the mirror tarball to restore a dropped
# 3.2 branch. When the tarball is corrupt or incompletely downloaded,
# this causes a fatal tar error:
#
#  gzip: stdin: unexpected end of file
#  tar: Unexpected EOF in archive
#  tar: Error is not recoverable: exiting now
#
# Override the function with a no-op in the bbappend to skip this
# fixup entirely, as it is not required in this context.
fixup_distcc_mirror_tarball () {
    :
}
