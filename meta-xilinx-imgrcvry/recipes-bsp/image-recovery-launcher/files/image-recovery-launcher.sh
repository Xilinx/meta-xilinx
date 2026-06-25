#!/bin/sh                                                                       

# Image recovery web launch
_IMGRCVRY_WEB="/var/imgrcry_web/"
_IMGRCVRY="/usr/sbin/httpd"
_IMGRCVRY_ARGS="-p 8080"

if [ -x "$_IMGRCVRY" ]; then
	if cd "$_IMGRCVRY_WEB"; then
		"$_IMGRCVRY" $_IMGRCVRY_ARGS
		cd - >/dev/null
	else
		echo "Warning: $_IMGRCVRY_WEB directory not found"
	fi
fi

# Image recovery IP read and display banner
_IP_UTIL="/usr/sbin/ip"
MAX_TRIES=10
ip_addr=""
tries=1
while [ "${tries}" -le "$MAX_TRIES" ]; do
	ip_addr=$("${_IP_UTIL}" -4 addr show scope global | awk '/inet / {print $2}' | cut -d/ -f1 | head -n1)
	if [ -n "${ip_addr}" ]; then
		echo "udhcpc: link is up, IP assigned -> ${ip_addr}"
		break
	else
		echo "Trying to get the IP address(${tries})"
	fi
	sleep 1
	tries=$((tries+1))
done

shell_msg=""
if [ -n "${ip_addr}" ]; then
	shell_msg=", Use ${ip_addr}:8080 to launch the Image Recovery web app."
else
	shell_msg=", No IP Addr found to launch the Image Recovery web app.   "
fi

echo "   ##############################################################################################"
echo "   #                                                                                            #"
echo "   #Launching to Image Recovery shell${shell_msg}#"
echo "   #                                                                                            #"
echo "   ##############################################################################################"

exit 0
