#!/bin/bash
# Assign and partition0 defaults come from DT xlnx,gpu-slices.

modprobe mali_gpu_pm
modprobe mali_gpu_power
modprobe mali_gpu_system

modprobe mali_gpu_assign
modprobe mali_gpu_partition_config
modprobe mali_gpu_partition_control
modprobe mali_arbiter
modprobe mali_gpu_resource_group

modprobe mali_gpu_aw
modprobe mali_kbase gpu_req_timeout=100
modprobe dma-buf-test-exporter
