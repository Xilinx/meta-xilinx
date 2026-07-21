# Xen-specific changes for qemu 10.2.x
#
# These patches add and refine Xen PVH/virtio-msg support: the virtio-msg
# transport and its Xen bus/backend hookup, virtio-msg enablement for the
# ARM and x86 PVH machines, and mapcache handling (assert its existence,
# check whether it is enabled, conditionalize its use in physmem, plumb it
# through xen_register_ioreq and xenpvh, and unify the mapcache-off RAM
# layout), plus a virtio mmio device ordering fix for PVH.
SRC_URI_XEN = ""
SRC_URI_XEN:class-target = "\
    file://0001-virtio-Introduce-notify_queue.patch \
    file://0002-virtio-Add-virtio_queue_get_rings.patch \
    file://0003-virtio-Add-the-virtio-msg-transport.patch \
    file://0004-hw-xen-Add-xendevicemodel_virtio_msg_bus_xen_connect.patch \
    file://0005-virtio-Add-a-virtio-msg-bus-between-backend-and-Xen.patch \
    file://0006-hw-arm-xenpvh-Enable-virtio-msg.patch \
    file://0007-hw-x86-xenpvh-Enable-virtio-msg.patch \
    file://0008-xen-mapcache-Assert-mapcache-existance.patch \
    file://0009-xen-mapcache-Add-function-to-check-if-the-mapcache-i.patch \
    file://0010-physmem-xen-Conditionalize-use-of-the-mapcache.patch \
    file://0011-hw-xen-hvm-Add-a-mapcache-arg-to-xen_register_ioreq.patch \
    file://0012-hw-xen-xenpvh-Add-prop-to-enable-disable-the-mapcach.patch \
    file://0013-xen-pvh-Fix-order-of-virtio-mmio-devices.patch \
    file://0014-hw-xen-pvh-Unify-mapcache-off-RAM-layout-with-mapcac.patch \
    "

FILESEXTRAPATHS:prepend:class-target := "${THISDIR}/qemu-xen-10.2:"

SRC_URI .= "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${SRC_URI_XEN}', '', d)}"
