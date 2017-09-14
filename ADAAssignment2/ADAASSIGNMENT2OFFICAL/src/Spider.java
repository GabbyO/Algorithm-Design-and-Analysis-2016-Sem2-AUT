//Gabriela Orellana, ID number: 1244821

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider 
{
    //URL exploring
    private String currentUrl, nextUrl;
    boolean success;
    
    //Collections (LIST and SET)
    private final int MAX_PAGES_SEARCH = 20;
    private final Set<String> pageVisited = new HashSet<>();
    private final List<String> pageToVisit = new LinkedList<>();
    
    //Called SpiderLeg class
    SpiderLeg sL;
    
    //Constructor
    public Spider()
    {
        sL = new SpiderLeg();
    }
    
    //Collect the urls - search next 
    private String nextUrl()
    {
        do 
        {
            nextUrl = this.pageToVisit.remove(0);
        } while(this.pageVisited.contains(nextUrl));
            
        this.pageVisited.add(nextUrl);
        
        return nextUrl;
    }
    
    //Searching url and word
    public void search(String url, String searchWord) throws IOException
    {
        while(this.pageVisited.size() < MAX_PAGES_SEARCH)
        {       
            //Check if empty, add links
            if(this.pageToVisit.isEmpty())
            {
                currentUrl = url;
                this.pageVisited.add(url);
            }
                else
            {
                currentUrl = this.nextUrl();
            }
            
            //Searching and outputs
            sL.crawling(currentUrl); 
            
            //Check if success finding a word
            success = sL.searchForWord(searchWord);
          
            if(success)
            {
                System.out.println(String.format("Success! Word '%s' is found at %s", searchWord, this.currentUrl));
                break;
            } else if(!success)
            {
                System.out.println(String.format("Do Not Success! Word '%s' ia not found at %s", searchWord, this.currentUrl));
            }
          
            //ADD Links to List<String>
            this.pageToVisit.addAll(sL.getLinks());
        }
      
        System.out.println("\nVisited at " + this.pageVisited.size() + " webpage/s");
        System.out.println("\nSeen at " + this.pageToVisit.toString() + " webpage/s");
    }
    
    //GET search word!
    protected String getSearch()
    {
        System.out.println(currentUrl);
        return this.currentUrl;
    }
    
}
