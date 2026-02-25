# Joban Client Mod

Joban Client Mod (Abbreviated as JCM) is an addon based on [Minecraft Transit Railway](https://github.com/jonafanho/Minecraft-Transit-Railway) Mod, adding various blocks from the Hong Kong MTR and utility blocks that will greatly improve your world.

Some of the blocks this mod adds including custom signal light, fare saver machine, the Railway Vision PIDS and more! 

![](https://user-images.githubusercontent.com/40461728/187031355-e71be327-e520-4add-aaba-dc9b6421fb44.png)

## FAQ & Support
### Why does my game crash?
There's a variety of reasons, one of the main reasons is that <b>you're using the wrong version of the MTR Mod</b>.  
Version labeled as <u>Pre-release</u> should only be used with a preview version of MTR Mod [in their Discord](https://discord.gg/hvddbya8rh).  
If you're unable to download the preview version of MTR, please download the versions labeled as <u>Release</u>.

### The game still crashes / I want to report a bug / I want to give suggestions
Most of the support are done in our Discord for easier communication, please join our [Discord Server](https://discord.gg/FNc2rgWmP2) here.

### I want to know more!
We have documented most parts of the mod in our [wiki](https://www.joban.tk/wiki/JCM:Joban_Client_Mod).

## Setup

1. Clone this repository
2. Sync the Gradle project
3. On First run:
   1. Sync the Gradle Project
   2. After finishing, Sync the Gradle Project again
   3. Restart IntelliJ IDEA

## Updating MTR Mod
MTR Mod is a required dependencies of Joban Client Mod.

To update MTR Mod to the latest version in your dev environment, run the `setupLibrary` task.

## Cross version development
We now use [Manifold](https://github.com/manifold-systems/manifold), which also supplies a preprocessor to conditionally apply java code.

A `MC_VERSION` is passed to the preprocessor, and it will look something like `11904`:  
- The 1st digit is the major version (1)
- The 2nd - 3rd digit is the main version (19 for 1.19)
- The 3nd - 4th digit is the minor version (04 for 1.19.4)

For example, if you want to apply code for 1.19.3 and above, but not anything else:
```
   #if MC_VERSION >= "11903"
      LOGGER.info("This line will appear in 1.19.3 and above")
   #else
      LOGGER.info("This line will appear in 1.19.2 or below")
   #endif
```

## Troubleshooting
### Command line is too long. Shorten the command line and rerun.
Enable Shorten Command Line (Edit Configuration)
<img src=https://i.imgur.com/1XKc6ts.png>

### Modules generated_XXXXXX and jsblock export package com.jsblock to module architectury
<b>Currently only Forge 1.16.5 works</b>

### Everything goes wrong, error after error
1. Delete `.gradle` folder in your user account folder
2. <b>[IMPORTANT]</b> commit and push any uncommited files
3. Delete the entire Joban Client Mod folder
4. Re-clone the project

## License

[Apache 2.0](https://github.com/DistrictOfJoban/Joban-Client-Mod/blob/main/LICENSE)
