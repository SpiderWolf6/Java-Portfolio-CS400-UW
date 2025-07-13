//Name: Agastya Rathee
//Project Name: P103 Role Code

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;

/**
 * Backend class implementing the BackendInterface for managing songs.
 */
public class Backend implements BackendInterface {
    private IterableSortedCollection<Song> tree;
    private Integer lowLoudness;
    private Integer highLoudness;
    private Integer songSpeed;

    /**
     * Constructor for the Backend class.
     *
     * @param tree The IterableSortedCollection to store songs.
     */
    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
        this.lowLoudness = null;
        this.highLoudness = null;
        this.songSpeed = null;
    }

    /**
     * Reads song data from a CSV file and inserts the data into the tree.
     *
     * @param filename The name of the CSV file to read from.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    @Override
    public void readData(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Read header line to skip it

            while ((line = br.readLine()) != null) {
                String[] data = parseHelper(line); // give to parser helper to parse correctly

                // Parse fields from the CSV row
                String title = data[0].replace("\"", "").trim();
                String artist = data[1].trim();
                String genre = data[2].trim();
                int year = Integer.parseInt(data[3].trim());
                int bpm = Integer.parseInt(data[4].trim());
                int energy = Integer.parseInt(data[5].trim());
                int danceability = Integer.parseInt(data[6].trim());
                int loudness = Integer.parseInt(data[7].trim());
                int liveness = Integer.parseInt(data[8].trim());

                // insertion to tree
                Song song = new Song(title, artist, genre, year, bpm, energy, danceability, loudness, liveness,
                        Comparator.comparingInt(Song::getLoudness));
                tree.insert(song);
            }
        }
    }

    /**
     * Helper method to parse a CSV line, handling commas within quoted fields.
     *
     * @param line The CSV line to parse.
     * @return An array of strings representing the parsed fields.
     */
    private String[] parseHelper(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                result.add(field.toString().trim());
                field.setLength(0); // Clearing by setting to 0
            } else {
                field.append(c); // adding
            }
        }
        result.add(field.toString().trim());

        return result.toArray(new String[0]);
    }

    /**
     * Retrieves a list of song titles within the specified loudness range.
     *
     * @param low  The lower bound of the loudness range (inclusive).
     * @param high The upper bound of the loudness range (inclusive).
     * @return A list of song titles that fall within the specified range.
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        System.out.println("getRange result for low=" + low + ", high=" + high);
        this.lowLoudness = low;
        this.highLoudness = high;

        List<String> titles = new ArrayList<>();
        for (Song song : tree) {
            System.out.println("Current song: " + song.getTitle() + ", Loudness: " + song.getLoudness());

            // Check if the song's loudness is within the specified range
            if ((low == null || song.getLoudness() >= low) &&
                    (high == null || song.getLoudness() <= high)) {
                if (songSpeed == null || song.getBPM() > songSpeed) {
                    titles.add(song.getTitle());
                    System.out.println("Added song: " + song.getTitle());
                }
            }
        }
        System.out.println("Returning " + titles.size() + " songs");
        return titles;
    }

    /**
     * Filters songs based on the speed threshold.
     *
     * @param threshold The minimum BPM for songs to be included.
     * @return A list of song titles that meet the speed criteria and are within the loudness range.
     */
    @Override
    public List<String> filterSongs(Integer threshold) {
        this.songSpeed = threshold;
        if (threshold == null) {
            return getRange(lowLoudness, highLoudness);
        } else {
            List<String> filtered = new ArrayList<>();
            for (Song song : tree) {
                if (song.getBPM() > threshold &&
                        (lowLoudness == null || song.getLoudness() >= lowLoudness) &&
                        (highLoudness == null || song.getLoudness() <= highLoudness)) {
                    filtered.add(song.getTitle());
                }
            }
            return filtered;
        }
    }

    /**
     * Retrieves the top five most danceable songs.
     *
     * @return A list of the top five most danceable song titles.
     */
    @Override
    public List<String> fiveMost() {
        List<Song> topSongList = new ArrayList<>();
        for (Song song : tree) {
            if ((songSpeed == null || song.getBPM() > songSpeed) &&
                    (lowLoudness == null || song.getLoudness() >= lowLoudness) &&
                    (highLoudness == null || song.getLoudness() <= highLoudness)) {
                topSongList.add(song);
            }
        }

        topSongList.sort(Comparator.comparingInt(Song::getDanceability).reversed());

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(5, topSongList.size()); i++) {
            result.add(topSongList.get(i).getTitle());
        }
        return result;
    }
}