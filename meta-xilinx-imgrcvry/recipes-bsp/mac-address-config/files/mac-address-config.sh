#!/bin/sh
# Copyright (c) 2026 Advanced Micro Devices, Inc. All Rights Reserved.
# SPDX-License-Identifier: MIT

# MAC Address Configuration from EEPROM
# Reads MAC addresses from I2C EEPROM at address 0x54 and configures network interfaces

# Wait for network subsystem to be ready
Wait_For_Network(){
	MAX_WAIT=10
	count=0
	while [ "$count" -lt "$MAX_WAIT" ]; do
		# Check if network interfaces exist and sysfs is ready
		if [ -d "/sys/class/net" ] && ls /sys/class/net/eth* >/dev/null 2>&1; then
			echo "mac-config: Network subsystem ready"
			return 0
		fi
		sleep 1
		count=$((count + 1))
	done
	echo "mac-config: Timeout waiting for network subsystem"
	return 1
}

# Configure MAC addresses from EEPROM at address 0x54
Configure_MAC_Addresses(){
	# Wait for network subsystem to be ready first
	Wait_For_Network || return 1

       # Check I2C subsystem is available
       [ -d "/sys/bus/i2c/devices" ] || { echo "mac-config: I2C subsystem not available"; return 1; }

       # Find EEPROM device
       eeprom=""
       for e in /sys/bus/i2c/devices/*-0054/eeprom; do
	       [ -r "$e" ] && eeprom="$e" && break
       done

       [ -z "$eeprom" ] && echo "mac-config: EEPROM not found" && return 1

       echo "mac-config: Found EEPROM at $eeprom"

       # Read MACs from offsets: 0x71 (113), 0x77 (119), 0x7d (125)
       offset=113
       for eth_path in /sys/class/net/eth*; do
	       [ -d "$eth_path" ] || continue
	       eth=$(basename "$eth_path")

	       # Read 6 bytes and convert to MAC format
	       mac=$(dd if="$eeprom" bs=1 skip=$offset count=6 2>/dev/null | hexdump -v -e '5/1 "%02x:" 1/1 "%02x"')

	       # Enhanced MAC validation
	       valid_mac=0

	       if [ -n "$mac" ]; then
		       # Check MAC format is valid (XX:XX:XX:XX:XX:XX)
		       if echo "$mac" | grep -qE '^([0-9a-fA-F]{2}:){5}[0-9a-fA-F]{2}$'; then
			       # Extract first octet for additional checks
			       first_octet=$(echo "$mac" | cut -d: -f1)
			       first_byte=$((0x$first_octet))

			       # Check for common invalid patterns
			       case "$mac" in
				       00:00:00:00:00:00)
					       echo "mac-config: Invalid MAC for $eth - all zeros"
					       ;;
				       ff:ff:ff:ff:ff:ff)
					       echo "mac-config: Invalid MAC for $eth - broadcast address"
					       ;;
				       *)
					       # Check if multicast bit is set (LSB of first octet)
					       if [ $((first_byte & 1)) -eq 1 ]; then
						       echo "mac-config: Invalid MAC for $eth - multicast address"
					       else
						       valid_mac=1
					       fi
					       ;;
			       esac
		       else
			       echo "mac-config: Invalid MAC format for $eth at offset $offset"
		       fi
	       else
		       echo "mac-config: Empty MAC read for $eth at offset $offset"
	       fi

	       # Apply MAC address if valid
	       if [ "$valid_mac" -eq 1 ]; then
		       ip link set "$eth" down 2>/dev/null
		       if ip link set "$eth" address "$mac" 2>/dev/null; then
			       ip link set "$eth" up 2>/dev/null
			       # Verify the MAC was set correctly
			       actual_mac=$(cat "/sys/class/net/$eth/address" 2>/dev/null)
			       if [ "$actual_mac" = "$mac" ]; then
				       echo "mac-config: Set $eth to $mac"
			       else
				       echo "mac-config: Warning - $eth set to $actual_mac (expected $mac)"
			       fi
		       else
			       echo "mac-config: Failed to apply MAC $mac to $eth"
		       fi
	       fi
	       offset=$((offset + 6))
       done
       return 0
}

case "$1" in
	start)
		echo "Starting MAC address configuration..."
		if Configure_MAC_Addresses; then
			echo "mac-config: MAC configuration completed successfully"
			exit 0
		else
			echo "mac-config: MAC configuration failed, using default MACs"
			exit 1
		fi
		;;
	stop)
		# Nothing to do on stop
		exit 0
		;;
	*)
		echo "Usage: $0 {start|stop}"
		exit 1
		;;
esac
