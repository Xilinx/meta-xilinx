IR_PATH ??= "${@d.getVarFlag('IR_PATH', d.getVar('MACHINE')) or ''}"
WEB_PATH ??= "${@d.getVarFlag('WEB_PATH', d.getVar('MACHINE')) or ''}"

# k26-smk-kv-sdt
IR_PATH[k26-smk-kv-sdt] = 'https://edf.amd.com/sswreleases/rel-v2025.2/image-recovery/2025.2/10090801/ImgRecovery.elf'
SRC_URI[k26-smk-kv-sdt_ir.sha256sum] = 'f0eebf410b3397b70e2aa3f33d16cdb31e1699b64e75cbb0a2b7a00364f4ded5'

WEB_PATH[k26-smk-kv-sdt] = 'https://edf.amd.com/sswreleases/rel-v2025.2/image-recovery/2025.2/10090801/web.img'
SRC_URI[k26-smk-kv-sdt_web.sha256sum] = '0825518aea24c3104a58b0f847436db03a92d8d4366baad6594c4d03de09d8cb'

# k26-smk-kr-sdt
IR_PATH[k26-smk-kr-sdt] = 'https://edf.amd.com/sswreleases/rel-v2025.2/image-recovery/2025.2/10090801/ImgRecovery.elf'
SRC_URI[k26-smk-kr-sdt_ir.sha256sum] = 'f0eebf410b3397b70e2aa3f33d16cdb31e1699b64e75cbb0a2b7a00364f4ded5'

WEB_PATH[k26-smk-kr-sdt] = 'https://edf.amd.com/sswreleases/rel-v2025.2/image-recovery/2025.2/10090801/web.img'
SRC_URI[k26-smk-kr-sdt_web.sha256sum] = '0825518aea24c3104a58b0f847436db03a92d8d4366baad6594c4d03de09d8cb'


# k24-smk-kd-sdt
IR_PATH[k24-smk-kd-sdt] = 'https://edf.amd.com/sswreleases/rel-v2025.2/image-recovery/2025.2/10090801/ImgRecovery.elf'
SRC_URI[k24-smk-kd-sdt_ir.sha256sum] = 'f0eebf410b3397b70e2aa3f33d16cdb31e1699b64e75cbb0a2b7a00364f4ded5'

WEB_PATH[k24-smk-kd-sdt] = 'https://edf.amd.com/sswreleases/rel-v2025.2/image-recovery/2025.2/10090801/web.img'
SRC_URI[k24-smk-kd-sdt_web.sha256sum] = '0825518aea24c3104a58b0f847436db03a92d8d4366baad6594c4d03de09d8cb'

# k24-smk-kd-sdt-multidomain
IR_PATH[k24-smk-kd-sdt-multidomain] = "${@d.getVarFlag('IR_PATH', 'k24-smk-kd-sdt') or ''}"
SRC_URI[k24-smk-kd-sdt-multidomain_ir.sha256sum] = "${@d.getVarFlag('SRC_URI', 'k24-smk-kd-sdt_ir.sha256sum') or ''}"

WEB_PATH[k24-smk-kd-sdt-multidomain] = "${@d.getVarFlag('WEB_PATH', 'k24-smk-kd-sdt') or ''}"
SRC_URI[k24-smk-kd-sdt-multidomain_web.sha256sum] = "${@d.getVarFlag('SRC_URI', 'k24-smk-kd-sdt_web.sha256sum') or ''}"

# k26-smk-kr-sdt-multidomain
IR_PATH[k26-smk-kr-sdt-multidomain] = "${@d.getVarFlag('IR_PATH', 'k26-smk-kr-sdt') or ''}"
SRC_URI[k26-smk-kr-sdt-multidomain_ir.sha256sum] = "${@d.getVarFlag('SRC_URI', 'k26-smk-kr-sdt_ir.sha256sum') or ''}"

WEB_PATH[k26-smk-kr-sdt-multidomain] = "${@d.getVarFlag('WEB_PATH', 'k26-smk-kr-sdt') or ''}"
SRC_URI[k26-smk-kr-sdt-multidomain_web.sha256sum] = "${@d.getVarFlag('SRC_URI', 'k26-smk-kr-sdt_web.sha256sum') or ''}"

# k26-smk-kv-sdt-multidomain
IR_PATH[k26-smk-kv-sdt-multidomain] = "${@d.getVarFlag('IR_PATH', 'k26-smk-kv-sdt') or ''}"
SRC_URI[k26-smk-kv-sdt-multidomain_ir.sha256sum] = "${@d.getVarFlag('SRC_URI', 'k26-smk-kv-sdt_ir.sha256sum') or ''}"

WEB_PATH[k26-smk-kv-sdt-multidomain] = "${@d.getVarFlag('WEB_PATH', 'k26-smk-kv-sdt') or ''}"
SRC_URI[k26-smk-kv-sdt-multidomain_web.sha256sum] = "${@d.getVarFlag('SRC_URI', 'k26-smk-kv-sdt_web.sha256sum') or ''}"
