import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

/**
 * Backend class for P1.03
 * Ryan Bradley
 * 2.12.25
 * 
 * Backend implementing BackendInterface
 */
public class Backend implements BackendInterface {

	private IterableSortedCollection<Song> tree;
	private Integer minLoudness = null;
	private Integer maxLoundness = null;
	private Integer speedThreshold = null;

	/**
	 * Constructor using required signature
	 * Uses tree to store, sort, and iterate songs
	 */
	public Backend(IterableSortedCollection<Song> tree) {
		this.tree = tree;
	}

	/**
	 * Loads data from CSV file
	 * Uses comparator to store songs
	 * 
	 * @param filename name of csv to load
	 * @throws IOException if errors loading/reading file 
	 */
	@Override
	public void readData(String filename) throws IOException {
	
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String header = br.readLine(); 
			if (header == null) {
				throw new IOException(); // something failed or CSV is empty
			}
			
			List<String> headerNames = csvReaderHelper(header);
			// header indexes - instructions say may not always be same order
			int iTitle = findIndex(headerNames, "title");
			int iArtist = findIndex(headerNames, "artist");
			int iGenre = findIndex(headerNames, "top genre");
			int iYear = findIndex(headerNames, "year");
			int iBPM = findIndex(headerNames, "bpm");
			int iEnergy = findIndex(headerNames, "nrgy");
			int iDance = findIndex(headerNames, "dnce");
			int iLoudness = findIndex(headerNames, "dB");
			int iLive = findIndex(headerNames, "live");

			// song data 
			String line;
			while((line = br.readLine()) != null) {
				List<String> songData = csvReaderHelper(line);
				
				// can add check for each required part to ensure half-full lines are not used.
				// parts for song item
				String title = songData.get(iTitle);
				String artist =  songData.get(iArtist);
				String genre = songData.get(iGenre);
				int year = Integer.parseInt(songData.get(iYear));
				int bp = Integer.parseInt(songData.get(iBPM));
				int energy = Integer.parseInt(songData.get(iEnergy));
				int dance = Integer.parseInt(songData.get(iDance));
				int loudness = Integer.parseInt(songData.get(iLoudness));
				int live = Integer.parseInt(songData.get(iLive));

				// new comparator for song that compares by loudness 
				// had to do some research on how to do it like this
				Comparator<Song> loudComp = new Comparator<Song>() {
					@Override
					public int compare(Song a, Song b) {
						return a.getLoudness() - b.getLoudness();
					}
				};
				// create song and insert into tree
				Song song = new Song(title, artist, genre, year, bp, energy, dance, loudness, live, loudComp);
				tree.insert(song);
			}
		}
	}

	/**
	 * List of song titles ordered by loudness, within loudness range
	 * Also filter by speed filter, if set
	 * Null low/high should be treated as unbounded
	 * 
	 * @param low min loudness of songs
	 * @param high max loundess of songs
	 * @return list of songs fitting parameters
	 */
	@Override
	public List<String> getRange(Integer low, Integer high) {
	    
		 this.minLoudness = low;
		 this.maxLoundness = high;
 
		 // set iterators in given IterableSortedCollection class
		 if (low == null) {
			 tree.setIteratorMin(null);
		 } else {
			 // same comparable logic as before 
			 tree.setIteratorMin(new Comparable<Song>() {
				 @Override
				 public int compareTo(Song other) {
					 return low.compareTo(other.getLoudness());
				 }
			 });
		 }
 
		 if (high == null) {
			 tree.setIteratorMax(null);
		 } else {
			 // same comparable logic as before 
			 tree.setIteratorMax(new Comparable<Song>() {
				 @Override
				 public int compareTo(Song other) {
					 return high.compareTo(other.getLoudness());
				 }
			 });
		 }
		 // now add Songs that meet requirements 
		 List<String> results = new ArrayList<>();
	    Iterator<Song> it = tree.iterator();
		 while(it.hasNext()) {
			 Song s = it.next();
			 if (this.speedThreshold == null || s.getBPM() > this.speedThreshold) {
				 results.add(s.getTitle());
			 }
		 }
		 return results;
	}
 


	/**
	 * List of songs ordered by loundess
	 * Similar to getRange but now with speed threshold
	 * Null passed into this clears speed threshold
	 * 
	 * @param threshold speed threshold 
	 * @return list of songs that meet requirements 
	 */
    @Override
    public List<String> filterSongs(Integer threshold) {
        
		// set speed variable
		this.speedThreshold = threshold;
		// call getRange now that new threshold is set
		return getRange(this.minLoudness, this.maxLoundness);
    }

	/**
	 * Returns list of 5 most danceable songs that fall within parameters
	 * 
	 * @return List of 5 most danceable songs. All songs if less than 5. Empty list if none
	 */
    @Override
    public List<String> fiveMost() {
        List<Song> passing = new ArrayList<>();
		Iterator<Song> it = tree.iterator();    
		while (it.hasNext()) {
			Song s = it.next();
			if (this.speedThreshold == null || s.getBPM() > this.speedThreshold) {
				passing.add(s);
			}
		}

		// sort based on danceability, same logic as previous
		passing.sort(new Comparator<Song>() {
			public int compare(Song a, Song b) {
				return b.getDanceability() - a.getDanceability();
			}
		});
		// add first 5 or less if not 5
		List<String> topFive = new ArrayList<>(); 
		for (int i = 0; i < 5 && i < passing.size(); i++) {
			System.out.println("returning the song " + passing.get(i).getTitle());
			topFive.add(passing.get(i).getTitle());
		}
		// System.out.println("returning this " + topFive);
		return topFive;
    }

	

	// helper findIndex for headers
	private static int findIndex(List<String> header, String name) {
		for (int i = 0; i < header.size(); i++) {
			if (header.get(i).equalsIgnoreCase(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Helper method for reading data in from CSV
	 * Emphasis on quotation handling in Song titles 
	 * Currently checks all fields, could switch to just title
	 */
	private static List<String> csvReaderHelper(String line) {

		List<String> fields = new ArrayList<>();
		StringBuilder s = new StringBuilder();
		boolean insideQuotes = false;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			// if starts with a quote then ensure commas are part of title and not parsed
			if (c == '\"') {
				insideQuotes = !insideQuotes;
			} else if (c == ',' && !insideQuotes) {
				fields.add(s.toString());
				s.setLength(0);
			} else {
				s.append(c);
			}
		}
		fields.add(s.toString());

		// get rid of quotes 
		for (int i = 0; i < fields.size(); i++) {
			String t = fields.get(i).trim();
			if (t.startsWith("\"") && t.endsWith("\"")) {
				t = t.substring(1, t.length()-1);
			}
			fields.set(i, t);
		}
		return fields;
	}
}