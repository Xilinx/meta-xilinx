# Microblaze Versions are defined as features sets, each containing 
# a set of hardware features.

MBCCARGSVERSION = ""
MBPKGVERSION = ""
require conf/machine/include/microblaze/feature-microblaze-v8.inc
require conf/machine/include/microblaze/feature-microblaze-v7.inc

# Setup a 'feature set' conflict list which ensures only one version is selected
# when defining the TUNE_FEATURES_tune-<>  in local.conf file.
TUNECONFLICTS[v850] += "v730 v800 v810 v820 v830 v840"
TUNECONFLICTS[v840] += "v730 v800 v810 v820 v830 v850"
TUNECONFLICTS[v830] += "v730 v800 v810 v820 v840 v850"
TUNECONFLICTS[v820] += "v730 v800 v810 v830 v840 v850"
TUNECONFLICTS[v810] += "v730 v800 v820 v830 v840 v850"
TUNECONFLICTS[v800] += "v730 v810 v820 v830 v840 v850"
TUNECONFLICTS[v730] += "v800 v810 v820 v830 v840 v850"
