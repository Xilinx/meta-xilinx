SUMMARY = "Xen hypervisor tools"
DESCRIPTION = "Tools and utility software for the Xen hypervisor"

COMPATIBLE_HOST = 'i686-.*-linux|(x86_64.*).*-linux|aarch64.*-linux|arm-.*-linux-gnueabi'

inherit setuptools3 update-rc.d systemd deploy
require xen-blktap.inc

RDEPENDS:${PN} = "\
    bash perl xz \
    ${PN}-console \
    ${PN}-libxenguest \
    ${PN}-libxenlight \
    ${PN}-libxenvchan \
    ${PN}-libxenctrl \
    ${PN}-libxlutil \
    ${PN}-libxenstat \
    ${PN}-libxenstore \
    ${PN}-libfsimage \
    ${PN}-fsimage \
    ${PN}-scripts-block \
    ${PN}-scripts-network \
    ${PN}-xen-watchdog \
    ${PN}-xencommons \
    ${PN}-xendomains \
    ${PN}-xenstore \
    virtual-xenstored \
    ${PN}-xl \
    "

RDEPENDS:${PN}-dev = ""

RRECOMMENDS:${PN} = " \
    qemu \
    ${@bb.utils.contains('XEN_TARGET_ARCH', 'x86_64', 'seabios ipxe vgabios', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'externalblktap', '', '${BLKTAP_RRECOMMENDS}', d)} \
    ${PN}-flask \
    ${PN}-hvmloader \
    ${PN}-libxenhypfs \
    ${PN}-shim \
    ${PN}-ucode \
    ${PN}-vchan \
    ${PN}-xenpaging \
    ${PN}-xenhypfs \
    "

RDEPENDS:${PN}-devd = " \
    ${PN}-xl \
    "

RDEPENDS:${PN}-fsimage = " \
    libext2fs \
    "

RDEPENDS:${PN}-misc = " \
    perl \
    python3 \
    ${PN}-xencov \
    "

RSUGGESTS:${PN}-misc = " \
    ${PN}-xencons \
    ${PN}-xenpvnetboot \
    "

RDEPENDS:${PN}-python = " \
    python3 \
    "

RDEPENDS:${PN}-pygrub = " \
    python3 \
    ${PN}-python \
    "

RDEPENDS:${PN}-remus = "bash"

RDEPENDS:${PN}-scripts-block = "\
    bash \
    ${PN}-scripts-common \
    ${PN}-volatiles \
    "

RDEPENDS:${PN}-scripts-common = "bash"

RDEPENDS:${PN}-scripts-network = "\
    bash \
    bridge-utils \
    ${PN}-scripts-common \
    ${PN}-volatiles \
    "

RRECOMMENDS:${PN}-scripts-network = "\
    ifupdown \
    "

RSUGGESTS:${PN}-xencov = "${PN}-xencov-split"

RDEPENDS:${PN}-xencommons = "\
    bash \
    util-linux-prlimit \
    ${PN}-console \
    ${PN}-xenstore \
    virtual-xenstored \
    ${PN}-xl \
    ${PN}-scripts-common \
    "

RDEPENDS:${PN}-xendomains = "\
    bash \
    ${PN}-console \
    ${PN}-scripts-block \
    ${PN}-scripts-common \
    virtual-xenstored \
    "

RDEPENDS:${PN}-xenhypfs = " \
    ${PN}-libxenhypfs \
    "

RDEPENDS:${PN}-xl = "libgcc"

RDEPENDS:${PN}-xenmon = " \
    python3 \
    "

RSUGGESTS:${PN}-xentrace = "${PN}-xentrace-format"

RDEPENDS:${PN}-xen-watchdog = "bash"

