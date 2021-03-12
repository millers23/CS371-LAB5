package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author Sebastian Miller
 * @version 3/9/2021
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String key = firstName + " " + lastName;
        if (database.containsKey(key)) {
            Log.d("addPlayer", "Database contains key");
            return false;
        }
        else {
            SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(key, player);
            Log.d("addPlayer", "Added to database");
            return true;
        }
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String key = firstName + " " + lastName;
        if (database.containsKey(key)) {
            database.remove(key);
            Log.d("removePlayer", "Player removed");
            return true;
        }
        else {
            Log.d("removePlayer", "Player not in database");
            return false;
        }
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String key = firstName + " " + lastName;
        if (database.containsKey(key)) {
            Log.d("getPlayer", "Player retrieved");
            return database.get(key);
        }
        else {
            Log.d("getPlayer", "Player not in database");
            return null;
        }
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String key = firstName + " " + lastName;
        if (database.containsKey(key)) {
            SoccerPlayer player = getPlayer(firstName, lastName);
            player.bumpGoals();
            Log.d("bumpGoals", "Goals updated");
            return true;
        }
        else {
            Log.d("bumpGoals", "Player not in database");
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String key = firstName + " " + lastName;
        if (database.containsKey(key)) {
            SoccerPlayer player = getPlayer(firstName, lastName);
            player.bumpYellowCards();
            Log.d("bumpYellowCards", "Yellow cards updated");
            return true;
        }
        else {
            Log.d("bumpYellowCards", "Player not in database");
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String key = firstName + " " + lastName;
        if (database.containsKey(key)) {
            SoccerPlayer player = getPlayer(firstName, lastName);
            player.bumpRedCards();
            Log.d("bumpRedCards", "Yellow cards updated");
            return true;
        }
        else {
            Log.d("bumpRedCards", "Player not in database");
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if (teamName.equals(null)) {
            return database.size();
        } else {
            int players = 0;
            Enumeration<String> keys = database.keys();
            while (keys.hasMoreElements()) {
                SoccerPlayer player = database.get(keys.nextElement());
                if (player.getTeamName().equals(teamName)) {
                    players++;
                }
            }
            return players;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        SoccerPlayer player = null;
        SoccerPlayer[] team = new SoccerPlayer[numPlayers(teamName)];
        int i = 0;
        if(teamName == null){
            Enumeration<String> keys = database.keys();
            while (keys.hasMoreElements()) {
                player = database.get(keys.nextElement());
                team[i] = player;
                i++;
            }
            if(idx < numPlayers(teamName)) {
                return team[idx];
            } else {
                return null;
            }
        } else {
            Enumeration<String> keys = database.keys();
            while (keys.hasMoreElements()) {
                player = database.get(keys.nextElement());
                if (player.getTeamName().equals(teamName)) {
                    team[i] = player;
                    i++;
                }
            }
            if(idx < numPlayers(teamName)) {
                return team[idx];
            } else {
                return null;
            }
        }
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        try {
            //set up file reading functions
            BufferedReader input = new BufferedReader(new FileReader(file));
            String line = "";
            ArrayList<String> fileAsString = new ArrayList<>();

            //initialize variables
            SoccerPlayer player = null;
            String firstName = "";
            String lastName = "";
            String teamName = "";
            String uniformNumber = "";
            String goals = "";
            String yellowCards = "";
            String redCards = "";

            //read file to ArrayList
            while ((line = input.readLine()) != null) {
                fileAsString.add(line);
            }

            //read ArrayList into variables
            for (int i = 0; i < fileAsString.size(); i+=7) {
                firstName = fileAsString.get(i);
                lastName = fileAsString.get(i + 1);
                teamName = fileAsString.get(i + 2);
                uniformNumber = fileAsString.get(i + 3);
                goals = fileAsString.get(i + 4);
                yellowCards = fileAsString.get(i + 5);
                redCards = fileAsString.get(i + 6);
            }

            //check if player exists
            String key = firstName + " " + lastName;
            if (getPlayer(firstName, lastName) == null) {
                //setup player
                player = new SoccerPlayer(firstName, lastName,
                        Integer.parseInt(uniformNumber), teamName);
            }

            //bump variables that cannot be set
            for (int i = 0; i < Integer.parseInt(goals); i++) {
                player.bumpGoals();
            }
            for (int i = 0; i < Integer.parseInt(yellowCards); i++) {
                player.bumpYellowCards();
            }
            for (int i = 0; i < Integer.parseInt(redCards); i++) {
                player.bumpRedCards();
            }

            //add to database and return true
            database.put(key, player);
            return true;
        }
        //catch error and return false
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        try {
            PrintWriter printWriter = new PrintWriter(file);

            Enumeration<String> keys = database.keys();
            while (keys.hasMoreElements()) {
                SoccerPlayer player = database.get(keys.nextElement());
                printWriter.println(logString(player.getFirstName()));
                printWriter.println(logString(player.getLastName()));
                printWriter.println(logString(player.getTeamName()));
                printWriter.println(logString(""+player.getUniform()));
                printWriter.println(logString(""+player.getGoals()));
                printWriter.println(logString(""+player.getYellowCards()));
                printWriter.println(logString(""+player.getRedCards()));
            }

            printWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        //Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
