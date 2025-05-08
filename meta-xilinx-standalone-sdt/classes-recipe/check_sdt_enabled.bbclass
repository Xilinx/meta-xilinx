python() {
    if d.getVar('XILINX_WITH_ESW') != 'sdt':
        raise bb.parse.SkipRecipe("This recipe requires sdt workflow, which is not enabled.  XILINX_WITH_ESW set to '%s'." % d.getVar('XILINX_WITH_ESW'))
}
