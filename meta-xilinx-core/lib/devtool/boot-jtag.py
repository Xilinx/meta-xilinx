# Copyright (C) 2021-2022, Xilinx, Inc.  All rights reserved.
# Copyright (C) 2022-2024, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# This script uses devtool and creates a boot-jtag.tcl script in
# ${DEPLOY_DIR_IMAGE} directory. This script is executed by xsdb tool to boot
# yocto generated images on HW via jtag boot mode.

import os
import glob
import logging
from devtool import setup_tinfoil

logger = logging.getLogger('devtool')

def bootjtag(args, config, basepath, workspace):
    """Entry point for the devtool 'boot-jtag' subcommand"""

    if not args.image:
        print('\nINFO: Please specify the target image name. \n\nExample: --image core-image-minimal or petalinux-image-minimal')
        return

    tinfoil = setup_tinfoil(basepath=basepath)
    # Get required boot variables
    try:
        rd = tinfoil.parse_recipe('u-boot-xlnx-scr')
        deploy_dir = rd.getVar('DEPLOY_DIR_IMAGE')
        machine = rd.getVar('MACHINE')
        arch =  rd.getVar('TARGET_ARCH')
        soc = rd.getVar("SOC_FAMILY")
        ddr_base_addr = rd.getVar('DDR_BASEADDR')
        kernel_img_name = rd.getVar('KERNEL_IMAGE')
        kernel_load_addr = rd.getVar('KERNEL_LOAD_ADDRESS')
        dtb_load_addr = rd.getVar('DEVICETREE_ADDRESS')
        rootfs_load_addr = rd.getVar('RAMDISK_IMAGE_ADDRESS')
        machine_features = rd.getVar('MACHINE_FEATURES')
        boot_mode = rd.getVar('BOOTMODE')
        image_name_suffix = rd.getVar('IMAGE_NAME_SUFFIX')
    finally:
        tinfoil.shutdown()

    if not args.hw_server:
        print("\nINFO: --hw_server is null so default URL description of hw_server/TCF agent and port number is set to: " +  str(args.hw_server))

    print("INFO: HW_SERVER Connected to: " + str(args.hw_server))
    print("INFO: Using DISTRO IMAGE: " + str(args.image))

    # Use arch for MB and SOC Family other devices.
    if arch == 'microblazeel':
        print("INFO: ARCH: " + arch)
    else:
        print("INFO: SOC FAMILY: " + soc)

    # Load Address of boot.scr in DDR(Except for QSPI/OSPI/NAND boot)
    # MB = (DDR base address + DDR Size) - 0xe00000
    # Zynq 7000 = DDR base address + 0x3000000
    # ZynqMP = DDR base address + 0x20000000
    # Versal & Versal-net = DDR base address + 0x20000000
    if arch == 'microblazeel':
        # Assuming DDR size is 2GB
        bootscr_addr = hex(int(ddr_base_addr, 16) + 0x80000000 - 0xe00000)
    elif soc == 'zynq':
        bootscr_addr = hex(int(ddr_base_addr, 16) + 0x3000000)
    else:
        bootscr_addr = hex(int(ddr_base_addr, 16) + 0x20000000)

    print("INFO: MACHINE: " + machine)

    if arch != 'microblazeel':
        if "fpga-overaly" in machine_features:
            print("INFO: fpga-overlay MACHINE_FEATURES is enabled, Hence PL bitstream or PDI will not be loaded at initial boot, User can load from u-boot or linux.")
        else:
            print("INFO: fpga-overlay MACHINE_FEATURES is not enabled, Hence PL bitstream or PDI will be loaded at initial boot.")

    #dictionary with all required artifacts
    data = {}

    # For MB, Zynq 7000 and ZynqMP.
    if arch == 'microblazeel' or soc == 'zynq' or soc == 'zynqmp':
        if not "fpga-overaly" in machine_features:
            data['bit'] = glob.glob(os.path.join(deploy_dir, '*' + machine + '.bit'))[0]
        data['uboot'] = os.path.join(deploy_dir, 'u-boot.elf')
        data['dtb'] = os.path.join(deploy_dir, machine + '-system.dtb')

    if soc == 'zynq' or soc == 'zynqmp':
        data['fsbl'] = os.path.join(deploy_dir, 'fsbl-' + machine + '.elf')

    if soc == 'zynqmp':
        data['atf'] = os.path.join(deploy_dir, 'arm-trusted-firmware.elf')
        data['pmufw'] = os.path.join(deploy_dir, 'pmu-firmware-' + machine + '.elf')

    if soc in ('versal', 'versal-net'):
        data['bootbin'] = os.path.join(deploy_dir, 'boot.bin')

    data['bootscr'] = os.path.join(deploy_dir, 'boot.scr')
    data['kernel'] = os.path.join(deploy_dir, kernel_img_name)

    if not args.norootfs:
        data['rfs'] = os.path.join(deploy_dir, args.image + '-' + machine + image_name_suffix + '.cpio.gz.u-boot')

    # Check if all the required boot images exists
    for key in data:
        if not os.path.isfile(data[key]):
            print('INFO:' + key + ' does not exist.')
            print('INFO: Please make sure you have run: \n\'MACHINE=' + machine + ' devtool build-image ' + args.image + '\'')
            return

    # Enable verbose mode
    if args.verbose:
        print("The following artifacts are being loaded:")
        for key in data:
            print('INFO: ' + key + ": " + data[key])

    # Start writing xsdb script
    lines = []
    lines.append('# Run \'xsdb ' + deploy_dir + '/boot-jtag.tcl\' to execute this script.')
    lines.append('connect -url ' + args.hw_server)

    if arch == 'microblazeel' or soc == 'zynq' or soc == 'zynqmp':
        lines.append('for {set i 0} {$i < 20} {incr i} {')
        lines.append('    if { [ta] != "" } break;')
        lines.append('    after 50')
        lines.append('}')
        if not "fpga-overaly" in machine_features:
            lines.append('')
            lines.append('puts stderr "INFO: Configuring the PL ..."')
            lines.append('puts stderr "INFO: Downloading bitstream: ' + data['bit'] + '"')
            lines.append('fpga -no-revision-check \"' + data['bit'] + '\"')

    if soc == 'zynqmp':
        # Disable Security gates to view PMU MB target
        lines.append('')
        lines.append('targets -set -nocase -filter {name =~ \"*PSU*\"}')

        # By default, JTAG security gates are enabled. This disables security gates for DAP, PLTAP and PMU.
        lines.append('mask_write 0xFFCA0038 0x1C0 0x1C0')
        lines.append('targets -set -nocase -filter {name =~ \"*MicroBlaze PMU*\"}')
        lines.append('')

        # Check if the target is already stopped or cannot be stopped.
        lines.append('catch {stop}; after 1000')
        lines.append('')

        # Download the pmufw.elf and run PMUFW
        lines.append('puts stderr "INFO: Downloading PMUFW ELF file: ' + data['pmufw'] + '"')
        lines.append('dow \"' + data['pmufw'] + '\"')
        lines.append('con')

        # Select A53 Core 0 to load and run FSBL
        lines.append('targets -set -nocase -filter {name =~ \"*A53*#0\"}')

        # Reset A53, If the reset is being triggered after powering on the device,
        # write bootloop at reset vector address (0xffff0000), or use
        # -clear-registers option, to avoid unpredictable behavior.
        # Further warnings will be suppressed
        lines.append('rst -processor -clear-registers')
        lines.append('')
    elif soc == 'versal':
        # Download boot.bin to versal device
        lines.append('targets -set -nocase -filter {name =~ \"*PMC*\"}')
        lines.append('puts stderr "INFO: Downloading BOOT bin file: ' + data['bootbin'] + '"')
        lines.append('device program \"' + data['bootbin'] + '\"')
        lines.append('')
        lines.append('targets -set -nocase -filter {name =~ \"*A72*#0\"}')
        lines.append('stop')
        lines.append('')
        lines.append('targets -set -nocase -filter {name =~ \"*Versal*\"}')
    elif soc == 'versal-net':
        # Download boot.bin to versal device
        lines.append('targets -set -nocase -filter {name =~ \"*PMC*\"}')
        lines.append('puts stderr "INFO: Downloading BOOT bin file: ' + data['bootbin'] + '"')
        lines.append('device program \"' + data['bootbin'] + '\"')
        lines.append('')
        lines.append('targets -set -nocase -filter {name =~ \"*A78*#0\"}')
        lines.append('stop')
        lines.append('')
        lines.append('targets -set -nocase -filter {name =~ \"*Versal*\"}')
    elif soc == 'zynq':
        lines.append('targets -set -nocase -filter {name =~ \"arm*#0\"}')
        # Check if the target is already stopped or cannot be stopped.
        lines.append('catch {stop}; after 1000')
        lines.append('')
    else:
        lines.append('targets -set -nocase -filter {name =~ \"microblaze*#0\"}')
        # Check if the target is already stopped or cannot be stopped.
        lines.append('catch {stop}; after 1000')
        lines.append('')


    if soc == 'zynq' or soc == 'zynqmp':
        # Download FSBL for Zynq 7000 and ZynqMP
        lines.append('puts stderr "INFO: Downloading FSBL ELF file: ' + data['fsbl'] + '"')
        lines.append('dow \"' + data['fsbl'] + '\"')
        lines.append('con')
        lines.append('after 4000; stop')
        lines.append('')

    # Download U-boot and DTB for MB, Zynq 7000 and ZynqMP
    if arch == 'microblazeel' or soc == 'zynq' or soc == 'zynqmp':
        lines.append('puts stderr "INFO: Downloading U-boot ELF file: ' + data['uboot'] + '"')
        lines.append('dow \"' + data['uboot'] + '\"')
        lines.append('')
        # For MB and Zynq 7000 we need to connect and stop before loading
        # kernel images
        if soc != 'zynqmp':
            lines.append('con')
            lines.append('after 1000; stop')
        lines.append('puts stderr "INFO: Downloading DTB file: ' + data['dtb'] + ' at ' + dtb_load_addr + '"')
        lines.append('dow -data \"' + data['dtb'] + '\" ' + dtb_load_addr)
        lines.append('')

    # Download Trusted Firmware-A(TF-A) for ZynqMP
    # Note: TF-A elf should be loaded after u-boot elf in JTAG boot mode else
    #       TF-A elf will not be loaded.
    if soc == 'zynqmp':
        lines.append('puts stderr "INFO: Downloading Trusted Firmware-A(TF-A) ELF file: ' + data['atf'] + '"')
        lines.append('dow \"' + data['atf'] + '\"')
        lines.append('')

    # If BOOTMODE is xen then boot till u-boot only.
    # Download Kernel Image for all architecture
    if boot_mode != 'xen':
        lines.append('puts stderr "INFO: Downloading Kernel Image file: ' + data['kernel'] + ' at ' + kernel_load_addr + '"')
        lines.append('dow -data \"' + data['kernel'] + '\" ' + kernel_load_addr)
        lines.append('')

    # Download Rootfs
    if not args.norootfs and boot_mode != 'xen':
        lines.append('puts stderr "INFO: Downloading Rootfs file: ' + data['rfs'] + ' at ' + rootfs_load_addr + '"')
        lines.append('dow -data \"' + data['rfs'] + '\" ' + rootfs_load_addr)
        lines.append('')

    lines.append('puts stderr "INFO: Downloading U-boot boot script: ' + data['bootscr'] + ' at ' + bootscr_addr + '"')
    lines.append('dow -data \"' + data['bootscr'] + '\" ' + bootscr_addr)
    lines.append('')

    # Select A72 Core 0 to load and run Versal images
    if soc == 'versal':
        lines.append('targets -set -nocase -filter {name =~ \"*A72*#0\"}')

    if soc == 'versal-net':
        lines.append('targets -set -nocase -filter {name =~ \"*A78*#0\"}')

    lines.append('con')
    lines.append('exit\n')

    script = os.path.join(deploy_dir, "boot-jtag.tcl")
    with open(script, "w") as f:
            f.write('\n'.join(lines))

    print('INFO: HW JTAG boot tcl script written to '+ script + "\n" \
        + 'INFO: User can run \'xsdb ' + script + '\' to execute.')

    return 0

def register_commands(subparsers, context):
    """Register devtool subcommands from this plugin"""
    parser_bootjtag = subparsers.add_parser('boot-jtag',
                                            help='Script to deploy target images on HW via JTAG boot mode.',
                                            description='Script to deploy target images on HW via JTAG boot mode. \
                                                Example command: MACHINE=zcu102-zynqmp devtool boot-jtag --image ${image_name} --hw_server ${hw_server}')
    required = parser_bootjtag.add_argument_group('required arguments')
    required.add_argument('--image',
                          help='Specify target image name. Example: core-image-minimal or petalinux-image-minimal')
    parser_bootjtag.add_argument('--hw_server', nargs='?', default='TCP:localhost:3121',
                                 help='URL description of hw_server/TCF agent and port number. (default: %(default)s) \
                                     Example: --hw_server TCP:puffball12:3121')

    parser_bootjtag.add_argument('-v', '--verbose',
                                 help='verbose mode', action="store_true")
    parser_bootjtag.add_argument('-n', '--norootfs',
                                 help='Don\'t include rootfs', action='store_true')
    parser_bootjtag.set_defaults(func=bootjtag, no_workspace=True)
