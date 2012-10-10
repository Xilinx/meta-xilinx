
pkg_postinst_${PN} () {
#!/bin/sh
# run this on the target
if [ "x$D" == "x" ]; then
	tmp="${SERIAL_CONSOLES_CHECK}"
	for i in $tmp
	do
		j=`echo ${i} | sed s/^.*\;//g`
		if [ -z "`cat /proc/consoles | grep ${j}`" ]; then
			sed -i /^.*${j}$/d /etc/inittab
		fi
	done
	kill -HUP 1
elif [ -d "$D" ]; then
	exit 0
else
	exit 1
fi
}

