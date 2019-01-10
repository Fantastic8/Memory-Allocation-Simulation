package panels;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ControlPanel extends JFrame {
//component
	private ImageIcon ImageIconMainBG;
	private ImageIcon ImageIconAddPanel;
	private JLabel LabelMainBG;
	private JLabel LabelAddPanel;
	private JPanel CPanel;
	private ArrayList<AnalyzePanel> APanel;
	
	private Checkbox CheckboxRandomBlockSize;
	private Checkbox CheckboxRandomProcessingTime;
	private Checkbox CheckboxAlgorithm;
	private Checkbox CheckboxRunMode;
	private Checkbox CheckboxRunStatus;
	
//data
	private int APNumber;
	private int xframeold;
	private int yframeold;
	
	public ControlPanel()
	{
		FrameConfig();
		ComponentConfig();
	}
	
	//frame configuration
	public void FrameConfig()
	{
		setUndecorated(true);//remove border
		setResizable(false);//disable resize
		setBounds(500, 100, 1300, 100);
		this.setSize(1300, 100);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CPanel=(JPanel)getContentPane();
		CPanel.setOpaque(false);
		CPanel.setLayout(null);
		Moveable();
	}
		
//component configuration
	public void ComponentConfig()
	{
		//Background
		ImageIconMainBG=new ImageIcon(ControlPanel.class.getResource("/UI/PPAControllerBG.jpg"));
		LabelMainBG=new JLabel(ImageIconMainBG);
		LabelMainBG.setBounds(0, 0, ImageIconMainBG.getIconWidth(),ImageIconMainBG.getIconHeight());
		this.getLayeredPane().setLayout(null);
		this.getLayeredPane().add(LabelMainBG, new Integer(Integer.MIN_VALUE));
		
		//border button
		
		//Close Button
		JLabel LabelExit = new JLabel();
		LabelExit.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				System.exit(0);
			}
		});
		LabelExit.setBounds(ControlPanel.this.getWidth()-32, 0, 30, 33);
		CPanel.add(LabelExit);
		
		//Minimize Button
		JLabel LabelMinimize=new JLabel();
		LabelMinimize.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				ControlPanel.this.setExtendedState(ControlPanel.this.ICONIFIED);//minimize
				for(int i=0;i<APanel.size();i++)
				{
					APanel.get(i).setExtendedState(APanel.get(i).ICONIFIED);
				}
			}
		});
		LabelMinimize.setBounds(ControlPanel.this.getWidth()-62, 0, 30, 33);
		CPanel.add(LabelMinimize);
		
		//Button Add Panel
		ImageIconAddPanel=new ImageIcon(ControlPanel.class.getResource("/UI/AddPanel.png"));
		LabelAddPanel=new JLabel(ImageIconAddPanel);
		LabelAddPanel.setBounds(30, 40, ImageIconAddPanel.getIconWidth(), ImageIconAddPanel.getIconHeight());
		CPanel.add(LabelAddPanel);
		//add listener
		LabelAddPanel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				AnalyzePanel ap=new AnalyzePanel(true,APanel,ControlPanel.this);
				ap.setVisible(true);
				APanel.add(ap);
				UpdateAPanelLocation();
			}
		});
		
		//add first analyzepanel
		APanel=new ArrayList<AnalyzePanel>();
		AnalyzePanel ap=new AnalyzePanel(false,APanel,ControlPanel.this);
		ap.setLocation(ControlPanel.this.getLocation().x, ControlPanel.this.getLocation().y+100);
		ap.setVisible(true);
		APanel.add(ap);
		
		//update location
		UpdateAPanelLocation();
		
	//add other component
		//take over random block size
		CheckboxRandomBlockSize=new Checkbox("Take Over");
		CheckboxRandomBlockSize.setBounds(100, 10, 75, 15);
		//CheckboxRandomBlockSize.setForeground(Color.OPAQUE);
		CheckboxRandomBlockSize.setState(false);
		CheckboxRandomBlockSize.setVisible(false);
		CPanel.add(CheckboxRandomBlockSize);
		
		//take over Random Processing Time
		CheckboxRandomProcessingTime=new Checkbox("Take Over");
		CheckboxRandomProcessingTime.setBounds(350, 10, 75, 15);
		CheckboxRandomProcessingTime.setState(false);
		CheckboxRandomProcessingTime.setVisible(false);
		CPanel.add(CheckboxRandomProcessingTime);
		
		//take over Algorithm
		CheckboxAlgorithm=new Checkbox("Take Over");
		CheckboxAlgorithm.setBounds(600, 10, 75, 15);
		CheckboxAlgorithm.setState(false);
		CheckboxAlgorithm.setVisible(false);
		CPanel.add(CheckboxAlgorithm);
		
		//take over Run Mode
		CheckboxRunMode=new Checkbox("Take Over");
		CheckboxRunMode.setBounds(850, 10, 75, 15);
		CheckboxRunMode.setState(false);
		CheckboxRunMode.setVisible(false);
		CPanel.add(CheckboxRunMode);
		
		//take over Run Status
		CheckboxRunStatus=new Checkbox("Take Over");
		CheckboxRunStatus.setBounds(1100, 10, 75, 15);
		CheckboxRunStatus.setState(false);
		CheckboxRunStatus.setVisible(false);
		CPanel.add(CheckboxRunStatus);
		
	}
	
	public void Moveable()
	{
		//Window Movable
		this.addMouseListener(new MouseAdapter() 
		{
		  @Override
		  public void mousePressed(MouseEvent e) {
		  xframeold = e.getX();
		  yframeold = e.getY();
		  }
		 });
		this.addMouseMotionListener(new MouseMotionAdapter() {
			  public void mouseDragged(MouseEvent e) {
			  int xOnScreen = e.getXOnScreen();
			  int yOnScreen = e.getYOnScreen();
			  int xframenew = xOnScreen - xframeold;
			  int yframenew = yOnScreen - yframeold;
			  ControlPanel.this.setLocation(xframenew, yframenew);
			  APanel.get(0).setLocation(xframenew,yframenew+100);
			  for(int i=1;i<APanel.size();i++)
			  {
				  APanel.get(i).setLocation(xframenew,yframenew+100+i*200);
			  }
			  }
		});
	}
	
	public void UpdateAPanelLocation()
	{
		  int xframenew = ControlPanel.this.getLocation().x;
		  int yframenew = ControlPanel.this.getLocation().y;
		  APanel.get(0).setLocation(xframenew,yframenew+100);
		  for(int i=1;i<APanel.size();i++)
		  {
			  APanel.get(i).setLocation(xframenew,yframenew+100+i*200);
		  }
	}
	
