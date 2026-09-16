# Security Policy

## How to report a potential vulnerability

If you would like to report an issue that is already public (for example, one
with a released CVE number), report it through the contribution process this
repository describes in its README or MAINTAINERS file, and submit a patch the
same way as any other patch if you have one ready.

If you are dealing with a not-yet-published or urgent issue, do not use any
public channel. Report it to the AMD Product Security Incident Response Team
(PSIRT) through the AMD product security page, which also carries AMD's
vulnerability disclosure policy and the address to which to send reports:

https://www.amd.com/en/resources/product-security.html

Either way, include as many details as possible: the layer and recipe
affected, the recipe version, the branch or release against which you observed
the issue, and example code or a reproducer where one is available.

## Scope

This policy covers the build metadata in this repository: recipes, classes,
machine and distro configuration, and the patches carried here.

It does not cover the software a recipe builds, or the build tooling itself.
Report those to the project that maintains them, through whatever security
process that project publishes.
