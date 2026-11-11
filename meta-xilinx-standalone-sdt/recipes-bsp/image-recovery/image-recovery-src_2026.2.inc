IMG_RCVRY_REPO ??= "git://github.com/Xilinx/image-recovery.git;protocol=https"

IMG_RCVRY_BRANCH ??= "main"
IMG_RCVRY_BRANCHARG = "${@['nobranch=1', 'branch=${IMG_RCVRY_BRANCH}'][d.getVar('IMG_RCVRY_BRANCH') != '']}"

IMG_RCVRY_SRCREV ?= "a3fb24d38a73b947adbc8897fd728b7b8ec93226"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:k26-smk-kr-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k26-smk-kv-sdt = "${MACHINE}"
COMPATIBLE_MACHINE:k24-smk-kd-sdt = "${MACHINE}"
