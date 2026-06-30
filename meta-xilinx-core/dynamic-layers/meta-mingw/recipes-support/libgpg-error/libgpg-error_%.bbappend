# -P is being passed into windres, where it's not supported
CPPFLAGS:remove:mingw32 = "-P"
