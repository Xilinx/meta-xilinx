#!/usr/bin/env python3
#
# SPDX-License-Identifier: MIT
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

"""
Generates updated-fpga.bit or updated-fpga.rcdo using updatemem
This script takes a FPGA (.bit) or RCDO (.rcdo) file without any elf
and adds an elf file to it. fs-boot.elf or u-boot-spl.elf or zephyr.elf is
embedded into the FPGA.

Once the updated FPGA file is generated, user can package u-boot, kernel, and
rootfs and generate an mcs or bin file. It can be used for flash programming
using Vivado's write_cfgmem command. The script can also generate a TCL script
to create a SPI flash image (e.g., .mcs or .bin) from the updated FPGA file,
which can then be programmed to SPI flash using Vivado or other tools.
"""

import os
import sys
import subprocess
import argparse
from pathlib import Path
import shutil


class UpdatememNotFoundError(Exception):
    """Exception raised when updatemem tool is not found"""
    pass


def check_updatemem_available():
    """Check if updatemem tool is available in PATH"""
    if shutil.which('updatemem') is None:
        raise UpdatememNotFoundError(
            "updatemem tool not found in PATH. "
            "Make sure Vivado tools are installed and sourced. "
            "Try running: source /path/to/Vivado/settings64.sh"
        )


def read_instpath_from_mmi(mmi_file, verbose=False):
    """
    Read InstPath value from MMI file
    Returns the InstPath value or None if not found
    
    Args:
        mmi_file: Path to the MMI file
        verbose: Enable verbose output
        
    Returns:
        str: InstPath value if found, None otherwise
    """
    import re
    
    try:
        with open(mmi_file, 'r') as f:
            content = f.read()
        
        # Search for InstPath= pattern in the MMI file
        # Pattern matches: InstPath="some/path" or InstPath='some/path'
        pattern = r'InstPath\s*=\s*["\']([^"\']+)["\']'
        match = re.search(pattern, content, re.IGNORECASE)
        
        if match:
            instpath = match.group(1)
            if verbose:
                print(f"Auto-detected InstPath from MMI: {instpath}")
            return instpath
        else:
            if verbose:
                print(f"WARNING: InstPath not found in MMI file: {mmi_file}")
            return None
            
    except FileNotFoundError:
        print(f"ERROR: MMI file not found: {mmi_file}", file=sys.stderr)
        return None
    except Exception as e:
        print(f"ERROR: Error reading MMI file {mmi_file}: {e}", file=sys.stderr)
        return None


def preprocess_argv(argv):
    """
    Pre-process argv to convert the --offset companion syntax into nargs=2 pairs.

    Converts:
      --u-boot <path> --offset <val>        →  --u-boot <path> <val>
      --boot-script <path> --offset <val>   →  --boot-script <path> <val>
      --kernel <path> --offset <val>        →  --kernel <path> <val>
      --rootfs <path> --offset <val>        →  --rootfs <path> <val>

    This lets argparse nargs=2 pick up (path, offset) as a pair while the
    command-line syntax uses a shared --offset flag after each file argument.
    """
    SPI_FILE_ARGS = {'--u-boot', '--boot-script', '--kernel', '--rootfs'}
    result = []
    i = 0
    pending_file_arg = False  # True after consuming a path for a SPI file arg

    while i < len(argv):
        arg = argv[i]
        if arg in SPI_FILE_ARGS:
            result.append(arg)
            i += 1
            # Consume the path value (next token that is not a flag)
            if i < len(argv) and not argv[i].startswith('-'):
                result.append(argv[i])
                i += 1
                pending_file_arg = True
            else:
                pending_file_arg = False
        elif arg == '--offset' and pending_file_arg:
            # Inject the offset value immediately after the path
            i += 1
            if i < len(argv):
                result.append(argv[i])
                i += 1
            pending_file_arg = False
        else:
            pending_file_arg = False
            result.append(arg)
            i += 1

    return result


