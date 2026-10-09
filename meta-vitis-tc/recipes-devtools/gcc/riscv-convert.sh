#! /bin/bash

# Call using:
#../riscv/sysroots/x86_64-oesdk-linux/usr/bin/riscv-xilinx-elf/riscv-xilinx-elf-gcc -print-multi-lib | sort -t/ -k1,1 -k2,2 | riscv-convert.sh

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

    # Drop extensions already implied by single-letter ISA bits before the
    # first '_' ('a' implies zaamo/zalrsc, 'b' implies zba/zbb/zbs).
    ml_base=$(echo $tune | sed -e 's,_.*,,')
    ml_exts=$(echo $tune | sed -e 's,^[^_]*,,')
    baseletters=$(echo $ml_base | sed -e 's,^rv[0-9]*,,')
    has_a=""
    has_b=""
    case "$baseletters" in *a*) has_a=1 ;; esac
    case "$baseletters" in *b*) has_b=1 ;; esac
    multilib="lib$ml_base"
    oldIFS=$IFS
    IFS=_
    for ext in $ml_exts; do
      [ -z "$ext" ] && continue
      if [ -n "$has_a" ] && { [ "$ext" = "zaamo" ] || [ "$ext" = "zalrsc" ]; }; then
        continue
      fi
      if [ -n "$has_b" ] && { [ "$ext" = "zba" ] || [ "$ext" = "zbb" ] || [ "$ext" = "zbs" ]; }; then
        continue
      fi
      multilib="${multilib}_${ext}"
    done
    IFS=$oldIFS
    echo "MULTILIBS += \"multilib:${multilib}\""
    echo >> $tempfile
    echo >> $tempfile
    echo "# $mlib" >> $tempfile
    echo "# CFLAGS:${cflags}" >> $tempfile
    echo "DEFAULTTUNE:virtclass-multilib-$multilib = \"$tune\"" >> $tempfile
    echo >> $tempfile
    echo "AVAILTUNES += \"$tune\"" >> $tempfile
    echo "TUNE_FEATURES:tune-$tune = \"\${@oe.tune.riscv_isa_to_tune(\"$tune\")}\"" >> $tempfile
    echo "PACKAGE_EXTRA_ARCHS:tune-$tune = \"\${TUNE_RISCV_PKGARCH}\"" >> $tempfile
    echo "BASE_LIB:tune-$tune = \"lib/$mlib\"" >> $tempfile
  done

echo
cat $tempfile
rm $tempfile
