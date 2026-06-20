SUMMARY = "Userspace benchmarking and IO-profiling utilities for AMD \
Xilinx boards."
DESCRIPTION = "Packages for Benchmarks"

inherit packagegroup

BENCHMARKS_EXTRAS = " \
	hdparm \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'iotop', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'nicstat', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'lmbench', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'iptraf', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'networking-layer', 'net-snmp', '', d)} \
	lsof \
	babeltrace2 \
	sysstat \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'dool', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'dhrystone', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'linpack', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'whetstone', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'iperf3', '', d)} \
	"
RDEPENDS:${PN} = "${BENCHMARKS_EXTRAS}"
