SUMMARY = "Extended userspace networking stack utilities."
DESCRIPTION = "Packages to extend network stack"

inherit packagegroup

NETWORKING_STACK_PACKAGES = " \
	ethtool \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'networking-layer', 'phytool', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'networking-layer', 'netcat', '', d)} \
	net-tools \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'networking-layer', 'dnsmasq', '', d)} \
	iproute2 \
	iptables \
	rpcbind \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'iperf2', '', d)} \
	${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'iperf3', '', d)} \
	"

RDEPENDS:${PN} = "${NETWORKING_STACK_PACKAGES}"
