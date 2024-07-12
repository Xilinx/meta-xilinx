#!/usr/bin/env python3
#
# Copyright (c) 2012 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0-only
#
# AUTHORS
# Laurentiu Palcu <laurentiu.palcu@intel.com>
#

# Copyright (C) 2019-2020, Xilinx, Inc.  All rights reserved.
# Copyright (C) 2023, Advanced Micro Devices, Inc.  All rights reserved.
#
# DESCRIPTION
# Runtime-relocation wrapper scripting based on YP relocation scripting
#
# AUTHORS
# Mark Hatle <mark.hatle@amd.com>

import struct
import sys
import stat
import os
import re
import errno

if sys.version < '3':
    def b(x):
        return x
else:
    def b(x):
        return x.encode(sys.getfilesystemencoding())

old_prefix = re.compile(b("##DEFAULT_INSTALL_DIR##"))

def get_arch():
    global endian_prefix
    f.seek(0)
    e_ident =f.read(16)
    ei_mag0,ei_mag1_3,ei_class,ei_data,ei_version = struct.unpack("<B3sBBB9x", e_ident)

    # ei_data = 1 for little-endian & 0 for big-endian
    if ei_data == 1:
        endian_prefix = '<'
    else:
        endian_prefix = '>'

    if (ei_mag0 != 0x7f and ei_mag1_3 != "ELF") or ei_class == 0:
        return 0

    if ei_class == 1:
        return 32
    elif ei_class == 2:
        return 64

def parse_elf_header(f):
    global e_type, e_machine, e_version, e_entry, e_phoff, e_shoff, e_flags,\
           e_ehsize, e_phentsize, e_phnum, e_shentsize, e_shnum, e_shstrndx

    f.seek(0)
    elf_header = f.read(64)

    if arch == 32:
        # 32bit
        hdr_fmt = endian_prefix + "HHILLLIHHHHHH"
        hdr_size = 52
    else:
        # 64bit
        hdr_fmt = endian_prefix + "HHIQQQIHHHHHH"
        hdr_size = 64

    e_type, e_machine, e_version, e_entry, e_phoff, e_shoff, e_flags,\
    e_ehsize, e_phentsize, e_phnum, e_shentsize, e_shnum, e_shstrndx =\
        struct.unpack(hdr_fmt, elf_header[16:hdr_size])

def is_elf_executable(f):
    global interp

    rc = False

    if arch == 32:
        ph_fmt = endian_prefix + "IIIIIIII"
    else:
        ph_fmt = endian_prefix + "IIQQQQQQ"

    """ look for PT_INTERP section """
    for i in range(0,e_phnum):
        f.seek(e_phoff + i * e_phentsize)
        ph_hdr = f.read(e_phentsize)
        if arch == 32:
            # 32bit
            p_type, p_offset, p_vaddr, p_paddr, p_filesz,\
                p_memsz, p_flags, p_align = struct.unpack(ph_fmt, ph_hdr)
        else:
            # 64bit
            p_type, p_flags, p_offset, p_vaddr, p_paddr, \
            p_filesz, p_memsz, p_align = struct.unpack(ph_fmt, ph_hdr)

        """ change interpreter """
        if p_type == 3:
            # PT_INTERP section
            f.seek(p_offset)
            # External SDKs with mixed pre-compiled binaries should not get
            # relocated so look for some variant of /lib
            fname = f.read(11)
            if fname.startswith(b("/lib/")) or fname.startswith(b("/lib64/")) or \
               fname.startswith(b("/lib32/")) or fname.startswith(b("/usr/lib32/")) or \
               fname.startswith(b("/usr/lib32/")) or fname.startswith(b("/usr/lib64/")):
                break
            if p_filesz == 0:
                break
            rc = True
            # Store the interpretor name to global interp
            f.seek(p_offset)
            chars = []
            while True:
                c = f.read(1)
                if c == b'\x00':
                    interp = (b''.join(chars)).decode('utf-8')
                    break
                chars.append(c)
            break

    return rc

# MAIN
if len(sys.argv) < 2:
    print('%s: <path>' % sys.argv[0])
    sys.exit(-1)

# In python > 3, strings may also contain Unicode characters. So, convert
# them to bytes
if sys.version_info < (3,):
    process_path = sys.argv[1]
else:
    process_path = sys.argv[1]

process_path = os.path.realpath(process_path)

