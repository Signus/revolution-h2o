Revolution-H2O (CleanWaterGame)
==============

# Team Members:
- Chris Card (*ccard*)
- Dylan Chau (*dchau*)
- Maria Deslis (*mdeslis*)
- Dustin Liang (*dliang*)
- Gurpreet Nanda (*gnanda*)
- Tony Nguyen (*tonnguye*)

# Game
Check out the game on Google Play -- https://play.google.com/store/apps/details?id=csci307.theGivingChild.CleanWaterGame

# Description:
This repository contains the CSCI370 field session project Clean Water Game for The Giving Child.

This will be the lite version of the game, only containing the first act, that other teams will complete in the future.


Core Requirements of the Game:

1. Educational:
 - The game needs to teach kids about the importance of clean water and its affect on developing countries
 - Show them why they should get involved
2. Entertaining:
 - Needs to keep kids engaged for the duration of the game
 - Needs to have replay value, has to be challenging and interesting enough that they want to play again
 - Has to be easy enough for a 5 year old but engagin enough and ahrd enough for a 13 year old
3. Has to incorporate water:
 - The game is about clean water and its necessity for the developing world. So it has to incorporate
 water and the fact that they need to find a clean source
 - Clean water will help improve the quality of living for these people


# Required enviroments:
All these enviroments 
- Designed for Android *17*, min android *8*

- [And Engine](https://github.com/nicolasgramlich/AndEngine.git):
Open source graphics engine for android
 - Use branch: GLES2-AnchorCenter

- [And Engine Physics Box 2D Extension](https://github.com/nicolasgramlich/AndEnginePhysicsBox2DExtension.git):
Open source physics extension for and engine
 - Use branch: GLES2-AnchorCenter

- Both *AndEngine* and *AndEginePhysicsBox2DExtension* have to be open in your working directory to run.
	They also have to be added as libraries to the project.

- ACRA:
Open source android bug reporting library should already be included in the project directory.
May need to added it as a library to your project


# Code Files:
	
Name space: ```csci307.theGivingChild.CleanWaterGame```

- src:
 - ```csci307.theGivingChild.CleanWaterGame```:
  - AboutDialog.java: This file contains code for reading in form a text file and creating a dialog that shows
		 information about the app, The Giving child, and developers
	
   - CleanWaterGame.java: This is the code pertaining to the whole application and is where the ACRA reporting library is implemented
so we can get bug info after it is released.
	
   - ExtrasMenu.java: This contains code for the extras menu activity that will allow users to send feed back, see info about the app and the giving child,
and see the privacy policy
	
   - FeedBack.java: This file contains the code for creating a dialog that allows people to email feed back to the giving child
like general feedback, about the animations, about sound, about characters etc.
	
   - GameLauncher.java: This is the start up acivity and contains the main menu allowing users to navigate to the game, extras menu, and donation page
	
   - PrivacyPolicy.java: This contains code to generate a dialog containing the giving childs privacy policy
	
- ```csci307.theGivingChild.CleanWaterGame.manager```:
	
   - ResourceManager.java: This file contains code to load resources that are needed by the current scene being played
	
   - SceneManager.java: This is used to easily switch between scenes, and is used to properly load and dispose of scenes
	
- ```csci307.theGivingChild.CleanWaterGame.objects```:
	
   - Player.java: This is the animated sprite for the main character. It contains all code dealing with movement and physics of the character

   - FallingPlatform.java: This Class controls the physics of the falling platforms

- ```csci307.theGivingChild.CleanWaterGame.scene```:

   - ActSelectScene.java: This contains the AND Egine code that generates the act selection menu and leads to scene selection menu. all done with and engine

   - AnimationScene.java: This Class controls the physics of the falling platforms
	
   - BaseScene.java: This is an abstract class that other scenes will extend.
	
   - GameScene.java: This is the primary scene for all scenes in each act and is responsible for loading the scene from the xml files,  for collision detection and
other game play acpects for each scene
	
   - LevelSelectScene.java: This contains the code for selecting a scene to play all done with and engine.

   - LoadingScene: This is what you see when the levels are loading
	
   - PauseScene.java: This contains the code for the pause menu when a user pauses the game

- ```assets```:

   - fonts: contains font files

   - gfx: this folder cotains all images for the background of the scenes and the AND Engine run menus

   - gfx/game: contains all scene related images

   - gfx/menu: contains all menu related images

- ```level```: folder contains all scene xml definitions

   - act1scene1.xml: This file contains all the objects(both obsticles and collectables) and their size and position in scene 1, also defines the scene's total size

   - act1scene2.xml: This file contains all the objects(both obsticles and collectables) and their size and position in scene 2, also defines the scene's total size

   - act1scene3.xml: This file contains all the objects(both obsticles and collectables) and their size and position in scene 3, also defines the scene's total size

   - act1scene4.xml: This file contains all the objects(both obsticles and collectables) and their size and position in scene 4, also defines the scene's total size

   - act1scene5.xml: This file contains all the objects(both obsticles and collectables) and their size and position in scene 5, also defines the scene's total size

   - sfx: cotains all soundeffect files for the game

- ```libs```:

   - acra-4.5.0.jar: This library is for acra and is used for bug reporting after the app is deployed

- ```res/layout```:

   - about_display.xml: This contains the layout for the about dilog

   - activity_extras_menu.xml: contains the layout for the extras menu

   - activity_game_launcher.xml: contains the layout for the main menu

   - privacy_policy.xml: contains the layout for the privacy policy dialog

   - userbugreport.xml: contains the layout for the feedback dialog

- ```res/raw```:

   - gamemusic.mp3: The in game background music

   - menumusic.mp3: The menu music background music

   - select_button.mp3: The sound when buttons are clicked

- ```res/drawable```:

   - ic_launcher.png: the launcher icon

   - menu_background.png: background for the main menu

   - menu_pressed.png: grpahic for when the menu button is pressed

   - menu_unpressed.png: graphic for when menu button is not pressed

   - mute.png: mute button graphic

   - unmuted.png: graphic for when sound is not muted
