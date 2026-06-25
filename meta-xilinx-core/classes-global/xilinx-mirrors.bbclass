# There is a bug in the git fetcher.  If it finds a mirror it will download
# and extract the mirror.  It then checks if the required commit is
# present.  If the commit is NOT present, it moves onto the next mirror.
#
# However, if the name of the mirror matches the expected name it never
# removes/renames/overwrites the bad mirror directory.
#
# So when the next step runs, it looks at the original name with the
# missing commit, even though a mirror name may be present.  We need
# to detect this situation, rename the bad mirror out of the way
# and symlink in the good mirror name to the expected name.
#
python xilinx_mirrors_prefetch() {
    import os

    dl_dir = d.getVar('DL_DIR')
    src_uri = (d.getVar('SRC_URI') or '').split()

    fetcher = bb.fetch2.Fetch(src_uri, d)
    mirrors = None

    for url in fetcher.urls:
        ud = fetcher.ud[url]

        # Check if the fetch needs to be corrected.
        if ud and ud.localpath and ud.method.need_update(ud, d):
            #bb.warn(f'Possibly out of date clone: {ud.clonedir}')

            if not mirrors:
                mirrors = bb.fetch2.mirror_from_string(d.getVar('PREMIRRORS') + d.getVar('MIRRORS'))

            uris, uds = bb.fetch2.build_mirroruris(ud, mirrors, d)

            for index, mirror_uri in enumerate(uris):
                mirror_ud = uds[index]

                if hasattr(mirror_ud.method,"build_mirror_data"):
                    try:
                        mirror_ud.method.build_mirror_data(mirror_ud, d)
                    except bb.fetch2.FetchError:
                        # Something is wrong, can't process this one
                        continue

                if not hasattr(mirror_ud,"clonedir"):
                    # Use clonedir as a trigger to check paths
                    continue

                # Attempt to assign this mirror to the 'real' name
                # but only if the mirror is up to date
                #
                # Check if the mirror has the required commit
                if not ud.method.need_update(mirror_ud, d):
                    # Rename the git2/<checkout>
                    src = ud.clonedir
                    dest = src
                    while os.path.exists(dest):
                        dest = dest + "_bad"
                    if src != dest:
                        #bb.warn(f'Rename {src} -> {dest}')
                        os.rename(src, dest)

                    # Now that is out of the way, we symlink the mirror
                    # to the original name
                    src = mirror_ud.clonedir
                    dest = ud.clonedir
                    relative_src = os.path.relpath(src, os.path.dirname(dest))
                    bb.warn(f'Fixing clone, linking mirror {relative_src} -> {dest}')
                    os.symlink(relative_src, dest)

                    break
}
do_fetch[postfuncs] += "xilinx_mirrors_prefetch"
# Exclude this function from do_fetch's task hash.  The function only
# creates DL_DIR symlinks at fetch time and has no effect on the build
# output.  Without this exclusion, adding the prefunc changes the
# do_fetch hash for every recipe, invalidating all sstate.
do_fetch[vardepsexclude] += "xilinx_mirrors_prefetch"
