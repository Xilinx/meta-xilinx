# meta-microblaze

This layer provides support specific to the MicroBlaze architecture

## Unsupported Content

This layer may contain code, machine configurations, and tune files for the
MicroBlaze V (MB-V) RISC-V soft processor and classic MicroBlaze soft
processor.

MicroBlaze-V (RISC-V) support will adjust OpenEmbedded-Core configurations
as necessary.

Classic MicroBlaze is only supported for baremetal applications, specifically
firmware for AMD FPGAs.  Linux and other operating systems are no longer
supported.

When used with AMD-EDF, MicroBlaze V is **not a supported configuration**
at this time.  Any sources in this layer are used for ongoing development,
but they are not validated, packaged or covered by the AMD Embedded
Development Framework (EDF) documentation, and no support is offered for
them.

## Dependencies

This layer depends on:

	URI: https://git.openembedded.org/openembedded-core
	layers: meta
	branch: wrynose

## Scripts

scripts/microblaze_dtb.py is a debugging helper; no recipe or class uses
it. It reads a device tree blob and prints AVAILTUNES and TUNE_FEATURES
settings for each MicroBlaze CPU described in it, for example:

	python3 scripts/microblaze_dtb.py -d system.dtb

It needs the Python libfdt module (pylibfdt).
