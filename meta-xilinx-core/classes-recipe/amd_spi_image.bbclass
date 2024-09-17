#
# Copyright (C) 2023, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#

QSPI_SIZE ?= "0x2280000"
QSPI_VERSION ?= ""
QSPI_IMAGE_VERSION ?= ""

# Register values
IDN_REG ?= "0x4D554241"
VERSION_REG ?= "0x1"
LENGTH_REG ?= "0x4"
PERSISTENT_REG ?= "0x01010000"

# QSPI Offsets
IMAGE_SELECTOR_OFFSET ?= "0x0"
IMAGE_SELECTOR_BACKUP_OFFSET ?= "0x80000"
PERSISTENT_REG_OFFSET ?= "0x100000"
PERSISTENT_REG_BACKUP_OFFSET ?= "0x120000"
IMAGE_A_OFFSET ?= "0x200000"
IMAGE_A_IMGSEL_OFFSET ?= "0xF00000"
IMAGE_B_OFFSET ?= "0xF80000"
IMAGE_B_IMGSEL_OFFSET ?= "0x1C80000"
IMAGE_RCVRY_OFFSET ?= "0x1E00000"
IMAGE_RCVRY_BACKUP_OFFSET ?= "0x2000000"
VERSION_OFFSET ?= "0x2240000"
CHECKSUM_OFFSET ?= "0x2250000"

def generate_spi_image(d):

    import io
    import hashlib
    import time

    qspi_size = int(d.getVar("QSPI_SIZE") or '0', 0)
    int(d.getVar("QSPI_SIZE") or '0', 0)

    # Register values
    idn_reg = int(d.getVar("IDN_REG") or '0', 0)
    version_reg = int(d.getVar("VERSION_REG") or '0', 0)
    length_reg = int(d.getVar("LENGTH_REG") or '0', 0)
    persistent_reg = int(d.getVar("PERSISTENT_REG") or '0', 0)

    # QSPI Offsets
    image_selector_offset = int(d.getVar("IMAGE_SELECTOR_OFFSET") or '0', 0)
    image_selector_backup_offset = int(d.getVar("IMAGE_SELECTOR_BACKUP_OFFSET") or '0', 0)
    persistent_reg_offset = int(d.getVar("PERSISTENT_REG_OFFSET") or '0', 0)
    persistent_reg_backup_offset = int(d.getVar("PERSISTENT_REG_BACKUP_OFFSET") or '0', 0)
    image_a_offset = int(d.getVar("IMAGE_A_OFFSET") or '0', 0)
    image_a_imgsel_offset = int(d.getVar("IMAGE_A_IMGSEL_OFFSET") or '0', 0)
    image_b_offset = int(d.getVar("IMAGE_B_OFFSET") or '0', 0)
    image_b_imgsel_offset = int(d.getVar("IMAGE_B_IMGSEL_OFFSET") or '0', 0)
    image_rcvry_offset = int(d.getVar("IMAGE_RCVRY_OFFSET") or '0', 0)
    image_rcvry_backup_offset = int(d.getVar("IMAGE_RCVRY_BACKUP_OFFSET") or '0', 0)
    version_offset = int(d.getVar("VERSION_OFFSET") or '0', 0)
    checksum_offset = int(d.getVar("CHECKSUM_OFFSET") or '0', 0)

    # QSPI data
    qspi_data = io.BytesIO()
    qspi_data.write(b'\xFF' * qspi_size)

    # Image Selector - Primary, Backup, Image A and Image B
    imgsel_file = d.getVar("DEPLOY_DIR_IMAGE")+"/image-selector-"+d.getVar("MACHINE")+".bin"
    try:
        with open(imgsel_file, "rb") as il:
            imgsel = il.read(-1)
    except OSError as err:
        bb.fatal("Unable to open imgsel file: " + str(err))

    qspi_data.seek(image_selector_offset)
    qspi_data.write(imgsel)
    qspi_data.seek(image_selector_backup_offset)
    qspi_data.write(imgsel)
    qspi_data.seek(image_a_imgsel_offset)
    qspi_data.write(imgsel)
    qspi_data.seek(image_b_imgsel_offset)
    qspi_data.write(imgsel)

    # Persistent Registers - Primary and Backup
    p_reg = [idn_reg, version_reg, length_reg, persistent_reg, \
            image_a_offset, image_b_offset, image_rcvry_offset]
    checksum = 0xffffffff - (0xffffffff & sum(p_reg))
    p_reg.insert(3, checksum)

    qspi_data.seek(persistent_reg_offset)
    for value in p_reg:
        qspi_data.write(value.to_bytes(4, byteorder="little"))

    qspi_data.seek(persistent_reg_backup_offset)
    for value in p_reg:
        qspi_data.write(value.to_bytes(4, byteorder="little"))

    # Image A and B - boot.bin
    try:
        with open(d.getVar("DEPLOY_DIR_IMAGE")+"/boot.bin", "rb") as bo:
            bootbin = bo.read(-1)
    except OSError as err:
        bb.fatal("Unable to open boot.bin file: " + str(err))

    qspi_data.seek(image_a_offset)
    qspi_data.write(bootbin)
    qspi_data.seek(image_b_offset)
    qspi_data.write(bootbin)

    # Recovery Image & Recovery Image Backup
    imgrcry_file = d.getVar("DEPLOY_DIR_IMAGE")+"/image-recovery-"+d.getVar("MACHINE")+".bin"
    try:
        with open(imgrcry_file, "rb") as iy:
            imgrcry = iy.read(-1)
    except OSError as err:
        bb.fatal("Unable to open imgrcry file: " + str(err))

    qspi_data.seek(image_rcvry_offset)
    qspi_data.write(imgrcry)
    qspi_data.seek(image_rcvry_backup_offset)
    qspi_data.write(imgrcry)

    # Version string and checksum
    version = d.getVar('QSPI_IMAGE_VERSION')
    qspi_version = f"{version}\x00"
    qspi_data.seek(version_offset)
    qspi_data.write(qspi_version.encode())

    qspi_sha = hashlib.sha256(qspi_data.getbuffer())
    qspi_data.seek(checksum_offset)
    qspi_data.write(qspi_sha.digest())

    # Write the QSPI data to file
    with open(d.getVar("B") + "/" + d.getVar("IMAGE_NAME") + ".bin", "wb") as sq:
        sq.write(qspi_data.getbuffer())

do_compile[depends] += "virtual/boot-bin:do_deploy virtual/imgsel:do_deploy virtual/imgrcry:do_deploy"

python amd_spi_image_do_compile() {
    generate_spi_image(d)
}

EXPORT_FUNCTIONS do_compile
