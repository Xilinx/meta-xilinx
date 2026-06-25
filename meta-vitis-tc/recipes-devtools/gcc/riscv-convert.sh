#! /bin/bash

# Call using:
#../riscv/sysroots/x86_64-oesdk-linux/usr/bin/riscv-xilinx-elf/riscv-xilinx-elf-gcc -print-multi-lib | riscv-convert.sh

# Then copy the output into the special riscv-tc BSP

tempfile=`mktemp`

echo "MULTILIBS  = \"\""

sed -e 's,;, ,' |
  while read mlib args ; do
    if [ $mlib = '.' ]; then
      echo '# Base configuration' >> $tempfile
      echo '# CFLAGS:' >> $tempfile
      echo 'DEFAULTTUNE = "rv32"' >> $tempfile
      echo >> $tempfile
      echo 'AVAILTUNES += "rv32"' >> $tempfile
      echo 'TUNE_FEATURES:tune-rv32 = "rv 32 i"' >> $tempfile
      echo 'PACKAGE_EXTRA_ARCHS:tune-rv32 = "${TUNE_RISCV_PKGARCH}"' >> $tempfile
      echo 'BASE_LIB:tune-rv32 = "lib"' >> $tempfile
      continue
    fi

    cflags=$(echo $args | sed -e 's,@, -,g')
    tune=$(echo $cflags | sed -e 's,-march=\([a-z0-9_]*\).*,\1,')
    multilib="lib$tune"
    echo "MULTILIBS += \"multilib:${multilib}\""
    echo >> $tempfile
    echo >> $tempfile
    echo "# $mlib" >> $tempfile
    echo "# CFLAGS:${cflags}" >> $tempfile
    echo "DEFAULTTUNE:virtclass-multilib-$multilib = \"$tune\"" >> $tempfile
    echo >> $tempfile
    echo "AVAILTUNES += \"$tune\"" >> $tempfile
    echo "TUNE_FEATURES:tune-$tune = \"\${@mbv.tune.riscv_isa_to_tune(\"$tune\")}\"" >> $tempfile
    echo "PACKAGE_EXTRA_ARCHS:tune-$tune = \"\${TUNE_RISCV_PKGARCH}\"" >> $tempfile
    echo "BASE_LIB:tune-$tune = \"lib/$mlib\"" >> $tempfile
  done

echo
cat $tempfile
rm $tempfile