PACKAGES = " \
    ${PN} \
    ${@bb.utils.contains('PACKAGECONFIG', 'externalblktap', '', '${BLKTAP_PACKAGES}', d)} \
    ${PN}-console \
    ${PN}-cpuid \
    ${PN}-dbg \
    ${PN}-dev \
    ${PN}-devd \
    ${PN}-doc \
    ${PN}-flask \
    ${PN}-flask-tools \
    ${PN}-fsimage \
    ${PN}-gdbsx \
    ${PN}-hvmloader \
    ${PN}-init-xenstore-dom \
    ${PN}-kdd \
    ${PN}-libfsimage \
    ${PN}-libfsimage-dev \
    ${PN}-libxencall \
    ${PN}-libxencall-dev \
    ${PN}-libxenctrl \
    ${PN}-libxenctrl-dev \
    ${PN}-libxendevicemodel \
    ${PN}-libxendevicemodel-dev \
    ${PN}-libxenevtchn \
    ${PN}-libxenevtchn-dev \
    ${PN}-libxenforeignmemory \
    ${PN}-libxenforeignmemory-dev \
    ${PN}-libxengnttab \
    ${PN}-libxengnttab-dev \
    ${PN}-libxenguest \
    ${PN}-libxenguest-dev \
    ${PN}-libxenhypfs \
    ${PN}-libxenhypfs-dev \
    ${PN}-libxenlight \
    ${PN}-libxenlight-dev \
    ${PN}-libxenstat \
    ${PN}-libxenstat-dev \
    ${PN}-libxenstore \
    ${PN}-libxenstore-dev \
    ${PN}-libxentoolcore \
    ${PN}-libxentoolcore-dev \
    ${PN}-libxentoollog \
    ${PN}-libxentoollog-dev \
    ${PN}-libxenvchan \
    ${PN}-libxenvchan-dev \
    ${PN}-libxlutil \
    ${PN}-libxlutil-dev \
    ${PN}-livepatch \
    ${PN}-misc \
    ${PN}-pygrub \
    ${PN}-python \
    ${PN}-remus \
    ${PN}-scripts-block \
    ${PN}-scripts-common \
    ${PN}-scripts-network \
    ${PN}-shim \
    ${PN}-staticdev \
    ${PN}-ucode \
    ${PN}-vchan \
    ${PN}-volatiles \
    ${PN}-xcutils \
    ${PN}-xencommons \
    ${PN}-xencov \
    ${PN}-xend \
    ${PN}-xend-examples \
    ${PN}-xendomains \
    ${PN}-xenhypfs \
    ${PN}-xenmon \
    ${PN}-xenpaging \
    ${PN}-xenpmd \
    ${PN}-xenstat \
    ${PN}-xenstore \
    ${PN}-xenstored \
    ${PN}-xentrace \
    ${PN}-xen-watchdog \
    ${PN}-xl \
    ${PN}-xl-examples \
    ${PN}-xm \
    ${PN}-xm-examples \
    ${PN}-xen-access \
    ${PN}-xen-memshare \
    ${PN}-test \
    ${PN}-xen-vmtrace \
    ${PN}-xen-mceinj \
    "

PROVIDES =+ " \
    virtual-xenstored \
    ${@bb.utils.contains('PACKAGECONFIG', 'externalblktap', '', '${BLKTAP_PROVIDES}', d)} \
    "

# There are multiple implementations of the XenStore daemon, so we use a
# virtual package to allow for substitution.
RPROVIDES:${PN}-xenstored = "virtual-xenstored"

FILES:${PN}-dbg += "\
    ${libdir}/xen/bin/.debug \
    ${nonarch_libdir}/${PYTHON_DIR}/site-packages/.debug \
    ${nonarch_libdir}/${PYTHON_DIR}/site-packages/xen/lowlevel/.debug \
    ${libdir}/fs/xfs/.debug \
    ${libdir}/fs/ufs/.debug \
    ${libdir}/fs/ext2fs-lib/.debug \
    ${libdir}/fs/fat/.debug \
    ${libdir}/fs/zfs/.debug \
    ${libdir}/fs/reiserfs/.debug \
    ${libdir}/fs/iso9660/.debug \
    ${libdir}/fs/**/.debug \
    ${sbindir}/.debug \
    ${libdir}exec/.debug \
    ${libdir}/xen/libexec/.debug \
    ${bindir}/.debug \
    ${nonarch_libdir}/${PYTHON_DIR}/dist-packages/.debug \
    ${nonarch_libdir}/${PYTHON_DIR}/dist-packages/xen/lowlevel/.debug \
    "

FILES:${PN}-dev = "\
    ${includedir} \
    "

