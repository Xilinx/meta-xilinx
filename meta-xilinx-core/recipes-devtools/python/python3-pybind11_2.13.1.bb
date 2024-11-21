SUMMARY = "Seamless operability between C++11 and Python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=774f65abd8a7fe3124be2cdf766cd06f"

SRC_URI[sha256sum] = "65be498b1cac516161add1508e65375674916bebf2570d057dc9c3c7bcbbc7b0"

inherit pypi python_setuptools_build_meta

BBCLASSEXTEND += "native"
