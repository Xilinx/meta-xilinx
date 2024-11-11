DESCRIPTION = "Packages for ROS2 Base and Demos including some basic pub/sub examples"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup
inherit ros_distro_${ROS_DISTRO}

PROVIDES = "${PACKAGES}"

PACKAGES += "${PN}-base ${PN}-demo ${PN}-control"

SUMMARY:${PN}-dev = "ROS packages required for on target development"
ROS_BUILDESSENTIAL_PACKAGES = "\
    ament-lint-auto \
    ament-cmake-auto \
    ament-cmake-core \
    ament-cmake-cppcheck \
    ament-cmake-cpplint \
    ament-cmake-export-definitions \
    ament-cmake-export-dependencies \
    ament-cmake-export-include-directories \
    ament-cmake-export-interfaces \
    ament-cmake-export-libraries \
    ament-cmake-export-link-flags \
    ament-cmake-export-targets \
    ament-cmake-gmock \
    ament-cmake-gtest \
    ament-cmake-include-directories \
    ament-cmake-libraries \
    ament-cmake \
    ament-cmake-pytest \
    ament-cmake-python \
    ament-cmake-ros \
    ament-cmake-target-dependencies \
    ament-cmake-test \
    ament-cmake-version \
    ament-cmake-uncrustify \
    ament-cmake-flake8 \
    ament-cmake-pep257 \
    ament-copyright \
    ament-cpplint \
    ament-flake8 \
    ament-index-python \
    ament-lint-cmake \
    ament-mypy \
    ament-package \
    ament-pclint \
    ament-pep257 \
    ament-pycodestyle \
    ament-pyflakes \
    ament-uncrustify \
    ament-xmllint \
    cmake \
    eigen3-cmake-module \
    fastcdr \
    fastrtps-cmake-module \
    fastrtps \
    foonathan-memory-vendor \
    gmock-vendor \
    gtest-vendor \
    libyaml \
    libyaml-vendor \
    packagegroup-core-buildessential \
    python-cmake-module \
    python3-catkin-pkg \
    python3-colcon-common-extensions \
    python3-empy \
    python3 \
    python3-pytest \
    rcutils \
    rmw-implementation-cmake \
    rosidl-cmake \
    rosidl-default-generators \
    rosidl-generator-c \
    rosidl-generator-cpp \
    rosidl-generator-dds-idl \
    rosidl-generator-py \
    rosidl-parser \
    rosidl-runtime-c \
    rosidl-runtime-cpp \
    rosidl-typesupport-c \
    rosidl-typesupport-cpp \
    rosidl-typesupport-fastrtps-cpp \
    rosidl-typesupport-interface \
    rosidl-typesupport-introspection-c \
    rosidl-typesupport-introspection-cpp \
"

SUMMARY:${PN}-base = "ROS_BASE_PACKAGES includes ROS base packages"
ROS_BASE_PACKAGES = "\
    ros-base \
    cyclonedds \
    rmw-cyclonedds-cpp \
    tmux \
    python3-argcomplete \
    glibc-utils \
    localedef \
    rt-tests \
    stress \
    xrt-dev \
    xrt \
    kernel-module-zocl \
    opencl-headers-dev \
    opencl-clhpp-dev \
"

SUMMARY:${PN}-demo = "ROS_DEMO_PACKAGES includes ROS examples demos packages"
ROS_DEMO_PACKAGES = "\
    examples-rclcpp-minimal-action-client \
    examples-rclcpp-minimal-action-server \
    examples-rclcpp-minimal-client \
    examples-rclcpp-minimal-composition \
    examples-rclcpp-minimal-publisher \
    examples-rclcpp-minimal-service \
    examples-rclcpp-minimal-subscriber \
    examples-rclcpp-minimal-timer \
    examples-rclcpp-multithreaded-executor \
    examples-rclpy-executors \
    examples-rclpy-minimal-action-server \
    examples-rclpy-minimal-client \
    examples-rclpy-minimal-publisher \
    examples-rclpy-minimal-service \
    examples-rclpy-minimal-subscriber \
    demo-nodes-cpp \
    demo-nodes-cpp-rosnative \
"

SUMMARY:${PN}-control = "ROS_CONTROL_PACKAGES includes ROS control packages"
ROS_CONTROL_PACKAGES = "\
    controller-interface \
    controller-manager \
    controller-manager-msgs \
    hardware-interface \
    ros2-control \
    ros2controlcli \
    ros2-control-test-assets \
    transmission-interface \
"

RDEPENDS:${PN}-base:aarch64 = "\
    ${ROS_BASE_PACKAGES} \
"

RDEPENDS:${PN}-demo:aarch64 = "\
    ${ROS_BASE_PACKAGES} \
    ${ROS_DEMO_PACKAGES} \
"

#RDEPENDS:${PN}-control:aarch64 = "\
#    ${ROS_BASE_PACKAGES} \
#    ${ROS_CONTROL_PACKAGES} \
#"

RDEPENDS:${PN}-dev:aarch64 = "\
    ${ROS_BUILDESSENTIAL_PACKAGES} \
"

# TODO
# 1. Due to failing hardware-interfaces disable ${PN}-control

RDEPENDS:${PN}:aarch64 = "\
    ${PN}-demo \
    rqt-runtime-monitor \
"

IMAGE_LINGUAS = "en-us"
GLIBC_GENERATE_LOCALES = "en_US.UTF-8"
