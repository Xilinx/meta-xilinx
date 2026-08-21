SUMMARY = "AMD GPU Unified SDK samples (OpenGL ES, OpenCL, Vulkan)"
DESCRIPTION = "OpenGL ES, OpenCL and Vulkan sample applications from the AMD GPU \
Unified SDK, with their runtime assets, installed under \
${datadir}/gpu-unified-sdk and classified into opencl/, vulkan/ and one \
opengles/<backend>/ directory per OpenGL ES windowing backend (grouped further \
into opengl_es_2.0/ and opengl_es_3.0/). SDK_PLATFORM selects the backend \
(wayland | x11 | fbdev), \
and also accepts a space-separated list or \"all\" to ship several; the OpenCL \
and Vulkan samples are backend independent and built once."
HOMEPAGE = "https://github.com/Xilinx/gpu-unified-sdk"
SECTION = "graphics"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f28795390e0ca5d6dac2ecd789bbc490"

PV .= "+git"

REPO ?= "git://github.com/Xilinx/gpu-unified-sdk.git;protocol=https"
BRANCH ?= "dev"
SRCREV ?= "710772bd1f3bf9e9e6fb736748bc6a2930bd87d3"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

SRC_URI = "${REPO};${BRANCHARG}"

# Known backends, in preference order.
def gpu_sdk_all_backends():
    return ('wayland', 'x11', 'fbdev')

# libmali-xlnx, or mesa with the lima gallium driver, which the layer's mesa.bbappend
# enables exactly when this feature is off.
def gpu_sdk_uses_libmali(d):
    return bb.utils.contains('DISTRO_FEATURES', 'libmali', True, False, d)

# The backends this machine can build: the driver has to provide one, and the distro
# feature that driver build hangs off has to be set. Also expands SDK_PLATFORM = "all".
def gpu_sdk_supported_backends(d):
    features = (d.getVar('DISTRO_FEATURES') or '').split()
    driver = gpu_sdk_driver_backends(d)
    backends = [b for b in gpu_sdk_all_backends()
                if b in driver and gpu_sdk_backend_feature(d, b) in features]
    # mesa has no fbdev EGL platform, so fbdev is a libmali-only backend.
    if not gpu_sdk_uses_libmali(d):
        backends = [b for b in backends if b != 'fbdev']
    return ' '.join(backends)

# Presets -> (GLES version groups, OpenCL, Vulkan, the MACHINE_FEATURE that calls for
# them, the backends their driver can run). Mirrors the top-level CMakeLists, keep in
# sync. The G78AE libMali has no x11; on lima, fbdev drops out again above.
def gpu_sdk_presets():
    return {
        'versal-2ve-2vm': (('opengl_es_2.0', 'opengl_es_3.0'), True, True, 'malig78ae', ('wayland', 'fbdev')),
        'zynqmp':         (('opengl_es_2.0',), False, False, 'mali400', ('wayland', 'x11', 'fbdev')),
    }

# An unknown or unset target enables nothing; do_configure makes it a hard error.
def gpu_sdk_preset(d):
    target = (d.getVar('SDK_PLATFORM_TARGET') or '').strip()
    return gpu_sdk_presets().get(target, ((), False, False, '', ()))

def gpu_sdk_gles_versions(d):
    return gpu_sdk_preset(d)[0]

# The preset this machine's GPU calls for, or "" if it advertises no known Mali block.
def gpu_sdk_expected_target(d):
    features = (d.getVar('MACHINE_FEATURES') or '').split()
    for target, preset in gpu_sdk_presets().items():
        if preset[3] in features:
            return target
    return ''

# Keyed off MACHINE_FEATURES, so a mismatched SDK_PLATFORM_TARGET cannot widen it.
def gpu_sdk_driver_backends(d):
    target = gpu_sdk_expected_target(d)
    if not target:
        return gpu_sdk_all_backends()
    return gpu_sdk_presets()[target][4]

