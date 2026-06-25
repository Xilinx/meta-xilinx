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

        # v6.18+ rq offset generation needs these scheduler sources/headers
        cp -a --parents kernel/sched/rq-offsets.c $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/sched/sched.h $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/sched/cpudeadline.h $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/sched/cpupri.h $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/sched/features.h $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/sched/stats.h $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/sched/ext.h $kerneldir/build 2>/dev/null || :
        cp -a --parents kernel/workqueue_internal.h $kerneldir/build 2>/dev/null || :

    )

    chown -R root:root ${D}
}
