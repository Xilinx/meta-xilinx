#
# This include file implements the merging of *.cfg files from the SRC_URI for
# the Kernel.
#

# returns all the elements from the src uri that are .cfg files
def find_config_fragments(d):
    sources=src_patches(d, True)
    sources_list=[]
    for s in sources:
        if s.endswith('.cfg'):
            sources_list.append(s)
    return sources_list

kernel_do_configure_prepend() {
	# Find all ".cfg" files and merge them together into a .config
	CFG_FILES="${@" ".join(find_config_fragments(d))}"
	if [ ! -z "$CFG_FILES" ]; then
		${S}/scripts/kconfig/merge_config.sh -m $CFG_FILES
	fi
}