# The distro feature a backend's driver build hangs off: for Mali-400 libmali and
# mesa/lima, which build one winsys per feature, the backend's own name; for the single
# G78AE library, serving both its backends, only wayland (its REQUIRED_DISTRO_FEATURES).
def gpu_sdk_backend_feature(d, backend):
    if gpu_sdk_expected_target(d) == 'versal-2ve-2vm':
        return 'wayland'
    return backend

# The wayland packages, for the two parts that need them. GlesFramework probes
# wayland-client/-egl/-protocols and wayland-scanner in its wayland branch alone, and
# links the two libraries, which ship in the "wayland" package. lib/sdk/Vulkan needs the
# same packages whatever the GLES backend is: it serves VK_KHR_wayland_surface, so it
# probes them unconditionally (REQUIRED, and a FATAL_ERROR without wayland-scanner) and
# links wayland-client. wayland-native is for the scanner, a build-time tool.
def gpu_sdk_wayland_deps(d, runtime):
    if ('wayland' not in (d.getVar('SDK_BUILD_PLATFORMS') or '').split()
            and d.getVar('SDK_HAVE_VULKAN') != '1'):
        return ''
    return 'wayland' if runtime else 'wayland wayland-native wayland-protocols'

# The X11 packages, same names either way. GlesFramework links libX11 and libXext in its
# x11 branch, and eglrenderingsample - fbdev only, see its CMakeLists - links them for
# the Xlib types it still names. A wayland-only build pulls in neither.
def gpu_sdk_x11_deps(d):
    if d.getVar('SDK_HAVE_OPENGLES') != '1':
        return ''
    backends = (d.getVar('SDK_BUILD_PLATFORMS') or '').split()
    if 'x11' not in backends and 'fbdev' not in backends:
        return ''
    return 'libx11 libxext'

# (name, group, backends) from samples/opengles/<group>/<sample>/CMakeLists.txt,
# enabled groups only. backends is empty for a sample that builds on any of them, or
# the list from a "# SDK_BACKENDS: <names>" marker for one that guards itself with
# SDK_Platform (eglrenderingsample, which drives the Mali fbdev path directly).
def gpu_sdk_gles_scan(d):
    import os, re
    root = os.path.join(d.getVar('S'), 'samples', 'opengles')
    versions = gpu_sdk_gles_versions(d)
    pat = re.compile(r'add_executable\(\s*([A-Za-z0-9_.-]+)')
    backend_pat = re.compile(r'^\s*#\s*SDK_BACKENDS:\s*(.+?)\s*$', re.M)
    found = []
    seen = set()
    for dirpath, _dirs, files in os.walk(root):
        if 'CMakeLists.txt' not in files:
            continue
        rel = os.path.relpath(dirpath, root).split(os.sep)
        if not rel or rel[0] in ('.', '') or rel[0] not in versions:
            continue
        try:
            with open(os.path.join(dirpath, 'CMakeLists.txt')) as f:
                text = f.read()
        except OSError:
            continue
        marker = backend_pat.search(text)
        backends = tuple(marker.group(1).split()) if marker else ()
        for m in pat.finditer(text):
            # Keyed by group too, so a name in both groups keeps both mappings.
            if (m.group(1), rel[0]) not in seen:
                seen.add((m.group(1), rel[0]))
                found.append((m.group(1), rel[0], backends))
    return found

# "<backend>=<t1>,<t2>,..." per backend built, for do_compile to pick from: a target
# CMake skips for a backend must not be asked for, or the build fails on its name.
# Deduplicated, since cmake --build cannot take the same target twice.
def gpu_sdk_gles_target_map(d):
    entries = []
    for backend in (d.getVar('SDK_BUILD_PLATFORMS') or '').split():
        names = []
        for name, _group, backends in gpu_sdk_gles_scan(d):
            if backends and backend not in backends:
                continue
            if name not in names:
                names.append(name)
        entries.append('%s=%s' % (backend, ','.join(names)))
    return ' '.join(entries)

# The same scan as "<name>:<group>", so do_install can restore the grouping. Not
# filtered by backend: it only classifies the binaries a backend actually built.
def gpu_sdk_gles_target_versions(d):
    return ' '.join('%s:%s' % (name, group) for name, group, _b in gpu_sdk_gles_scan(d))

