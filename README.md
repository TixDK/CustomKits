# Help

If you need any help, read below or else go check out the gitbook.

# Skript

Add kit to player, this effect contains cooldown.
```
command /kit [<String>]:
	trigger:
		kit give player with name "%arg-1%"
```

Add kit to player where it bypasses the cooldown.
```
command /kit [<String>]:
	trigger:
		force kit give player with name "%arg-1%"
```

Create a kit preview, with a item added in slot %integer%
```
command /preview [<String>]:
	trigger:
		create preview for player of kit "%arg-1%"
		ckit create slot %integer% of preview to %itemstack% named %string%
```

The {_KitCooldown} variabel will collect the cooldown of the kit.  
```
set {_KitCooldown} to cooldown of kit "%arg-1%"
```

The {_PlayerCooldown} variabel will collect the cooldown of the player before able to take the kit again.     
```
set {_PlayerCooldown} to cooldown of kit "%arg-1%" for player
```

To reset the cooldown for a player, you can create a command that looks like this.
```
command /reset [<player>] [<String>]:
	trigger:
		send "&6%arg-1%'s&f cooldown for the kit &6%arg-2% has been reset."
		reset cooldown of kit "%arg-2%" for player 
```

# Gitbook

* https://customkits.gitbook.io/itz_tix/
