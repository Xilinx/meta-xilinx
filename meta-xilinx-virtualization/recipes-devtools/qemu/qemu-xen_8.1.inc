# Xen-4.18 specific changes are only applicable on the target
SRC_URI_XEN = ""
SRC_URI_XEN:class-target = " \
    file://0001-xen_arm-Create-virtio-mmio-devices-during-initializa.patch \
    file://0002-xen_arm-Initialize-RAM-and-add-hi-low-memory-regions.patch \
    file://0003-Xen-Fix-xen_set_irq-and-xendevicemodel_set_irq_level.patch \
    file://0004-xen-when-unplugging-emulated-devices-skip-virtio-dev.patch \
    file://0005-softmmu-physmem-Split-ram_block_add.patch \
    file://0006-xen-add-pseudo-RAM-region-for-grant-mappings.patch \
    file://0007-softmmu-let-qemu_map_ram_ptr-use-qemu_ram_ptr_length.patch \
    file://0008-xen-let-xen_ram_addr_from_mapcache-return-1-in-case-.patch \
    file://0009-memory-add-MemoryRegion-map-and-unmap-callbacks.patch \
    file://0010-xen-add-map-and-unmap-callbacks-for-grant-region.patch \
    file://0011-hw-arm-Add-grant-mapping.patch \
    file://0001-arm-xenpvh-Introduce-virtio-pci-support.patch \
    "

FILESEXTRAPATHS:prepend:class-target := "${THISDIR}/qemu-xilinx-8.1:"

SRC_URI .= "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${SRC_URI_XEN}', '', d)}"
