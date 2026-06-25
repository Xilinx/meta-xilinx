# While we don't support musl, we do need to eliminate a parse issue
EXTRA_OEMESON:microblaze = "-Dcpu=microblaze"

MSG ?= ""
MSG:microblaze = "musl is not supported on microblaze"

SKIP_RECIPE[libucontext] = "${MSG}"