def copy_artifacts_dir(src_dir, verbose=False):
    """
    Copy *src_dir* into the current working directory with a '-tmp' suffix.

    The destination is: <cwd>/<basename-of-src_dir>-tmp
    If the destination already exists it is removed first so the copy is fresh.

    Args:
        src_dir: Absolute path to the source artifacts directory
        verbose: Enable verbose output

    Returns:
        str: Absolute path to the newly created copy, or None on failure
    """
    src_path = Path(src_dir)
    dest_path = Path(os.getcwd()) / f"tmp-{src_path.name}"

    try:
        if dest_path.exists():
            if verbose:
                print(f"Removing existing temp directory: {dest_path}")
            shutil.rmtree(dest_path)

        shutil.copytree(src_path, dest_path)

        if verbose:
            print(f"Copied artifacts to: {dest_path}")

        return str(dest_path)
    except Exception as e:
        print(f"ERROR: Failed to copy artifacts directory: {e}", file=sys.stderr)
        return None


def find_file_by_extension(search_dir, extensions, verbose=False):
    """
    Search *search_dir* recursively for the first file matching one of the
    given *extensions* (e.g. ['.mmi'] or ['.bit', '.rcdo']).

    Args:
        search_dir: Directory to search in
        extensions:  List of lower-case extensions including the dot
        verbose:     Enable verbose output

    Returns:
        str: Absolute path of the first matching file, or None if not found
    """
    search_path = Path(search_dir)
    if not search_path.is_dir():
        print(f"ERROR: Artifacts directory not found: {search_dir}", file=sys.stderr)
        return None

    matches = [
        p for p in sorted(search_path.rglob('*'))
        if p.is_file() and p.suffix.lower() in extensions
    ]

    if not matches:
        print(
            f"ERROR: No {'/'.join(extensions)} file found under: {search_dir}",
            file=sys.stderr,
        )
        return None

    if len(matches) > 1:
        print(
            f"WARNING: Multiple {'/'.join(extensions)} files found under {search_dir}; "
            f"using the first one: {matches[0]}",
            file=sys.stderr,
        )

    if verbose:
        print(f"Auto-detected {matches[0].suffix} file: {matches[0]}")

    return str(matches[0])


def parse_arguments():
    """Parse command line arguments"""
    parser = argparse.ArgumentParser(
        description='Generate updated-fpga.bit or updated-fpga.rcdo using updatemem'
    )
    parser.add_argument(
        '-s', '--sdt-artifacts-dir',
        required=True,
        help='Directory containing system-dt artifacts. The script will '
             'automatically search for a .mmi file and a .bit or .rcdo file.'
    )
    parser.add_argument(
        '-d', '--data-file',
        required=True,
        help='Path to ELF file to embed (e.g., fs-boot.elf, u-boot-spl.elf, zephyr.elf, etc.)'
    )
    parser.add_argument(
        '-o', '--output-directory',
        default=None,
        help='Output directory to deploy the generated FPGA. Defaults to current working directory.'
    )
    parser.add_argument(
        '-f', '--output-filename',
        default=None,
        help='Output filename for the generated FPGA file (e.g., my-fpga.bit). '
             'Defaults to updated-fpga.bit or updated-fpga.rcdo based on input file type.'
    )
    parser.add_argument(
        '-v', '--verbose',
        action='store_true',
        help='Enable verbose output'
    )
    parser.add_argument(
        '--spi-image-format',
        default=None,
        help='Image format for the output FPGA file (e.g., BIN, MCS)'
    )
    parser.add_argument(
        '--spi-interface',
        default=None,
        help='SPI interface type for flash programming (e.g., SPIx1, SPIx2, SPIx4, SPIx8)'
    )
    parser.add_argument(
        '--spi-size',
        default=None,
        help='SPI flash size in MB (e.g., 16, 32, 64, 128)'
    )
    parser.add_argument(
        '--u-boot',
        nargs=2,
        default=None,
        metavar=('<u-boot.bin>', '<offset>'),
        help='Path to U-Boot binary followed by --offset <hex-offset> '
             '(e.g., --u-boot u-boot.bin --offset 0xa00000)'
    )
    parser.add_argument(
        '--boot-script',
        nargs=2,
        default=None,
        metavar=('<boot.scr>', '<offset>'),
        help='Path to U-Boot boot script followed by --offset <hex-offset> '
             '(e.g., --boot-script boot.scr --offset 0xc00000)'
    )
    parser.add_argument(
        '--kernel',
        nargs=2,
        default=None,
        metavar=('<Image>', '<offset>'),
        help='Path to kernel image followed by --offset <hex-offset> '
             '(e.g., --kernel Image --offset 0xF80000)'
    )
    parser.add_argument(
        '--rootfs',
        nargs=2,
        default=None,
        metavar=('<rootfs.cpio.gz.u-boot>', '<offset>'),
        help='Path to root filesystem image followed by --offset <hex-offset> '
             '(e.g., --rootfs rootfs.cpio.gz.u-boot --offset 0x3180000)'
    )
    parser.add_argument(
        '--offset',
        default=None,
        metavar='<hex-offset>',
        help='Flash offset for the preceding --u-boot / --boot-script / --kernel / --rootfs argument. '
             'Specify once after each file argument '
             '(e.g., --u-boot u-boot.bin --offset 0xa00000 --boot-script boot.scr --offset 0x1F80000)'
    )

    return parser.parse_args(preprocess_argv(sys.argv[1:]))


