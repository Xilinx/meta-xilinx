LICENSE = "Apache-2.0 & BSD-3-Clause & GPL-2.0-only"
ROS_EXEC_DEPENDS:remove = "${ROS_UNRESOLVED_DEP-liblz4}"
DEPENDS:remove = "${ROS_UNRESOLVED_DEP-liblz4-dev}"
INSANE_SKIP:${PN} += "dev-so"
