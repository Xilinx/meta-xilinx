# SPDX-License-Identifier: MIT
#
# bootgen-bif.bbclass - Generate BIF files for AMD/Xilinx bootgen tool
#
# Generates Boot Image Format (BIF) files for bootgen to create BOOT.bin/PDI.
# See AMD UG1283 "Bootgen User Guide" for the complete BIF specification.
#
# Supported: zynq, zynqmp, versal, versal-net, versal-2ve-2vm
#
# Variables (used as defaults when not passed as parameters):
#   BIF_FILE_PATH          - Output path for generated BIF file
#   BIF_TOPLEVEL_ATTR      - Space-separated top-level attribute names
#   BIF_TOPLEVEL_ATTR[a]   - Value for top-level attribute 'a' (emitted as: a = value)
#   BIF_COMMON_ATTR        - Space-separated common attribute names
#   BIF_COMMON_ATTR[name]  - Flags for common attribute
#   BIF_OPTIONAL_DATA      - Semicolon-separated: "<filepath>, id=<id>;"
#   BIF_PARTITION_ATTR     - Space-separated partition names (boot order)
#   BIF_PARTITION_ATTR[p]  - Attribute flags for partition 'p'
#   BIF_PARTITION_IMAGE[p] - File path for partition 'p'
#   BIF_PARTITION_ID[p]    - Subsystem ID for partition 'p' (Versal only)
#   BIF_PARTITION_NAME[id] - Subsystem name for ID (Versal only)
#
# Note:
# - All partition and optional-data files are copied to workdir by basename.
# - Two files with the same basename (e.g., /a/fw.elf and /b/fw.elf) will fail
#   with a collision error.
# - Empty partition files are skipped with a warning.
# - Use skip_check parameter for files that don't exist yet (e.g., rootfs).
#
# Map SOC_FAMILY to bootgen -arch argument
# Most map directly, some need hyphen adjustments for bootgen
BOOTGEN_ARCH = ""
BOOTGEN_ARCH:zynq = "zynq"
BOOTGEN_ARCH:zynqmp = "zynqmp"
BOOTGEN_ARCH:versal = "versal"
BOOTGEN_ARCH:versal-net = "versalnet"
BOOTGEN_ARCH:versal-2ve-2vm = "versal_2ve_2vm"


def bootgen_bif_write_toplevel_attrs(biffd, d, toplevel_attrs=None):
    """
    Write bare key = value top-level BIF attributes.

    These are scalar attributes like id_code, extended_id_code, and id that
    appear at the top of the BIF outside any image block. They are needed for
    standalone images that don't include a base-pdi partition (which normally
    provides these values to bootgen automatically).
    """
    if toplevel_attrs is None:
        toplevel_attrs = (d.getVar("BIF_TOPLEVEL_ATTR") or "").split()
    if not toplevel_attrs:
        return

    toplevel_flags = d.getVarFlags("BIF_TOPLEVEL_ATTR") or {}
    for name in toplevel_attrs:
        if name not in toplevel_flags:
            bb.fatal("BIF_TOPLEVEL_ATTR[%s] not defined, but '%s' is listed "
                     "in BIF_TOPLEVEL_ATTR" % (name, name))
        value = d.expand(toplevel_flags[name])
        biffd.write("\t%s = %s\n" % (name, value))

def bootgen_bif_create_zynq(common_attrs, partitions, local_files, biffd, d):
    """Generate BIF content for Zynq/ZynqMP (flat partition list)."""

    common_attrflags = d.getVarFlags("BIF_COMMON_ATTR") or {}
    partition_attrflags = d.getVarFlags("BIF_PARTITION_ATTR") or {}

    for name in common_attrs:
        if name not in common_attrflags:
            bb.fatal("BIF_COMMON_ATTR[%s] not defined, but '%s' is listed in BIF_COMMON_ATTR" % (name, name))
        flags = d.expand(common_attrflags[name])
        flag_list = [f.strip() for f in flags.split(',') if f.strip()]
        biffd.write("\t [%s] %s\n" % (name, ', '.join(flag_list)))

    # Process partitions that were successfully copied (uses local basenames)
    for name in partitions:
        if name not in local_files:
            continue
        filepath = local_files[name]
        flags = d.expand(partition_attrflags.get(name, ''))
        if flags:
            flag_list = [f.strip() for f in flags.split(',') if f.strip()]
            biffd.write("\t [%s] %s\n" % (', '.join(flag_list), filepath))
        else:
            biffd.write("\t %s\n" % filepath)

