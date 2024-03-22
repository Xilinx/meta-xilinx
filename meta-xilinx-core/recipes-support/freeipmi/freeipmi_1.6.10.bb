SUMMARY = "Tools and libraries for IPMI"
DESCRIPTION = " \
  FreeIPMI provides in-band and out-of-band IPMI software based on the IPMI \
  v1.5/2.0 specification. The IPMI specification defines a set of interfaces \
  for platform management and is implemented by a number vendors for system \
  management. The features of IPMI that most users will be interested in are \
  sensor monitoring, system event monitoring, power control, and \
  serial-over-LAN (SOL). The FreeIPMI tools and libraries listed below should \
  provide users with the ability to access and utilize these and many other \
  features. A number of useful features for large HPC or cluster environments \
  have also been implemented into FreeIPMI. \
"
HOMEPAGE = "https://www.gnu.org/software/freeipmi/"

LICENSE = "GPL-3.0-only & BSD-3-Clause"
LIC_FILES_CHKSUM = " \
	file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ZRESEARCH;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.bmc-watchdog;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmi-dcmi;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmi-fru;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmiconsole;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmidetect;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmimonitoring;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmipower;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.ipmiseld;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.pstdout;md5=d32239bcb673463ab874e80d47fae504 \
	file://COPYING.sunbmc;md5=c03f21cd76ff5caba6b890d1213cbfbb \
	"

BRANCH ?= "freeipmi-1-6-0-stable"
SRC_URI = " \
	git://git.savannah.gnu.org/git/freeipmi.git;protocol=https;branch=${BRANCH} \
	"
SRCREV ?= "816a69eb15a9034351381211d9cd15de81da10c7"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig systemd

PACKAGECONFIG ??= ""
PACKAGECONFIG[libgcrypt] = "--with-encryption,--without-encryption,libgcrypt,"

EXTRA_OECONF = " \
	--without-random-device \
	--with-systemdsystemunitdir=${systemd_system_unitdir} \
	"

SYSTEMD_SERVICE:${PN} = " \
	bmc-watchdog.service \
	ipmidetectd.service \
	ipmiseld.service \
	"
SYSTEMD_AUTO_ENABLE = "disable"
