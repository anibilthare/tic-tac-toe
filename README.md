# A4 Tic Tac Toe  
| Project Name | Tic Tac Toe |  
|---|---|  
| Name | Animesh Bilthare |  
| BITS ID | 2018B4A70817G |  
| email | f20180817@goa.bits-pilani.ac.in |  


## Description
This app is a Tic Tac Toe game app. 

>Tic-tac-toe (American English), noughts and crosses (Commonwealth English), or Xs and Os, is a paper-and-pencil game for two players, X and O, who take turns marking the spaces in a 3×3 grid. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row is the winner.

The app has uses Firebase-User Authentication system. Once Registered/Login, the app shows the User Dashboard and user can start a new game. There are 2 game modes :
1) Single Player Mode(against computer player)
2) Multiplayer Mode (1vs1)

The app uses firebase realtime database for multiplayer gameplay.

#### Known Bugs 
1) Landscape mode has not been implemented.
2) First Tap Bug - In Multiplayer mode, for the second player to get the first players move initially, he needs to press a random tile 😕
3) Listeners have not been deactivated causing the issue in multiplayer mode.



## Tasks

### Task 1 - User Authentication
Entry point is the Login Fragment. For new users, there is a CTA for the Register Fragment and in Register Fragment there is a CTA to the Login Fragment.

When Registering :
1) It takes the Email id and Password from the Input Field and uses them to create a new user using the Email and Password method from the Firebase Authentication feature.
2) It also takes the Name of the user, and updates the User Name when the user is created in the Auth System.
3) It sets up a Realtime Database from the name of the userID of the person and Initiates the win and loss count to 0. This part of Realtime Database of Firebase is further used to increment the win and loss count
4) UserData is a special class which has the parameters of Wins and Loss.

Once registered, the use of Navigation Action is done to take the user to the Dashboard. 

When Signing in :
1) It takes the email and password and tries to sign in with email and password feature of Firebase. If the user exists with given credentials, they are taken to their main dashboard.
2) If the user with given credentials does not exist, A toast message is given to tell that email/password is incorrect.

Except these 2 fragments, every fragment has a signout button in menu, on calling which the current user of the FirebaseAuth signs out from it and is taken back to the login fragment.

In Dashboard :
1) The Database Reference is given based on User's UID. Every Reference to "User Data/UserUID" returns {Wins,Loss} number as int. They are called to get the current Wins and Loss of the currentUser.
2) Every time there is a change in the "User Data/UserUID", i.e the number of Wins/Loss is upgraded, there is a listener which makes the appropriate changes in the text field.
3) It also gives a "Welcome, UserName" message, which takes the username from FirebaseAuth.getInstance()'s current users name.



### Task 2 - Single PLayer  
When the user selects Single Player Mode, he is taken to the Game Fragment, with the string signifying the mode and a null value (which was added for use in multiplayer). In the Game Fragment, the game begins whenever the user presses any of the Button (Blue Tiles),

On Press :
1) It checks whether the tile is not already filled ("X"/"O") fill the tile with "X"
2) After that, it checks if there is a win with the move, if there is, it displays an Alert Dialog saying you won and on  increments your win count and brings you back to the dashboard. If not, it checks whether there is a draw .
3) If not, now it is for the computers turn to play. It generates a random position to mark "O", if the position is already marked, it finds another random position till it finds one. After Marking, it checks if there is a win. If there is the user gets an Alert Dialog saying you Lost and increments your loss count
4) If not, the game continues.

### Task 3  - Multi Player
I have created a OpenGames class whose object is stored in the realtime database. This object contains grid values, player turn, game status and player informations.  By refering to [Firebase realtime database documentation](https://firebase.google.com/docs/database/android/read-and-write?hl=ca-AD&skip_cache=false) , I was able to set up appropritate listeners and setters for the realtime database. My database features two main branches, one for player stats and other for available games. 


## Hosting and Running
The app runs completely on my firebase project. Both the Authentication and Realtime Database feature is used to achieve every result needed.


## Testing

## Ratings
Time take to complete this assignment : ~60 hours <br />
Rating in terms of difficulty: 10/10

## References 
https://swaroopjoshi.in/courses/mobile-app-dev <br />
https://developer.android.com/guide <br />
https://stackoverflow.com/
