FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

B = "${S}"
SRC_URI:remove = "file://0001-CMakeLists.txt-fetch-dependencies-with-bitbake-fetch.patch"
SRC_URI:append = " file://0001-fix-include-dir.patch"
SRCREV_release:forcevariable = "6884e7ba7d29d8db98ad2bbf09a6875bf5141e19"