def bootgen_bif_create_versal(common_attrs, partitions, local_files, biffd, d):
    """
    Generate BIF content for Versal (partitions grouped into image blocks by ID).

    Partitions with the same BIF_PARTITION_ID are grouped together.
    ID '0' is the default anonymous image; non-zero IDs get explicit id/name lines.
    """

    common_attrflags = d.getVarFlags("BIF_COMMON_ATTR") or {}
    partition_attrflags = d.getVarFlags("BIF_PARTITION_ATTR") or {}
    partition_ids = d.getVarFlags("BIF_PARTITION_ID") or {}

    for name in common_attrs:
        if name not in common_attrflags:
            bb.fatal("BIF_COMMON_ATTR[%s] not defined, but '%s' is listed in BIF_COMMON_ATTR" % (name, name))
        flags = d.expand(common_attrflags[name])
        flag_list = [f.strip() for f in flags.split(',') if f.strip()]
        biffd.write("\t { %s %s }\n" % (name, ', '.join(flag_list)))

    # Group partitions by ID, preserving order of first occurrence
    id_partitions = []  # [(id, [entries]), ...]
    id_seen = {}        # id -> index in id_partitions

    for name in partitions:
        # Versal requires flags for all partitions
        flags = partition_attrflags.get(name, '')
        if not flags:
            bb.note("Partition '%s' skipped: no BIF_PARTITION_ATTR[%s] flags defined" % (name, name))
            continue

        # Skip partitions that weren't copied (empty files)
        if name not in local_files:
            continue

        filepath = local_files[name]
        flags = d.expand(flags)

        partition_id = d.expand(partition_ids.get(name, '0'))
        flag_list = [f.strip() for f in flags.split(',') if f.strip()]
        partition_entry = "\t { %s, file=%s }\n" % (', '.join(flag_list), filepath)

        if partition_id in id_seen:
            id_partitions[id_seen[partition_id]][1].append(partition_entry)
        else:
            id_seen[partition_id] = len(id_partitions)
            id_partitions.append((partition_id, [partition_entry]))

    # Write image blocks
    for partition_id, entries in id_partitions:
        biffd.write("\timage {\n")
        if partition_id != '0':
            subsys_name = d.getVarFlag("BIF_PARTITION_NAME", partition_id) or "apu_ss"
            biffd.write("\t id = %s, name=%s\n" % (partition_id, subsys_name))
        for entry in entries:
            biffd.write(entry)
        biffd.write("\t}\n")

def bootgen_bif_write_optional_data(biffd, optional_data, workdir, seen_basenames, d):
    """
    Process BIF_OPTIONAL_DATA entries and copy files to workdir.

    Format: "<filepath>, id=<id>;" (semicolon-separated)
    Common IDs: 0x21=version string, 0x22=user data, 0x23=PDI unique ID

    Updates seen_basenames dict to track copied files (shared with partition copy).
    """

    import os
    import shutil

    for entry in optional_data.split(';'):
        entry = entry.strip()
        if not entry:
            continue

        try:
            filepath, id_part = entry.split(',', 1)
            filepath = d.expand(filepath.strip())
            id_part = id_part.strip()
        except ValueError:
            bb.fatal('BIF_OPTIONAL_DATA entry "%s" invalid, expected: <filepath>, id=<id>' % entry)

        if not os.path.exists(filepath):
            bb.warn('BIF_OPTIONAL_DATA file does not exist, skipping: %s' % filepath)
            continue

        basename = os.path.basename(filepath)
        if basename in seen_basenames:
            bb.fatal("Basename collision: '%s' from BIF_OPTIONAL_DATA entry '%s' "
                     "would overwrite file from %s" % (basename, entry, seen_basenames[basename]))
        seen_basenames[basename] = "BIF_OPTIONAL_DATA entry '%s'" % entry

        dest = os.path.join(workdir, basename)
        if filepath != dest:
            shutil.copyfile(filepath, dest)
        biffd.write("\toptionaldata { %s, %s }\n" % (basename, id_part))

