import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.*;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;


/**
 * 
 * Run a SQL Query to export data into text file
 * 
 * @author Billy Lee
 * 2017
 * 
 * Linkedin: https://www.linkedin.com/in/billylee1
 *
 */
public class SQL_Project
{

	private JFrame frame;
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField queryField;
	private JButton btnStop;
	private JButton btnStart;
	private JLabel lblTimer;
	private String username;
	private String password;
	private String query;
	private String timerText;
	private Timer myTimer;
	private Timer timer = new Timer();
	private JLabel lblDatabaseName;
	private JLabel lblDiv;
	private JTextField lblDiv2;
	private JTextField textFieldDatabase;
	private JLabel lblHowManySeconds;
	private JTextField textFieldTimer;
	private int filecounter = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					SQL_Project window = new SQL_Project();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SQL_Project()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[][][grow]", "[][][][][][][][][]"));
		
		lblDatabaseName = new JLabel("Database Name:");
		frame.getContentPane().add(lblDatabaseName, "cell 1 0,alignx trailing");
		
		textFieldDatabase = new JTextField();
		frame.getContentPane().add(textFieldDatabase, "cell 2 0,growx");
		textFieldDatabase.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		frame.getContentPane().add(lblUsername, "cell 1 1,alignx trailing");
		
		usernameField = new JTextField();
		frame.getContentPane().add(usernameField, "cell 2 1,growx");
		usernameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		frame.getContentPane().add(lblPassword, "cell 1 2,alignx trailing");
		
		passwordField = new JTextField();
		frame.getContentPane().add(passwordField, "cell 2 2,growx");
		passwordField.setColumns(10);
		
		JLabel lblSqlQuery = new JLabel("SQL Query:");
		frame.getContentPane().add(lblSqlQuery, "cell 1 4,alignx trailing");
		
		queryField = new JTextField();
		frame.getContentPane().add(queryField, "cell 2 4,growx");
		queryField.setColumns(10);
		
		lblHowManySeconds = new JLabel("How Many Seconds:");
		frame.getContentPane().add(lblHowManySeconds, "cell 1 5,alignx trailing");
		
		textFieldTimer = new JTextField();
		frame.getContentPane().add(textFieldTimer, "cell 2 5,growx");
		textFieldTimer.setColumns(10);
		
		
		lblDiv = new JLabel("Location to Save (ie C:/File/InputFile): ");
		frame.getContentPane().add(lblDiv, "cell 1 6");
		
		lblDiv2 = new JTextField(" ");
		frame.getContentPane().add(lblDiv2, "cell 2 6,growx");
		
		lblTimer = new JLabel("");
		frame.getContentPane().add(lblTimer, "cell 2 14");
		
		btnStart = new JButton("Start");
		frame.getContentPane().add(btnStart, "cell 2 12");
		
		btnStart.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		        timer.schedule(new App(), 0, 1000);
		    }
		});
		
		
	}
	
	
	class App extends TimerTask {

//		900
	    int countdown = 900;

	    public void run() {
	        countdown = countdown - 1;
	        lblTimer.setText(((countdown/60)%60) +" Minutes " + (countdown%60) + "Seconds Left");
	        if (countdown == 0) {
	        	countdown = 10;
	        	try {
	        		//connect to the database
	        		Connection conn = DriverManager.getConnection("jdbc:myDriver:" + textFieldDatabase.getText(), usernameField.getText(), passwordField.getText());  
	        		Statement stmt = conn.createStatement();
	        		//here is the query you will execute
	        		ResultSet rs = stmt.executeQuery(queryField.getText());
	        		ResultSetMetaData rsmd = rs.getMetaData();
	        		int columnCount = rsmd.getColumnCount();
	        		FileWriter fw = new FileWriter(lblDiv.getText() + filecounter + ".txt"); 
	        	    BufferedWriter bw = new BufferedWriter(fw);
	        		while(rs.next()){
	        			StringBuilder row = new StringBuilder();
        		        for(int i = 0; i < columnCount; i++){
        		           row.append(rs.getObject(i));
        		        }
        		      try{
        	                  bw.write(String.valueOf(row));
        	                  bw.newLine();
        	          }  
        	          catch (Exception e) {  
        	              System.err.println("Couldn't Write To File"+e);  
        	          }            
	        		}
	                conn.close();
	                filecounter++;
	            } catch (Exception e) {
	                System.err.println("Got an exception! ");
	                System.err.println(e.getMessage());
	            }
	        }
	    }

	}
}


