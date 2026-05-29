# Compatibility shim: exposes BootimgPcbiosPlugin under the underscore module
# name expected by meta-virtualization's bootimg_biosxen.py.
# The canonical file uses a hyphenated name (bootimg-pcbios.py) which Python
# cannot import directly; load it via importlib and re-export the class.

import importlib.util
import os
import sys

_hyphen_path = os.path.join(os.path.dirname(__file__), "bootimg-pcbios.py")
_spec = importlib.util.spec_from_file_location(
    "wic.plugins.source._bootimg_pcbios_impl", _hyphen_path)
_mod = importlib.util.module_from_spec(_spec)
sys.modules[_spec.name] = _mod
_spec.loader.exec_module(_mod)

BootimgPcbiosPlugin = _mod.BootimgPcbiosPlugin