FILES:${PN}-doc = "\
    ${sysconfdir}/xen/README \
    ${sysconfdir}/xen/README.incompatibilities \
    ${datadir}/doc \
    ${datadir}/man \
    "

FILES:${PN}-staticdev += "\
    ${libdir}/libxenguest.a \
    ${libdir}/libxenlight.a \
    ${libdir}/libxenvchan.a \
    ${libdir}/libxenctrl.a \
    ${libdir}/libxlutil.a \
    ${libdir}/libxenstat.a \
    ${libdir}/libxenstore.a \
    "

FILES:${PN}-libxencall = "${libdir}/libxencall.so.*"
FILES:${PN}-libxencall-dev = " \
    ${libdir}/libxencall.so \
    ${libdir}/pkgconfig/xencall.pc \
    ${datadir}/pkgconfig/xencall.pc \
    "

FILES:${PN}-libxenctrl = "${libdir}/libxenctrl.so.*"
FILES:${PN}-libxenctrl-dev = " \
    ${libdir}/libxenctrl.so \
    ${libdir}/pkgconfig/xencontrol.pc \
    ${datadir}/pkgconfig/xencontrol.pc \
    "

FILES:${PN}-libxendevicemodel = "${libdir}/libxendevicemodel.so.*"
FILES:${PN}-libxendevicemodel-dev = " \
    ${libdir}/libxendevicemodel.so \
    ${libdir}/pkgconfig/xendevicemodel.pc \
    ${datadir}/pkgconfig/xendevicemodel.pc \
    "

FILES:${PN}-libxenevtchn = "${libdir}/libxenevtchn.so.*"
FILES:${PN}-libxenevtchn-dev = " \
    ${libdir}/libxenevtchn.so \
    ${libdir}/pkgconfig/xenevtchn.pc \
    ${datadir}/pkgconfig/xenevtchn.pc \
    "

FILES:${PN}-libxenforeignmemory = "${libdir}/libxenforeignmemory.so.*"
FILES:${PN}-libxenforeignmemory-dev = " \
    ${libdir}/libxenforeignmemory.so \
    ${libdir}/pkgconfig/xenforeignmemory.pc \
    ${datadir}/pkgconfig/xenforeignmemory.pc \
    "

FILES:${PN}-libxengnttab = "${libdir}/libxengnttab.so.*"
FILES:${PN}-libxengnttab-dev = " \
    ${libdir}/libxengnttab.so \
    ${libdir}/pkgconfig/xengnttab.pc \
    ${datadir}/pkgconfig/xengnttab.pc \
    "

FILES:${PN}-libxenguest = "${libdir}/libxenguest.so.*"
FILES:${PN}-libxenguest-dev = " \
    ${libdir}/libxenguest.so \
    ${libdir}/pkgconfig/xenguest.pc \
    ${datadir}/pkgconfig/xenguest.pc \
    "

FILES:${PN}-libxenhypfs = "${libdir}/libxenhypfs.so.*"
FILES:${PN}-libxenhypfs-dev = " \
    ${libdir}/libxenhypfs.so \
    ${libdir}/pkgconfig/xenhypfs.pc \
    "

FILES:${PN}-libxenlight = "${libdir}/libxenlight.so.*"
FILES:${PN}-libxenlight-dev = " \
    ${libdir}/libxenlight.so \
    ${libdir}/pkgconfig/xenlight.pc \
    ${datadir}/pkgconfig/xenlight.pc \
    "

FILES:${PN}-libxenstat = "${libdir}/libxenstat.so.*"
FILES:${PN}-libxenstat-dev = " \
    ${libdir}/libxenstat.so \
    ${libdir}/pkgconfig/xenstat.pc \
    ${datadir}/pkgconfig/xenstat.pc \
    "

FILES:${PN}-libxenstore = "${libdir}/libxenstore.so.*"
FILES:${PN}-libxenstore-dev = " \
    ${libdir}/libxenstore.so \
    ${libdir}/pkgconfig/xenstore.pc \
    ${datadir}/pkgconfig/xenstore.pc \
    "

FILES:${PN}-libxentoolcore = "${libdir}/libxentoolcore.so.*"
FILES:${PN}-libxentoolcore-dev = " \
    ${libdir}/libxentoolcore.so \
    ${libdir}/pkgconfig/xentoolcore.pc \
    ${datadir}/pkgconfig/xentoolcore.pc \
    "

