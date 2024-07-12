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
      echo 'DEFAULTTUNE = "riscv"' >> $tempfile
      echo >> $tempfile
      echo 'AVAILTUNES += "riscv"' >> $tempfile
      echo 'PACKAGE_EXTRA_ARCHS:tune-riscv = "${TUNE_PKGARCH:tune-riscv}"' >> $tempfile
      echo 'BASE_LIB:tune-riscv = "lib"' >> $tempfile
      echo 'TUNE_FEATURES:tune-riscv = "riscv"' >> $tempfile
      echo 'TUNE_CCARGS:tune-riscv = ""' >> $tempfile
      echo 'TUNE_PKGARCH:tune-riscv = "riscv32"' >> $tempfile
      echo 'TUNE_ARCH:tune-riscv = "riscv32"' >> $tempfile
      continue
    fi

    cflags=$(echo $args | sed -e 's,@, -,g')
    multilib="lib$(echo $mlib | sed -e 's,/,,g')"
    tune="$(echo $mlib | sed -e 's,/,,g')"
    case $mlib in
        .)  arch="riscv32" ;;
        rv32*) arch="riscv32" ;;
        rv64*) arch="riscv64" ;;
        *) arch="unknwon" ;;
    esac
    echo "MULTILIBS += \"multilib:${multilib}\""
    echo >> $tempfile
    echo >> $tempfile
    echo "# $mlib" >> $tempfile
    echo "# CFLAGS:${cflags}" >> $tempfile
    echo "DEFAULTTUNE:virtclass-multilib-$multilib = \"$tune\"" >> $tempfile
    echo >> $tempfile
    echo "AVAILTUNES += \"$tune\"" >> $tempfile
    echo "PACKAGE_EXTRA_ARCHS:tune-$tune = \"\${TUNE_PKGARCH:tune-$tune}\"" >> $tempfile
    echo "BASE_LIB:tune-$tune = \"lib/$mlib\"" >> $tempfile
    echo "TUNE_FEATURES:tune-$tune = \"riscv\"" >> $tempfile
    echo "TUNE_CCARGS:tune-$tune = \"$cflags\"" >> $tempfile
    echo "TUNE_PKGARCH:tune-$tune = \"$tune\"" >> $tempfile
    echo "TUNE_ARCH:tune-$tune = \"$arch\"" >> $tempfile
  done

echo
cat $tempfile
rm $tempfile
