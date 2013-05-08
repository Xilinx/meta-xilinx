/* Microblaze is microblaze_0 */
#define XILINX_USE_MSR_INSTR	1
#define XILINX_PVR		2
#define XILINX_FSL_NUMBER	0
#define XILINX_USE_ICACHE	1
#define XILINX_USE_DCACHE	1
#define XILINX_DCACHE_BYTE_SIZE	16384

/* Interrupt controller is microblaze_0_intc */
#define XILINX_INTC_BASEADDR		0x41200000
#define XILINX_INTC_NUM_INTR_INPUTS	5

/* Timer pheriphery is system_timer */
#define XILINX_TIMER_BASEADDR	0x41c00000
#define XILINX_TIMER_IRQ	2

/* System Timer Clock Frequency */
#define XILINX_CLOCK_FREQ	100000000

/* Uart console is rs232_uart_1 */
#define XILINX_UART16550
#define XILINX_UART16550_BASEADDR	0x40400000
#define XILINX_UART16550_CLOCK_HZ	100000000
#define XILINX_UARTLITE_BASEADDR	0x41400000
#define CONFIG_CONS_INDEX		1

/* IIC doesn't exist */

/* GPIO is dip_switches_8bits*/
#define XILINX_GPIO_BASEADDR	0x40060000

/* SDIO doesn't exist */

/* Main Memory is ddr3_sdram */
#define XILINX_RAM_START	0x80000000
#define XILINX_RAM_SIZE		0x08000000

/* Flash Memory is linear_flash */
#define XILINX_FLASH_START	0xa0000000
#define XILINX_FLASH_SIZE	0x08000000

/* Sysace doesn't exist */

/* Ethernet controller is ethernet */
#define XILINX_AXIEMAC_BASEADDR			0x44f40000
#define XILINX_AXIDMA_BASEADDR			0x41e00000
