import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Scanner;

public class FrontendTests {

    @Test
    public void frontendTest1() {
        String input = "load\nload songs.csv\nloudness 6\nloudness 2 4\nloudness 2 to 4\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Using Tree_Placeholder as the backend data structure
        IterableSortedCollection<Song> songCollection = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(songCollection);

        // Create the Frontend object with a Scanner and backend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the frontend command loop
        frontend.runCommandLoop();

        // Capture and check the output from the test
        String output = tester.checkOutput();

        // Check that the output correctly behaves for load and loadness, and ensure errors are caught correctly
        Assertions.assertTrue(output.contains("load command has incorrect number of arguments"));
        Assertions.assertTrue(output.contains("songs.csv has been successfully loaded"));
        Assertions.assertTrue(output.contains("the upper limit on loudness has been set to 6"));
        Assertions.assertTrue(output.contains("loudness command must have either a MAX or a MIN to MAX range provided"));
        Assertions.assertTrue(output.contains("the limits on loudness have been set from 2 to 4"));

    }
    @Test
    public void frontendTest2() {
        String input = "help\nspeed 5 11\nspeed 5\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Using Tree_Placeholder as the backend data structure
        IterableSortedCollection<Song> songCollection = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(songCollection);

        // Create the Frontend object with a Scanner and backend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the frontend command loop
        frontend.runCommandLoop();

        // Capture and check the output from the test
        String output = tester.checkOutput();

        // Check that the output correctly behaves for help and speed, and ensure errors are caught correctly
        Assertions.assertTrue(output.contains("Displaying instructions for syntax of commands below:"));
        Assertions.assertTrue(output.contains("speed command has incorrect number of arguments"));
        Assertions.assertTrue(output.contains("the threshold has been set to 5"));
    }

    @Test
    public void frontendTest3() {
        String input = "show most danceable\nshow 1\nshow top6\nshow 23 32\nfake command\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Using Tree_Placeholder as the backend data structure
        IterableSortedCollection<Song> songCollection = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(songCollection);

        // Create the Frontend object with a Scanner and backend
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the frontend command loop
        frontend.runCommandLoop();

        // Capture and check the output from the test
        String output = tester.checkOutput();

        // Check that the output correctly behaves for show command, and ensure errors are caught correctly
        Assertions.assertTrue(output.contains("Top five songs are listed below:"));
        Assertions.assertTrue(output.contains("Listing the top 1 songs below:"));
        Assertions.assertTrue(output.contains("Invalid argument for 'show' command. MAX_COUNT should be an integer."));
        Assertions.assertTrue(output.contains("show command arguments not recognized as expected"));
        Assertions.assertTrue(output.contains("command not recognized"));
    }

        @Test
        public void integrationTest1() {
                String input = "load songs.csv\nloudness -4\nshow 3\nloudness -100\nshow 3\nquit\n";
                TextUITester tester = new TextUITester(input);

                // Using Tree_Placeholder as the backend data structure
                IterableSortedCollection<Song> songCollection = new IterableRedBlackTree<>();
                Backend backend = new Backend(songCollection);

                // Create the Frontend object with a Scanner and backend
                Frontend frontend = new Frontend(new Scanner(System.in), backend);

                // Run the frontend command loop
                frontend.runCommandLoop();

                // Capture and check the output from the test
                String output = tester.checkOutput();

                // Check that the output correctly behaves for load and loadness, and ensure errors are caught correctly
                Assertions.assertTrue(output.contains("songs.csv has been successfully loaded"));
                Assertions.assertTrue(output.contains("the upper limit on loudness has been set to -4"));
                Assertions.assertTrue(output.contains("Listing the top 3 songs below:\n" +
                                                      "Kills You Slowly\n" +
                                                      "Talk (feat. Disclosure)\n" + 
                                                      "Beautiful People (feat. Khalid)"));
                Assertions.assertTrue(output.contains("the upper limit on loudness has been set to -100"));
                Assertions.assertTrue(output.contains("Listing the top 0 songs below:"));
        }

