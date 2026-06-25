This readme file covers the tool:
  meta-xilinx-core/scripts/qemu-serial-port-tool

# Serial Port Tool

A utility for reading the AMD qemu-xilinx hardware DTB files to list
the serial ports.  It can also load domain specific DTB's to
correlate the QEMU serial ports into the ones available in a
specific domain.

qemu-xilinx style hardware device trees are different than the domain
specific device trees.  Both the format and contents are different due
to their different purposes.  The qemu-xilinx hardware device tree is
intended to define the hardware and how it is to be emulated.  The
domain specific device tree is a software view of available hardware
and how to use it.

## Usage

```
qemu-serial-port-tool <hardware-dtb | qemuboot.conf> \
    [--domain <domain-dtb> ...] [--stdout]
```

A hardware DTB (or a qemuboot.conf file containing a `qb_dtb`
entry) is required.  The output of the tool will list the
available serial ports in order of their definition.

If a qemuboot.conf file is provided, the tool will read the
`qb_dtb` value and use it as the hardware DTB path (resolved
relative to the conf file).

If one or more `--domain` values are passed, the tool will
correlate the domain's serial ports to the hardware dtb.

Adding in `--stdout` will produce a qemu style command line that
will set the stdout-path serial port to `mon:stdio`.

### Output

Hardware dtb only:

```
+-----------+---------------+--------------+------------+
| hw serial | device        | type         | address    |
+-----------+---------------+--------------+------------+
| serial0   | ppu0_mdm_uart | xps-uartlite | 0xf0110000 |
| serial1   | ppu1_mdm_uart | xps-uartlite | 0xf0310000 |
| serial2   | serial        | pl011        | 0xf1920000 |
| serial3   | serial        | pl011        | 0xf1930000 |
| serial4   | asu_mdm_uart  | xps-uartlite | 0xebef0000 |
+-----------+---------------+--------------+------------+
```

Hardware dtb + linux domain dtb w/ `--stdout`:

```
+-----------+---------------+---------------+---------------+------------+
| hw serial | domain serial | device        | type          | address    |
+-----------+---------------+---------------+---------------+------------+
| serial0   |               | ppu0_mdm_uart | xps-uartlite  | 0xf0110000 |
| serial1   |               | ppu1_mdm_uart | xps-uartlite  | 0xf0310000 |
| serial2   | serial0       | serial        | pl011         | 0xf1920000 |
| serial3   | serial1       | serial        | pl011         | 0xf1930000 |
| serial4   |               | asu_mdm_uart  | xps-uartlite  | 0xebef0000 |
|           | serial3       | coresight     | coresight-1.0 | 0xf0800000 |
+-----------+---------------+---------------+---------------+------------+

stdout-path: (serial1:115200n8)
domain serial1 is hardware serial3

qemu configuration: -serial null -serial null -serial null -serial mon:stdio -serial null
```

## How it works

The utility will accept a qemu-xilinx style hardware device tree.
Inside of this device tree will be hardware definitions for the
serial ports, including the ordering that qemu will process the
serial ports.  For example:

```dts
serial@0xf1920000 {
        compatible = "pl011";
        interrupts = <0x19>;
        reg = <0x00 0xf1920000 0x00 0x10000 0x00>;
        reset-gpios = <0x1a 0x05>;
        chardev = "serial2";
        phandle = <0x122>;
};

serial@0xf1930000 {
        compatible = "pl011";
        interrupts = <0x1a>;
        reg = <0x00 0xf1930000 0x00 0x10000 0x00>;
        reset-gpios = <0x1a 0x06>;
        chardev = "serial3";
        phandle = <0x123>;
};

asu_mdm_uart@0xebef0000 {
        compatible = "xlnx,xps-uartlite";
        reg = <0x00 0xebef0000 0x00 0x10 0x01>;
        chardev = "serial4";
        phandle = <0x1af>;
};

ppu0_mdm_uart@0xf0110000 {
        doc-status = "complete";
        compatible = "xlnx,xps-uartlite";
        reg-extended = <0xa2 0x00 0xf0110000 0x00 0x10 0x01>;
        chardev = "serial0";
};

ppu1_mdm_uart@0xf0310000 {
        doc-status = "complete";
        compatible = "xlnx,xps-uartlite";
        reg-extended = <0xc5 0x00 0xf0310000 0x00 0x10 0x01>;
        chardev = "serial1";
};
```

