def xlnx_run(d, cmd, *args):
	import bb.process
	import subprocess

	topdir = d.getVar('TOPDIR', True)
	toolchain_path = d.getVar('EXTERNAL_TOOLCHAIN', True)
	if not toolchain_path:
		return 'UNKNOWN', 'UNKNOWN'

	target_prefix = d.getVar('TARGET_PREFIX', True)
	path = os.path.join(toolchain_path, 'bin', target_prefix + cmd)
	args = [path] + list(args)

	return bb.process.run(args, cwd=topdir, stderr=subprocess.PIPE)

def xlnx_get_version(d):
	try:
		stdout, stderr = xlnx_run(d, 'gcc', '-v')
	except bb.process.CmdError as exc:
		bb.error('Failed to obtain CodeSourcery toolchain version: %s' % exc)
		bb.error('Make sure that MACHINE is set correctly in your local.conf and the toolchain supports %s.' % d.getVar("TARGET_ARCH", True))
		return 'UNKNOWN'
	else:
		lines = stderr.splitlines()
		for i in reversed(range(len(lines))):
			if len(lines[i]) != 0:
				return lines[i]
		return 'UNKNOWN'

def xlnx_get_gcc_version(d):
	version = xlnx_get_version(d)
	if version != 'UNKNOWN':
		return version.split()[2]
	else:
		return version

def xlnx_get_libc_version(d):
	import fnmatch

	syspath = d.expand('${EXTERNAL_TOOLCHAIN_SYSROOT}')
	if not syspath:
		return 'UNKNOWN'

	libpath = syspath + '/lib/'
	if not os.path.exists(libpath):
		libpath = syspath + '/sgxx-glibc/lib/'

	if os.path.exists(libpath):
		for root,dirs,files in os.walk(libpath):
			for filename in fnmatch.filter(files,'libc-*.so'):
				return filename[5:-3]
	return 'UNKNOWN'

def xlnx_get_kernel_version(d):
	syspath = d.expand('${EXTERNAL_TOOLCHAIN_SYSROOT}')
	if not syspath:
		return 'UNKNOWN'

	vf = syspath + '/usr/include/linux/version.h'
	if not os.path.exists(vf):
		vf = syspath + '/sgxx-glibc/usr/include/linux/version.h'

	try:
		f = open(vf, 'r')
	except (OSError, IOError):
		return 'UNKNOWN'

	l = f.readlines();
	f.close();
	for s in l:
		if s.find('LINUX_VERSION_CODE') > 0:
			ver = int(s.split()[2])
			maj = ver / 65536
			ver = ver % 65536
			min = ver / 256
			ver = ver % 256
			return str(maj)+'.'+str(min)+'.'+str(ver)
	return 'UNKNOWN'

def xlnx_get_gdb_version(d):
	try:
		stdout, stderr = xlnx_run(d, 'gdb', '-v')
	except bb.process.CmdError:
		return 'UNKNOWN'
	else:
		first_line = stdout.splitlines()[0]
		return first_line.split()[-1]

python xilinx_version_handler () {
    d = e.data
    ld = d.createCopy()
    ld.finalize()

    d.setVar('XILINX_VER_GCC', xlnx_get_gcc_version(ld))
    d.setVar('XILINX_VER_LIBC', xlnx_get_libc_version(ld))
    d.setVar('XILINX_VER_KERNEL', xlnx_get_kernel_version(ld))
    d.setVar('XILINX_VER_GDB', xlnx_get_gdb_version(ld))
}
addhandler xilinx_version_handler
xilinx_version_handler[eventmask] = "bb.event.ConfigParsed"

# Ensure that any variable which includes the --sysroot (CC, CXX, etc) also
# depends on the toolchain version
TOOLCHAIN_OPTIONS[vardeps] += "XILINX_VER_MAIN XILINX_VER_GCC"
