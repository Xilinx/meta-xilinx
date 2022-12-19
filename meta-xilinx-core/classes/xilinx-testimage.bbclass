inherit testimage

HOSTTOOLS += 'ip ping ps scp ssh stty'

python do_testimage:prepend () {
    from oeqa.core.target.qemu import supported_fstypes
    supported_fstypes.append('wic.qemu-sd')
}

IMAGE_FSTYPES = "wic.qemu-sd"