# The in-tree xlnx.cmake (for standalone Yocto-SDK builds) is deliberately unused.
inherit cmake pkgconfig features_check

# Mali-400 builds on either driver; G78AE has no open one.
REQUIRED_DISTRO_FEATURES = "${@bb.utils.contains('MACHINE_FEATURES', 'malig78ae', 'libmali', '', d)}"

# The two SoCs the samples are built for; the MACHINE_FEATURES below then pick out the
# variants of those that carry a Mali block (not, say, a ZynqMP CG).
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal-2ve-2vm = "versal-2ve-2vm"

# mali400 on ZynqMP/Kria, malig78ae on Versal Gen 2.
ANY_OF_MACHINE_FEATURES = "mali400 malig78ae"

# Contents depend on SDK_PLATFORM_TARGET and SDK_PLATFORM, so machines sharing a tune
# must not share a package.
PACKAGE_ARCH = "${MACHINE_ARCH}"

# do_configure fails without SDK_PLATFORM_TARGET, so keep this out of "bitbake world".
EXCLUDE_FROM_WORLD = "1"

# --- Dependencies -----------------------------------------------------------
# Appended per sample set by the anonymous python below; the wayland and X11 build deps
# ride with the backends built instead. gbm/png/drm do belong here: libSdkCommon links
# them and is built for every sample set, but only ships with the flat APIs (see
# SDK_RDEPENDS_FLAT).
SDK_DEPENDS_COMMON = "virtual/egl virtual/libgles2 virtual/libgbm libpng libdrm"
# glm and the OpenCL packages are not in this layer's dependencies; they usually come
# from meta-openembedded/meta-oe, needed only by the versal-2ve-2vm target.
SDK_DEPENDS_VULKAN = "vulkan-loader vulkan-headers glm glslang-native"
SDK_DEPENDS_OPENCL = "opencl-headers opencl-icd-loader"

# --- Configure --------------------------------------------------------------
# The OpenGL ES backend(s): wayland | x11 | fbdev, a space-separated list of those, or
# "all" for SDK_PLATFORM_ALL. One CMake configuration builds exactly one backend, so
# every extra backend gets its own GLES-only pass.
#
# A backend is only usable where its GPU driver was built for it, so DISTRO_FEATURES has
# to carry the matching feature ("wayland" here by default, and on Versal Gen 2 whatever
# the backend, since the one G78AE library covers both of its backends), plus "libmali"
# for fbdev. Without it the recipe skips itself, naming the feature it wanted; bitbake
# repeats that message when something asks for the samples.
SDK_PLATFORM ?= "wayland"

# Gets the full OpenCL + Vulkan + GLES tree, when it is one of the backends being built;
# otherwise the first of those wins.
SDK_PRIMARY_PLATFORM ?= "wayland"
SDK_PLATFORM_ALL ?= "${@gpu_sdk_supported_backends(d)}"

