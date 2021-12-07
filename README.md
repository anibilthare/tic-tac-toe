# A4 Tic Tac Toe  
| Project Name | Tic Tac Toe |  
|---|---|  
| Name | Animesh Bilthare |  
| BITS ID | 2018B4A70817G |  
| email | f20180817@goa.bits-pilani.ac.in |  
  
## Deliverables  
- [Project Description](#project-description)  
- [Development Description](#development-description)  
- [Instructions](#instructions)
- [Testing](#testing)
- [Approximate Numbers of hours spend](#approximate-number-of-hours-spend)  
- [Project Difficulty rating](#difficulty-rating)  
  
### Project Description  
TicTacToe uses firebases realtime database and authentication to implement a multiplayer tic tac toe game. It uses password and email for login/registeration and keeps track of a players win and loss count. It features a dashboard which displays the players stats along with a list of available open multiplayer games to join.
  
Known bugs  
- No separate registeration portal. If a user account does not exist on signin then it is created.
- No forgot password functionality
- No advance single player cpu player logic
  
### Development Description  
### Task 1 - User Authentication
 I refered to [Firebase documentation for password authentication ](https://firebase.google.com/docs/auth/android/password-auth) to implement the email and password authentication. I used the generated profile for the created/signed in user as the node value to store player stats ( win and loss count ) in the realtime database. For signout I simply called the sign out and then set the graph again on the Navigation component to get to login fragment.
  
### Task 2 - Single PLayer  
I have implemented a view model for the game fragment, which implements the game logic and stores the tic tac toe grid values. For single player, after the  player makes a move immediately a random grid location is marked using the designed view model. The method to check game status is also implemented in the view model, that is it checks if the player won, loss or it is a draw. Appropriate alert dialog is called after recieving the game outcome.
 
### Task 3  - Multi Player
I have created a OpenGames class whose object is stored in the realtime database. This object contains grid values, player turn, game status and player informations.  By refering to [Firebase realtime database documentation](https://firebase.google.com/docs/database/android/read-and-write?hl=ca-AD&skip_cache=false) , I was able to set up appropritate listeners and setters for the realtime database. My database features two main branches, one for player stats and other for available games. The available games are used for the 
  
### Instructions

### Testing
  
### Approximate number of hours spend  
    48 hrs  
  
### Difficulty Rating  
    10/10
