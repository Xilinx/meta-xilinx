# Xen-4.20 specific changes are only applicable on the target
SRC_URI_XEN = ""
SRC_URI_XEN:class-target = " \
    file://0001-softmmu-physmem-Split-ram_block_add.patch \
    file://0002-softmmu-let-qemu_map_ram_ptr-use-qemu_ram_ptr_length.patch \
    file://0003-hw-xen-Rename-ram_memory-global-variable-as-xen_memo.patch \
    file://0004-xen-add-pseudo-RAM-region-for-grant-mappings.patch \
    file://0005-hw-xen-xen-mapcache.c-convert-DPRINTF-to-tracepoints.patch \
    file://0006-hw-xen-convert-stderr-prints-to-error-warn-reports.patch \
    file://0007-xen-let-xen_ram_addr_from_mapcache-return-1-in-case-.patch \
    file://0008-memory-add-MemoryRegion-map-and-unmap-callbacks.patch \
    file://0009-xen-add-map-and-unmap-callbacks-for-grant-region.patch \
    file://0010-hw-arm-Add-grant-mapping.patch \
    file://0011-hw-arm-xenpvh-Update-file-header-to-use-SPDX.patch \
    file://0012-hw-arm-xenpvh-Tweak-machine-description.patch \
    file://0013-hw-arm-xenpvh-Add-support-for-SMP-guests.patch \
    file://0014-hw-arm-xen_arm.c-convert-DPRINTF-to-trace-events-and.patch \
    file://0015-hw-arm-xenpvh-Remove-double-negation-in-warning.patch \
    file://0016-hw-arm-xenpvh-Move-stubbed-functions-to-xen-stubs.c.patch \
    file://0017-hw-arm-xenpvh-Break-out-a-common-PVH-machine.patch \
    file://0018-hw-arm-xenpvh-Rename-xen_arm.c-xen-pvh.c.patch \
    file://0019-hw-arm-xenpvh-Reverse-virtio-mmio-creation-order.patch \
    file://0020-hw-xen-pvh-common-Add-support-for-creating-PCIe-GPEX.patch \
    file://0021-hw-i386-xen-Add-a-Xen-PVH-x86-machine.patch \
    file://0022-hw-xen-xen-hvm-common.c-convert-DPRINTF-to-tracepoin.patch \
    file://0023-hw-xen-Expose-handle_bufioreq-in-xen_register_ioreq.patch \
    file://0024-hw-xen-xenpvh-Disable-buffered-IOREQs-for-ARM.patch \
    file://0025-hw-xen-xenpvh-Add-pci-intx-irq-base-property.patch \
    file://0026-hw-arm-xenpvh-Enable-PCI-for-ARM-PVH.patch \
    file://0027-memory-Fix-use-of-uninitialized-variable.patch \
    file://0028-virtio-Introduce-notify_queue.patch \
    file://0029-virtio-Add-virtio_queue_get_rings.patch \
    file://0030-virtio-Add-the-virtio-msg-transport.patch \
    file://0031-hw-xen-Add-xendevicemodel_virtio_msg_bus_xen_connect.patch \
    file://0032-virtio-Add-a-virtio-msg-bus-between-backend-and-Xen.patch \
    file://0033-hw-arm-xenpvh-Enable-virtio-msg.patch \
    file://0034-hw-x86-xenpvh-Enable-virtio-msg.patch \
    file://0035-xen-mapcache-Assert-mapcache-existance.patch \
    file://0036-xen-mapcache-Add-function-to-check-if-the-mapcache-i.patch \
    file://0037-physmem-xen-Conditionalize-use-of-the-mapcache.patch \
    file://0038-hw-xen-hvm-Add-a-mapcache-arg-to-xen_register_ioreq.patch \
    file://0039-hw-xen-xenpvh-Add-prop-to-enable-disable-the-mapcach.patch \
    file://0040-xen-pvh-Fix-order-of-virtio-mmio-devices.patch \
    file://0041-hw-xen-pvh-Unify-mapcache-off-RAM-layout-with-mapcac.patch \
    "

FILESEXTRAPATHS:prepend:class-target := "${THISDIR}/qemu-xen-8.2:"

SRC_URI .= "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${SRC_URI_XEN}', '', d)}"
