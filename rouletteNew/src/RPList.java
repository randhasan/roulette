import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Rand Hasan, Period 11
 * 2/19/2019
 * RPList.java CHS Assignment 3
 * This program is a class that creates an RPList that will hold RoulettePlayer objects that will be used
 * in a game of Roulette
*/
public class RPList
{
    private File file;
    private String fileName;
    private ArrayList<RoulettePlayer> rp;
    
    public RPList(String f)
    {
        fileName = f; //store to use later for writing information
        file = new File(fileName);
        rp = fillList(); //fills the ArrayList with information about the RoulettePlayer objects in the file
    }
    
    /**
     * Accessor method for the ArrayList of RoulettePlayer objects
     * @return the ArrayList
     */
    public ArrayList<RoulettePlayer> getList()
    {
        return rp;
    }
    
    /**
     * Fills the ArrayList of RoulettePlayer objects with information about the RoulettePlayer objects in the text file
     * @return an updated ArrayList
     */
    public ArrayList<RoulettePlayer> fillList()
    {
        try {
            ArrayList<RoulettePlayer> r = new ArrayList<RoulettePlayer>();
            Scanner chop = new Scanner(file);
            int numPlayers = Integer.parseInt(chop.next()); //first line is the number of lines that follow it
            chop.nextLine(); //moves cursor to next line
            for (int i = 0; i<numPlayers; i++)
            {
                int numCommas = 0;
                RoulettePlayer p = new RoulettePlayer();
                String sentence = chop.nextLine();
                for (int j = 0; j<sentence.length(); j++)
                {
                    if (sentence.substring(j,j+1).equals(","))
                        numCommas ++;
                }
                if (numCommas == 7) //contains all information including questions and answers
                {
                    String name = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    String password = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    double cash = Double.parseDouble(sentence.substring(0,sentence.indexOf(',')));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    double debt = Double.parseDouble(sentence.substring(0,sentence.indexOf(',')));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    String q1 = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    String a1 = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    String q2 = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    String a2 = sentence;
                    Question ques1 = new Question(q1,a1);
                    Question ques2 = new Question(q2,a2);
                    Question[] q = new Question[2];
                    q[0] = ques1;
                    q[1] = ques2;
                    p = new RoulettePlayer(name, password, cash, debt, q);
                }
                else if (numCommas == 3) //contains all information excluding questions and answers
                {
                    String name = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    String password = sentence.substring(0,sentence.indexOf(','));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    double cash = Double.parseDouble(sentence.substring(0,sentence.indexOf(',')));
                    sentence = sentence.substring(sentence.indexOf(',')+1);
                    double debt = Double.parseDouble(sentence);
                    p = new RoulettePlayer(name, password, cash, debt);
                }
                r.add(p); //adds the RoulettePlayer object to the ArrayList
            }
            return r; //return updated ArrayList
        }
        catch (FileNotFoundException e)
        {
            ArrayList<RoulettePlayer> r = new ArrayList<RoulettePlayer>();
            System.out.println("Error: That file does not exist.");
            return r;
        }
    }
    
    /**
     * Returns a formatted String with information about each player's name, cash, and debt
     * @return the formatted String
     */
    public String toString()
    {
        String str = "";
        System.out.println("Players:");for (int i = 0; i<rp.size(); i++)
        {
            str += "\tName:"+rp.get(i).getName()+" Cash:"+rp.get(i).getMoney()+" Debt:"+rp.get(i).getDebt()+"\n";
        }
        return str;
    }
    
    /**
     * @return
     */
    public int getASize()
    {
        return rp.size();
    }
    
    /**
     * @return
     */
    public int getSize()
    {
        return rp.size();
    }
    
    /**
     * Rewrite information about every RoulettePlayer object in the ArrayList in a file
     */
    public void saveList()
    {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(rp.size()+"\n");
            for (int i = 0; i<rp.size(); i++)
            {
                writer.write(rp.get(i).format());
            }
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("Error: File does not exist");
        }
    }
    
    /**
     * Adds a RoulettePlayer object if it isn't already in the ArrayList
     * @param p a RoulettePlayer object that could be added to the ArrayList
     * @return a boolean value based on whether or not the player was successfully added to the ArrayList or not
     */
    public boolean add(RoulettePlayer p)
    {
        for (int i = 0; i<rp.size(); i++)
        {
            if (rp.get(i).getName().equals(p.getName()) && rp.get(i).getPassword().equals(p.getPassword()))
            {
                return false;
            }
        }
        rp.add(p); //adds player to ArrayList
        return true;
    }
    
    /**
     * Gets a RoulettePlayers questions based on their name
     * @param name the name of a player
     * @return a String Array with both of the player's questions
     */
    public String[] getQuestions(String name)
    {
        String[] q = new String[2];
        for (int i = 0; i<rp.size(); i++)
        {
            if (rp.get(i).getName().equals(name))
            {
                q = rp.get(i).getQuestions();
            }
        }
        return q;
    }
    
    /**
     * Tries to find and return a RoulettePlayer with a name and password that are the same as the arguments sent into the method
     * @param name the name of a player
     * @param pass the password of a player
     * @return return a RoulettePlayer with the same name and password that are sent in as arguments
     */
    public RoulettePlayer getPlayerPassword(String name, String pass)
    {
        for (int i = 0; i<rp.size(); i++)
        {
            if (rp.get(i).getName().equals(name) && rp.get(i).getPassword().equals(pass))
            {
                return rp.get(i);
            }
        }
        return null;
    }
    
    /**
     * Tries to find and return a RoulettePlayer with a name and questions/answers that are the same as the arguments sent into the method
     * @param name the name of a player
     * @param q the list of a players questions and answers
     * @return the player with the matching name and questions/answers if found
     */
    public RoulettePlayer getPlayerQuestions(String name, Question[] q)
    {
        try {
            for (int i = 0; i<rp.size(); i++)
            {
                if (rp.get(i).getName().equals(name) && rp.get(i).matchQuestions(q))
                {
                    return rp.get(i);
                }
            }
            return null;
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }
    
    /**
     * Checks if a player with a certain id exists
     * @param id an id of a player
     * @return a boolean value based on whether the player with that id exists
     */
    public boolean checkId(String id)
    { 
        for (int i = 0; i<rp.size(); i++)
        {
            if (rp.get(i).getName().equals(id))
            {
                return true;
            }
        }
        return false;
    } 
}