#Emergent Villages

WIP Minecraft mod to add emergent village creation based on playtime or wealth - whatever is easier or more fun to add!

IMPLEMENTED
Get data (spawn radius, max villagers per chunk, dimensions to spawn villagers in) from a config file
Randomly select a chunk near a player
Check if the clamped regional difficulty is higher than a randomly generated number
Check that no villager has spawned here before
Find a suitable spawn location for a villager
Spawn a villager
Store the fact that a villager has been spawned in the chunk data
Save this data to the world for later use

TODO
Notify the player if a villager has spawned close to them

KNOWN ISSUES
Because I use clamped regional difficulty, no villagers ever spawn on Peaceful difficulty, while villagers can start spawning from the first creation of a world on Hard difficulty