# -> SDK_BUILD_PLATFORMS (every backend to build), SDK_PRIMARY, SDK_FLAT_APIS. Rejections
# are SkipRecipe, not bb.fatal: a fatal here fails the parse, aborting the whole bitbake
# invocation, including builds that never ask for the SDK.
python () {
    if not (d.getVar('SDK_PLATFORM_TARGET') or '').strip():
        return

    valid = gpu_sdk_all_backends()
    supported = gpu_sdk_supported_backends(d).split()
    driver = gpu_sdk_driver_backends(d)
    libmali = gpu_sdk_uses_libmali(d)
    plat = (d.getVar('SDK_PLATFORM') or '').strip()
    if plat == 'all':
        backends = (d.getVar('SDK_PLATFORM_ALL') or '').split()
        if not backends:
            raise bb.parse.SkipRecipe(
                "SDK_PLATFORM = \"all\" but no backend is available: DISTRO_FEATURES "
                "declares none of %s. Add the distro feature for the backend you want, "
                "e.g. DISTRO_FEATURES:append = \" wayland\"" % ', '.join(valid))
    else:
        backends = plat.split()
        if not backends:
            raise bb.parse.SkipRecipe(
                "SDK_PLATFORM is empty; set one of: %s all (or a space-separated list, "
                "e.g. \"fbdev wayland\")" % ' '.join(valid))

    # The primary only counts if it is one of the backends being built.
    primary_req = (d.getVar('SDK_PRIMARY_PLATFORM') or '').strip()
    primary = primary_req or backends[0]
    if primary not in backends:
        primary = backends[0]

    seen = []
    for b in backends:
        if b not in valid:
            raise bb.parse.SkipRecipe(
                "invalid backend '%s': SDK_PLATFORM must be one of %s or all (or a "
                "space-separated list of those), and SDK_PRIMARY_PLATFORM one of %s "
                "(SDK_PLATFORM='%s', SDK_PRIMARY_PLATFORM='%s')"
                % (b, ', '.join(valid), ', '.join(valid), plat, primary_req))
        # Before the feature test below, which would otherwise blame the fbdev feature.
        if b == 'fbdev' and not libmali:
            raise bb.parse.SkipRecipe(
                "backend 'fbdev' needs the libmali driver: without it this distro "
                "builds mesa with the lima gallium driver, whose EGL platforms are "
                "x11, wayland, gbm and surfaceless only. Add 'libmali' to "
                "DISTRO_FEATURES, or drop fbdev from SDK_PLATFORM "
                "(SDK_PLATFORM='%s')" % plat)
        # Also before it: the driver has no such build, whatever DISTRO_FEATURES says.
        if b not in driver:
            raise bb.parse.SkipRecipe(
                "backend '%s' is not supported by this machine's GPU driver, which "
                "provides %s only (MACHINE='%s'). Set SDK_PLATFORM accordingly, or "
                "build for a machine whose driver has that backend (SDK_PLATFORM='%s')"
                % (b, ', '.join(driver), d.getVar('MACHINE') or '', plat))
        # Without it, the samples would link here and find no driver on the target.
        if b not in supported:
            feature = gpu_sdk_backend_feature(d, b)
            raise bb.parse.SkipRecipe(
                "backend '%s' needs '%s' in DISTRO_FEATURES: without it this machine's "
                "GPU driver is not built for that backend. Add it with "
                "DISTRO_FEATURES:append = \" %s\", or drop the backend from SDK_PLATFORM "
                "(SDK_PLATFORM='%s', usable backends here: %s)"
                % (b, feature, feature, plat, ', '.join(supported) or 'none'))
        if b not in seen:
            seen.append(b)
    d.setVar('SDK_BUILD_PLATFORMS', ' '.join(seen))
    d.setVar('SDK_PRIMARY', primary)

    versions, opencl, vulkan = gpu_sdk_preset(d)[:3]
    d.setVar('SDK_HAVE_OPENGLES', '1' if versions else '0')
    d.setVar('SDK_HAVE_OPENCL', '1' if opencl else '0')
    d.setVar('SDK_HAVE_VULKAN', '1' if vulkan else '0')
    # Backend-independent sets, in do_install's order.
    d.setVar('SDK_FLAT_APIS', ' '.join(api for api, on in
                                       (('opencl', opencl), ('vulkan', vulkan)) if on))

    # Paired so the DEPENDS and RDEPENDS groups cannot drift apart.
    for flag, group in (('OPENGLES', 'COMMON'), ('VULKAN', 'VULKAN'), ('OPENCL', 'OPENCL')):
        if d.getVar('SDK_HAVE_' + flag) != '1':
            continue
        d.appendVar('DEPENDS', ' ' + (d.getVar('SDK_DEPENDS_' + group) or ''))
        d.appendVar('RDEPENDS:' + d.getVar('PN'), ' ' + (d.getVar('SDK_RDEPENDS_' + group) or ''))

    # These follow the backends built and the flat APIs, not the API groups.
    depends = ' '.join(x for x in (gpu_sdk_wayland_deps(d, False), gpu_sdk_x11_deps(d)) if x)
    if depends:
        d.appendVar('DEPENDS', ' ' + depends)
    rdepends = [gpu_sdk_wayland_deps(d, True), gpu_sdk_x11_deps(d)]
    if d.getVar('SDK_FLAT_APIS'):
        rdepends.append(d.getVar('SDK_RDEPENDS_FLAT') or '')
    rdepends = ' '.join(r for r in rdepends if r)
    if rdepends:
        d.appendVar('RDEPENDS:' + d.getVar('PN'), ' ' + rdepends)
}

