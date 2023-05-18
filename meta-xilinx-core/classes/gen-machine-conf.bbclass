#Add scripts path with the tools to PATH to be able to use from eSDK
sdk_ext_postinst:append() {
    if [ -d $target_sdk_dir/layers/meta-xilinx/meta-xilinx-core/gen-machine-conf ]; then
        echo "export PATH=$target_sdk_dir/layers/meta-xilinx/meta-xilinx-core/gen-machine-conf:\$PATH" >> $env_setup_script
    fi
}
