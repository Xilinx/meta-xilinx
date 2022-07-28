# Using atomic C++ templates requires libatomic on microblaze
CXXFLAGS:append:microblaze = " -latomic"
