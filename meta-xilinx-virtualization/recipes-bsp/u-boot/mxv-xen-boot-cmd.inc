# Add xen variables as addendum.
# Variable SCRIPT_SED_ADDENDUM is defined in meta-xilinx and set to null by
# default and enable only for xen builds.
SCRIPT_SED_ADDENDUM += "-e 's/@@XEN_IMAGE_NAME@@/${XEN_IMAGE_NAME}/' \
        -e 's/@@XEN_OFFSET@@/${XEN_OFFSET}/' \
        -e 's/@@XEN_LOAD_ADDRESS@@/${XEN_LOAD_ADDRESS}/' \
        -e 's/@@DOM0_MEM@@/${DOM0_MEM}/' \
        -e 's/@@DOM0_MAX_VCPUS@@/${DOM0_MAX_VCPUS}/' \
        -e 's:@@XEN_SERIAL_CONSOLES@@:${XEN_SERIAL_CONSOLES}:' \
        -e 's:@@XEN_CMDLINE_APPEND@@:${XEN_CMDLINE_APPEND}:' \
"