FILES:${PN}-libxentoollog = "${libdir}/libxentoollog.so.*"
FILES:${PN}-libxentoollog-dev = " \
    ${libdir}/libxentoollog.so \
    ${libdir}/pkgconfig/xentoollog.pc \
    ${datadir}/pkgconfig/xentoollog.pc \
    "

FILES:${PN}-libxenvchan = "${libdir}/libxenvchan.so.*"
FILES:${PN}-libxenvchan-dev = " \
    ${libdir}/libxenvchan.so \
    ${libdir}/pkgconfig/xenvchan.pc \
    ${datadir}/pkgconfig/xenvchan.pc \
    "

FILES:${PN}-libxlutil = "${libdir}/libxlutil.so.*"
FILES:${PN}-libxlutil-dev = " \
    ${libdir}/libxlutil.so \
    ${libdir}/pkgconfig/xlutil.pc \
    ${datadir}/pkgconfig/xlutil.pc \
    "
FILES:${PN}-libvhd = "${libdir}/libvhd.so.*"
FILES:${PN}-libvhd-dev = " \
    ${libdir}/libvhd.so \
    ${libdir}/pkgconfig/vhd.pc \
    ${datadir}/pkgconfig/vhd.pc \
    "

FILES:${PN}-libfsimage = " \
    ${libdir}/libfsimage.so.* \
    ${libdir}/libxenfsimage.so.* \
    "

FILES:${PN}-libfsimage-dev = " \
    ${libdir}/libfsimage.so \
    ${libdir}/libxenfsimage.so \
    ${libdir}/pkgconfig/fsimage.pc \
    ${datadir}/pkgconfig/fsimage.pc \
    ${libdir}/pkgconfig/xenfsimage.pc \
    ${datadir}/pkgconfig/xenfsimage.pc \
    "

FILES:${PN}-fsimage = " \
    ${libdir}/fs/**/[a-z]*fsimage.so \
    ${libdir}/xenfsimage/**/fsimage.so \
    "

FILES:${PN}-init-xenstore-dom = "${libdir}/xen/bin/init-xenstore-domain"

FILES:${PN} = "\
    ${sysconfdir}/xen/auto \
    ${sysconfdir}/xen/cpupool \
    ${localstatedir}/xen/dump \
    "

FILES:${PN}-console = "\
    ${libdir}/xen/bin/xenconsole \
    ${sbindir}/xenconsoled \
    "

FILES:${PN}-cpuid = "\
    ${bindir}/xen-cpuid \
    "

FILES:${PN}-devd = "\
    ${sysconfdir}/init.d/xendriverdomain \
    ${systemd_unitdir}/system/xendriverdomain.service \
    "

FILES:${PN}-flask = "\
    /boot/xenpolicy-* \
    "

FILES:${PN}-flask-tools = "\
    ${sbindir}/flask-get-bool \
    ${sbindir}/flask-getenforce \
    ${sbindir}/flask-label-pci \
    ${sbindir}/flask-loadpolicy \
    ${sbindir}/flask-set-bool \
    ${sbindir}/flask-setenforce \
    "

FILES:${PN}-gdbsx = "\
    ${sbindir}/gdbsx \
    "

INSANE_SKIP:${PN}-hvmloader = "arch"
FILES:${PN}-hvmloader = "\
    ${libdir}/xen/boot/hvmloader \
    "

FILES:${PN}-kdd = "\
    ${sbindir}/kdd \
    ${sbindir}/xen-kdd \
    "

FILES:${PN}-livepatch += " \
    ${sbindir}/xen-livepatch \
    "

