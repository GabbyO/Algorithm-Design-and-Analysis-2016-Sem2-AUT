//Gabriela Orellana, ID number: 1244821

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SpiderGUI extends JPanel implements KeyListener,ActionListener
{
    //Called the internal java
    SpiderLeg sL;
    Spider s;
    
    //GUI Compontents
    JLabel spiderWebsite, spiderSearch, com, http;
    JButton enter, exit, clear, openFile;
    JTextField textEnterW, textEnterS, dataText;
    JDialog dialog;
    JFrame frame;
    JScrollPane scrollBar;
    JTextArea textArea;

    //JPanels
    JPanel newPanelButton;
    JPanel newTextPanel;
    JPanel newDataPanel;

    //For TextField: empty at beginning
    String stringStart = "";

    //Website address assigned
    String httpW;
    
    //Constructor
    public SpiderGUI()
    {
        //Layout
        super(new BorderLayout());
        
        //Start
        sL = new SpiderLeg();
        s = new Spider();
        
        //Set up size
        this.setPreferredSize(new Dimension(650,400));
        
        //GUI Labels
        spiderWebsite = new JLabel("WEBSITE");
        spiderSearch = new JLabel("SEARCH");
        http = new JLabel("http://www.");
        
        //TextFields
        textEnterW = new JTextField(stringStart, 15);
        textEnterS = new JTextField(stringStart, 15);
        
        //TextArea settings
        textArea = new JTextArea();
        textArea.setFont(new Font("Time New Roman", Font.ITALIC, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        
        //Stroller in TextField
        scrollBar = new JScrollPane(textArea);
        scrollBar.setPreferredSize(new Dimension(400, 300));

        //JButtons
        enter = new JButton("ENTER");
        exit = new JButton("EXIT");
        clear = new JButton("CLEAR");
        openFile = new JButton("FILE");

        //DIALOG
        dialog = new JDialog();
        
        //ButtonPanels
        newPanelButton = new JPanel();
        newPanelButton.add(this.enter);
        newPanelButton.add(openFile);
        newPanelButton.add(this.clear);
        newPanelButton.add(this.exit);
        
        //TextPanels
        newTextPanel = new JPanel();
        newTextPanel.add(this.spiderWebsite);
        newTextPanel.add(this.http);
        newTextPanel.add(this.textEnterW);
        newTextPanel.add(this.spiderSearch);
        newTextPanel.add(this.textEnterS);
        
        //DataPanels (outputs)
        newDataPanel = new JPanel();
        newDataPanel.add(scrollBar);
        
        
        //ADD panels and layout settings
        this.add(newTextPanel, BorderLayout.NORTH);
        this.add(newPanelButton, BorderLayout.CENTER);
        this.add(newDataPanel, BorderLayout.SOUTH);
        
        //ActionListners added!
        this.enter.addActionListener(this);
        this.exit.addActionListener(this);
        this.clear.addActionListener(this);
        this.openFile.addActionListener(this);
        this.textEnterW.addActionListener(this);
        this.textEnterS.addActionListener(this);

        this.enter.addKeyListener(this);
        this.textEnterW.addKeyListener(this);
    }

    //Check if integers for textfield
    public boolean isInteger(String url) 
    {
        try 
        { 
            Integer.parseInt(url); 
        } catch(NumberFormatException e) 
        { 
            return true; 
        }
        
        return false;
    }
    
    //Check if empty?
    public boolean isEmpty(String url) 
    {
        try 
        { 
            url = "";
        } catch(NullPointerException e) 
        { 
            return true; 
        }
        
        return false;
    }
    
    //ACTION LISTENERS
    @Override
    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();
        if (source == enter)
        {
            httpW = "http://www." + getTextW();
                    
            //CHECK if not integers
            if(!isInteger(textEnterW.getText()))
            {
                JOptionPane.showMessageDialog(frame, "Please enter the website address");
            }

            //CHECK if not empty
            if(!this.textEnterW.getText().equals(""))
            {

                if(sL.crawling(httpW) == true)
                {
                    textArea.append("\n");
                    textArea.append("Title: " + sL.getTitle(httpW));
                    
                    for(String s : sL.getImages(httpW))
                    {
                        textArea.append(s);
                        textArea.append("\n");
                    }
                    
                    for(String s : sL.getMeta(httpW))
                    {
                        textArea.append(s);
                        textArea.append("\n");
                    }
                    
                    for(String s : sL.getHyperlink(httpW))
                    {
                        textArea.append(s);
                        textArea.append("\n");
                    }
                    
                    sL.createFile(textArea.getText());
                }
                
            } else if(this.textEnterW.getText().equals("") || this.textEnterS.getText().equals(""))
            {
                JOptionPane.showMessageDialog(frame, "Please enter the website address");
            }
        }
        
        //EXIT the program
        if (source == exit)
        {
            System.exit(0);
        }
        
        //CLEAR the textarea
        if (source == clear)
        { 
            this.textEnterW.setText("");
            this.textEnterS.setText("");
            this.textArea.setText("");
        }
        
        //Open and read the file
        if(source == openFile)
        {
            openFile();
            System.out.print("OPEN FILE!");
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    //ENTER pressed
    @Override
    public void keyPressed(KeyEvent event) 
    {
        int key = event.getKeyCode();
        if(!getTextW().isEmpty())
        {
            if (key == KeyEvent.VK_ENTER) 
            {
                System.out.print("f");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {}
    
    //GET website address
    String getTextW()
    {
        return this.textEnterW.getText();
    }

    //GET search word
    String getTextS()
    {
        return this.textEnterS.getText();
    }
    
    //OPEN FILE (HTML_Data_Outputs.txt specific)
    private void openFile()
    {  
        try
        {  
            BufferedReader br = new BufferedReader(new FileReader("HTML_Data_Outputs.txt"));
            textArea.setText(null); // clear the displayArea
            String line = br.readLine();
            while (line != null)
            {  
                textArea.append(line+"\n");
                line = br.readLine();
            }
            
            br.close();
        }
        catch (IOException e)
        {  
            JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error Opening File", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) 
    {
        //starter!
        SpiderGUI spiderGUI = new SpiderGUI();
        JFrame frame = new JFrame("Spider GUI");
        
        //FRAME settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(spiderGUI);
        frame.pack();
        JRootPane rootPane = SwingUtilities.getRootPane(spiderGUI); 
        rootPane.setDefaultButton(spiderGUI.enter);
    
        Toolkit tk = Toolkit.getDefaultToolkit();
        
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        
        frame.setLocation((screenDimension.width-frameDimension.width)/2,
            (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
    }

}
