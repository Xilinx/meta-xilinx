SUMMARY = "OpenAMP matrix-multiply RPU firmware payload."
DESCRIPTION = "Pre-built RPU OpenAMP firmware payload \
(matrix-multiplyd) loaded by the APU-side OpenAMP example application \
on Zynq UltraScale+ / Versal / Versal NET boards."
require openamp-fw.inc

PROVIDES:append:armv7r = " openamp-fw-mat-muld "
PROVIDES:append:armv8r = " openamp-fw-mat-muld "

OPENAMP_FW_NAME = "image_matrix_multiply"
OPENAMP_XLNX_RECIPE = "open-amp-xlnx-matrix"

RPROVIDES:${PN} += "openamp-fw-mat-muld"

python() {
    preferred = d.getVar('PREFERRED_PROVIDER_openamp-fw-mat-muld')
    if not preferred or preferred == d.getVar('PN'):
        d.setVar('BB_DONT_CACHE', '1')
        d.appendVar('PROVIDES', ' openamp-fw-mat-muld')
}
