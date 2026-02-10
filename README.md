![](https://i.imgur.com/psmBBkG.png "Steves Carts")

[![](http://cf.way2muchnoise.eu/full_253462_downloads.svg)](https://minecraft.curseforge.com/projects/steves-carts-reborn) [![](http://cf.way2muchnoise.eu/versions/253462.svg)](https://minecraft.curseforge.com/projects/steves-carts-reborn) [![](https://img.shields.io/badge/Discord-TeamReborn-738bd7.svg)](https://discord.gg/teamreborn) [![Crowdin](https://d322cqt584bo4o.cloudfront.net/steves-carts/localized.svg)](https://crowdin.com/project/steves-carts)


Steve's Carts Reborn
===

All credit goes to VSWE for the orginal mod

Steve's Carts Reborn allows for the player to customize and design minecarts that can be used for automation. 

# License

Steve's Carts Reborn is licensed under the MIT license. Full license is  in **LICENSE.md**.

# Translation

Steve's Carts Reborn is available in a range of diffrent languages, if you want to help out translate the mod please see our crowdin project at [https://crowdin.com/project/steves-carts](https://crowdin.com/project/steves-carts) The translations are automaticly included in the jar files at build time.

 /proc/net/tcp" 
10073 10263 10289

A. Désinstaller les mises à jour de GMS
Puisqu'on a deux versions qui se battent, on va forcer le retour à la version d'usine.

Va dans Paramètres > Applications > Services Google Play.

Appuie sur les 3 petits points en haut à droite.

Choisis "Désinstaller les mises à jour".

Note : Si c'est grisé, il faut d'abord désactiver "Localiser mon appareil" dans les administrateurs de sécurité.

B. Nettoyer les résidus de Titanium / Root
Tape cette commande pour supprimer les fichiers temporaires où les malwares cachent leurs scripts :

PowerShell

adb shell rm -rf /data/local/tmp/*
C. Bloquer EasyShare (par précaution)
Si Marcus ne s'en sert pas, on va "geler" l'application qui a trop d'accès :

PowerShell

adb shell pm disable-user com.vivo.easyshare
adb forward --remove-all
adb reverse --remove-all


LOG QUI PORUIOVE TOUT SecurityException: Unknown calling package name 'com.google.android.gms' signatures=[98deb0ca], past signatures=[e3ca78d8...]