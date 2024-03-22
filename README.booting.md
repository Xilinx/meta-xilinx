# Booting OS Images onto AMD Xilinx target devices

AMD Xilinx Devices support different boot modes such as JTAG, SD, eMMC, QSPI etc.

## Booting Images with QEMU

Once images are built, you can simulate the image using QEMU emulator.

> **Note:** Use `slirp` option if you don't have sudo permissions and tap devices
  are enabled on your build host.

* Without slirp
```
$ MACHINE=<target_mahcine_name> runqemu nographic
```

* With slirp
```
$ MACHINE=<target_mahcine_name> runqemu nographic slirp
```

> **Note:** To terminate qemu, usually the command is ctrl-a x

## Booting Images with Hardware

Follow booting instructions [README](docs) for more details.
