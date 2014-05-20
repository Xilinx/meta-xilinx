
# MicroBlaze does not correctly define __ELF__ despite being an ELF platform
CPPFLAGS_microblaze += " -D__ELF__ "