FILES:${PN}-misc = "\
    ${bindir}/xen-detect \
    ${libdir}/xen/bin/depriv-fd-checker \
    ${sbindir}/gtracestat \
    ${sbindir}/gtraceview \
    ${sbindir}/xen-bugtool \
    ${sbindir}/xenperf \
    ${sbindir}/xenpm \
    ${sbindir}/xsview \
    ${sbindir}/xen-diag \
    ${sbindir}/xen-tmem-list-parse \
    ${sbindir}/xen-python-path \
    ${sbindir}/xen-ringwatch \
    ${sbindir}/xen-hptool \
    ${sbindir}/xen-hvmcrash \
    ${sbindir}/xen-hvmctx \
    ${sbindir}/xenlockprof \
    ${sbindir}/xen-lowmemd \
    ${sbindir}/xen-mfndump \
    ${libdir}/xen/bin/verify-stream-v2 \
    ${libdir}/xen/bin/convert-legacy-stream \
    "

FILES:${PN}-pygrub = "\
    ${bindir}/pygrub \
    ${libdir}/xen/bin/pygrub \
    "

# Depending on the version of Xen libdir or nonarch libdir is used
FILES:${PN}-python = "\
    ${libdir}/${PYTHON_DIR} \
    ${nonarch_libdir}/${PYTHON_DIR} \
    "

FILES:${PN}-remus = "\
    ${sysconfdir}/xen/scripts/remus-netbuf-setup \
    "

FILES:${PN}-scripts-network = " \
    ${sysconfdir}/xen/scripts/colo-proxy-setup \
    ${sysconfdir}/xen/scripts/network-bridge \
    ${sysconfdir}/xen/scripts/network-nat \
    ${sysconfdir}/xen/scripts/network-route \
    ${sysconfdir}/xen/scripts/qemu-ifup \
    ${sysconfdir}/xen/scripts/vif2 \
    ${sysconfdir}/xen/scripts/vif-bridge \
    ${sysconfdir}/xen/scripts/vif-common.sh \
    ${sysconfdir}/xen/scripts/vif-nat \
    ${sysconfdir}/xen/scripts/vif-openvswitch \
    ${sysconfdir}/xen/scripts/vif-route \
    ${sysconfdir}/xen/scripts/vif-setup \
    "

FILES:${PN}-scripts-block = " \
    ${sysconfdir}/xen/scripts/blktap \
    ${sysconfdir}/xen/scripts/block \
    ${sysconfdir}/xen/scripts/block-common.sh \
    ${sysconfdir}/xen/scripts/block-dummy \
    ${sysconfdir}/xen/scripts/block-enbd \
    ${sysconfdir}/xen/scripts/block-iscsi \
    ${sysconfdir}/xen/scripts/block-nbd \
    ${sysconfdir}/xen/scripts/block-drbd-probe \
    ${sysconfdir}/xen/scripts/block-tap \
    ${sysconfdir}/xen/scripts/vscsi \
    "

FILES:${PN}-scripts-common = " \
    ${sysconfdir}/xen/scripts/external-device-migrate \
    ${sysconfdir}/xen/scripts/hotplugpath.sh \
    ${sysconfdir}/xen/scripts/locking.sh \
    ${sysconfdir}/xen/scripts/logging.sh \
    ${sysconfdir}/xen/scripts/xen-hotplug-cleanup \
    ${sysconfdir}/xen/scripts/xen-hotplug-common.sh \
    ${sysconfdir}/xen/scripts/xen-network-common.sh \
    ${sysconfdir}/xen/scripts/xen-script-common.sh \
    "

INSANE_SKIP:${PN}-shim = "arch"
FILES:${PN}-shim = " \
    ${libdir}/xen/boot/xen-shim \
    "

FILES:${PN}-ucode = "\
    ${sbindir}/xen-ucode \
    "

FILES:${PN}-vchan = "\
    ${bindir}/vchan-socket-proxy \
    "

FILES:${PN}-volatiles = "\
    ${sysconfdir}/default/volatiles/99_xen \
    ${sysconfdir}/tmpfiles.d/xen.conf \
    "

FILES:${PN}-xcutils = "\
    ${libdir}/xen/bin/lsevtchn \
    ${libdir}/xen/bin/readnotes \
    ${libdir}/xen/bin/xc_restore \
    ${libdir}/xen/bin/xc_save \
    "

FILES:${PN}-xencov = "\
    ${sbindir}/xencov \
    "

FILES:${PN}-xend-examples = "\
    ${sysconfdir}/xen/xend-config.sxp \
    ${sysconfdir}/xen/xend-pci-permissive.sxp \
    ${sysconfdir}/xen/xend-pci-quirks.sxp \
    "

