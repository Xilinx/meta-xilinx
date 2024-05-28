python() {
    if d.getVar('XILINX_WITH_ESW') != 'sdt':
        raise bb.parse.SkipRecipe("This package requires sdt, which is not enabled.  XILINX_WITH_ESW set to '%s'." % d.getVar('XILINX_WITH_ESW'))
}
