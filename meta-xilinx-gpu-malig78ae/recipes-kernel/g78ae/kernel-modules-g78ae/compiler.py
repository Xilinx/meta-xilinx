#!/usr/bin/env python3

import argparse
from dataclasses import dataclass, field
import os
import shutil
import subprocess


@dataclass
class Module:
	name: str
	path: str
	includes: list[str]
	options: list[str]
	debug: bool = False
	use_common_options: bool = True
	extra_symbols: list[str] = None


def get_abspath(cmd: str):
	if cmd and os.path.dirname(cmd):
		return os.path.abspath(cmd)
	return cmd


def kbuild_to_cflag(option: str):
	key, value = option.split('=', maxsplit=1)
	if value in ['m', 'y']:
		return f'-D{key}=1'
	if value == "n":
		return f'-U{key}'
	if value.isdigit():
		return f'-D{key}={value}'
	return str.format("-D{}='\"{}\"'", key, value)


def parse_args():
	parser = argparse.ArgumentParser()
	parser.add_argument('--driver', required=True, help='Path to the source driver directory')
	parser.add_argument('--debug', required=False, action='store_true', help='Enable debug build')
	parser.add_argument('--make', required=False, help='make command')
	parser.add_argument('--kernel', required=False, help='Path to the kernel build artifacts')
	parser.add_argument('--arch', required=False, help='arm64')
	parser.add_argument('--cross-compile', required=False, help='CROSS_COMPILE prefix')
	parser.add_argument('--target-prefix', required=False, help='Remap __FILE__ from source driver directory into target prefix')
	parser.add_argument('--install', required=False, help='Install directory to copy .ko files into')
	return parser.parse_args()