FILES:${PN}-xenhypfs = "\
    ${sbindir}/xenhypfs \
    "

FILES:${PN}-xenpaging = "\
    ${libdir}/xen/bin/xenpaging \
    ${localstatedir}/lib/xen/xenpaging \
    "

FILES:${PN}-xenpmd = "\
    ${sbindir}/xenpmd \
    "

FILES:${PN}-xenstat = "\
    ${sbindir}/xentop \
    "

FILES:${PN}-xenstore = "\
    ${bindir}/xenstore \
    ${bindir}/xenstore-chmod \
    ${bindir}/xenstore-control \
    ${bindir}/xenstore-exists \
    ${bindir}/xenstore-list \
    ${bindir}/xenstore-ls \
    ${bindir}/xenstore-read \
    ${bindir}/xenstore-rm \
    ${bindir}/xenstore-watch \
    ${bindir}/xenstore-write \
    "

FILES:${PN}-xenstored = "\
    ${sbindir}/xenstored \
    ${localstatedir}/lib/xenstored \
    "

FILES:${PN}-xentrace = "\
    ${bindir}/xentrace \
    ${bindir}/xentrace_setsize \
    ${libdir}/xen/bin/xenctx \
    ${bindir}/xenalyze \
    ${sbindir}/xentrace \
    ${sbindir}/xentrace_setsize \
    ${sbindir}/xentrace_setmask \
    "

FILES:${PN}-xen-watchdog = "\
    ${sbindir}/xenwatchdogd \
    ${sysconfdir}/init.d/xen-watchdog \
    ${systemd_unitdir}/system/xen-watchdog.service \
    "

FILES:${PN}-xl = "\
    ${sysconfdir}/bash_completion.d/xl.sh \
    ${sysconfdir}/bash_completion.d/xl \
    ${sysconfdir}/xen/xl.conf \
    ${libdir}/xen/bin/libxl-save-helper \
    ${sbindir}/xl \
    ${libdir}/xen/bin/xen-init-dom0 \
    ${libdir}/xen/bin/init-dom0less \
    "

FILES:${PN}-xl-examples = "\
    ${sysconfdir}/xen/xlexample.hvm \
    ${sysconfdir}/xen/xlexample.pvlinux \
    ${sysconfdir}/xen/xlexample.pvhlinux \
    "

FILES:${PN}-xm-examples = "\
    ${sysconfdir}/xen/xmexample1 \
    ${sysconfdir}/xen/xmexample2 \
    ${sysconfdir}/xen/xmexample3 \
    ${sysconfdir}/xen/xmexample.hvm \
    ${sysconfdir}/xen/xmexample.hvm-stubdom \
    ${sysconfdir}/xen/xmexample.nbd \
    ${sysconfdir}/xen/xmexample.pv-grub \
    ${sysconfdir}/xen/xmexample.vti \
    "

FILES:${PN}-xenmon = "\
    ${sbindir}/xenbaked \
    ${sbindir}/xenmon.py \
    ${sbindir}/xenmon \
    "

FILES:${PN}-xm = "\
    ${sysconfdir}/xen/xm-config.xml \
    ${datadir}/xen/create.dtd \
    ${sbindir}/xm \
    "

FILES:${PN}-xencommons += "\
    ${nonarch_libdir}/modules-load.d/xen.conf \
    ${sysconfdir}/default/xencommons \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', '${sysconfdir}/init.d/xencommons', d)} \
    ${sysconfdir}/xen/scripts/launch-xenstore \
    ${systemd_unitdir}/system/proc-xen.mount \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/xen-qemu-dom0-disk-backend.service', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/xenconsoled.service', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/xen-init-dom0.service', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/xenstored.service', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}/system/var-lib-xenstored.mount', '', d)} \
    ${localstatedir} \
    "

FILES:${PN}-xend += " \
    ${sysconfdir}/init.d/xend \
    ${sbindir}/xend \
    "

FILES:${PN}-xendomains += "\
    ${libdir}/xen/bin/xendomains \
    ${sysconfdir}/default/xendomains \
    ${sysconfdir}/init.d/xendomains \
    ${sysconfdir}/sysconfig/xendomains \
    ${systemd_unitdir}/system/xendomains.service \
    "
