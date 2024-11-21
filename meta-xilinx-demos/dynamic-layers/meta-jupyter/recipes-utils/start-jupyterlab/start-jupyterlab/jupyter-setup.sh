#!/bin/sh
#/etc/init.d/jupyterlab-start: start jupyterlab daemon

### BEGIN INIT INFO
# Provides:          jupyter-lab
# Default-Start:     3 5
# Default-Stop:      0 1 2 6
# Short-Description: Start Jupyter Lab server as petalinux user
### END INIT INFO

OWNER="petalinux"
GROUP="petalinux"
HOME=`(cd ~petalinux && pwd) || echo 'none'`
NBDIR="${HOME}/notebooks"

DAEMON_PATH="/sbin/start-jupyter.sh"
DAEMON_NAME=`basename $DAEMON_PATH`
PIDFILE="/var/run/${DAEMON_NAME}.pid"

PATH=/bin:/usr/bin:/sbin:/usr/sbin

. /etc/init.d/functions

wait_for_ip() {
    echo -n "Waiting for IP address..."

    for i in {1..20}
    do
        echo -n "."
        ip=$(ip -4 addr show eth0 | grep -oE "inet ([0-9]{1,3}[\.]){3}[0-9]{1,3}" | cut -d ' ' -f2)
        [ -n "$ip" ] && break
        if [ -d /sys/class/net/eth1 ]; then
            ip=$(ip -4 addr show eth1 | grep -oE "inet ([0-9]{1,3}[\.]){3}[0-9]{1,3}" | cut -d ' ' -f2)
            [ -n "$ip" ] && break
        fi
        sleep 2
    done

    if [ -z $ip ]; then
        echo " TIMEOUT"
    else
        echo " SUCCESS"
    fi
}

log_begin_msg() {
    echo -n $*
}

log_end_msg() {
    if [ "$1" = "0" ]; then
        echo ' OK'
    else
        echo ' ERROR'
    fi
}

log_daemon_msg() {
    echo $*
}

log_progress_msg() {
    echo $*
}

test -x $DAEMON_PATH || exit 0

case "$1" in
    start)
        log_begin_msg "Starting jupyter-lab server daemon... "

        # Various jupter paths are incorrect if this daemon is run as part of the
        # init process. Override the directories with these environment variables.
        export JUPYTER_CONFIG_DIR="${HOME}/.jupyter"
        export JUPYTER_DATA_DIR="${HOME}/.local/share/jupyter"
        export JUPYTER_RUNTIME_DIR="${HOME}/.local/share/jupyter/runtime"
        export HOME="${HOME}"

        # check owner and group are valid
        id $OWNER > /dev/null 2>&1
        if [ "$?" = "1" ]; then echo "'$OWNER': no such owner... ERROR" ; exit 1 ; fi
        grep $GROUP /etc/group > /dev/null
        if [ "$?" = "1" ]; then echo "'$GROUP': no such group... ERROR" ; exit 1 ; fi

        # create nb dir if it doesn't exist
        if [ ! -d "$NBDIR" ] ; then install -o $OWNER -g $GROUP -d $NBDIR ; fi

        # start the daemon
        wait_for_ip
        start-stop-daemon -S -c $OWNER:$GROUP -m -p $PIDFILE -x $DAEMON_NAME &
        log_end_msg $?
        ;;

    stop)
        log_begin_msg "Stopping jupyter-lab server daemon..."
        start-stop-daemon -K -q -p $PIDFILE
        log_end_msg $?
        ;;

    restart)
        $0 force-reload
        ;;

    force-reload)
        log_daemon_msg "Restarting jupyter-lab server daemon"
        $0 stop
        $0 start
        ;;

    status)
        status $DAEMON_PATH
        ;;

    *)
        echo "usage: $0 {start|stop|restart|force-reload|status}"
        exit 1
esac

exit 0