def validate_inputs(mmi_file, bit_file, data_file):
    """Validate that required input files exist and have correct extensions"""
    errors = []
    
    if not os.path.exists(mmi_file):
        errors.append(f"MMI file not found: {mmi_file}")
    
    if not os.path.exists(bit_file):
        errors.append(f"FPGA .bit or .rcdo file not found: {bit_file}")
    else:
        # Check if file has valid extension
        _, ext = os.path.splitext(bit_file)
        if ext.lower() not in ['.bit', '.rcdo']:
            errors.append(f"FPGA file must have .bit or .rcdo extension: {bit_file}")
    
    if not os.path.exists(data_file):
        errors.append(f"Data file not found: {data_file}")
    else:
        # Check if file has valid extension
        _, ext = os.path.splitext(data_file)
        if ext.lower() != '.elf':
            errors.append(f"Data file must have .elf extension: {data_file}")
    
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return False
    
    return True


def configure(output_directory, mmi_file, bit_file, data_file, processor, output, verbose):
    """Configure the build (equivalent to do_configure)"""
    if verbose:
        print(f"Configuring build in {output_directory}")
    
    # Create output directory if it doesn't exist
    Path(output_directory).mkdir(parents=True, exist_ok=True)
    
    # Create updatemem.conf
    conf_file = os.path.join(output_directory, 'updatemem.conf')
    conf_content = f"MMI={mmi_file} BIT={bit_file} DATA={data_file} PROCESSOR={processor} OUT={output}"
    
    try:
        with open(conf_file, 'w') as f:
            f.write(conf_content)
        
        if verbose:
            print(f"Created configuration file: {conf_file}")
            print(f"Content: {conf_content}")
        
        return True
    except Exception as e:
        print(f"ERROR: Failed to create updatemem.conf: {e}", file=sys.stderr)
        return False


