# Copyright (C) 2026 Advanced Micro Devices, Inc.  All rights reserved.
#
# lyrical adds rmw-zenoh-cpp to rmw-implementation's build dependencies, but the
# zenoh RMW needs a meta-zenoh layer that is not in our manifest, so it is listed
# in ROS_WORLD_SKIP_GROUPS. The upstream rmw-implementation bbappend already
# drops the connext RMW from the build deps when connext is skipped; do the same
# for zenoh so the recipe resolves against the middlewares we ship (fastrtps and
# cyclonedds).
ROS_BUILD_DEPENDS:remove = "${@bb.utils.contains('ROS_WORLD_SKIP_GROUPS', 'zenoh', 'rmw-zenoh-cpp', '', d)}"
