package cred;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

//import MultipleWindowsDemo.MyInternalFrame;

public class MainView extends JFrame implements ActionListener {

	private static final long serialVersionUID = -8690504951421676530L;
	JDesktopPane desktop;

	public MainView() {
		super("Créditos");
		
//		Make the big window be indented 50 pixels from each edge
//		of the screen.
        int inset = 100;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);

        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
//        setFocusableWindowState(false);
//        createFrame(); //create first "window"
        setContentPane(desktop);
        setJMenuBar(createMenuBar());		

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				try {
					UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
					System.setProperty("SeaGlass.JTextArea.drawLineSeparator", "false");					
					
				} catch (Exception e) {
					e.printStackTrace();
				}            	
//				UIManager.put("swing.boldMetal", Boolean.FALSE);				
                createAndShowGUI();
            }
        });        
        
	}
	
	protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("Clientes");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Crear");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("crear");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Set up the first menu item.
        menuItem = new JMenuItem("Modificar");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("modificar");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        //Set up the second menu item.
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }	
	
//	React to menu selections.
    public void actionPerformed(ActionEvent e) { 
    	
        if ("crear".equals(e.getActionCommand())) {
            createFrame();         
        } else { //quit
            quit();
        }    	    	
    } 
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Make sure we have nice window decorations.
//        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
//        MainView frame = new MainView();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        setVisible(true);
    }
    
    //Create a new internal frame.
    protected void createFrame() {
        ClientView clientView = new ClientView();
        clientView.setVisible(true); //necessary as of 1.3
        desktop.add(clientView);
        try {
            clientView.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }
    
    //Quit the application.
    protected void quit() {
        System.exit(0);
    }
    
}