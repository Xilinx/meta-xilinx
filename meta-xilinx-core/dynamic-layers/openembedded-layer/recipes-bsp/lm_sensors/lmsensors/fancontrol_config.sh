#!/bin/sh
#
# Copyright (C) 2025 Advanced Micro Devices, Inc. All rights reserved.
# SPDX-License-Identifier: GPL-2.0-or-later
#

# Script exits with 0 to prevent disruption of fancontrol service and continue
# with defualt configuration.

eeprom=$(ls /sys/bus/i2c/devices/*/eeprom_cc*/nvmem 2> /dev/null)
if [ -n "${eeprom}" ]; then
    if [ ! -r "${eeprom}" ]; then
        echo "ERROR: cannot read EEPROM: ${eeprom}" >&2
        exit 0
    fi

    # Execute ipmi-fru once and capture output
    if ! fru_data=$(ipmi-fru --fru-file="${eeprom}" --interpret-oem-data 2>&1); then
        echo "ERROR: failed to read FRU data: ${fru_data}" >&2
        exit 0
    fi

    # Parse board ID
    boardid=$(ipmi-fru --fru-file="${eeprom}" --interpret-oem-data | awk -F": " '/FRU Board Product/ { print tolower ($2) }')

    if [ -z "${boardid}" ]; then
        echo "ERROR: could not extract board ID from FRU data: ${boardid}" >&2
        exit 0
    fi

    # Check for board specific config without revision
    config="/usr/share/config/fancontrol_${boardid}.conf"
    if [ -e "${config}" ]; then
        if ! ln -sf "${config}" /etc/fancontrol; then
            echo "ERROR: failed to create a symlink to ${config}" >&2
            exit 0
        fi
    fi
fi
