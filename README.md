# LiteAccountSwitcher
Ingame account switcher for LiteLoader

Do you get annoyed that your Minecraft takes too long to start up when you just want to switch to your alt account? 
Well this mod is for you! There are mods out there for Forge but I didn't see any updated LiteLoader ones, so I took 
Its_its's outdated [AltManager](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2459189-account-manager-v1-6-login-to-your-accounts) and pretty much revamped it. Mine looks better if I may say so myself o3o

![](http://puu.sh/raQky/208ebf5fe8.png)


###How to Use
From the main menu, multiplayer menu, or ingame you can use the key binding to open up the account manager. 
The key binding to open up the account manager can be changed in Options > Controls; it's set to **P** by default.

Obviously, the list will be blank when you first use it. At this point, I would recommend choosing your secret key first, 
so click on the [?] next to the text box to see information about what it is, also quoted below:

    Your key is the "password" that is used to encrypt your login 
    credentials. Think of it as an extra layer of security so that someone 
    who finds the files on your computer won't be able to immediately steal 
    your accounts. When you add a new account to the user list, the current 
    key you have entered will be used as the password for that account. 
    Logging into any accounts afterwards will require their respective keys.

It is recommended that you use a custom key, since the login credentials are stored in your liteconfig/common/ folder 
(the password is encrypted using the key), it's remotely possible that a virus steals your information. 
The text box will save your key for as long as you have Minecraft still open; if you re-launch Minecraft, 
you will need to enter your key again to switch to different accounts.

When adding a new account, enter only your username and the optional "short note" section if you want the account to be cracked only. 
If you want to use premium, enter your Mojang login credentials, usually an email, along with your password. 
If you want to be sure that I'm not stealing your account information, the source code is available here on Github, 
or you can decompile the mod, whatever, I don't care.

Just like the multiplayer server list menu, you can move entries up and down and move them all the way up or all the way 
down if you hold down shift while clicking on the arrows. Double-click on the entry or click on the right-pointing triangle 
to use the account; if there is a password entered for the account, auto-login will use premium mode to log in.

You'll need to reconnect in order to have the name change take effect if you change while ingame. 

###Known Issues
There has been a report of getting disconnected when moving servers within a network (e.g. move from Build1 to Build4 on TE), but it may have to do with the player using Forge as well, since I haven't been able to reproduce this with running just Litemods. 

###Installation

#####Getting to the .minecraft Folder
Here are two easy ways to access the .minecraft folder:

**From ingame:** Options > Resource Packs > Open resource pack folder. From there, just go up one level in your file browser and that will be .minecraft.

**From the start menu:** Type %appdata% and press enter. This will bring you to the Roaming folder, and .minecraft will be in there.
#####Getting LiteLoader
This mod uses LiteLoader!

If you're using a modpack, you can check the .minecraft folder. If you see a folder called "liteconfig" you also have LiteLoader. 
Make sure it's the right version!

If you do NOT have LiteLoader, you can download it from here. Make sure you are on the 1.10.2 tab and download the LiteLoaderInstaller.
#####Installing LiteAccountSwitcher
Once you have LiteLoader, the rest is easy. Simply download the LiteAccountSwitcher mod from the below link and drag it into the 1.10.2 folder under mods folder under the .minecraft folder.


###Download
####For Minecraft version 1.10.2
[v1.1.0] [MediaFire](http://www.mediafire.com/download/c2g10fxsdpbxb29/mod-liteaccountswitcher-1.1.0-mc1.10.2.litemod)  |  [Github](https://github.com/Kyzderp/LiteAccountSwitcher/releases)

###Changelog

#####1.0.0
* Initial private-ish release (used TE staff as guinea pigs)

#####1.1.0
* Added double-click to use account
* Added "notes" section
* Release to public
