# Xen-4.17 specific changes are only applicable on the target
SRC_URI_XEN = ""
SRC_URI_XEN:class-target = " \
          file://0001-xen-pt-fix-syntax-error-that-causes-FTBFS-in-some-co.patch \
          file://0001-xen_common-return-error-from-xen_create_ioreq_server.patch \
          file://0002-xen-mapcache-move-xen-mapcache.c-to-hw-xen.patch \
          file://0003-hw-i386-xen-rearrange-xen_hvm_init_pc.patch \
          file://0004-xen-hvm-move-x86-specific-fields-out-of-XenIOState.patch \
          file://0005-xen-hvm-create-arch_handle_ioreq-and-arch_xen_set_me.patch \
          file://0006-xen-hvm-move-common-functions-to-hw-xen-xen-hvm-comm.patch \
          file://0007-xen-skip-ioreq-creation-on-ioreq-registration-failur.patch \
          file://0008-accel-xen-xen-all-export-xenstore_record_dm_state.patch \
          file://0009-xen-hvm-enable-xen-hvm-common-build-for-ARM.patch \
          file://0010-hw-arm-introduce-xenpv-machine.patch \
          file://0011-meson.build-do-not-set-have_xen_pci_passthrough-for-.patch \
          file://0012-xen-arm-call-qemu_find_tpm_be-if-CONFIG_TPM.patch \
          file://0013-arm-xenpv-fix-TPM-address-print-warning.patch \
          file://0014-xen_arm-Create-virtio-mmio-devices-during-initializa.patch \
          file://0015-xen_arm-Initialize-RAM-and-add-hi-low-memory-regions.patch \
          file://0016-xen_arm-Add-accel-xen-and-drop-extra-interface-openi.patch \
          file://0001-xen-when-unplugging-emulated-devices-skip-virtio-dev.patch \
          file://0002-xen-add-pseudo-RAM-region-for-grant-mappings.patch \
          file://0003-softmmu-let-qemu_map_ram_ptr-use-qemu_ram_ptr_length.patch \
          file://0004-xen-let-xen_ram_addr_from_mapcache-return-1-in-case-.patch \
          file://0005-memory-add-MemoryRegion-map-and-unmap-callbacks.patch \
          file://0006-xen-add-map-and-unmap-callbacks-for-grant-region.patch \
          file://0007-xen-mapcache-Fix-build-on-Arm.patch \
          file://0008-hw-arm-Add-grant-mapping.patch \
          "

FILESEXTRAPATHS:prepend:class-target := "${THISDIR}/qemu-xilinx-7.1:"

SRC_URI .= "${@bb.utils.contains('DISTRO_FEATURES', 'xen', '${SRC_URI_XEN}', '', d)}"
