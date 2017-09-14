//Gabriela Orellana, ID number: 1244821

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpiderLeg 
{
    //Outputs to file .txt
    FileWriter fw;
    BufferedWriter bw;
    File file;
                    
    //Crawling
    Connection connection;
        
    //GET data
    String bodyText;
    Elements image, linksOnPage, meta;
    
    
    //Collections
    private List<String> links = new LinkedList<String>();
    private static final String USER_AGENT = "";
    
    //GETTERS for textfield and file (write data)
    private ArrayList<String> getImagesList = new ArrayList<String>();
    private ArrayList<String> getMetaList = new ArrayList<String>();
    private ArrayList<String> getHyperList = new ArrayList<String>();
    private ArrayList<String> getLinesFileList = new ArrayList<String>();
    
    //GET TITLE
    protected String getTitle(String url) //Print out the title of the web page url.
    {
        Document docTitle = new Document(url);
        docTitle = Jsoup.parse(url);
        
        try {
            docTitle = Jsoup.connect(url).get();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error with the HTTP request! " + e);
        }
            
        url = docTitle.title();
        
        return url;
    }
    
    //GET HYPERLINKS
    protected ArrayList<String> getHyperlink(String url) //Print out all URLs in the web page url.
    {
        Document docHyperLink = new Document(url);
        
        try 
        {
            docHyperLink = Jsoup.connect(url).get();
            
            linksOnPage = docHyperLink.select("a[href]");
            
            getHyperList.add("\n");
            getHyperList.add("\nHYPERLINKS");
            //System.out.println("\nFound " + linksOnPage.size() + " Links!");

            for(Element link : linksOnPage)
            {
                getHyperList.add("\nLink: " + link.attr("href"));
                getHyperList.add("\nText: " + link.text());
            }
            
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error in out HTTP request " + e);
        }

        return getHyperList;
    }
    
    //GET IMAGE LINKS
    protected ArrayList<String> getImages(String url) //Print out names of all image files in the web page url, as well as the height, width and alt attributes.
    {
        Document docImageLink = new Document(url);
        
        try 
        {
            docImageLink = Jsoup.connect(url).get();
            image = docImageLink.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

            getImagesList.add("\n");
            getImagesList.add("\nIMAGE");
            //System.out.println("\nFound " + image.size() + " image links!");

            for (Element images : image) 
            {
                getImagesList.add("\nSource: " +images.attr("src"));
                getImagesList.add("\nHeight : " +images.attr("height"));
                getImagesList.add("\nWidth: " +images.attr("width"));
                getImagesList.add("\nAlt : " +images.attr("alt"));
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error with the image request " + e);
        }

        return getImagesList;
    }
    
    //GET META
    protected ArrayList<String> getMeta(String url) //Print out the meta data, including meta description, and meta keywords of the web page url.
    {
        Document docMeta = new Document(url);
        
        try 
        {
            docMeta = Jsoup.connect(url).get();
            meta = docMeta.select("meta");
            //System.out.println("\nFound " + meta.size() + " Meta links!");
            
            getMetaList.add("\n");
            getMetaList.add("\nMETA");
            getMetaList.add("\nMeta Description: " + meta.select("meta[name=description]").attr("content"));
            
            for(Element link : meta)
            {
                getMetaList.add("\nMeta Link: " + link.toString());
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error with the image request " + e);
        }

        return getMetaList;
    }
    
    //SEARCH word in urls
    boolean searchForWord(String word) throws IOException
    {
        Document docSearch = new Document(word);
        
        try
        {
            System.out.println("\nSearching for a word... " + word + " ...");
            bodyText = docSearch.body().text();
            
            return bodyText.toLowerCase().contains(word.toLowerCase());
        } catch(NullPointerException e) 
        {
            return true;
        }
    }

    //CRAWLINGs
    protected boolean crawling(String url)
    {
        Document docCrawl = new Document(url);
        
        try
        {
            connection = Jsoup.connect(url).userAgent(USER_AGENT);
            docCrawl = connection.get();
            
            if(connection.response().statusCode() == 2000)
            {
                System.out.println("\nVisiting! Received a webpage at " + url);
            }
            
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("Failure! Retrieved something other than HTML");
                return false;
            }
            
            return true;
        }
        catch(IOException e)
        {
            return false;
        }
    }
    
    //CREATE FILE .txt
    protected void createFile(String url)
    {
        try 
        {
            file = new File("HTML_Data_Outputs.txt");

            //create .txt if a file is not here
            if (!file.exists()) 
            {
                file.createNewFile();
            }
            System.out.println("\nData_Outputs.txt is creating...");
            
            //File is creating...
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            
            getLinesFileList.add(url);
            //WRITTINGs
            
            int size = getLinesFileList.size();
            for (int i=0; i < size; i++)
            {
                String str = getLinesFileList.get(i);
                bw.write(str);
                if(i < size-1)
                    bw.write("\n");
            }

            bw.flush();
            bw.close();
            
            System.out.println("\nCreated: Data_Outputs.txt is Completed!");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //LIST URLs LINKS
    protected List<String> getLinks()
    {
        return this.links;
    }
}