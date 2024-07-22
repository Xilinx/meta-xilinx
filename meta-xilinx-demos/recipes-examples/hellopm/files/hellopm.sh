#!/bin/sh

#*******************************************************************************
#
# Copyright (C) 2019 Xilinx, Inc.  All rights reserved.
# Copyright (c) 2022-2023 Advanced Micro Devices, Inc. All Rights Reserved.
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy of
# this software and associated documentation files (the "Software"), to deal in
# the Software without restriction, including without limitation the rights to
# use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
# of the Software, and to permit persons to whom the Software is furnished to do
# so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in all 
# copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
# AUTHORS OR COPYRIGHT HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
# SOFTWARE.
#
# Author:     Will Wong <willw@xilinx.com>
#
# ******************************************************************************

# Xilinx Power Management Demo

# Stop on errors
set -e

suspend_menu () {
	while true; do
		cat <<-EOF

		++++++++++++++++++
		+ Suspend/Resume +
		++++++++++++++++++

		EOF

		cat <<-EOF

		This operation suspends the kernel and powers off the processor.  All
		peripherals not used as a result of this operation will be powered
		down or put into retention mode.  (Note that peripherals that are still
		being used by the RPU, such as memory, will not be powered down.)

		If no peripherals on the FPD are being used as a result of this operation,
		the FPD will be powered off.

		While the processor is suspended, all the data and context information is
		stored in the DRAM.  If the FPD is powered off, the DRAM will be put into
		self-refresh mode.

		Options:
		1. Suspend now, wake-up on Real-time Clock (RTC)
		0. Go Back

		EOF
		echo -n "Input: "
		read choice
		echo ""


		case $choice in
		0)
			break
			;;
		1)
			cat <<-EOF

			Suspending now, wake-up on Real-time Clock (RTC)

			Enable RTC as the wake-up source.
			Command: echo +10 > /sys/class/rtc/rtc0/wakealarm

			EOF

			echo +10 > /sys/class/rtc/rtc0/wakealarm

			cat <<-EOF

			Invoke suspend sequence
			Command: echo mem > /sys/power/state
			Waking up in about 10 seconds ...

			EOF
			sleep 1
			echo mem > /sys/power/state

			echo "Press RETURN to continue ..."
			read dummy
			;;
		esac
	done
	echo ""
}


cpu_hotplug_menu () {
	while true; do
		cat <<-EOF
		+++++++++++++++
		+ CPU Hotplug +
		+++++++++++++++

		EOF

		cat <<-EOF
		This operation brings individual CPU cores online and offline.  Processes
		running on a CPU core will be moved over to another core before the core
		is taken offline."

		CPU Hotlplug is different from, but can be operating along with, CPU Idle.
		The latter is having the kernel power off CPU cores when they are idling.

		Options:
		1. Check CPU3 Status
		2. Take CPU3 Offline
		3. Bring CPU3 Online
		0. Go Back

		EOF
		echo -n "Input: "
		read choice
		echo ""


		case $choice in
		0)
			break
			;;
		1)
			cat <<-EOF
			Check CPU3 Status

			Command: cat /sys/devices/system/cpu/cpu3/online

			EOF

			echo `cat /sys/devices/system/cpu/cpu3/online`
			echo ""
			echo "Press RETURN to continue ..."
			read dummy
			echo ""
			;;
		2)
			cat <<-EOF
			Take CPU3 Offline

			Command: echo 0 > /sys/devices/system/cpu/cpu3/online
			EOF
			echo `echo 0 > /sys/devices/system/cpu/cpu3/online`
			echo ""
			echo "Press RETURN to continue ..."
			read dummy
			echo ""
			;;
		3)
			cat <<-EOF
			Bring CPU3 Online

			Command: echo 1 > /sys/devices/system/cpu/cpu3/online
			EOF
			echo `echo 1 > /sys/devices/system/cpu/cpu3/online`
			echo ""
			echo "Press RETURN to continue ..."
			read dummy
			echo ""
			;;
		esac
	echo ""
	done
}

cpu_freq_menu () {
	while true; do
		cat <<-EOF
		++++++++++++
		+ CPU Freq +
		++++++++++++

		EOF

		cat <<-EOF
		This feature adjusts the CPU frequency on-the-fly.  All CPU cores are running
		at the same frequency.  A lower CPU frequency consumes less power, at the
		expense of performance.

		The CPU frequency can be adjusted automatically by a governor, which controls
		the CPU frequency based on its policy.  A special 'userspace' governor does
		not actually adjust the CPU frequency.  Instead, it allows the users to
		adjust the CPU frequency by themselves."

		Options:
		1. Show current CPU frequency
		2. List supported CPU frequencies
		3. Change CPU frequency
		0. Go Back

		EOF
		echo -n "Input: "
		read choice
		echo ""

		case $choice in
		0)
			break
			;;
		1)
			cat <<-EOF
			Show current CPU frequency

			Command: cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq
			EOF

			echo `cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq`
			echo ""
			echo "Press RETURN to continue ..."
			read dummy
			echo ""
			;;
		2)
			cat <<-EOF
			List supported CPU frequencies

			Command: cat /sys/devices/system/cpu/cpu1/cpufreq/scaling_available_frequencies
			EOF

			echo `cat /sys/devices/system/cpu/cpu1/cpufreq/scaling_available_frequencies`
			echo ""
			echo "Press RETURN to continue ..."
			read dummy
			echo ""
			;;
		3)
			echo "Change CPU frequency"
			echo ""
			echo -n "Enter new frequency: "
			read freq
			echo "Command: echo $freq > /sys/devices/system/cpu/cpu0/cpufreq/scaling_setspeed"
			echo `echo $freq > /sys/devices/system/cpu/cpu0/cpufreq/scaling_setspeed`
			echo ""
			echo "Press RETURN to continue ..."
			read dummy
			echo ""
			;;
		esac
	done
	echo ""
}