//get
	public ArrayList<AnalyzePanel> getAPanelList()
	{
		return APanel;
	}

//set
	/*
	CheckboxRandomBlockSize;
	CheckboxRandomProcessingTime;
	CheckboxAlgorithm;
	CheckboxRunMode;
	CheckboxRunStatus;
	 */
	public void setMouseEventStart(MouseEvent e) {
		if(!CheckboxRunMode.getState())
		{
			return;
		}
		
		for(int i=1;i<APanel.size();i++)
		{
			APanel.get(i).getMouseAdapterStart().mouseClicked(e);
		}
	}

	public void setMouseEventPause(MouseEvent e) {
		if(!CheckboxRunMode.getState())
		{
			return;
		}
		
		for(int i=1;i<APanel.size();i++)
		{
			APanel.get(i).getMouseAdapterPause().mouseClicked(e);
		}
	}

	public void setMouseEventStop(MouseEvent e) {
		if(!CheckboxRunMode.getState())
		{
			return;
		}
		
		for(int i=1;i<APanel.size();i++)
		{
			APanel.get(i).getMouseAdapterStop().mouseClicked(e);
		}
	}

	public void setActionEventSingleStep(ActionEvent e) {
		
	}

	public void setActionEventRunTWaits(ActionEvent e) {
		
	}

	public void setActionEventRunTSteps(ActionEvent e) {
		
	}

	public void setActionEventSizeRandomMode(ActionEvent e) {
		
	}

	public void setActionEventProcessingTimeRandomMode(
			ActionEvent e) {
		
	}

	public void setChangeEventSpinnerRunMode0Param1(
			ChangeEvent e) {
		
	}

	public void setChangeEventSpinnerRunMode0Param2(
			ChangeEvent e) {
		
	}

	public void setChangeEventSizeMin(ChangeEvent e) {
		
	}

	public void setChangeEventSizeMax(ChangeEvent e) {
		
	}

	public void setChangeEventSizeFormulaParam1(
			ChangeEvent e) {
		
	}

	public void setChangeEventSizeFormulaParam2(
			ChangeEvent e) {
		
	}

	public void setChangeEventProcessingTimeMin(
			ChangeEvent e) {
		
	}

	public void setChangeEventProcessingTimeMax(
			ChangeEvent e) {
		
	}

	public void setChangeEventProcessingTimeFormulaParam1(
			ChangeEvent e) {
		
	}

	public void setChangeEventProcessingTimeFormulaParam2(
			ChangeEvent e) {
		
	}
	
}
