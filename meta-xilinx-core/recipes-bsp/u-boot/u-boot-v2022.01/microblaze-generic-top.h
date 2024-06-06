/*
 * This file is use for addition u-boot configurations for microblaze.
 */
#include <configs/microblaze-generic.h>

/* Core microblaze boot configurations */
#define XILINX_USE_DCACHE	1
#define CONFIG_CMD_IRQ
#define CONFIG_DCACHE
#define CONFIG_ICACHE