FILES:${PN}-xen-access += "\
    ${sbindir}/xen-access \
    "

FILES:${PN}-xen-memshare += "\
    ${sbindir}/xen-memshare \
    "

# memshare is only built for x86, so allow empty package for other archs
ALLOW_EMPTY:${PN}-xen-memshare = "1"

FILES:${PN}-test += "\
    ${libdir}/xen/bin/test-xenstore \
    ${libdir}/xen/bin/test-resource \
    ${libdir}/xen/bin/test-cpu-policy \
    ${libdir}/xen/bin/test-tsx \
    ${libdir}/xen/bin/test-paging-mempool \
    "

# test-xenstore and test-resource currently only exist in 4.16
# test-cpu-policy and test-tsx only exist in 4.16 for x86
ALLOW_EMPTY:${PN}-test = "1"

FILES:${PN}-xen-mceinj +="\
    ${sbindir}/xen-mceinj \
    "

# xen-mceinj is only built for x86 4.16, so allow empty package
ALLOW_EMPTY:${PN}-xen-mceinj = "1"

FILES:${PN}-xen-vmtrace +="\
    ${sbindir}/xen-vmtrace \
    "

# xen-vmtrace is only built for x86 4.16, so allow empty package
ALLOW_EMPTY:${PN}-xen-vmtrace = "1"

INSANE_SKIP:${PN} = "already-stripped"

# configure init.d scripts
INITSCRIPT_PACKAGES = "${PN}-xend ${PN}-xen-watchdog ${PN}-xendomains ${PN}-devd"
INITSCRIPT_PACKAGES += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', '${PN}-xencommons', d)}"
INITSCRIPT_NAME:${PN}-xencommons = "xencommons"
INITSCRIPT_PARAMS:${PN}-xencommons = "defaults 80"
INITSCRIPT_NAME:${PN}-xen-watchdog = "xen-watchdog"
INITSCRIPT_PARAMS:${PN}-xen-watchdog = "defaults 81"
INITSCRIPT_NAME:${PN}-xend = "xend"
INITSCRIPT_PARAMS:${PN}-xend = "defaults 82"
INITSCRIPT_NAME:${PN}-xendomains = "xendomains"
INITSCRIPT_PARAMS:${PN}-xendomains = "defaults 83"
INITSCRIPT_NAME:${PN}-devd = "xendriverdomain"
INITSCRIPT_PARAMS:${PN}-devd = "defaults 82"

# systemd packages
SYSTEMD_PACKAGES = "${PN}-xen-watchdog ${PN}-xencommons ${PN}-xendomains ${PN}-devd"
SYSTEMD_SERVICE:${PN}-devd = "xendriverdomain.service"
SYSTEMD_SERVICE:${PN}-xen-watchdog = "xen-watchdog.service"
SYSTEMD_SERVICE:${PN}-xencommons = " \
    proc-xen.mount \
    xen-qemu-dom0-disk-backend.service \
    xenconsoled.service \
    xen-init-dom0.service \
    xenstored.service \
    "
SYSTEMD_SERVICE:${PN}-xendomains = "xendomains.service"

QEMU_ARCH = "i386"
QEMU_ARCH:aarch64 = "aarch64"

EXTRA_OECONF += " \
    --with-systemd=${systemd_unitdir}/system \
    --with-initddir=${INIT_D_DIR} \
    --with-sysconfig-leaf-dir=default \
    --with-system-qemu=${bindir}/qemu-system-${QEMU_ARCH} \
    "

do_configure() {
    do_configure_common
}

do_compile() {
    cd ${S}
    oe_runmake tools PYTHON="${PYTHON}" \
                     EXTRA_CFLAGS_XEN_TOOLS="${EXTRA_CFLAGS_XEN_TOOLS}"
}