The above identifies five serial ports, serial0, serial1, serial2,
serial3, serial4 and serial5.  This correlates on the qemu command
line to the `-serial <value>` entries.  In this configuration, we
must defined five `-serial` entries for the qemu command line.  See
QEMU documentation for more information:
<https://www.qemu.org/docs/master/>

This utility is also be able to correlate these serial ports to one
or more domain specific device trees.  For example:

If the Linux domain device tree contains the following:

```dts
aliases {
        serial1 = "/axi/serial@f1930000";
        serial3 = "/axi/coresight@f0800000";
        nvmem1 = [00];
};

serial@f1920000 {
        phandle = <0x90>;
        bootph-all;
        compatible = "arm,pl011\0arm,primecell";
        status = "disabled";
        reg = <0x00 0xf1920000 0x00 0x1000>;
        interrupt-parent = <0x1b>;
        interrupts = <0x00 0x19 0x04>;
        reg-io-width = <0x04>;
        clock-names = "uartclk\0apb_pclk";
        clocks = <0xb6 0x5c 0xb6 0x52>;
        power-domains = <0xbc 0x18224021>;
        device_type = "serial";
        xlnx,clock-freq = <0x5f5dd19>;
        xlnx,uart-board-interface = "custom";
        xlnx,has-modem = <0x00>;
        xlnx,ip-name = "sbsauart";
        xlnx,baudrate = <0x1c200>;
        cts-override;
        port-number = <0x00>;
        xlnx,uart-clk-freq-hz = <0x5f5dd19>;
};

serial@f1930000 {
        phandle = <0x91>;
        bootph-all;
        compatible = "arm,pl011\0arm,primecell";
        status = "okay";
        reg = <0x00 0xf1930000 0x00 0x1000>;
        interrupt-parent = <0x1b>;
        interrupts = <0x00 0x1a 0x04>;
        reg-io-width = <0x04>;
        clock-names = "uartclk\0apb_pclk";
        clocks = <0xb6 0x5d 0xb6 0x52>;
        power-domains = <0xbc 0x18224022>;
        device_type = "serial";
        xlnx,clock-freq = <0x5f5dd19>;
        xlnx,uart-board-interface = "custom";
        xlnx,has-modem = <0x00>;
        xlnx,ip-name = "sbsauart";
        xlnx,baudrate = <0x1c200>;
        cts-override;
        port-number = <0x00>;
        xlnx,uart-clk-freq-hz = <0x5f5dd19>;
};

chosen {
        stdout-path = "serial1:115200n8";
        bootargs = "earlycon=pl011,mmio32,0xf1930000 console=ttyAMA1,115200";
};

__symbols__ {
        serial0 = "/axi/serial@f1920000";
        serial1 = "/axi/serial@f1930000";
};
```

Using the memory addresses it will identify Linux domain
serial0 correlates to the hardware serial serial2.  Then Linux
domain serial1 correlates to hardware serial3.  The Linux domain
serial3 has no correlation in the emulated hardware.

Further reviewing the `chosen` field indicates that the stdout-path
is defined as `serial1` in this domain, so hardware serial3 is the
standard output for this domain.  The QEMU serial port arguments
will direct all of the serial consoles to null, except for
this domain's output which will go to the special `mon:stdio`
device.

```
-serial null -serial null -serial null -serial mon:stdio -serial null
```

See: <https://qemu-project.gitlab.io/qemu/system/invocation.html>

## License

This tool is licensed under the MIT License - see the tool source
for more details.
