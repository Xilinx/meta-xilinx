/* Microblaze is microblaze_0 */
#define XILINX_USE_MSR_INSTR	1
#define XILINX_PVR		2
#define XILINX_FSL_NUMBER	0
#define XILINX_USE_ICACHE	1
#define XILINX_USE_DCACHE	1
#define XILINX_DCACHE_BYTE_SIZE	8192

/* Interrupt controller is interrupt_cntlr */
#define XILINX_INTC_BASEADDR		0x40100000
#define XILINX_INTC_NUM_INTR_INPUTS	8

/* Timer pheriphery is dual_timer_counter */
#define XILINX_TIMER_BASEADDR	0x40300000
#define XILINX_TIMER_IRQ	3

/* System Timer Clock Frequency */
#define XILINX_CLOCK_FREQ	100000000

/* Uart console is rs232_uart_1 */
#define XILINX_UART16550
#define XILINX_UART16550_BASEADDR	0x40400000
#define XILINX_UART16550_CLOCK_HZ	100000000
#define XILINX_UARTLITE_BASEADDR	0x40200000
#define CONFIG_CONS_INDEX		1

/* IIC pheriphery is iic_eeprom */
#define XILINX_IIC_0_BASEADDR	0x40a00000
#define XILINX_IIC_0_FREQ	100000
#define XILINX_IIC_0_BIT	0

/* GPIO doesn't exist */

/* SDIO doesn't exist */

/* Main Memory is ddr3_sdram */
#define XILINX_RAM_START	0x80000000
#define XILINX_RAM_SIZE		0x40000000

/* Flash Memory is linear_flash */
#define XILINX_FLASH_START	0x48000000
#define XILINX_FLASH_SIZE	0x08000000

/* Sysace doesn't exist */

/* Ethernet controller is soft_ethernet_mac */
#define XILINX_AXIEMAC_BASEADDR			0x50100000
#define XILINX_AXIDMA_BASEADDR			0x50000000
