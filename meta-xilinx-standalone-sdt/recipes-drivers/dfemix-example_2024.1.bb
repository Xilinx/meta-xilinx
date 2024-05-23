# Copyright (C) 2024 Advanced Micro Devices, Inc.  All rights reserved.
inherit esw_examples features_check

REQUIRED_MACHINE_FEATURES = "dfemix"

DEPENDS += " dfemix"

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/dfemix/examples/"