def compile_bitstream(mmi_file, bit_file, data_file, processor, output, verbose):
    """Compile the bitstream (equivalent to do_compile)"""
    if verbose:
        print("Compiling bitstream with updatemem")
    
    # Build updatemem command
    cmd = [
        'updatemem',
        '-force',
        '-meminfo', mmi_file,
        '-bit', bit_file,
        '-data', data_file,
        '-proc', processor,
        '-out', output
    ]
    
    if verbose:
        print(f"Running command: {' '.join(cmd)}")
    
    try:
        # Run updatemem
        result = subprocess.run(
            cmd,
            capture_output=True,
            text=True,
            check=True
        )
        
        if verbose and result.stdout:
            print("STDOUT:", result.stdout)
        
        if result.stderr:
            print("STDERR:", result.stderr, file=sys.stderr)
        
        # Check if output file was created
        if not os.path.exists(output):
            print(f"ERROR: Output file not created: {output}", file=sys.stderr)
            return False
        
        if verbose:
            print(f"Successfully created: {output}")
        
        return True
        
    except subprocess.CalledProcessError as e:
        print(f"ERROR: updatemem failed with return code {e.returncode}", file=sys.stderr)
        if e.stdout:
            print(f"STDOUT: {e.stdout}", file=sys.stderr)
        if e.stderr:
            print(f"STDERR: {e.stderr}", file=sys.stderr)
        return False
    except FileNotFoundError:
        raise UpdatememNotFoundError(
            "updatemem command not found. "
            "Make sure Vivado tools are in PATH"
        )


def copy_rcdo_to_pdi_files(output_rcdo, original_rcdo, work_dir, verbose=False):
    """
    If *work_dir* contains a .pdi file (anywhere in the tree), replace the
    original .rcdo (*original_rcdo* / bit_file) in-place with *output_rcdo*,
    keeping the original filename.

    Args:
        output_rcdo:   Absolute path to the newly generated .rcdo file
        original_rcdo: Absolute path to the original .rcdo inside the tmp
                       artifacts directory (bit_file)
        work_dir:      Path to the tmp artifacts directory (searched for .pdi)
        verbose:       Enable verbose output

    Returns:
        str: Destination path if replaced, None if no .pdi found or on error
    """
    pdi_files = list(Path(work_dir).rglob('*.pdi'))
    if not pdi_files:
        if verbose:
            print(f"No .pdi file found under {work_dir}; skipping rcdo replacement.")
        return None

    if verbose:
        print(f"Found .pdi file: {pdi_files[0]}; proceeding with rcdo replacement.")

    dest = Path(original_rcdo)

    try:
        shutil.copy2(output_rcdo, dest)
        print(f"Updating original rcdo: {dest}")
        print(f"  Original rcdo '{dest.name}' has been updated with '{Path(output_rcdo).name}'")
        if verbose:
            print(f"  Source : {output_rcdo}")
            print(f"  Dest   : {dest}")
        return str(dest)
    except Exception as e:
        print(f"ERROR: Failed to replace original rcdo: {e}", file=sys.stderr)
        return None


def run_bootgen(pdi_files_dir, output_directory, verbose=False):
    """
    Run bootgen from *pdi_files_dir* using the .bif file found there to
    generate an updated .pdi in *output_directory*.

    Equivalent to:
        cd <pdi_files_dir>
        bootgen -arch spartanup -image <name>.bif -w -o <output_directory>/updated-fpga.pdi

    Args:
        pdi_files_dir:    Directory containing the .bif file (and updated .rcdo)
        output_directory: Directory where updated-fpga.pdi will be written
        verbose:          Enable verbose output

    Returns:
        str: Path to the generated .pdi file, or None on failure
    """
    bif_files = list(Path(pdi_files_dir).glob('*.bif'))
    if not bif_files:
        print(f"ERROR: No .bif file found in {pdi_files_dir}", file=sys.stderr)
        return None

    if len(bif_files) > 1:
        print(
            f"WARNING: Multiple .bif files found in {pdi_files_dir}; "
            f"using: {bif_files[0]}",
            file=sys.stderr,
        )

    bif_file = bif_files[0].name
    output_pdi = os.path.join(output_directory, 'updated-fpga.pdi')

    cmd = [
        'bootgen',
        '-arch', 'spartanup',
        '-image', bif_file,
        '-w',
        '-o', output_pdi,
    ]

    if verbose:
        print(f"Running bootgen in: {pdi_files_dir}")
        print(f"Command: {' '.join(cmd)}")

    try:
        result = subprocess.run(
            cmd,
            cwd=str(pdi_files_dir),
            capture_output=True,
            text=True,
            check=True,
        )

        if verbose and result.stdout:
            print("bootgen STDOUT:", result.stdout)
        if result.stderr:
            print("bootgen STDERR:", result.stderr, file=sys.stderr)

        if not os.path.exists(output_pdi):
            print(f"ERROR: bootgen did not create output: {output_pdi}", file=sys.stderr)
            return None

        print(f"Successfully generated PDI: {output_pdi}")
        return output_pdi

    except subprocess.CalledProcessError as e:
        print(f"ERROR: bootgen failed with return code {e.returncode}", file=sys.stderr)
        if e.stdout:
            print(f"STDOUT: {e.stdout}", file=sys.stderr)
        if e.stderr:
            print(f"STDERR: {e.stderr}", file=sys.stderr)
        return None
    except FileNotFoundError:
        print(
            "ERROR: bootgen not found in PATH. "
            "Make sure Vitis/Bootgen tools are installed and sourced.",
            file=sys.stderr,
        )
        return None


