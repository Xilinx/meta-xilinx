
# Scan all files in MACHINE_DEVICETREE and MACHINE_KCONFIG and populate SRC_URI,
# FILESEXTRAPATHS and KERNEL_DEVICETREE.
python __anonymous () {
    workdir = d.getVar("WORKDIR", True)
    machine_devicetree = d.getVar("MACHINE_DEVICETREE", True) or ''
    machine_kconfigs = d.getVar("MACHINE_KCONFIG", True) or ''
    extrapaths = set()
    sources = set()
    if machine_devicetree:
        files=set()
        for path in machine_devicetree.split():
            sources.add("file://" + os.path.basename(path))
            extrapaths.add(os.path.dirname(path))
            if os.path.splitext(os.path.basename(path))[1] == '.dts':
                files.add(os.path.join(workdir, os.path.basename(path)))
        d.setVar("KERNEL_DEVICETREE", ' '.join(files))
    if machine_kconfigs:
        for path in machine_kconfigs.split():
            files.add(os.path.join(workdir, os.path.basename(path)))
            sources.add("file://" + os.path.basename(path))
            extrapaths.add(os.path.dirname(path))

    if len(extrapaths) != 0:
        d.setVar("FILESEXTRAPATHS_append", ":".join(extrapaths) + ":")
    if len(sources) != 0:
        d.setVar("SRC_URI_append", " %s " % " ".join(sources))
}
