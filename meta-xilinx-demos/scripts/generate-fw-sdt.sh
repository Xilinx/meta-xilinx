#! /bin/bash -e

### The following table controls the automatic generated of the firmware demos
### Machine                 Recipe
#M# vek280-pl-bram-gpio-fw  recipes-firmware/vek280-pl-bram-uart-gpio-fw/vek280-pl-bram-uart-gpio-fw_1.0-2025.1.bb
#M# vek280-pl-aie-vdu-fw    recipes-firmware/vek280-pl-aie-vdu-fw/vek280-pl-aie-vdu-fw_1.0-2025.1.bb
#M# zcu104-pl-vcu-fw        recipes-firmware/zcu104-pl-vcu-fw/zcu104-pl-vcu-fw_1.0-2025.1.bb
#M# zcu111-pl-bram-gpio-fw  recipes-firmware/zcu111-pl-bram-gpio-fw/zcu111-pl-bram-gpio-fw_1.0-2025.1.bb

this=$(realpath $0)

if [ $# -lt 2 ]; then
  echo "$0: <conf_path> <machine_url_index> [machine]" >&2
  exit 1
fi

conf_path=$(realpath $1)
if [ ! -d ${conf_path} ]; then
  mkdir -p ${conf_path}
fi


mach_index=$(realpath $2)
count=0
while read mach_id mach_url; do
  if [ ${mach_id} = '#' ]; then
      continue
  fi

  MACHINE_ID[$count]=${mach_id}
  MACHINE_URL[$count]=${mach_url}

  count=$(expr $count + 1)
done < ${mach_index}


# Load in the arrays from this script
count=0
while read marker machine recipe ; do
  if [ "${marker}" != "#M#" ]; then
      continue
  fi

  MACHINES[$count]=${machine}
  RECIPES[$count]=${recipe}
  for mach in ${!MACHINE_ID[@]}; do
    if [ ${MACHINE_ID[${mach}]} = ${machine} ]; then
      URLS[$count]=${MACHINE_URL[${mach}]}
      break
    fi
  done
  if [ -z "${URLS[$count]}" ]; then
    echo "ERROR: Unable to find ${machine} in ${mach_index}" >&2
    exit 1
  fi

  count=$(expr $count + 1)
done < ${this}


for mach in ${!MACHINES[@]}; do
  if [ -n "$3" -a "$3" != "${MACHINES[${mach}]}" ]; then
    continue
  fi

  echo "Firmware: ${MACHINES[${mach}]}"
  echo "Recipe:   ${RECIPES[${mach}]}"
  echo "URL:      ${URLS[${mach}]}"
  echo

  url=${URLS[${mach}]}
  url=$(echo $url | sed 's,https://petalinux.xilinx.com/sswreleases/.*/sdt,https://artifactory.xilinx.com/artifactory/petalinux-hwproj-dev/sdt,')

  wget $url -O ${conf_path}/output.sdt
  sha=$(sha256sum ${conf_path}/output.sdt | cut -d ' ' -f 1)
  rm -f ${conf_path}/output.sdt

  sed -e 's,SRC_URI = .*,SRC_URI = "'${URLS[${mach}]}'",' \
      -e 's,SRC_URI\[sha256sum\] = .*,SRC_URI\[sha256sum\] = "'${sha}'",' \
      -i $(dirname $0)/../${RECIPES[${mach}]}
done
