DESCRIPTION = "Packages for Benchmarks"

inherit packagegroup

BENCHMARKS_EXTRAS = " \
	hdparm \
	iotop \
	nicstat \
	lmbench \
	iptraf \
	net-snmp \
	lsof \
	babeltrace \
	sysstat \
	dool \
	dhrystone \
	linpack \
	whetstone \
	iperf3 \
	"
RDEPENDS:${PN} = "${BENCHMARKS_EXTRAS}"
