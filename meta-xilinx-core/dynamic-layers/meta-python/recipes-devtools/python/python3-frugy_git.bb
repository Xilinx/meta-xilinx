SUMMARY = "Python 3.3+'s frugy for Python 2.6, 2.7, 3.2."
DESCRIPTION = "FRU Generator YAML (frugy): build, edit and inspect \
IPMI Field-Replaceable-Unit (FRU) information records using a \
human-readable YAML schema."
HOMEPAGE = "https://github.com/MicroTCA-Tech-Lab/frugy"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=750d848625d8174091b953f2de0e8f8e"

FILESEXTRAPATHS: := "${THISDIR}/${PN}:"

SRC_URI = " \
    git://github.com/MicroTCA-Tech-Lab/frugy.git;branch=master;protocol=https \
    "

SRCREV = "aebe8142233667034153a1806ac29dd199acb99f"


inherit setuptools3

RDEPENDS:${PN} += " \
    python3-bitstruct \
    python3-pyyaml \
    python3-bidict \
    python3-macaddress \
    "
