# Microblaze Versions are defined as features sets, each containing 
# a set of hardware features.

MBCCARGSVERSION = ""
MBPKGVERSION = ""
require conf/machine/include/microblaze/feature-microblaze-v10.inc
require conf/machine/include/microblaze/feature-microblaze-v9.inc
require conf/machine/include/microblaze/feature-microblaze-v8.inc
require conf/machine/include/microblaze/feature-microblaze-v7.inc

# Setup a 'feature set' conflict list which ensures only one version is selected
# when defining the TUNE_FEATURES_tune-<>  in local.conf file.
TUNECONFLICTS[v10.0] += "v7.30 v8.00 v8.10 v8.20 v8.30 v8.40 v8.50 v9.0 v9.6"
TUNECONFLICTS[v9.6]  += "v7.30 v8.00 v8.10 v8.20 v8.30 v8.40 v8.50 v9.0 v10.0"
TUNECONFLICTS[v9.0]  += "v7.30 v8.00 v8.10 v8.20 v8.30 v8.40 v8.50 v9.6 v10.0"
TUNECONFLICTS[v8.50] += "v7.30 v8.00 v8.10 v8.20 v8.30 v8.40 v9.0 v9.6 v10.0"
TUNECONFLICTS[v8.40] += "v7.30 v8.00 v8.10 v8.20 v8.30 v8.50 v9.0 v9.6 v10.0"
TUNECONFLICTS[v8.30] += "v7.30 v8.00 v8.10 v8.20 v8.40 v8.50 v9.0 v9.6 v10.0"
TUNECONFLICTS[v8.20] += "v7.30 v8.00 v8.10 v8.30 v8.40 v8.50 v9.0 v9.6 v10.0"
TUNECONFLICTS[v8.10] += "v7.30 v8.00 v8.20 v8.30 v8.40 v8.50 v9.0 v9.6 v10.0"
TUNECONFLICTS[v8.00] += "v7.30 v8.10 v8.20 v8.30 v8.40 v8.50 v9.0 v9.6 v10.0"
TUNECONFLICTS[v7.30] += "v8.00 v8.10 v8.20 v8.30 v8.40 v8.50 v9.0 v9.6 v10.0"
