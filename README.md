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
To ask a specific question, hit one of the question items in the item frames on the board. After the host reads the question, they can activate the buzzers using "/jactivate". Players can then buzz in and give an answer, which the host can mark right or wrong with "/janswer 1" or "/janswer 0". If nobody else wants to guess, the host can reveal the answer with "/jreveal". To bet on final jeopardies or daily doubles, players can use "/jbet <amount>" and to answer final jeopardies players can use "/jfinal <answer>". Once the first board has been played, the second board can be loaded with "/jload second_board". To start the final jeopardy, the host can use "/jfinal". After all final answers have been submitted, the host can use "/jactivate" to reveal the next players answer, and "janswer 1/0" to award points to the correct answers.
## Command List
Given below is the full list of commands. Angular brackets (<>) indicate mandatory arguments and square brackets ([]) indicate optional arguments.
* **/jactivate**: Activates the buzzers.
* **/janswer <0 or 1>**: Answers a question correctly or incorrectly (admin).
* **/jask <category (1-6)> <question (1-5)>**: Asks a question from the given category (1 leftmost, 6 rightmost) for the given point amount (1 lowest, 5 highest) (admin).
* **/jbet <amount>**: Bets the given amount on the daily double or final question.
* **/jload <board_name>**: Loads the board with the given name from the config (admin).
* **/jplayer [add/remove/list/swap] [player1] [player2]**: Command for managing players in the game. Can list the players, add or remove them or swap a player in the game for one not yet in the game (admin).
* **/jreveal**: Reveals the answer to the current question (admin).
* **/jreset**: Resets the jeopardy game (admin).
* **/jfinal [answer]**: Starts the final question if left blank, otherwise submits an answer to the final question.
* **/jadd <player> <amount>**: Adds the given amount of points to the players score (admin).
* **/jset <player> <amount>**: Sets the players score to the given amount (admin).
* **/jremove <player> <amount>**: Removes the given amount of points from the players score (admin).
* **/jskip**: Skips the current question (admin).
* **/jsaveimage**: Saves an image template to the image board (admin).
* **/jloadimage**: Loads an image template from the config (admin).
* **/jhelp**: Sends the command help.
* **/vanish**: Toggles the players visibility (admin).
## Permission List
Given below is the full list of permissions.
* 
