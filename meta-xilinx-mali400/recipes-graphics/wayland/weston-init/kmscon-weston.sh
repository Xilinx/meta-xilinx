# Launch Weston when running under kmscon and the configured default
# target is graphical. Uses get-default (reads config) rather than
# is-active (waits for target activation) to avoid blocking on
# network-online.target during boot.
# In multi-user target, kmscon provides a text console without Weston.
if [ -z "$WAYLAND_DISPLAY" ] && [ -n "$KMS_START_SCRIPT" ] \
   && [ "$(systemctl get-default)" = "graphical.target" ]; then
    exec kmscon-launch-gui weston --continue-without-input --log="${XDG_RUNTIME_DIR:-/tmp}/weston.log" 2>&1
fi
