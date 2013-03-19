# enforce the following configure setting, glib cannot resolve them for a cross
# target like microblaze/microblazeel.
#
# glib_cv_have_qsort_r=no
# glib_cv_long_long_format=I64
# glib_cv_stack_grows=no
# glib_cv_uscore=yes
# ac_cv_func_posix_getpwuid_r=yes
# ac_cv_func_posix_getgrgid_r=yes

#EXTRA_OECONF_append_microblaze = " glib_cv_have_qsort_r=no"
#EXTRA_OECONF_append_microblaze = " glib_cv_long_long_format=I64"
#EXTRA_OECONF_append_microblaze = " glib_cv_stack_grows=no"
#EXTRA_OECONF_append_microblaze = " glib_cv_uscore=yes"
#EXTRA_OECONF_append_microblaze = " ac_cv_func_posix_getpwuid_r=yes"
#EXTRA_OECONF_append_microblaze = " ac_cv_func_posix_getgrgid_r=yes"

#EXTRA_OECONF_append_microblazeel = " glib_cv_have_qsort_r=no"
#EXTRA_OECONF_append_microblazeel = " glib_cv_long_long_format=I64"
#EXTRA_OECONF_append_microblazeel = " glib_cv_stack_grows=no"
#EXTRA_OECONF_append_microblazeel = " glib_cv_uscore=yes"
#EXTRA_OECONF_append_microblazeel = " ac_cv_func_posix_getpwuid_r=yes"
#EXTRA_OECONF_append_microblazeel = " ac_cv_func_posix_getgrgid_r=yes"
