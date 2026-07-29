#!/bin/bash

DEVICE_ID=$(cat /proc/device-tree/device_id | tr -d '\0')

modprobe mali_gpu_pm
modprobe mali_gpu_power
modprobe mali_gpu_system

case "$DEVICE_ID" in
    xc2vm3654)
        PTM_CONFIG="A:S0:P0:W0"
        ACTIVE_SLICES="0x1"
        ;;
    xc2ve3858)
        PTM_CONFIG="A:S0:S1:P0:W0"
        ACTIVE_SLICES="0x3"
        ;;
    *)
        echo "Unknown device: $DEVICE_ID"
        exit 1
        ;;
esac

modprobe mali_gpu_assign ptm_config="$PTM_CONFIG"
modprobe mali_gpu_partition_config
modprobe mali_gpu_partition_control
modprobe mali_arbiter
modprobe mali_gpu_resource_group

echo "$ACTIVE_SLICES" > /sys/module/mali_gpu_resource_group/drivers/*/*/arbiter/partitions/partition0/active_slices
echo 0x1 > /sys/module/mali_gpu_resource_group/drivers/*/*/arbiter/partitions/partition0/assigned_access_windows

modprobe mali_gpu_aw
modprobe mali_kbase gpu_req_timeout=100
modprobe dma-buf-test-exporter