def generate_spi_image_tcl(bit_file, spi_image_format, spi_interface, spi_size,
                           output_directory, verbose=False,
                           u_boot=None, boot_script=None, kernel=None, rootfs=None):
    """
    Generate a Vivado write_cfgmem TCL script for SPI flash image creation.

    Args:
        bit_file:         Path to the input .bit (updatemem output) or .pdi file 
        spi_image_format: Output image format (e.g., 'mcs', 'bin')
        spi_interface:    SPI interface type (e.g., 'SPIx1', 'SPIx4')
        spi_size:         SPI flash size in MB (e.g., '128')
        output_directory: Directory where the TCL file and output image are written
        verbose:          Enable verbose output
        u_boot:           [path, offset] for U-Boot binary, or None
        boot_script:      [path, offset] for U-Boot boot script, or None
        kernel:           [path, offset] for kernel image, or None
        rootfs:           [path, offset] for root filesystem image, or None

    Returns:
        str: Path to the generated TCL file, or None on failure
    """
    # Derive output image filename from format
    bit_basename = os.path.splitext(os.path.basename(bit_file))[0]
    output_image = os.path.join(output_directory, f'{bit_basename}.{spi_image_format.lower()}')
    tcl_file = os.path.join(output_directory, 'gen-spi-boot-image.tcl')

    # Build -loaddata entries and TCL variable definitions for optional files
    loaddata_entries = []
    tcl_var_defs = []
    for entry, var_name in [(u_boot, 'u_boot_file'), (boot_script, 'boot_script_file'), (kernel, 'kernel_file'), (rootfs, 'rootfs_file')]:
        if entry is not None:
            filepath, offset = entry[0], entry[1]
            tcl_var_defs.append(f'set {var_name} "{filepath}"')
            loaddata_entries.append(f'up {offset} ${var_name}')

    tcl_var_defs_str = '\n'.join(tcl_var_defs)
    if tcl_var_defs_str:
        tcl_var_defs_str = '\n' + tcl_var_defs_str

    loaddata_str = ''
    if loaddata_entries:
        loaddata_str = ' -loaddata "' + ' '.join(loaddata_entries) + '"'

    # Use -loadpdi for .pdi files, -loadbit for .bit/.rcdo files
    _, bit_ext = os.path.splitext(bit_file)
    load_option = "-loadpdi" if bit_ext.lower() == ".pdi" else "-loadbit"

    tcl_content = f"""# Auto-generated by update-fpga.py
# SPI flash image generation script for Vivado write_cfgmem

# Define variables
set format {spi_image_format.upper()}
set interface {spi_interface}
set size {spi_size}
set bit_file_path "{bit_file}"
set output_file_path "{output_image}"{tcl_var_defs_str}

# Run the command
write_cfgmem -force -format $format -size $size -interface $interface {load_option} "up 0x0 $bit_file_path"{loaddata_str} -file $output_file_path
"""

    try:
        with open(tcl_file, 'w') as f:
            f.write(tcl_content)
        if verbose:
            print(f"Generated SPI image TCL script: {tcl_file}")
            print(f"TCL content:\n{tcl_content}")

        # Run Vivado in batch mode to execute the TCL script
        vivado_cmd = ['vivado', '-mode', 'batch', '-source', tcl_file]
        if verbose:
            print(f"Running Vivado: {' '.join(vivado_cmd)}")
        vivado_result = subprocess.run(
            vivado_cmd,
            capture_output=True,
            text=True
        )
        if verbose and vivado_result.stdout:
            print("Vivado STDOUT:", vivado_result.stdout)
        if vivado_result.stderr:
            print("Vivado STDERR:", vivado_result.stderr, file=sys.stderr)
        if vivado_result.returncode != 0:
            print(f"ERROR: Vivado failed with return code {vivado_result.returncode}", file=sys.stderr)
            return None

        print(f"Successfully generated {spi_image_format.lower()} image: {output_image}")

        return tcl_file
    except Exception as e:
        print(f"ERROR: Failed to write TCL file: {e}", file=sys.stderr)
        return None


