PACKAGECONFIG:append = "${@bb.utils.contains('DISTRO_FEATURES', 'tpm', ' tpm', '', d)}"

PACKAGECONFIG[tpm] = "--enable-tpm,--disable-tpm,,swtpm libtpm"