def bootgen_bif_copy_partition_files(partitions, partition_attrimage, workdir, seen_basenames, d, skip_check=None):
    """
    Copy partition files to workdir for bootgen.

    Returns dict mapping partition name to the path to use in the BIF file
    (basename for copied files, full path for skip_check files).
    Updates seen_basenames dict (shared with optional-data).
    Empty files are skipped with a warning.
    Raises bb.fatal on basename collision or missing files.

    Args:
        skip_check: List of partition names to skip file existence check and copy.
                    These files use full paths in the BIF (for files built later).
    """
    skip_check = skip_check or []

    import os
    import shutil

    local_files = {}

    for name in partitions:
        if name not in partition_attrimage:
            bb.fatal("BIF_PARTITION_IMAGE[%s] not defined, but '%s' is listed in BIF_PARTITION_ATTR" % (name, name))

        filepath = d.expand(partition_attrimage[name])
        basename = os.path.basename(filepath)

        # Skip file checks for partitions in skip_check (e.g., rootfs built later)
        if name in skip_check:
            # Use full path in BIF (file will exist when bootgen runs)
            if basename in seen_basenames:
                bb.fatal("Basename collision: '%s' from BIF_PARTITION_IMAGE[%s] (%s) "
                         "would overwrite file from %s" %
                         (basename, name, filepath, seen_basenames[basename]))
            seen_basenames[basename] = "BIF_PARTITION_IMAGE[%s]" % name
            local_files[name] = filepath  # full path since file is not copied (skip_check)
            continue

        if not os.path.exists(filepath):
            bb.fatal("Partition file does not exist: %s" % filepath)

        # Skip empty files with warning
        if os.stat(filepath).st_size == 0:
            bb.warn("Empty file %s, excluding from BIF" % filepath)
            continue

        if basename in seen_basenames:
            bb.fatal("Basename collision: '%s' from BIF_PARTITION_IMAGE[%s] (%s) "
                     "would overwrite file from %s" %
                     (basename, name, filepath, seen_basenames[basename]))
        seen_basenames[basename] = "BIF_PARTITION_IMAGE[%s]" % name

        dest = os.path.join(workdir, basename)
        bb.note("Copying BIF_PARTITION_IMAGE[%s]: %s -> %s" % (name, filepath, dest))
        if filepath != dest:
            shutil.copyfile(filepath, dest)
        local_files[name] = basename  # basename since file is copied to workdir

    return local_files

def bootgen_bif_generate(d, bif_path=None, workdir=None, partitions=None, optional_data=None, skip_check=None):
    """
    Generate a complete BIF file, copying partition files to workdir.

    Main entry point for BIF generation. Copies all partition files to workdir
    and generates arch-specific BIF content using local basenames.

    Args:
        d: BitBake datastore
        bif_path: Output BIF file path (default: BIF_FILE_PATH variable)
        workdir: Directory to copy partition files to (default: B variable)
        partitions: List of partition names (default: BIF_PARTITION_ATTR variable)
        optional_data: Semicolon-separated optional data entries
                       (default: BIF_OPTIONAL_DATA variable)
        skip_check: Partition names to skip file existence check
                    (for files built later, e.g., rootfs)
    """
    # Apply defaults from datastore
    if bif_path is None:
        bif_path = d.getVar('BIF_FILE_PATH')
    if workdir is None:
        workdir = d.getVar('B')

    soc_family = d.getVar("SOC_FAMILY") or ""

    # Shared basename tracker to detect collisions between optional-data and partitions
    seen_basenames = {}  # basename -> description string for error messages

    with open(bif_path, 'w') as biffd:
        biffd.write("the_ROM_image:\n")
        biffd.write("{\n")

        bootgen_bif_write_toplevel_attrs(biffd, d)

        # Optional data (Versal version strings, user data) - use parameter or variable
        opt_data = optional_data if optional_data is not None else (d.getVar("BIF_OPTIONAL_DATA") or "")
        if opt_data:
            bootgen_bif_write_optional_data(biffd, opt_data, workdir, seen_basenames, d)

        common_attrs = (d.getVar("BIF_COMMON_ATTR") or "").split()

        # Partitions - use parameter or variable
        part_list = partitions if partitions is not None else (d.getVar("BIF_PARTITION_ATTR") or "").split()

        # Copy files and get list of valid (non-empty) partitions
        partition_attrimage = d.getVarFlags("BIF_PARTITION_IMAGE") or {}
        local_files = bootgen_bif_copy_partition_files(part_list, partition_attrimage, workdir,
                                                       seen_basenames, d, skip_check=skip_check)

        if not local_files:
            bb.fatal("No valid partition files found - cannot generate BIF")

        if soc_family in ('zynq', 'zynqmp'):
            bootgen_bif_create_zynq(common_attrs, part_list, local_files, biffd, d)
        elif soc_family in ('versal', 'versal-net', 'versal-2ve-2vm'):
            bootgen_bif_create_versal(common_attrs, part_list, local_files, biffd, d)
        else:
            bb.fatal("bootgen_bif_generate: unsupported SOC_FAMILY '%s'" % soc_family)

        biffd.write("}\n")
