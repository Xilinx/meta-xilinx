XILINX_VER_MAIN ??= ""

python () {
	if not d.getVar("XILINX_VER_MAIN", True):
		raise bb.parse.SkipPackage("External toolchain not configured (XILINX_VER_MAIN not set).")
}

