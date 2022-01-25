import java.util.ArrayList;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

class Game 
{
    private Parser parser;
    private Room currentRoom;
    Room outside, weightroom, trainer, hallway1, mlockerroom, threesixteen, hallway2,
    mbathroom, wbathroom, threethirteen, hallway3, hallway4, hallway5, hallway6, gym, keyweightroom, keymlockerroom;
    
    ArrayList<Item> inventory = new ArrayList<Item>();
        
    /**
     * Create the game and initialize its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }
    
    public static void main(String[] args) {
    	Game mygame = new Game();
    	mygame.play();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
      
        // create the rooms
        outside = new Room("You are outside the back entrance of Sunset High School, the weight room is to the east of this room");
        weightroom = new Room("You try to open the door to the weight room, but it's locked, you need a key");
        trainer = new Room("You are in the athletic trainers office, Kari is currently unavaiable");
        hallway1 = new Room("You are in 3 hall, to the south is outside, to the west is the Athletic Trainer, "
        		+ "to the north is 3 hall, to the east is the men's lockerroom");
        mlockerroom = new Room("You try to open the door to the locker room, but it's locked, you need a key");
        threesixteen = new Room("You are in the 3-16 classroom");
        hallway2 = new Room("You are in 3 hall, to the south is 3 Hall, to the west is 3-16, "
        		+ "to the north is 3 hall");
        mbathroom = new Room("You are in the men's bathroom, its a warzone in here");
        wbathroom = new Room("You are in the women's bathroom");
        threethirteen = new Room("You are in the 3-13 classroom");
        hallway3 = new Room("You are in 3 hall, to the south is 3 Hall, to the west is 3-13, "
        		+ "to the east is J Hall");
        hallway4 = new Room("You are in J hall, to the south is the men's bathrrooom, to the west is 3 Hall, "
        		+ "to the east is J Hall");
        hallway5 = new Room("You are in J hall, to the west is J Hall, to the east is J Hall");
        hallway6 = new Room("You are in J hall, to the west is J Hall, to the south is the women's bathroom");
        gym = new Room("You are in the gym of Sunset High School, there is currently a men's basketball game going on");
        
        keyweightroom = new Room("You are in the weight room, you see your backpack");
        keymlockerroom = new Room("You are in the men's locker room, Mr. Merrick gives you the key to the weight room");
                
        // initialise room exits
        outside.setExit("north", hallway1);
        outside.setExit("east", weightroom);

        weightroom.setExit("west", outside);
                
        trainer.setExit("east", hallway1);
        
        hallway1.setExit("north", hallway2);
        hallway1.setExit("east", mlockerroom);
        hallway1.setExit("south", outside);
        hallway1.setExit("west", trainer);
        
        mlockerroom.setExit("west", hallway1);
        
        threesixteen.setExit("east", hallway2);

        hallway2.setExit("north", hallway3);
        hallway2.setExit("south", hallway1);
        hallway2.setExit("west", threesixteen);
        
        mbathroom.setExit("north", hallway4);
        
        wbathroom.setExit("north", hallway6);
        
        threethirteen.setExit("east", hallway3);
        
        hallway3.setExit("east", hallway4);
        hallway3.setExit("south", hallway2);
        hallway3.setExit("west", threethirteen);
        
        hallway4.setExit("north", gym);
        hallway4.setExit("east", hallway5);
        hallway4.setExit("south", mbathroom);
        hallway4.setExit("west", hallway3);
        
        hallway5.setExit("east", hallway6);
        hallway5.setExit("west", hallway4);   
        
        hallway6.setExit("south", wbathroom);
        hallway6.setExit("west", hallway5);
        
        gym.setExit("south", hallway4);
        
        keyweightroom.setExit("west", outside);
        
        keymlockerroom.setExit("west", hallway1);
        
        currentRoom = outside;  // start game outside
        
        // items and what room they're in
        wbathroom.setItem(new Item("Toilet_Paper"));
        mbathroom.setItem(new Item("Weird_Key"));
        keymlockerroom.setItem(new Item("Weight_Room_Key"));
        keyweightroom.setItem(new Item("Backpack"));
        gym.setItem(new Item("Basketball"));
        trainer.setItem(new Item("Athletic_Tape"));
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("You forgot your backpack in the weight room to the east of this room, but you need a key to get in.");
        System.out.println("Mr. Merrick has a key, but hes inside the locker room and the door is locked");
        System.out.println("The commands are \"go\", \"help\", \"quit\", and \"inventory\".");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
            System.out.println(currentRoom.getLongDescription());
        }
        else if (commandWord.equals("go")) {
            wantToQuit = goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("inventory")) {
        	printInventory();
            System.out.println(currentRoom.getLongDescription());
        }
        else if (commandWord.equals("get")) {
        	getItem(command);
            System.out.println(currentRoom.getLongDescription());
        }
        else if (commandWord.equals("drop")) {
        	dropItem(command);
            System.out.println(currentRoom.getLongDescription());
        }
        return wantToQuit;
    }
    
    private void getItem(Command command) 
    {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to get...
            System.err.println("Get what?");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = currentRoom.getItem(item);

        if (newItem == null) {
            System.err.println("That item is not here!");
        }
        else {
            inventory.add(newItem);
            currentRoom.removeItem(item);;
            System.out.println("Picked up: " + item);
           
        }
    }
    
    private void dropItem(Command command) 
    {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.err.println("Drop what?");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = null;
        int index = 0;
        for (int i = 0; i < inventory.size(); i++) {
        	if (inventory.get(i).getDescription().equals(item)) {
        		newItem = inventory.get(i);
        		index = i;
        	}
        }

        if (newItem == null) {
            System.err.println("That item is not in your inventory!");
        }
        else {
            inventory.remove(index);
            currentRoom.setItem(new Item(item));;
            System.out.println("Dropped: " + item);
           
        }
    }

    private void printInventory() {
    	String output = "";
    	for (int i = 0; i < inventory.size(); i++) {
    		output += inventory.get(i).getDescription() + " ";
    	}
    	System.out.println("You are carrying:");
    	System.out.println(output);
	}
    
    

	// implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private boolean goRoom(Command command) 
    {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.err.println("Go where?");
            return false;
        }
        
        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.err.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            if (currentRoom == weightroom && hasItem("Weight_Room_Key") == true) {
            	currentRoom = keyweightroom;
            }
            
            if (currentRoom == mlockerroom && hasItem("Weird_Key") == true) {
            	currentRoom = keymlockerroom;
            }

            System.out.println(currentRoom.getLongDescription());
            if (hasItem("Backpack") ==  true) {
            	System.out.println("You found your backpack! You win!");
            	return true;
            }
        }
        return false;
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.err.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
    
    private boolean hasItem(String desc) {
    	for (int a = 0; a < inventory.size(); a++) {
    		if (inventory.get(a).getDescription().equals(desc)) {
    			return true;
    		}
    	}
    	return false;
    }
}