def main():
    """Main function"""
    args = parse_arguments()
    
    try:
        # Check if updatemem is available first
        check_updatemem_available()
        
        # Copy sdt artifacts to <cwd>/<dirname>-tmp and use the copy as input
        artifacts_dir = os.path.abspath(args.sdt_artifacts_dir)
        work_dir = copy_artifacts_dir(artifacts_dir, args.verbose)
        if work_dir is None:
            return 1
        print(f"Using artifacts copy: {work_dir}")

        detected = find_file_by_extension(work_dir, ['.mmi'], args.verbose)
        if detected is None:
            return 1
        mmi_file = os.path.abspath(detected)

        detected = find_file_by_extension(work_dir, ['.bit', '.rcdo'], args.verbose)
        if detected is None:
            return 1
        bit_file = os.path.abspath(detected)

        data_file = os.path.abspath(args.data_file)
        output_directory = os.path.abspath(args.output_directory) if args.output_directory else os.getcwd()
        
        # Determine output file extension based on input file type
        _, input_ext = os.path.splitext(bit_file)
        if args.output_filename:
            output_filename = args.output_filename
        else:
            output_filename = f'updated-fpga{input_ext}'
        output = os.path.join(output_directory, output_filename)
        
        # Auto-detect processor from MMI file
        processor = read_instpath_from_mmi(mmi_file, args.verbose)
        if processor is None:
            print("ERROR: Could not determine processor path. Ensure MMI file contains InstPath.", file=sys.stderr)
            return 1

        # Always print a summary of what the script is doing
        print("=== Running FPGA Update ===")
        print(f"Using --mmi-file {mmi_file} as input")
        print(f"Using --bit-file {bit_file} as input")
        print(f"Adding --data-file {data_file} to {output_filename}")
        print(f"Using --output-directory {output_directory}")
        print(f"Auto-detected --processor {processor} from MMI file")
        if args.spi_image_format:
            print(f"Using --spi-image-format {args.spi_image_format}")
            print(f"Using --spi-interface {args.spi_interface}")
            print(f"Using --spi-size {args.spi_size}MB")
        if args.u_boot:
            print(f"Adding --u-boot {os.path.abspath(args.u_boot[0])} at offset {args.u_boot[1]}")
        if args.boot_script:
            print(f"Adding --boot-script {os.path.abspath(args.boot_script[0])} at offset {args.boot_script[1]}")
        if args.kernel:
            print(f"Adding --kernel {os.path.abspath(args.kernel[0])} at offset {args.kernel[1]}")
        if args.rootfs:
            print(f"Adding --rootfs {os.path.abspath(args.rootfs[0])} at offset {args.rootfs[1]}")
        print()

        if args.verbose:
            print("=== Bitstream Generation Script ===")
            print(f"MMI File: {mmi_file}")
            print(f"Bitstream File: {bit_file}")
            print(f"Data File: {data_file}")
            print(f"Processor (auto-detected): {processor}")
            print(f"Output Directory: {output_directory}")
            print(f"Output File: {output}")
            if args.spi_image_format:
                print(f"SPI Image Format: {args.spi_image_format}")
                print(f"SPI Interface: {args.spi_interface}")
                print(f"SPI Size: {args.spi_size} MB")
            if args.u_boot:
                print(f"U-Boot: {args.u_boot[0]} @ offset {args.u_boot[1]}")
            if args.boot_script:
                print(f"Boot script: {args.boot_script[0]} @ offset {args.boot_script[1]}")
            if args.kernel:
                print(f"Kernel: {args.kernel[0]} @ offset {args.kernel[1]}")
            if args.rootfs:
                print(f"Rootfs: {args.rootfs[0]} @ offset {args.rootfs[1]}")
            print()
        
        # Validate inputs
        if not validate_inputs(mmi_file, bit_file, data_file):
            return 1
        
        # Configure
        if not configure(output_directory, mmi_file, bit_file, data_file, processor, output, args.verbose):
            return 1
        
        # Compile
        if not compile_bitstream(mmi_file, bit_file, data_file, processor, output, args.verbose):
            return 1
        
        print(f"Successfully generated : {output} with {bit_file} embeddeded with {data_file} elf file")

        # If the output is a .rcdo and work_dir has a .pdi anywhere in the
        # tree, replace the original rcdo (bit_file) in-place with the updated one
        # then run bootgen from the pdi_files directory to regenerate the .pdi
        _, out_ext = os.path.splitext(output)
        if out_ext.lower() == '.rcdo':
            replaced = copy_rcdo_to_pdi_files(output, bit_file, work_dir, args.verbose)
            if replaced:
                pdi_files_dir = Path(bit_file).parent
                generated_pdi = run_bootgen(pdi_files_dir, output_directory, args.verbose)
                # If user requested an SPI image format, generate MCS/BIN from
                # the PDI using Vivado write_cfgmem (generate_spi_image_tcl)
                if generated_pdi and args.spi_image_format:
                    if not args.spi_interface or not args.spi_size:
                        print("ERROR: --spi-interface and --spi-size are required when --spi-image-format is specified.", file=sys.stderr)
                        return 1
                    tcl_file = generate_spi_image_tcl(
                        bit_file=generated_pdi,
                        spi_image_format=args.spi_image_format,
                        spi_interface=args.spi_interface,
                        spi_size=args.spi_size,
                        output_directory=output_directory,
                        verbose=args.verbose,
                        u_boot=args.u_boot,
                        boot_script=args.boot_script,
                        kernel=args.kernel,
                        rootfs=args.rootfs,
                    )
                    if tcl_file is None:
                        return 1
                    print(f"Successfully generated SPI TCL script: {tcl_file}")
                    return 0

        # Generate SPI image TCL script if SPI args are provided
        if args.spi_image_format:
            if not args.spi_interface or not args.spi_size:
                print("ERROR: --spi-interface and --spi-size are required when --spi-image-format is specified.", file=sys.stderr)
                return 1
            tcl_file = generate_spi_image_tcl(
                bit_file=output,
                spi_image_format=args.spi_image_format,
                spi_interface=args.spi_interface,
                spi_size=args.spi_size,
                output_directory=output_directory,
                verbose=args.verbose,
                u_boot=args.u_boot,
                boot_script=args.boot_script,
                kernel=args.kernel,
                rootfs=args.rootfs,
            )
            if tcl_file is None:
                return 1
            print(f"Successfully generated SPI TCL script: {tcl_file}")

        return 0
        
    except UpdatememNotFoundError as e:
        print(f"ERROR: {e}", file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
