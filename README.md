### Lines-98
Lines-98 game written originally using Java Applet and then converted to Java Swing back in 2012.

It supports 3 difference types of game where you get scores if you move balls into one of the following shapes
- Line Game: Move balls into a line (at lease 5 balls in a line)
- Square Game: Move balls to a square (4 balls)
- Block Game: Move balls to a block (7 neighbor balls)

![Lines98-Screenshot](Lines98-Screenshot.png)

Something need to be improved:
- [x] Updated some frame properties like size, position, title, etc (They are not necessary in an Applet)
- [x] Correct high score function since it is still for Applet and connects to my old website
- [ ] Make the ball look better
- [ ] Improve concurrent processing in order make movement smoother
- [x] Refactor source code since it was developed long time ago
- [x] Add an option dialog
- [ ] Convert to an Android game

Bugs
- [ ] Closing frame should ask for storing new high score
