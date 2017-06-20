SUMMARY = "U-Boot uEnv.txt SD boot environment generation for Zynq targets"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynq = "zynq"

inherit deploy

def bootfiles_bitstream(d):
    expectedfiles = [("bitstream", True)]
    expectedexts = [(".bit", True), (".bin", False)]
    # search for bitstream paths, use the renamed file. First matching is used
    for f in (d.getVar("IMAGE_BOOT_FILES") or "").split():
        sf, rf = f, f
        if ';' in f:
            sf, rf = f.split(';')

        # skip boot.bin, it is not a bitstream
        if sf == "boot.bin" or rf == "boot.bin":
            continue

        for e, t in expectedfiles:
            if sf == e or rf == e:
                return rf, t
        for e, t in expectedexts:
            if sf.endswith(e) or rf.endswith(e):
                return rf, t
    return "", False

def bootfiles_dtb_filepath(d):
    if d.getVar("IMAGE_BOOT_FILES"):
        dtbs = d.getVar("IMAGE_BOOT_FILES").split(" ")
        # IMAGE_BOOT_FILES has extra renaming info in the format '<source>;<target>'
        dtbs = [f.split(";")[0] for f in dtbs]
        dtbs = [f for f in dtbs if f.endswith(".dtb")]
        if len(dtbs) != 0:
            return dtbs[0]
    return ""

do_compile() {
	echo "machine_name=${MACHINE}" > ${WORKDIR}/uEnv.txt

	echo "kernel_image=${KERNEL_IMAGETYPE}" >> ${WORKDIR}/uEnv.txt
	echo "kernel_load_address=0x2080000" >> ${WORKDIR}/uEnv.txt
	echo "devicetree_image=${@bootfiles_dtb_filepath(d)}" >> ${WORKDIR}/uEnv.txt
	echo "devicetree_load_address=0x2000000" >> ${WORKDIR}/uEnv.txt

	# bootargs, default to booting with the rootfs device being partition 2 of the first mmc device
	echo 'bootargs=console=ttyPS0,115200 root=/dev/mmcblk0p2 rw rootwait earlyprintk' >> ${WORKDIR}/uEnv.txt

	echo 'loadkernel=fatload mmc 0 ${kernel_load_address} ${kernel_image}' >> ${WORKDIR}/uEnv.txt
	echo 'loaddtb=fatload mmc 0 ${devicetree_load_address} ${devicetree_image}' >> ${WORKDIR}/uEnv.txt
	echo 'bootkernel=run loadkernel && run loaddtb && bootm ${kernel_load_address} - ${devicetree_load_address}' >> ${WORKDIR}/uEnv.txt

	BITSTREAMPATH="${@bootfiles_bitstream(d)[0]}"
	if [ ! -z "$BITSTREAMPATH" ]; then
		echo "bitstream_image=$BITSTREAMPATH" >> ${WORKDIR}/uEnv.txt
		# if bitstream is "bit" format use loadb, otherwise use load
		echo "bitstream_type=${@'loadb' if bootfiles_bitstream(d)[1] else 'load'}" >> ${WORKDIR}/uEnv.txt
		echo 'loadfpga=fatload mmc 0 ${loadbit_addr} ${bitstream_image} && fpga ${bitstream_type} 0 ${loadbit_addr} ${filesize}' >> ${WORKDIR}/uEnv.txt

		# load bitstream first
		echo "uenvcmd=run loadfpga && run bootkernel" >> ${WORKDIR}/uEnv.txt
	else
		# no need to load bitstream during boot
		echo "uenvcmd=run bootkernel" >> ${WORKDIR}/uEnv.txt
	fi
}

FILES_${PN} += "/boot/uEnv.txt"

do_install() {
	install -Dm 0644 ${WORKDIR}/uEnv.txt ${D}/boot/uEnv.txt
}

do_deploy() {
	install -Dm 0644 ${WORKDIR}/uEnv.txt ${DEPLOYDIR}/uEnv.txt
}
addtask do_deploy after do_compile before do_build

