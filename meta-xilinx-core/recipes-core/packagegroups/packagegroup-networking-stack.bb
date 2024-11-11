DESCRIPTION = "Packages to extend network stack"

inherit packagegroup

NETWORKING_STACK_PACKAGES = " \
	ethtool \
	phytool \
	netcat \
	net-tools \
	dnsmasq \
	iproute2 \
	iptables \
	rpcbind \
	iperf2 \
	iperf3 \
	"

RDEPENDS:${PN} = "${NETWORKING_STACK_PACKAGES}"
