#!/bin/sh

PATH=/sbin:/bin:/usr/sbin:/usr/bin

# Copied from initramfs-framework. The core of this script probably should be
# turned into initramfs-framework modules to reduce duplication.
udev_daemon() {
	OPTIONS="/sbin/udev/udevd /sbin/udevd /lib/udev/udevd /lib/systemd/systemd-udevd"

	for o in $OPTIONS; do
		if [ -x "$o" ]; then
			echo $o
			return 0
		fi
	done

	return 1
}

_UDEV_DAEMON=`udev_daemon`

early_setup() {
    mkdir -p /proc /sys /run /var/run
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
    mount -t devtmpfs none /dev

    # support modular kernel
    modprobe isofs 2> /dev/null

    $_UDEV_DAEMON --daemon
    udevadm trigger --action=add
}


echo_msg() {
    echo "$1" >$CONSOLE
}


launch_shell() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}


_UDHCPC="/sbin/udhcpc"
# -n to exit if no lease
_UDHCPC_ARGS="-n"

run_udhcpc() {
	_UDHCPC_INT="$1"
	if [ -x $_UDHCPC ]; then
		$_UDHCPC $_UDHCPC_INT $_UDHCPC_ARGS
	fi
	return 0
}


_IMGRCVRY_WEB="/var/imgrcry_web/"
_IMGRCVRY="/usr/sbin/httpd"
_IMGRCVRY_ARGS="-p 8080"

launch_imgrcvry() {
	if [ -x $_IMGRCVRY ]; then
		cd $_IMGRCVRY_WEB
		$_IMGRCVRY $_IMGRCVRY_ARGS
		cd -
	fi
	return 0
}


[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

early_setup

run_udhcpc

run_udhcpc "-i eth1"

launch_imgrcvry

ip_addr=$(ip addr | grep -w inet | xargs | cut -d ' ' -f 2 | cut -d '/' -f 1)
shell_msg=""
if [ ! -z $ip_addr ]; then
	shell_msg=", Use $ip_addr:8080 to launch the Image Recovery web app."
else
	shell_msg=", No IP Addr found to launch the Image Recovery web app."
fi


echo_msg "   ##############################################################################################"
echo_msg "   #                                                                                            #"
echo_msg "   #Launching to Image Recovery shell${shell_msg}#"
echo_msg "   #                                                                                            #"
echo_msg "   ##############################################################################################"

launch_shell