        @Test
        public void integrationTest2() {
                String input = "load songs.csv\nloudness -10 to -5\nshow 3\nloudness 10 to 100\nshow 3\nquit\n";
                TextUITester tester = new TextUITester(input);

                // Using Tree_Placeholder as the backend data structure
                IterableSortedCollection<Song> songCollection = new IterableRedBlackTree<>();
                Backend backend = new Backend(songCollection);

                // Create the Frontend object with a Scanner and backend
                Frontend frontend = new Frontend(new Scanner(System.in), backend);

                // Run the frontend command loop
                frontend.runCommandLoop();

                // Capture and check the output from the test
                String output = tester.checkOutput();

                // Check that the output correctly behaves for load and loadness, and ensure errors are caught correctly
                Assertions.assertTrue(output.contains("songs.csv has been successfully loaded"));
                Assertions.assertTrue(output.contains("the limits on loudness have been set from -10 to -5"));
                Assertions.assertTrue(output.contains("Listing the top 3 songs below:\n" +
                                                      "Kills You Slowly\n" +
                                                      "Talk (feat. Disclosure)\n" + 
                                                      "Beautiful People (feat. Khalid)"));
                Assertions.assertTrue(output.contains("the limits on loudness have been set from 10 to 100"));
                Assertions.assertTrue(output.contains("Listing the top 0 songs below:"));
            }

        @Test
        public void integrationTest3() {
                String input = "load songs.csv\nshow most danceable\nloudness -3\nshow most danceable\nloudness -10 to -5\nshow most danceable\nquit\n";
                TextUITester tester = new TextUITester(input);

                // Using Tree_Placeholder as the backend data structure
                IterableSortedCollection<Song> songCollection = new IterableRedBlackTree<>();
                Backend backend = new Backend(songCollection);

                // Create the Frontend object with a Scanner and backend
                Frontend frontend = new Frontend(new Scanner(System.in), backend);

                // Run the frontend command loop
                frontend.runCommandLoop();

                // Capture and check the output from the test
                String output = tester.checkOutput();

                // Check that the output correctly behaves for load and loadness, and ensure errors are caught correctly
                Assertions.assertTrue(output.contains("songs.csv has been successfully loaded"));
                Assertions.assertTrue(output.contains("Top five songs are listed below:\n" +
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too"));
                Assertions.assertTrue(output.contains("the upper limit on loudness has been set to -3"));
                Assertions.assertTrue(output.contains("Top five songs are listed below:\n" +
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too"));
                Assertions.assertTrue(output.contains("the limits on loudness have been set from -10 to -5"));
                Assertions.assertTrue(output.contains("Top five songs are listed below:\n" +
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too\n" + 
                                                      "Me Too"));
        }

        @Test
        public void integrationTest4() {
                String input = "load songs.csv\nspeed 100\nshow 3\nspeed 1000\nshow 3\nquit\n";
                TextUITester tester = new TextUITester(input);

                // Using Tree_Placeholder as the backend data structure
                IterableSortedCollection<Song> songCollection = new IterableRedBlackTree<>();
                Backend backend = new Backend(songCollection);

                // Create the Frontend object with a Scanner and backend
                Frontend frontend = new Frontend(new Scanner(System.in), backend);

                // Run the frontend command loop
                frontend.runCommandLoop();

                // Capture and check the output from the test
                String output = tester.checkOutput();

                // Check that the output correctly behaves for load and loadness, and ensure errors are caught correctly
                Assertions.assertTrue(output.contains("songs.csv has been successfully loaded"));
                Assertions.assertTrue(output.contains("the threshold has been set to 100"));
                Assertions.assertTrue(output.contains("Listing the top 3 songs below:\n" +
                                                      "Kills You Slowly\n" +
                                                      "Talk (feat. Disclosure)\n" + 
                                                      "Find U Again (feat. Camila Cabello)"));
                Assertions.assertTrue(output.contains("the threshold has been set to 1000"));
                Assertions.assertTrue(output.contains("Listing the top 0 songs below:"));
        }

}