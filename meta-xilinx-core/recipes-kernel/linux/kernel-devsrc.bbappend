# Backport of 6.12 support based on Poky commit ce6a0b875b79f44cc00bdc9cbe7015d1fbaf2582
do_install:append () {
    (
        cd ${S}
        if [ "${ARCH}" = "arm64" ]; then
            # 6.12+
            cp -a --parents arch/arm64/kernel/vdso/vgetrandom.c $kerneldir/build/ 2>/dev/null || :
            cp -a --parents arch/arm64/kernel/vdso/vgetrandom-chacha.S $kerneldir/build/ 2>/dev/null || :

            # 6.12+
            cp -a --parents arch/arm64/tools/syscall_64.tbl $kerneldir/build/   2>/dev/null || :
            cp -a --parents arch/arm64/tools/syscall_32.tbl $kerneldir/build/   2>/dev/null || :
        fi
    )

    chown -R root:root ${D}
}
