import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.List;

public class TeamTests {

    /**
     * Tests the getRange method by checking if it returns a correct list of placeholder songs given
     * the loudness range.
     * Also tests the readData method in the backend role code (and exceptions thrown).
     */
    @Test
    public void testGetRange() throws IOException {
        Backend backend = new Backend(new Tree_Placeholder());
    
        // passing in a non existent file path into readData is expected to throw an error
        try {
            backend.readData("wrongPath.csv");
            Assertions.assertTrue(false, "IOException error not correctly caught.");
        } catch (IOException e) {
            Assertions.assertTrue(true, "IOException error correctly caught.");
        }

        // passing in existent file path into readData is not expected to throw an error
        try {
            backend.readData("songs.csv");
            Assertions.assertTrue(true, "Songs.csv correctly parsed.");
        } catch (Exception e) {
            Assertions.assertTrue(false, "Songs.csv not correctly parsed, should have no issues.");
        }

        List<String> songNames = backend.getRange(-7, 11);

        // song names list should not be empty (since existing songs do fall in range)
        Assertions.assertTrue(!songNames.isEmpty(), "Song list should not be empty.");
    
        // make sure that all three songs are correctly returned (all three have -5, which is within range)
        Assertions.assertTrue(songNames.contains("A L I E N S"), "Expected song 'A L I E N S' to be within the loudness range");
        Assertions.assertTrue(songNames.contains("BO$$"), "Expected song 'BO$$' to be within the loudness range");
        Assertions.assertTrue(songNames.contains("Cake By The Ocean"), "Expected song 'Cake By The Ocean' to be within the loudness range");
    
        // songs with no loudness range (both bounds are null)
        songNames = backend.getRange(null, null);
        Assertions.assertTrue(!songNames.isEmpty(), "Song list should not be empty with null range.");

        // high loudness range (no songs fit this range)
        songNames = backend.getRange(30, 50);
        Assertions.assertTrue(songNames.isEmpty(), "Song list should be empty because no songs fall in range.");
    }
    
    /**
     * Tests the filterSongs method by passing in a given threshold and checking if it returns
     * the correct list of placeholder songs.
     */
    @Test
    public void testFilterSongs() {
        Backend backend = new Backend(new Tree_Placeholder());
    
        List<String> filtSongs = backend.filterSongs(108);
    
        // filteredSongs list should not be empty (since placeholder songs satisfy threshold)
        Assertions.assertTrue(!filtSongs.isEmpty(), "Filtered song list should not be empty.");
    
        // make sure that the returned list contains filtered songs with BPM > 108
        Assertions.assertTrue(filtSongs.contains("A L I E N S"), "Expected song 'A L I E N S' with BPM > 108");
        Assertions.assertTrue(filtSongs.contains("Cake By The Ocean"), "Expected song 'Cake By The Ocean' with BPM > 108");
        Assertions.assertTrue(!filtSongs.contains("BO$$"), "Song 'BO$$' does not satisfy BPM > 108");

        // high speed filter (200) should return empty list
        filtSongs = backend.filterSongs(200);
        Assertions.assertTrue(filtSongs.isEmpty(), "Filtered song list should be empty if threshold is higher than all songs.");
    }
    
    /**
     * Tests the fiveMost method by checking if it returns the five most danceable songs. 
     * (only three placeholder songs in this case)
     */
    @Test
    public void testFiveMost() {
        Backend backend = new Backend(new Tree_Placeholder());
    
        List<String> topFive = backend.fiveMost();
    
        // topFive song list should not be empty (if songs exist)
        Assertions.assertTrue(!topFive.isEmpty(), "Top five list should not be empty.");
    
        // make sure the correct songs are returned (only 3 since that is how many placeholder songs we have)
        Assertions.assertTrue(topFive.contains("A L I E N S"), "Expected song 'A L I E N S' to be in list");
        Assertions.assertTrue(topFive.contains("BO$$"), "Expected song 'BO$$' to be in list");
        Assertions.assertTrue(topFive.contains("Cake By The Ocean"), "Expected song 'Cake By The Ocean' to be in list");
    
        // if there is a larger song set, make sure only five are returned
        List<String> maxSongs = backend.fiveMost();
        Assertions.assertTrue(maxSongs.size() <= 5, "The list should contain at most five songs.");
    }    
}