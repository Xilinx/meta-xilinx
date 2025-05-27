#!/bin/bash

modprobe mali_gpu_power
modprobe mali_gpu_system
modprobe mali_gpu_assign ptm_config='A:S0:S1:P0:W0'
modprobe mali_gpu_partition_config
modprobe mali_gpu_partition_control
modprobe mali_arbiter
modprobe mali_gpu_resource_group

echo 0x3 > /sys/module/mali_gpu_resource_group/drivers/*/*/arbiter/partitions/partition0/active_slices
echo 0x1 > /sys/module/mali_gpu_resource_group/drivers/*/*/arbiter/partitions/partition0/assigned_access_windows

modprobe mali_gpu_aw
modprobe mali_kbase
modprobe dma-buf-test-exporter
