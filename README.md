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

# Gitbook

* https://customkits.gitbook.io/itz_tix/