class Compiler:
	modules = [
		Module(
			name='dma-buf-test-exporter',
			path='base/arm/dma_buf_test_exporter',
			includes=[
			],
			options=[
				'CONFIG_DMA_SHARED_BUFFER_TEST_EXPORTER=y',
			],
			use_common_options=False,
		),
		Module(
			name='kutf',
			debug=True,
			path='gpu/arm/midgard/tests/kutf',
			includes=[
				'gpu/arm/midgard/tests/include',
				'gpu/arm',
				'gpu/arm/midgard',
				'gpu/arm/midgard/tests',
			],
			options=[
				'CONFIG_MALI_KUTF=y',
			],
		),
		Module(
			name='mali_arbiter',
			path='gpu/arm/arbitration/arbiter/mali_arbiter',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_ARBITER_MODULES=m',
			],
		),
		Module(
			name='mali_emu_kbase',
			path='gpu/arm/arbitration/tests/mali_emu_kbase',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_ARBITER_MODULES=m',
			],
		),
		Module(
			name='mali_gpu_assign',
			path='gpu/arm/arbitration/ptm/mali_gpu_assign',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_PARTITION_MANAGER=m',
			],
		),
		Module(
			name='mali_gpu_aw',
			path='gpu/arm/arbitration/ptm/mali_gpu_aw',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_PARTITION_MANAGER=m',
			],
		),
		Module(
			name='mali_gpu_partition_config',
			path='gpu/arm/arbitration/ptm/mali_gpu_partition_config',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_PARTITION_MANAGER=m',
			],
		),
		Module(
			name='mali_gpu_partition_control',
			path='gpu/arm/arbitration/ptm/mali_gpu_partition_control',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_PARTITION_MANAGER=m',
			],
		),
		Module(
			name='mali_gpu_power',
			path='gpu/arm/arbitration/power/mali_gpu_power',
			includes=[
				'drivers/gpu/arm/arbitration',
				'drivers/gpu/arm/arbitration/ptm/common',
				'drivers/gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_GPU_POWER_MODULES=m',
			],
		),
		Module(
			name='mali_gpu_resource_group',
			path='gpu/arm/arbitration/ptm/mali_gpu_resource_group',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_PARTITION_MANAGER=m',
			],
			extra_symbols=[
				'mali_arbiter',
			],
		),
		Module(
			name='mali_gpu_system',
			path='gpu/arm/arbitration/ptm/mali_gpu_system',
			includes=[
				'gpu/arm/arbitration',
				'gpu/arm/arbitration/ptm/common',
				'gpu/arm/midgard',
			],
			options=[
				'CONFIG_MALI_PARTITION_MANAGER=m',
			],
		),
		Module(
			name='mali_kbase',
			path='gpu/arm/midgard',
			includes=[
			],
			options=[
				'CONFIG_MALI_MIDGARD=m',
				'CONFIG_MALI_KUTF=n',
			],
		),
		Module(
			name='memory_group_manager',
			path='base/arm/memory_group_manager',
			includes=[
			],
			options=[
				'CONFIG_MALI_MEMORY_GROUP_MANAGER=y',
			],
			use_common_options=False,
		),
		Module(
			name='mali_kutf_clk_rate_trace_test_portal',
			debug=True,
			path='gpu/arm/midgard/tests/mali_kutf_clk_rate_trace/kernel',
			includes=[
				'gpu/arm/midgard/tests/include',
				'gpu/arm',
				'gpu/arm/midgard',
				'gpu/arm/midgard/tests',
			],
			options=[
				'CONFIG_MALI_KUTF=y',
				'CONFIG_MALI_KUTF_CLK_RATE_TRACE=y',
			],
			extra_symbols=[
				'mali_kbase',
				'kutf',
			],
		),
		Module(
			name='mali_kutf_irq_test',
			debug=True,
			path='gpu/arm/midgard/tests/mali_kutf_irq_test',
			includes=[
				'gpu/arm/midgard/tests/include',
				'gpu/arm',
				'gpu/arm/midgard',
				'gpu/arm/midgard/tests',
			],
			options=[
				'CONFIG_MALI_KUTF=y',
				'CONFIG_MALI_KUTF_IRQ_TEST=y',
			],
			extra_symbols=[
				'mali_kbase',
				'kutf',
			],
		),
		Module(
			name='mali_kutf_mgm_integration_test',
			debug=True,
			path='gpu/arm/midgard/tests/mali_kutf_mgm_integration_test',
			includes=[
				'gpu/arm/midgard/tests/include',
				'gpu/arm',
				'gpu/arm/midgard',
				'gpu/arm/midgard/tests',
			],
			options=[
				'CONFIG_MALI_KUTF=y',
				'CONFIG_MALI_KUTF_MGM_INTEGRATION_TEST=y',
			],
			extra_symbols=[
				'mali_kbase',
				'kutf',
			],
		),
	]


	def __init__(self, driver_dir: str, debug: bool):
		self.driver_dir = driver_dir
		self.debug = debug


	def get_module_dir(self, module: Module):
		return f'{self.driver_dir}/drivers/{module.path}'


	def get_module_ko_filename(self, module: Module):
		return f'{module.name}.ko'


	def get_module_for_name(self, name: str):
		for module in Compiler.modules:
			if module.name == name:
				return module


	def compile(self, make_cmd: list, kernel_dir: str, arch: str, cross_compile: str,
			 target_prefix: str):
		common_options = [
			'CONFIG_LARGE_PAGE_SUPPORT=y',
			'CONFIG_MALI_ARBITER_SUPPORT=y',
			'CONFIG_MALI_DEVFREQ=y',
			'CONFIG_MALI_FENCE_DEBUG=y',
			'CONFIG_MALI_FW_TRACE_MODE_MANUAL=y',
			'CONFIG_MALI_GATOR_SUPPORT=y',
			'CONFIG_MALI_PLATFORM_NAME=devicetree',
			'CONFIG_MALI_PRFCNT_SET_PRIMARY=y',
			'CONFIG_MALI_REAL_HW=y',
			'CONFIG_MALI_TRACE_POWER_GPU_WORK_PERIOD=y',
			'CONFIG_PAGE_MIGRATION_SUPPORT=y',
			'MALI_BASE_CSF_PERFORMANCE_TESTS=0',
			'MALI_CUSTOMER_RELEASE=1',
			'MALI_JIT_PRESSURE_LIMIT_BASE=1',
			'MALI_UNIT_TEST=0',
			'MALI_USE_CSF=0',
		]

		if self.debug:
			common_options += [
				'CONFIG_MALI_DEBUG=y',
				'CONFIG_MALI_MIDGARD_ENABLE_TRACE=y',
				'CONFIG_MALI_SYSTEM_TRACE=y',
				'MALI_KERNEL_TEST_API=1',
			]

		common_cflags=[
			'-DKBUILD_EXTRA_WARN1',
			'-DKBUILD_EXTRA_WARN2',
			'-Wall',
			'-Wdisabled-optimization',
			'-Werror',
			'-Wextra',
			'-Wframe-larger-than=4096',
			'-Wlogical-op',
			'-Wmaybe-uninitialized',
			'-Wmissing-declarations',
			'-Wmissing-field-initializers',
			'-Wmissing-format-attribute',
			'-Wmissing-prototypes',
			'-Wno-cast-function-type',
			'-Wno-ignored-qualifiers',
			'-Wno-shift-negative-value',
			'-Wno-sign-compare',
			'-Wno-type-limits',
			'-Wno-unused-parameter',
			'-Wold-style-definition',
			'-Wpacked-not-aligned',
			'-Wstringop-truncation',
			'-Wunused',
			'-Wunused-but-set-variable',
			'-Wunused-const-variable',
			'-Wunused-macros',
		]

		if not target_prefix is None:
			common_cflags += [
				f'-fmacro-prefix-map={self.driver_dir}={target_prefix}',
				f'-fdebug-prefix-map={self.driver_dir}={target_prefix}',
			]

		for module in Compiler.modules:
			if module.debug and not self.debug:
				# skip this debug module
				continue

			module_dir = self.get_module_dir(module)
			ko_file_name = self.get_module_ko_filename(module)

			# build .ko files
			options = module.options + (common_options if module.use_common_options else [])
			includes = ['include'] + [f'drivers/{include}' for include in module.includes]

			include_args = [f'-I{self.driver_dir}/{include}' for include in includes]
			define_args = [kbuild_to_cflag(option) for option in options]

			if not module.extra_symbols is None:
				extra_symbols_paths = [f'{self.driver_dir}/drivers/{self.get_module_for_name(name).path}/Module.symvers' for name in module.extra_symbols]
				extra_symbols_args = [f'KBUILD_EXTRA_SYMBOLS={" ".join(extra_symbols_paths)}']
			else:
				extra_symbols_args = []

			env = os.environ
			env['KBUILD_VERBOSE'] = '1'

			# build the .ko
			subprocess.run([*make_cmd, '-C', kernel_dir,
				f'M={module_dir}',
				f'EXTRA_CFLAGS={" ".join(include_args + define_args + common_cflags)}',
				*options,
				f'ARCH={arch}',
				f'CROSS_COMPILE={get_abspath(cross_compile)}',
				*extra_symbols_args,
			], env=env, check=True)


	def install(self, install_dir: str):
		os.makedirs(install_dir, exist_ok=True)

		for module in Compiler.modules:
			if module.debug and not self.debug:
				continue

			module_dir = self.get_module_dir(module)
			ko_file_name = self.get_module_ko_filename(module)

			# copy .ko files into the install dir
			shutil.copyfile(f'{module_dir}/{ko_file_name}', f'{install_dir}/{ko_file_name}')


def main():
	args = parse_args()

	compiler = Compiler(args.driver, args.debug)

	if not args.install:
		compiler.compile(args.make.split(), args.kernel, args.arch, args.cross_compile,
				args.target_prefix)
	else:
		compiler.install(args.install)


if __name__ == '__main__':
	main()