OECMAKE_SOURCEPATH = "${S}"

# Which sample sets to build. MANDATORY, no default -- the samples are opt-in:
#   "versal-2ve-2vm" -> OpenGL ES 2.0 + 3.0, OpenCL, Vulkan (Mali G78AE)
#   "zynqmp"         -> OpenGL ES 2.0 only (Mali 400)
SDK_PLATFORM_TARGET ?= ""

# The backend-specific -DSDK_Platform is appended per pass in do_configure.
EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DSDK_PLATFORM_TARGET=${SDK_PLATFORM_TARGET} \
"

# One pass per backend, each in its own subdirectory of ${B}: cmake is called directly
# because the class drives a single build dir, and staying under ${B} keeps build paths
# out of the debug info (DEBUG_PREFIX_MAP already remaps ${B}).
do_configure() {
    # Fail at build time, not parse time: an accidental build must not ship empty.
    case "${SDK_PLATFORM_TARGET}" in
        versal-2ve-2vm|zynqmp)
            ;;
        "")
            bbfatal "SDK_PLATFORM_TARGET is not set. Building the GPU Unified SDK examples is opt-in: set SDK_PLATFORM_TARGET to 'versal-2ve-2vm' or 'zynqmp' in local.conf (e.g. SDK_PLATFORM_TARGET:pn-gpu-unified-sdk = \"zynqmp\")."
            ;;
        *)
            bbfatal "SDK_PLATFORM_TARGET='${SDK_PLATFORM_TARGET}' is invalid; use 'versal-2ve-2vm' or 'zynqmp'."
            ;;
    esac

    # A mismatch (Versal Gen 2 samples on a Mali-400 board) fails here, not on target.
    expected_target="${@gpu_sdk_expected_target(d)}"
    if [ -n "${expected_target}" ] && [ "${SDK_PLATFORM_TARGET}" != "${expected_target}" ]; then
        bbfatal "SDK_PLATFORM_TARGET='${SDK_PLATFORM_TARGET}' does not match MACHINE='${MACHINE}', whose MACHINE_FEATURES select '${expected_target}'. Set SDK_PLATFORM_TARGET to '${expected_target}' (e.g. SDK_PLATFORM_TARGET:pn-gpu-unified-sdk = \"${expected_target}\") or build for a matching machine."
    fi
    for backend in ${SDK_BUILD_PLATFORMS}; do
        bbnote "Configuring GPU Unified SDK (backend: ${backend})"
        backend_b="${B}/${backend}"
        mkdir -p "${backend_b}"
        cd "${backend_b}"
        cmake \
            ${OECMAKE_GENERATOR_ARGS} \
            ${OECMAKE_SOURCEPATH} \
            ${OECMAKE_ARGS} \
            ${EXTRA_OECMAKE} \
            -DSDK_Platform=${backend} \
            -Wno-dev
    done
}

SDK_GLES_TARGETS = "${@gpu_sdk_gles_target_map(d)}"

do_compile() {
    for backend in ${SDK_BUILD_PLATFORMS}; do
        backend_b="${B}/${backend}"
        if [ "${backend}" = "${SDK_PRIMARY}" ]; then
            bbnote "Compiling GPU Unified SDK full tree (primary backend: ${backend})"
            cmake --build "${backend_b}" --target ${OECMAKE_TARGET_COMPILE}
        else
            # One cmake --build for all of this backend's GLES targets, so ninja
            # schedules one graph.
            bbnote "Compiling GPU Unified SDK OpenGL ES samples (backend: ${backend})"
            gles_targets=""
            for entry in ${SDK_GLES_TARGETS}; do
                case "${entry}" in
                    "${backend}="*) gles_targets="$(echo "${entry#*=}" | tr ',' ' ')" ;;
                esac
            done
            if [ -n "${gles_targets}" ]; then
                cmake --build "${backend_b}" --target ${gles_targets}
            fi
        fi
    done
}

