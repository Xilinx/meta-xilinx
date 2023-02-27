# Booting OS Images onto AMD Xilinx target devices

AMD Xilinx Devices support different boot modes such as JTAG, SD, eMMC, QSPI etc.

## Booting Images with QEMU

Once images are built, you can simulate the image using QEMU emulator.
```
$ MACHINE=<target_mahcine_name> runqemu nographic
```

## Booting Images with Hardware

Follow booting instructions [README](docs) for more details.
