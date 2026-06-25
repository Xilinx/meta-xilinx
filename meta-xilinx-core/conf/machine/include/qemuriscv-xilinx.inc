# This include file is a copy of poky/meta/conf/machine/include/riscv/qemuriscv.inc
# with some QB_* params adjusted to boot qemu for qemu-microblaze-v-32 and
# qemu-microblaze-v-64 machines.

# qemuboot options, based on qemuriscv
# Currently microblaze-v is only single thread
#QB_SMP ?= "-smp 4"
QB_KERNEL_CMDLINE_APPEND = "earlycon=sbi"
QB_MACHINE = "-machine virt"
QB_DEFAULT_BIOS = "fw_jump.elf"
QB_TAP_OPT = "-netdev tap,id=net0,ifname=@TAP@,script=no,downscript=no"
QB_NETWORK_DEVICE = "-device virtio-net-device,netdev=net0,mac=@MAC@"
QB_ROOTFS_OPT = "-drive id=disk0,file=@ROOTFS@,if=none,format=raw -device virtio-blk-device,drive=disk0"
QB_SERIAL_OPT = "-device virtio-serial-device -chardev null,id=virtcon -device virtconsole,chardev=virtcon"
QB_TCPSERIAL_OPT = " -device virtio-serial-device -chardev socket,id=virtcon,port=@PORT@,host=127.0.0.1,nodelay=on -device virtconsole,chardev=virtcon"
# Graphics are not supported
#QB_GRAPHICS = "-device bochs-display"

# Default when using wic.qemu-sd
QB_KERNEL_ROOT ??= "/dev/vda2"
