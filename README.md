# CurseforgeModpackDownloaderRemaster
 Another way to install curseforge modpacks
# Usage
1. Download The Zip file from the Curseforge.

2. Unzip the zip and find out the manifest.json file in the zip. 

3. Run `java -jar Curseforge.jar <Your Manifest.json's Position> <Output File Name>` in the folder containing this project.

4. It should display something like this:
```
Modpack Information:
Pack=Volcano Block by al132mc
Manifest=minecraftModpack of v1
Override=overrides

Minecraft Information:
MC Version=1.12.2
Modloader=[ModLoader [id=forge-14.23.5.2847, primary=true]]
```

5. Install Minecraft and modloaders on your native launcher while waiting the program to terminate.

6. Open the output file and download all the links.

7. Put all the downloaded mods into the "mods" version and copy all the content of "override" in the zip folder to ".minecraft"

8. Open the game and you should be set!