# Layout: opencl/<sample>/, vulkan/<sample>/ and
# opengles/<backend>/opengl_es_2.0/<sample>/ (and 3.0/).
SDK_INSTALL_ROOT = "${datadir}/gpu-unified-sdk"

# Run one subtree's cmake_install.cmake into a private DESTDIR, left in ${stage} for the
# caller to consume and remove. $1 = cmake_install.cmake path, $2 = stage tag.
gpu_sdk_stage() {
    if [ ! -f "$1" ]; then
        bbfatal "Install script missing: $1"
    fi
    stage="${WORKDIR}/install-stage-$2"
    rm -rf "${stage}"
    DESTDIR="${stage}" cmake -DCMAKE_INSTALL_PREFIX=${prefix} -P "$1"
}

# Install one sample set into ${D}${SDK_INSTALL_ROOT}:
#   flat <build dir> <api>      bin/<sample>/ goes straight under <api>/, with the SDK
#                               libraries in <api>/lib for the samples' RPATH.
#   gles <build dir> <backend>  bin/<sample>/ is regrouped by version under
#                               opengles/<backend>/, plus run_exports_<backend>.sh.
#
# --no-preserve=ownership throughout: do_compile does not run under pseudo, so its output
# carries the build user's uid/gid and would trip host-user-contaminated.
gpu_sdk_install_samples() {
    mode="$1"
    build_dir="$2"
    name="$3"

    # The stage tag cannot just be ${dest}: for gles that contains a "/".
    case "${mode}" in
        flat) subtree="samples/${name}"  ; dest="${name}"          ; tag="${name}" ;;
        gles) subtree="samples/opengles" ; dest="opengles/${name}" ; tag="gles-${name}" ;;
        *)    bbfatal "gpu_sdk_install_samples: unknown mode '${mode}'" ;;
    esac

    gpu_sdk_stage "${build_dir}/${subtree}/cmake_install.cmake" "${tag}"
    install -d "${D}${SDK_INSTALL_ROOT}/${dest}"

    if [ "${mode}" = "flat" ]; then
        if [ -d "${stage}${bindir}" ]; then
            cp -a --no-preserve=ownership "${stage}${bindir}/." "${D}${SDK_INSTALL_ROOT}/${dest}/"
        fi
        rm -rf "${stage}"

        gpu_sdk_stage "${build_dir}/lib/sdk/cmake_install.cmake" "${tag}-lib"
        install -d "${D}${SDK_INSTALL_ROOT}/${dest}/lib"
        # The SDK libs install to "<prefix>/lib"; be robust to lib vs lib64.
        for sodir in "${stage}${prefix}/lib" "${stage}${libdir}"; do
            if [ -d "${sodir}" ]; then
                cp -a --no-preserve=ownership "${sodir}/." "${D}${SDK_INSTALL_ROOT}/${dest}/lib/"
                break
            fi
        done
        rm -rf "${stage}"
        return
    fi

    if [ ! -d "${stage}${bindir}" ]; then
        rm -rf "${stage}"
        return
    fi
    for sample_path in "${stage}${bindir}"/*; do
        [ -e "${sample_path}" ] || continue
        sample="$(basename "${sample_path}")"
        version=""
        for pair in ${@gpu_sdk_gles_target_versions(d)}; do
            if [ "${pair%%:*}" = "${sample}" ]; then
                version="${pair##*:}"
                break
            fi
        done
        if [ -n "${version}" ]; then
            install -d "${D}${SDK_INSTALL_ROOT}/${dest}/${version}"
            cp -a --no-preserve=ownership "${sample_path}" "${D}${SDK_INSTALL_ROOT}/${dest}/${version}/"
        else
            bbwarn "No version group for GLES sample '${sample}'; installing flat"
            cp -a --no-preserve=ownership "${sample_path}" "${D}${SDK_INSTALL_ROOT}/${dest}/"
        fi
    done
    rm -rf "${stage}"

    # Note the uppercase "X11" in the file name.
    case "${name}" in
        x11)     run_script="run_exports_X11.sh" ;;
        wayland) run_script="run_exports_wayland.sh" ;;
        fbdev)   run_script="run_exports_fbdev.sh" ;;
        *)       run_script="" ;;
    esac
    if [ -n "${run_script}" ]; then
        src_script="${S}/samples/opengles/${run_script}"
        if [ -f "${src_script}" ]; then
            install -m 0755 "${src_script}" "${D}${SDK_INSTALL_ROOT}/${dest}/${run_script}"
        else
            bbwarn "Run-exports script not found: ${src_script}"
        fi
    fi
}

do_install() {
    # OpenCL and Vulkan are backend independent: installed once, from the primary build.
    for api in ${SDK_FLAT_APIS}; do
        bbnote "Installing GPU Unified SDK ${api} samples"
        gpu_sdk_install_samples flat "${B}/${SDK_PRIMARY}" "${api}"
    done

    # Every backend built ships its own GLES tree, the primary one included.
    if [ "${SDK_HAVE_OPENGLES}" = "1" ]; then
        for backend in ${SDK_BUILD_PLATFORMS}; do
            bbnote "Installing GPU Unified SDK OpenGL ES samples (backend: ${backend})"
            gpu_sdk_install_samples gles "${B}/${backend}" "${backend}"
        done
    fi
}

# --- Packaging --------------------------------------------------------------
# No FILES:${PN} append: SDK_INSTALL_ROOT is ${datadir}/${BPN}, which the default in
# bitbake.conf already covers.
#
# The libSdk* libraries are private, one copy per API folder, reached through an
# $ORIGIN/../lib RPATH; shipping the same SONAME twice would have the shlib resolver
# report "Multiple shlib providers".
EXCLUDE_FROM_SHLIBS = "1"

# Those .so files live under ${datadir} rather than ${libdir} on purpose (self-contained
# bundles), and -dbg needs the same skip for their .debug files. No "rpaths" skip: it
# must stay on to catch a build tree or sysroot path leaking into a RUNPATH.
INSANE_SKIP:${PN} += "libdir"
INSANE_SKIP:${PN}-dbg += "libdir"

# --- Runtime dependencies ----------------------------------------------------
# EXCLUDE_FROM_SHLIBS also disables the automatic shlib RDEPENDS, so these are by hand,
# as runtime package names (e.g. "wayland", not "libwayland-client0"); bash for the
# run_exports shebang. The libc is named per LIBCOVERRIDE below: nothing derives it from
# the ELF NEEDED entries here, and it is "glibc" or "musl" depending on TCLIBC.
#
# libmali-xlnx ships EGL, GLESv2 and both ICDs in one package and RPROVIDES libgbm; mesa
# splits those and its lima driver is in mesa-megadriver.
SDK_RDEPENDS_DRIVER = "${@'libmali-xlnx' if gpu_sdk_uses_libmali(d) else 'libegl-mesa libgles2-mesa mesa-megadriver'}"
# The X11 packages ride with the backends built instead, see gpu_sdk_x11_deps.
SDK_RDEPENDS_COMMON = "${SDK_RDEPENDS_DRIVER} libstdc++ libgcc bash"
SDK_RDEPENDS_COMMON:append:libc-glibc = " glibc"
SDK_RDEPENDS_COMMON:append:libc-musl = " musl"
# gbm, drm and png are linked by libSdkCommon alone, which ships with the flat APIs.
SDK_RDEPENDS_FLAT = "libgbm libdrm libpng"
# The loaders dlopen the matching Mali ICD out of libmali-xlnx.
SDK_RDEPENDS_VULKAN = "vulkan-loader"
SDK_RDEPENDS_OPENCL = "opencl-icd-loader"
