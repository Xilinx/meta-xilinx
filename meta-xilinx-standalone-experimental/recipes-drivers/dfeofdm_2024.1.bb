# Copyright (C) 2024 Advanced Micro Devices, Inc.  All rights reserved.
inherit features_check

REQUIRED_MACHINE_FEATURES = "dfeofdm"

inherit esw python3native

DEPENDS += "xilstandalone libmetal "

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dfeofdm/src/"
ESW_COMPONENT_NAME = "libdfeofdm.a"

addtask do_generate_driver_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"
