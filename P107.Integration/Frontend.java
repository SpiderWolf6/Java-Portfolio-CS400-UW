import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {
    Scanner in;
    BackendInterface backend;
    private Integer min = null;
    private Integer max = null;
    private List<String> songs;
    private int minSpeed;
    private int maxCount;

    public Frontend(Scanner in, BackendInterface backend) {
        this.in = in;
        this.backend = backend;
    }

    /**
    * Starts the frontend process using a loop
    * handling user inputs and appropriate outputs
    */
    public void runCommandLoop() {
        displayCommandInstructions(); // printing intial instructions (FIX)
        String currentCommand = in.nextLine().trim().toLowerCase(); // getting command in lower case (FIX)
        while (currentCommand != null) {
            if(currentCommand.equals("quit")) {
                break; // if command is quit, end loop
            }
            try {
                executeSingleCommand(currentCommand);
            } catch (Exception e) {
                System.out.println("The type of error is " + e.getClass().getSimpleName() + ". " + e.getMessage()); // improved error handling with input command from user (FIX)
            }
            currentCommand = in.nextLine(); // retrieve next user command
        }
    };

    /**
    * Prints syntax rules for all valid commands to the output
    * upon start and "help" command
    */
    public void displayCommandInstructions() {
        System.out.println(
            "* load FILEPATH\n" +
            "* loudness MAX\n" +
            "* loudness MIN to MAX\n" +
            "* speed MIN\n" +
            "* show MAX_COUNT\n" +
            "* show most danceable\n" +
            "* help\n" +
            "* quit"
        );
    };


    /**
    * Executes a single command's functionality by appropriately following rules
    * @param command a String containing a user command
    */
    public void executeSingleCommand(String command) {
        String errorMessage = "None"; // initializes error message as a String, for use later
        String[] arr = command.split(" "); // simplified command split (FIX)
        if(command.startsWith("load")) { // handles load command properly and entered correctly (FIX)
            if(arr.length == 2) {
                try {
                    backend.readData(arr[1]); // calls readData with right arguments
                    System.out.println(arr[1] + " has been successfully loaded");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                errorMessage = "load command has incorrect number of arguments";
            }
        }
        else if(command.contains("loudness")) { // handles loudness command properly and sets error message if syntax does not match either expected case
            if(arr.length == 2) {
                max = Integer.parseInt(arr[1]);
                backend.getRange(min, max); // calls getRange with right arguments
                System.out.println("the upper limit on loudness has been set to " + max);
            }
            else if(arr.length == 4) {
                min = Integer.parseInt(arr[1]);
                max = Integer.parseInt(arr[3]);
                backend.getRange(min, max); // calls getRange with right arguments
                System.out.println("the limits on loudness have been set from " + min + " to " + max);
            }
            else {
                errorMessage = "loudness command must have either a MAX or a MIN to MAX range provided";
            }
        }
        else if(command.contains("speed")) { // handles speed command properly and sets error message if syntax is incorrect
            if(arr.length == 2) {
                minSpeed = Integer.parseInt(arr[1]);
                backend.filterSongs(minSpeed); // calls filterSongs with right arguments
                System.out.println("the threshold has been set to " + minSpeed);
            }
            else {
                errorMessage = "speed command has incorrect number of arguments";
            }
        }
        else if (command.equals("show most danceable")) { // handles show command properly and sets error message if syntax does not match either case
            songs = backend.fiveMost(); // calls fiveMost if command matches
            if (songs.isEmpty()) {
                System.out.println("No songs found for the most danceable list.");
            } else {
                System.out.println("Top five songs are listed below:");
                // songs = backend.getRange(min,max);
                for(String song : songs) {
                    System.out.println(song);
                }
                // songs.forEach(System.out::println);  // Print all the titles
            }
        }
        else if (command.contains("show")) {
            if (arr.length == 2) {
                try {
                    maxCount = Integer.parseInt(arr[1]);
                    songs = backend.getRange(min, max);  // Get all songs within the current loudness range
                    if (maxCount > songs.size()) {
                        maxCount = songs.size();
                    }
                    System.out.println("Listing the top " + maxCount + " songs below:");
                    for (int i = 0; i < maxCount; i++) {
                        System.out.println(songs.get(i));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument for 'show' command. MAX_COUNT should be an integer.");
                }
            }
            else {
                errorMessage = "show command arguments not recognized as expected";
            }
        }
        else if(command.equals("help")) {
            System.out.println("Displaying instructions for syntax of commands below:");
            displayCommandInstructions(); // display syntax instructions
        }
        else if(command.equals("quit")) {
            return; // directly return which is more efficient for exiting process (FIX)
        }
        else {
            System.out.println("command not recognized");
        }
        if(!errorMessage.equals("None")) {
            System.out.println(errorMessage);
        }
    };
}