for root, _, files in os.walk(process_path):
    for file in files:
        if file.endswith('.real'):
            continue

        e = os.path.join(root, file)

        if not os.path.isfile(e) or not os.access(e, os.X_OK) or os.path.islink(e):
            continue

        if os.path.dirname(e).endswith('/lib') and (os.path.basename(e).startswith('libc-') or os.path.basename(e).startswith('libc.so')):
            # Special case, don't wrap this...
            continue

        if os.path.dirname(e).endswith('/lib') and os.path.basename(e).startswith('libpthread-'):
            # Special case, don't wrap this...
            continue

        perms = os.stat(e)[stat.ST_MODE]
        if os.access(e, os.R_OK):
            perms = None
        else:
            os.chmod(e, perms|stat.S_IRWXU)

        try:
            f = open(e, "r+b")
        except IOError:
            exctype, ioex = sys.exc_info()[:2]
            if ioex.errno == errno.ETXTBSY:
                print("Could not open %s. File used by another process.\nPlease "\
                      "make sure you exit all processes that might use any SDK "\
                      "binaries." % e)
            else:
                print("Could not open %s: %s(%d)" % (e, ioex.strerror, ioex.errno))
            sys.exit(-1)

        # Save old size and do a size check at the end. Just a safety measure.
        old_size = os.path.getsize(e)
        if old_size >= 64:
            arch = get_arch()
            if arch:
                parse_elf_header(f)

                if is_elf_executable(f):
                    dirpath = os.path.dirname(e)
                    destfile = os.path.join(dirpath, file + '.real')

                    wrapper = os.path.join(dirpath, 'execwrapper.sh')

                    if not os.path.exists(wrapper):
                        #print('write %s' % wrapper)
                        with open(wrapper, "w+") as wrapperf:
                            ldso = os.path.basename(interp)
                            libbasepath = os.path.dirname(interp) # should be /lib
                            libdirname = os.path.basename(libbasepath) # lib or lib32 or lib64 or ....
                            basepath = os.path.dirname(libbasepath) # should be /

                            libpath = os.path.join(basepath, 'usr', libdirname)

                            # Generate relative names to the path of the execwrapper
                            libbasepath = os.path.relpath(libbasepath, dirpath)
                            libpath = os.path.relpath(libpath, dirpath)

                            print('')
                            print('wrapper: %s' % wrapper)
                            print('ldso       = %s' % ldso)
                            print('lib        = %s' % libdirname)
                            print('libpath    = %s' % libbasepath)
                            print('usrlibpath = %s' % libpath)
                            print('')

                            wrapperf.write('#!/bin/bash\n')
                            wrapperf.write('# Written by Mark Hatle <mhatle@xilinx.com>\n')
                            wrapperf.write('# Copyright (C) 2019-2020, Xilinx, Inc.  All rights reserved\n')
                            wrapperf.write('# Copyright (C) 2023, Advanced Micro Devices, Inc.  All rights reserved\n')
                            wrapperf.write('#\n')
                            wrapperf.write('# SPDX-License-Identifier: GPL-2.0-only\n')
                            wrapperf.write('LDSO=%s\n' % ldso)
                            wrapperf.write('LIBBASEPATH=%s\n' % libbasepath)
                            wrapperf.write('LIBPATH=%s\n' % libpath)
                            wrapperf.write('executable=$(basename $0)\n')
                            wrapperf.write('wrapper=$0\n')
                            wrapperf.write('BASEPATH=$(dirname ${wrapper})\n')
                            wrapperf.write('if [ ! -x $0 ]; then\n')
                            wrapperf.write(' wrapper=$(which $0)\n')
                            wrapperf.write('fi\n')
                            wrapperf.write('if [ -h $0 ]; then\n')
                            wrapperf.write(' executable=$(basename "$(readlink $0)" )\n')
                            wrapperf.write(' BASEPATH=$(dirname "$(realpath $0)")\n')
                            wrapperf.write('fi\n')
                            wrapperf.write('LIBBASEPATH=$(realpath ${BASEPATH}/${LIBBASEPATH})\n')
                            wrapperf.write('LIBPATH=$(realpath ${BASEPATH}/${LIBPATH})\n')
                            wrapperf.write('export COLLECT_GCC=${COLLECT_GCC%%.real}\n')
                            wrapperf.write('exec ${LIBBASEPATH}/${LDSO} --library-path ${LIBPATH}:${LIBBASEPATH} ${BASEPATH}/${executable}.real $@\n')
                        #print('chmod %s 0775' % wrapper)
                        os.chmod(wrapper, 0o775)

                    print('%s -> %s' % (e, destfile))
                    #print('mv %s %s' % (e, destfile))
                    os.rename(e, destfile)
                    #print('ln %s %s' % (wrapper, e))
                    os.link(wrapper, e)

        """ change permissions back """
        if perms:
            os.chmod(e, perms)

        f.close()
