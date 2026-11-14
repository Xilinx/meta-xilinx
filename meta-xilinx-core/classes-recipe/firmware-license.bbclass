#
# Copyright (C) 2026, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# Set LICENSE for recipes which deploy a firmware binary built elsewhere.
#
# FW_LICENSE_VARS lists the variables which name the recipe producing the
# binary, either in task dependency form ("recipe:task") or mcdepends form
# ("mc:from:to:recipe:task"). Each known producer is listed as
# FW_LICENSE_PRODUCER[<recipe>] = "<license>" using the producer's LICENSE.
#
# If every producer named is known, LICENSE is the producer's license. If an
# unknown recipe is named, or none is (so a binary supplied by the user is
# used), the source of the binary is unknown and LICENSE is
# LicenseRef-Proprietary.

FW_LICENSE_VARS ??= ""

# md5 of the generic license files, for LIC_FILES_CHKSUM
FW_LICENSE_CHKSUM[MIT] = "0835ade698e0bcf8506ecda2f7b4f302"
FW_LICENSE_CHKSUM[BSD-3-Clause] = "550794465ba0ec5312d6919e203a55f9"
FW_LICENSE_CHKSUM[LicenseRef-Proprietary] = "0557f9d92cf58f2ccdd50f62f8ac0b28"

def fw_producer_license(d):
    licenses = set()
    for var in (d.getVar('FW_LICENSE_VARS') or '').split():
        for dep in (d.getVar(var) or '').split():
            fields = dep.split(':')
            recipe = fields[3] if fields[0] == 'mc' and len(fields) > 3 else fields[0]
            licenses.add(d.getVarFlag('FW_LICENSE_PRODUCER', recipe))
    if len(licenses) != 1 or None in licenses:
        return 'LicenseRef-Proprietary'
    return licenses.pop()

def fw_license_chksum(d):
    license = d.getVar('LICENSE')
    md5 = d.getVarFlag('FW_LICENSE_CHKSUM', license)
    if not md5:
        return ''
    return 'file://${COMMON_LICENSE_DIR}/%s;md5=%s' % (license.replace('LicenseRef-', '', 1), md5)

LICENSE ??= "${@fw_producer_license(d)}"
LICENSE[vardeps] += "${FW_LICENSE_VARS} FW_LICENSE_PRODUCER"
LIC_FILES_CHKSUM ??= "${@fw_license_chksum(d)}"
