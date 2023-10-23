#! /bin/sh

# Copyright (C) 2018 Xilinx, Inc. All rights reserved.
# Copyright (C) 2023 Advanced Micro Devices, Inc. All rights reserved.
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

# Adapt this script to your needs.

DEVICES=$(find /sys/class/drm/*/status)

# inspired by /etc/acpd/lid.sh and the function it sources.

# Read first X display number from the list.
displaynum=`ls /tmp/.X11-unix/* | sed s#/tmp/.X11-unix/X## | head -n 1`
displaynum=${displaynum%% *}

display=":$displaynum.0"
export DISPLAY=":$displaynum.0"

# from https://wiki.archlinux.org/index.php/Acpid#Laptop_Monitor_Power_Off

# Clear XAUTHORITY by default in case X session is not using display manager.
unset XAUTHORITY

# Detect X session command line started for the display $displaynum and extract
# -auth argument if any.
ps -eo args | grep -e "Xorg\W*:$displaynum" | grep -e -auth | while read -r line
do
	if [[ "${line%% *}" == *Xorg ]]; then
		export XAUTHORITY=`echo $line | sed -n 's/.*-auth //; s/ -[^ ].*//; p'`
		break
	fi
done

for i in /sys/class/drm/*/*/status ;
do
	status=$(cat $i);
	connector=${i%/status*};
	connector=${connector#*-};
	if [ "$status" == "disconnected" ]; then
		xset dpms force off
	elif [ "$status" == "connected" ]; then
		xset dpms force on
		if [ "$(xrandr | grep '\*')" = "" ]; then
			xrandr --output $connector --auto
		fi
	fi
done