do_install() {
    cd ${S}
    oe_runmake DESTDIR="${D}" install-tools

    # Remove unported python 2 scripts -- see the separate xen-python2 recipe
    rm -f ${D}${bindir}/xentrace_format \
          ${D}${bindir}/xencons \
          ${D}${bindir}/xencov_split \
          ${D}${libdir}/xen/bin/xenpvnetboot

    # remove installed volatiles
    rm -rf ${D}${base_prefix}/run \
        ${D}${localstatedir}/run \
        ${D}${localstatedir}/lock \
        ${D}${localstatedir}/log \
        ${D}${localstatedir}/volatile \
        ${D}${localstatedir}/lib/xen

    VOLATILE_DIRS=" \
        ${base_prefix}/run/xenstored \
        ${base_prefix}/run/xend \
        ${base_prefix}/run/xend/boot \
        ${base_prefix}/run/xen \
        ${localstatedir}/log/xen \
        ${localstatedir}/lock/xen \
        ${localstatedir}/lock/subsys \
        ${localstatedir}/lib/xen \
        "

    # install volatiles using populate_volatiles mechanism
    install -d ${D}${sysconfdir}/default/volatiles
    for i in $VOLATILE_DIRS; do
        echo "d root root 0755 $i none"  >> ${D}${sysconfdir}/default/volatiles/99_xen
    done

    # workaround for xendomains script which searchs sysconfig if directory exists
    install -d ${D}${sysconfdir}/sysconfig
    ln -sf ${sysconfdir}/default/xendomains ${D}${sysconfdir}/sysconfig/xendomains

    # systemd
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        # install volatiles using systemd tmpfiles.d
        install -d ${D}${sysconfdir}/tmpfiles.d
        for i in $VOLATILE_DIRS; do
            echo "d $i 0755 root root - -"  >> ${D}${sysconfdir}/tmpfiles.d/xen.conf
        done
    fi

    if [ -e ${D}${systemd_unitdir}/system/xen-qemu-dom0-disk-backend.service ]; then
        sed -i 's#ExecStart=.*qemu-system-i386\(.*\)$#ExecStart=/usr/bin/qemu-system-i386\1#' \
            ${D}${systemd_unitdir}/system/xen-qemu-dom0-disk-backend.service
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        rm -f ${D}/${sysconfdir}/init.d/xencommons
    else
        # fixup default path to qemu-system-i386
        sed -i 's#\(test -z "$QEMU_XEN" && QEMU_XEN=\).*$#\1"/usr/bin/qemu-system-i386"#' ${D}/etc/init.d/xencommons

        # remove the uncondiontally installed systemd service files
        rm -f ${D}/${systemd_unitdir}/system/xen-qemu-dom0-disk-backend.service
        rm -f ${D}/${systemd_unitdir}/system/xenconsoled.service
        rm -f ${D}/${systemd_unitdir}/system/xen-init-dom0.service
        rm -f ${D}/${systemd_unitdir}/system/xenstored.service
        rm -f ${D}/${systemd_unitdir}/system/var-lib-xenstored.mount
    fi
}

pkg_postinst:${PN}-volatiles() {
    if [ -z "$D" ]; then
        if command -v systemd-tmpfiles >/dev/null; then
            systemd-tmpfiles --create ${sysconfdir}/tmpfiles.d/xen.conf
        elif [ -e ${sysconfdir}/init.d/populate-volatile.sh ]; then
            ${sysconfdir}/init.d/populate-volatile.sh update
        fi
    fi
}

do_deploy() {
    XEN_FULLVERSION=$(oe_runmake -C ${S}/xen xenversion --no-print-directory)
    FLASK_POLICY_FILE="xenpolicy-${XEN_FULLVERSION}"

    install -d ${DEPLOYDIR}

    # Install the flask policy in the deploy directory if it exists
    if [ -f ${D}/boot/${FLASK_POLICY_FILE} ]; then
        install -m 0644 ${D}/boot/${FLASK_POLICY_FILE} ${DEPLOYDIR}
        ln -sf ${FLASK_POLICY_FILE} ${DEPLOYDIR}/xenpolicy-${MACHINE}
    fi
}
# Scheduling the do_deploy task:
# - deploy copies files from ${D} that are written during do_install so must run
#   after that task
# - the tools binaries are included in the image filesystem, so we must ensure
#   that the binaries deployed match what is staged in the sysroot:
#   so do_deploy must run after do_populate_sysroot
# - add the task before do_build to ensure that deployment has completed when
#   the recipe build done stamp is written
addtask deploy after do_install do_populate_sysroot before do_build
