# Utility functions for various Xilinx specific recipes

# Returns a ':' seperated list of expanded '${BBPATH}/$path'
def get_additional_bbpath_filespath(path, d):
    board_extrapaths = []
    bbpath = d.getVar("BBPATH", True) or ""
    for i in bbpath.split(":"):
        board_extrapaths.append(os.path.join(i, path))
    if len(board_extrapaths):
        return ":".join(board_extrapaths) + ":"
    return ""

# Add a prefix or suffix to all paths in the list of paths
#   e.g. add 'file://' to all paths
def paths_affix(paths, suffix = "", prefix = ""):
    if paths:
        files=set()
        for path in paths.split():
            newpath = path
            if suffix and len(suffix) != 0:
                newpath = newpath + suffix
            if prefix and len(prefix) != 0:
                newpath = prefix + newpath
            files.add(newpath)
        if len(files) != 0:
            return ' '.join(files)
    return ''

# Expand all relative paths to absolute based on the WORKDIR location
def expand_workdir_paths(variable, d):
    workdir = d.getVar("WORKDIR", True)
    variable_value = d.getVar(variable, True) or ''
    if variable_value:
        files=set()
        for path in variable_value.split():
            if workdir:
                files.add(os.path.join(workdir, path))
            else:
                files.add(path)
        if len(files) != 0:
            return ' '.join(files)
    return ''

# Returns a space seperated list of all files which match the extension, joined
# with the dir path.
def expand_dir_basepaths_by_extension(variable, dir, extension, d):
    variable_value = d.getVar(variable, True) or ''
    if variable_value:
        files=set()
        for path in variable_value.split():
            if os.path.splitext(path)[1] == extension or extension == None:
                if dir:
                    files.add(os.path.join(dir, os.path.basename(path)))
                else:
                    files.add(os.path.basename(path))
        if len(files) != 0:
            return ' '.join(files)
    return ''
