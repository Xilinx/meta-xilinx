# MicroBlaze versions are defined as features, the features are setup to
# conflict with other versions as well as unavailable features for particular
# versions.

def microblaze_parse_version(s):
    # versions before v9 use the "vX.YY.Z" scheme where Z = [ab]
    # versions after v8 use the "vX.Y" scheme
    import re
    m = re.search("^v(\d+)\.(\d+)(\.([ab]))?", s)
    if m:
        major, minor = int(m.group(1)), int(m.group(2))
        if major < 9:
            return (major, minor, m.group(4) or "a")
        return (major, minor)
    return None

def microblaze_version_conflict(ver, d):
    tunes = d.getVarFlags("TUNEVALID").keys()
    conflict = []
    version = microblaze_parse_version(ver)
    for i in tunes:
        iversion = microblaze_parse_version(i)
        if iversion and iversion != version:
            conflict.append(i)
    return " ".join(conflict)

def microblaze_current_version(d, gcc = False):
    tunes = (d.getVar("TUNE_FEATURES") or "").split()
    version = next((v for v in [microblaze_parse_version(i) for i in tunes] if v), None)
    if version:
        if version[0] <= 8 and len(version) > 2 and (gcc or version[2] != "a"):
            return "v%d.%02d.%s" % version[0:3]
        else:
            return "v%d.%d" % version[0:2]
    return ""

# MicroBlaze versions
TUNEVALID[v8.00] = "MicroBlaze version 8.00"
TUNEVALID[v8.10] = "MicroBlaze version 8.10"
TUNEVALID[v8.20] = "MicroBlaze version 8.20"
TUNEVALID[v8.30] = "MicroBlaze version 8.30"
TUNEVALID[v8.40] = "MicroBlaze version 8.40"
TUNEVALID[v8.50] = "MicroBlaze version 8.50"
TUNEVALID[v9.0] = "MicroBlaze version 9.0"
TUNEVALID[v9.1] = "MicroBlaze version 9.1"
TUNEVALID[v9.2] = "MicroBlaze version 9.2"
TUNEVALID[v9.3] = "MicroBlaze version 9.3"
TUNEVALID[v9.4] = "MicroBlaze version 9.4"
TUNEVALID[v9.5] = "MicroBlaze version 9.5"
TUNEVALID[v9.6] = "MicroBlaze version 9.6"
TUNEVALID[v10.0] = "MicroBlaze version 10.0"

# Version conflict matrix
TUNECONFLICTS[v8.00] := "${@microblaze_version_conflict('v8.00', d)}"
TUNECONFLICTS[v8.10] := "${@microblaze_version_conflict('v8.10', d)}"
TUNECONFLICTS[v8.20] := "${@microblaze_version_conflict('v8.20', d)}"
TUNECONFLICTS[v8.30] := "${@microblaze_version_conflict('v8.30', d)}"
TUNECONFLICTS[v8.40] := "${@microblaze_version_conflict('v8.40', d)}"
TUNECONFLICTS[v8.50] := "${@microblaze_version_conflict('v8.50', d)}"
TUNECONFLICTS[v9.0] := "${@microblaze_version_conflict('v9.0', d)}"
TUNECONFLICTS[v9.1] := "${@microblaze_version_conflict('v9.1', d)}"
TUNECONFLICTS[v9.2] := "${@microblaze_version_conflict('v9.2', d)}"
TUNECONFLICTS[v9.3] := "${@microblaze_version_conflict('v9.3', d)}"
TUNECONFLICTS[v9.4] := "${@microblaze_version_conflict('v9.4', d)}"
TUNECONFLICTS[v9.5] := "${@microblaze_version_conflict('v9.5', d)}"
TUNECONFLICTS[v9.6] := "${@microblaze_version_conflict('v9.6', d)}"
TUNECONFLICTS[v10.0] := "${@microblaze_version_conflict('v10.0', d)}"

# Version/feature conflicts
TUNECONFLICTS[v8.00] += "reorder"
TUNECONFLICTS[v8.10] += "reorder"
TUNECONFLICTS[v8.20] += "reorder"

# Version flags
TUNE_CCARGS += "-mcpu=${@microblaze_current_version(d, True)}"
MBPKGARCH_VERSION = "-${@microblaze_current_version(d)}"

