# Minecraft Jeopardy
A plugin for playing the game show Jeopardy in Minecraft. The plugin allows a 3 player game of Jeopardy where players can buzz in to answer questions, and includes daily doubles and final jeopardies, as well as image questions.
## Game Setup

### Plugin Setup
This plugin can be put on a server by compiling the "src" folder into a .jar file. This plugin depends on [DecentHolograms v2.8.4](https://www.spigotmc.org/resources/decentholograms-1-8-1-20-4-papi-support-no-dependencies.96927/download?version=515097) which must also be present on the server.
### Board Setup
A jeopardy board should be built somewhere on the server. The section of the board where questions are spawned should be 6 blocks wide and 5 blocks tall and made of blue concrete. To make this board part of the plugin, the following steps should be taken:
* Look at the top left corner of the board on the side where questions should appear. Using the f3 menu, find the co-ordinates of the block you're looking at and fill in the top_left_board x, y, z and world sections
* Look at the block one to the right of the top left block. Notice the change in the X or Z co-ordinates. Fill in the "horizontal" field with the correct value of "x", "z", "-x", or "-z".
* Place a block in front of the top left block and get its co-ordinates. If the X or Z co-ordinate increases, set the "out_offset" to 1, otherwise set it to -1.
* Set the "cover_offset" to the number of blocks the question cover should come out from the board (3 is a good default)
There are other options in the config to customise
To make the most out of the board, a datapack which gives appropriate textures to diamond pickaxes with the names "100", "200", "300", "400", "500", "600", "800" and "1000" should be used.

### Other Config Setup
Set up 3 player tables built from redstone lamps, and put a button on top of each of them. Leave the space below the redstone lamps as air. In the config, set the buzzer locations to the block the buttons are on, and set the player hologram locations to somewhere above the tables (may require trial and error). Set the question hologram to somewhere in front of the cover screen (will require trial and error also). Change the information on the "first_board" and "second_board" boards to fit the questions you want in the game, as well as the final jeopardy. 

## How To Play

### Starting The Game
To add players to the game, the host (or any admin) can use the command "/jplayer add <name". Once 3 players are in the game, the first board of questions can be loaded using "/jload first_board".
### Playing The Game

## Command List

## Permission List