reboot_menu () {
	while true; do
		cat <<-EOF
		++++++++++
		+ Reboot +
		++++++++++

		EOF

		cat <<-EOF
		There are 3 types of reboot: APU-only, PS-only and System reboot. APU-only
		reboot will only reboot the APU.  The RPU, PMU and PL will not be affected.

		PS-only reboot will reboot the PS, including the APU, RPU and PMU.
		The PL will not be affected by a PS-only reboot, provided that the PL
		is completely isolated from the PS.

		System reboot will reboot the entire system, including the PS and the PL.

		Options:
		1. Reboot APU
		2. Reboot PS
		3. Reboot System
		0. Go Back

		EOF
		echo -n "Input: "
		read choice
		echo ""

		case $choice in
		0)
			break
			;;
		1)
			echo "Reboot APU"
			echo ""
			echo -n "This will end the demo.  Continue (Y/n)? "

			read continue

			if [ "$continue" != "n" ] && [ "$continue" != "N" ]; then 
				cat <<-EOF
				Set reboot scope to APU
				Command: echo "subsystem" > /sys/devices/platform/firmware\:zynqmp-firmware/shutdown_scope
				EOF
				echo `echo "subsystem" > /sys/devices/platform/firmware\:zynqmp-firmware/shutdown_scope`
				echo ""
				echo "Command: reboot"
				echo ""
				reboot
				while true; do
					read dummy
				done
			fi
			;;
		2)
			echo "Reboot PS"
			echo ""
			echo -n "This will end the demo.  Continue (Y/n)? "

			read continue

			if [ "$continue" != "n" ] && [ "$continue" != "N" ]; then
			  cat <<-EOF
				Set reboot scope to PS
				Command: echo "ps_only" > /sys/devices/platform/firmware\:zynqmp-firmware/shutdown_scope
				EOF
				echo `echo "ps_only" > /sys/devices/platform/firmware\:zynqmp-firmware/shutdown_scope`
				echo ""

				echo "Command: reboot"
				echo ""
				reboot
				while true; do
					read dummy
				done
			fi
			;;
		3)
			echo "Reboot System"
			echo ""
			echo -n "This will end the demo.  Continue (Y/n)? "

			read continue
			echo ""
			if [ "$continue" != "n" ] && [ "$continue" != "N" ]; then
				cat <<-EOF
				Set reboot scope to System
				Command: echo "system" > /sys/devices/platform/firmware\:zynqmp-firmware/shutdown_scope
				EOF

				echo `echo "system" > /sys/devices/platform/firmware\:zynqmp-firmware/shutdown_scope`
				echo ""
				echo "Command: reboot"
				echo ""
				reboot
				while true; do
					read dummy
				done
			fi
			;;
		esac
	done
	echo ""
}

shutdown_menu () {
	while true; do
		cat <<-EOF
		++++++++++++
		+ Shutdown +
		++++++++++++

		EOF

		cat <<-EOF
		The command will shut down the APU. The RPU and PL will not be affected.

		Options:
		1. Shutdown APU
		0. Go Back

		EOF

		echo -n "Input: "
		read choice
		echo ""

		case $choice in
		0)
			break
			;;
		1)
			echo "Shutdown APU"
			echo ""
			echo -n "This will end the demo.  Continue (Y/n)? "
			read continue
			echo ""
			if [ "$continue" != "n" ] && [ "$continue" != "N" ]; then
				echo "Command: shutdown -h now"
				echo ""
				shutdown -h now
				while true; do
					read dummy
				done
			fi
			;;
		esac
	done
	echo ""
}


main_menu () {
	while true; do

		cat <<-EOF

		+++++++++++++
		+ Main Menu +
		+++++++++++++

		EOF

                cat <<-EOF

		These are Linux-based power management features available for the
		Zynq (c) UltraScale+ MPSoC.  These examples assume you are using
		the ZCU102 board, although most of them are independent of the
		board type.

		Options:
		1. Suspend/Resume
		2. CPU Hotplug
		3. CPU Freq
		4. Reboot
		5. Shutdown
		0. Quit

		EOF

		echo -n "Input: "
		read choice
		echo ""

		case $choice in
		0)
			break
			;;
		1)
			suspend_menu
			;;
		2)
			cpu_hotplug_menu
			;;
		3)
			cpu_freq_menu
			;;
		4)
			reboot_menu
			;;
		5)
			shutdown_menu

		esac
	done
	echo ""
}

echo ""
cat <<-EOF
============================
      Hello PM World
Xilinx Power Management Demo
============================
EOF
echo ""

main_menu
echo ""

