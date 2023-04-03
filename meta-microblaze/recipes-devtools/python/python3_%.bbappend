# Microblaze's libpython seems to be named slightly differently
#  /usr/lib/python3.11/config-3.11/libpython3.11.a
FILES:libpython3-staticdev:append:microblaze = " ${libdir}/python${PYTHON_MAJMIN}/config-${PYTHON_MAJMIN}/libpython${PYTHON_MAJMIN}.a"
