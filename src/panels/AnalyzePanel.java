package panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algorithm.AlgorithmContainer;
import algorithm.BuddySystem;

public class AnalyzePanel extends JFrame {
//component
	private ImageIcon ImageIconMainBG;
	private ImageIcon ImageIconStart;
	private ImageIcon ImageIconPause;
	private ImageIcon ImageIconStop;
	
	private JLabel LabelMainBG;
	private JLabel LabelSingleStep;
	private JLabel LabelRunTWaits;
	private JLabel LabelRunTSteps;
	private JLabel LabelStart;
	private JLabel LabelPause;
	private JLabel LabelStop;
	private JLabel LabelAlgorithm;
	private JLabel LabelLoadTime;
	private JLabel LabelSizeRandomMode;
	private JLabel LabelProcessTimeRandomMode;
	private JLabel LabelSizeFormula;
	private JLabel LabelSizeFormulaParam1;
	private JLabel LabelSizeFormulaParam2;
	private JLabel LabelProcessingTimeFormula;
	private JLabel LabelProcessingTimeFormulaParam1;
	private JLabel LabelProcessingTimeFormulaParam2;
	private JLabel LabelSizeMaxRandom;
	private JLabel LabelSizeMinRandom;
	private JLabel LabelProcessingTimeMaxRandom;
	private JLabel LabelProcessingTimeMinRandom;
	private JLabel LabelRunMode0Param1;
	private JLabel LabelRunMode0Param2;
	private JLabel LabelRunMode1Param;
	private JLabel LabelRunMode2Param;
	private JLabel LabelResultTime;
	private JLabel LabelResultInsertTimes;
	private JLabel LabelResultWaitTimes;
	private JLabel LabelResultStatus;
	private JLabel LabelResultTotalSize;
	private JLabel LabelResultTotalProcessingTime;
	private JLabel LabelResultMaxDebris;
	
	
	private JPanel APanel;
	
	private ButtonGroup ButtonGroupRunMode;
	private JRadioButton RadioButtonSingleStep;
	private JRadioButton RadioButtonRunTWaits;
	private JRadioButton RadioButtonRunTSteps;
	
	private Rectangle Disk;
	private Rectangle NextPartition;
	private int NextProcessingTime;
	private Rectangle CurrentPartition;
	private LinkedList<Rectangle> OccupiedPartition;
	
	private JComboBox ComboAlgorithm;
	private JComboBox ComboSizeRandomMode;
	private JComboBox ComboProcessingTimeRandomMode;
	private ComboBoxModel ComboModelAlgorithm;
	private ComboBoxModel ComboModelSizeRandomMode;
	private ComboBoxModel ComboModelProcessingTimeRandomMode;
	private String[] StringAlgorithm={"First Fit","Next Fit","Best Fit","Worst Fit","Buddy System"};
	private String[] StringSizeRandomMode={"Average","Normal Distribution"};
	private String[] StringProcessingTimeRandomMode={"Average","Normal Distribution"};
	
	private JSpinner SpinnerLoadTime;
	private JSpinner SpinnerSizeFormulaParam1;
	private JSpinner SpinnerSizeFormulaParam2;
	private JSpinner SpinnerProcessingTimeFormulaParam1;
	private JSpinner SpinnerProcessingTimeFormulaParam2;
	private JSpinner SpinnerSizeMax;
	private JSpinner SpinnerSizeMin;
	private JSpinner SpinnerProcessingTimeMax;
	private JSpinner SpinnerProcessingTimeMin;
	private JSpinner SpinnerRunMode0Param1;
	private JSpinner SpinnerRunMode0Param2;
	private JSpinner SpinnerRunMode1Param;
	private JSpinner SpinnerRunMode2Param;
	
	//upward implement listener
	private MouseAdapter MouseAdapterStart;
	private MouseAdapter MouseAdapterPause;
	private MouseAdapter MouseAdapterStop;
	private ActionListener ActionListenerSingleStep;
	private ActionListener ActionListenerRunTWaits;
	private ActionListener ActionListenerRunTSteps;
	private ActionListener ActionListenerSizeRandomMode;
	private ActionListener ActionListenerProcessingTimeRandomMode;
	private ChangeListener ChangeListenerSpinnerRunMode0Param1;
	private ChangeListener ChangeListenerSpinnerRunMode0Param2;
	private ChangeListener ChangeListenerSizeMin;
	private ChangeListener ChangeListenerSizeMax;
	private ChangeListener ChangeListenerSizeFormulaParam1;
	private ChangeListener ChangeListenerSizeFormulaParam2;
	private ChangeListener ChangeListenerProcessingTimeMin;
	private ChangeListener ChangeListenerProcessingTimeMax;
	private ChangeListener ChangeListenerProcessingTimeFormulaParam1;
	private ChangeListener ChangeListenerProcessingTimeFormulaParam2;
//data
	private final int DiskY=160;
	private final int DiskHeight=30;
	private ArrayList<AnalyzePanel> ListAPanel;
	private ControlPanel CPanel;
	private int RunMode;//0-single step; 1-run till waits; 2-run till steps;
	private int RunStatus;//0-stop; 1-running; 2-pause;
	private JRadioButton CurrentMode;
	private AlgorithmContainer CurrentAC;//current running algorithm
	private Thread CurrentThread;
	private boolean ResultIsCounting;
	
	public AnalyzePanel(boolean closeable,ArrayList<AnalyzePanel> ListAPanel,ControlPanel CPanel)
	{
		//data config
		this.ListAPanel=ListAPanel;
		this.CPanel=CPanel;
		RunMode=0;//set default runmode
		RunStatus=0;//set default RunStatus
		Disk=new Rectangle(10, DiskY, 512, DiskHeight);//disk block
		OccupiedPartition=new LinkedList<Rectangle>();//used partition block
		
		FrameConfig();
		ComponentConfig(closeable);
		ChangeRunMode("SingleStep");//default run mode
	}

//component configuration
	private void ComponentConfig(final boolean closeable)
	{
//Background
		ImageIconMainBG=new ImageIcon(ControlPanel.class.getResource("/UI/PPAAnalyzerBG.jpg"));
		LabelMainBG=new JLabel(ImageIconMainBG);
		LabelMainBG.setBounds(0, 0, ImageIconMainBG.getIconWidth(),ImageIconMainBG.getIconHeight());
		this.getLayeredPane().setLayout(null);
		this.getLayeredPane().add(LabelMainBG, new Integer(Integer.MIN_VALUE));
		
//border button
		
		//Close Button
		if(closeable)
		{
			ImageIcon ImageIconClose=new ImageIcon(ControlPanel.class.getResource("/UI/Exit.png")); 
			JLabel LabelClose = new JLabel(ImageIconClose);
			LabelClose.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
					ListAPanel.remove(AnalyzePanel.this);//remove from apanel list
					CPanel.UpdateAPanelLocation();//notify controller to update location of each frame
					dispose();//close this window
				}
			});
			LabelClose.setBounds(AnalyzePanel.this.getWidth()-32, 0, 30, 33);
			APanel.add(LabelClose);
		}
		
		
//radio button RunMode
		ButtonGroupRunMode=new ButtonGroup();
		//Run Mode single step
		LabelSingleStep=new JLabel("Single Step");
		LabelSingleStep.setBounds(845,20,100,20);
		LabelSingleStep.setFont(new Font("Lao UI", Font.PLAIN, 15));
		APanel.add(LabelSingleStep);		
		
		RadioButtonSingleStep=new JRadioButton("Single Step");
		RadioButtonSingleStep.setBounds(820,20,20,20);
		RadioButtonSingleStep.setOpaque(false);
		RadioButtonSingleStep.setSelected(true);
		CurrentMode=RadioButtonSingleStep;//set current radiobutton
		ButtonGroupRunMode.add(RadioButtonSingleStep);//add to button group
		APanel.add(RadioButtonSingleStep);//add to panel
		//add listener
		if(closeable)
		{
			ActionListenerSingleStep=new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(RunStatus!=0)
					{
						//roll back
						RadioButtonSingleStep.setSelected(false);
						CurrentMode.setSelected(true);
						//alert
						JOptionPane.showMessageDialog(null, "Please Stop Current Analyze First!");
						return;
					}
					
					System.out.println("RunMode change to 0");
					RunMode=0;
					//show param
					ChangeRunMode("SingleStep");
					resetPaint();//reset paint
					CurrentMode=RadioButtonSingleStep;//change current radiobutton pointer
				}};
		}
		else
		{
			ActionListenerSingleStep=new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					CPanel.setActionEventSingleStep(e);//take over
					if(RunStatus!=0)
					{
						//roll back
						RadioButtonSingleStep.setSelected(false);
						CurrentMode.setSelected(true);
						//alert
						JOptionPane.showMessageDialog(null, "Please Stop Current Analyze First!");
						return;
					}
					
					System.out.println("RunMode change to 0");
					RunMode=0;
					//show param
					ChangeRunMode("SingleStep");
					resetPaint();//reset paint
					CurrentMode=RadioButtonSingleStep;//change current radiobutton pointer
				}};
		}
		
		RadioButtonSingleStep.addActionListener(ActionListenerSingleStep);
		//param
		LabelRunMode0Param1=new JLabel("Next Size");
		LabelRunMode0Param2=new JLabel("Next PTime");
		SpinnerRunMode0Param1=new JSpinner();
		SpinnerRunMode0Param2=new JSpinner();
		LabelRunMode0Param1.setBounds(820,110,150,20);
		LabelRunMode0Param2.setBounds(910,110,150,20);
		SpinnerRunMode0Param1.setBounds(820,130,70,20);
		SpinnerRunMode0Param2.setBounds(910,130,70,20);
		SpinnerRunMode0Param1.setValue(1);
		SpinnerRunMode0Param2.setValue(1);
		APanel.add(LabelRunMode0Param1);
		APanel.add(LabelRunMode0Param2);
		APanel.add(SpinnerRunMode0Param1);
		APanel.add(SpinnerRunMode0Param2);
		//add listener
		if(closeable)
		{
			ChangeListenerSpinnerRunMode0Param1=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerRunMode0Param1.getValue()>1024)
					{
						SpinnerRunMode0Param1.setValue(1024);
					}
					if((int)SpinnerRunMode0Param1.getValue()<1)
					{
						SpinnerRunMode0Param1.setValue(1);
					}
					setNextPartition((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
				}
			};
		}
		else
		{
			ChangeListenerSpinnerRunMode0Param1=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSpinnerRunMode0Param1(e);
					if((int)SpinnerRunMode0Param1.getValue()>1024)
					{
						SpinnerRunMode0Param1.setValue(1024);
					}
					if((int)SpinnerRunMode0Param1.getValue()<1)
					{
						SpinnerRunMode0Param1.setValue(1);
					}
					setNextPartition((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
				}
			};
		}
		SpinnerRunMode0Param1.addChangeListener(ChangeListenerSpinnerRunMode0Param1);
		
		if(closeable)
		{
			ChangeListenerSpinnerRunMode0Param2=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerRunMode0Param2.getValue()>10000)
					{
						SpinnerRunMode0Param2.setValue(10000);
					}
					if((int)SpinnerRunMode0Param2.getValue()<1)
					{
						SpinnerRunMode0Param2.setValue(1);
					}
					setNextPartition((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
				}
			};
		}
		else
		{
			ChangeListenerSpinnerRunMode0Param2=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSpinnerRunMode0Param2(e);
					if((int)SpinnerRunMode0Param2.getValue()>10000)
					{
						SpinnerRunMode0Param2.setValue(10000);
					}
					if((int)SpinnerRunMode0Param2.getValue()<1)
					{
						SpinnerRunMode0Param2.setValue(1);
					}
					setNextPartition((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
				}
			};
		}
		
		SpinnerRunMode0Param2.addChangeListener(ChangeListenerSpinnerRunMode0Param2);
		
		//Run Mode run till waits
		LabelRunTWaits=new JLabel("Run Till Waits");
		LabelRunTWaits.setBounds(845,40,150,20);
		LabelRunTWaits.setFont(new Font("Lao UI", Font.PLAIN, 15));
		APanel.add(LabelRunTWaits);
		
		RadioButtonRunTWaits=new JRadioButton("Run Till First Wait");
		RadioButtonRunTWaits.setBounds(820,40,20,20);
		RadioButtonRunTWaits.setOpaque(false);
		ButtonGroupRunMode.add(RadioButtonRunTWaits);//add to button group
		APanel.add(RadioButtonRunTWaits);//add to panel
		//add listener
		if(closeable)
		{
			ActionListenerRunTWaits=new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(RunStatus!=0)
					{
						//roll back
						RadioButtonRunTWaits.setSelected(false);
						CurrentMode.setSelected(true);
						//alert
						JOptionPane.showMessageDialog(null, "Please Stop Current Analyze First!");
						return;
					}
					
					System.out.println("RunMode change to 1");
					RunMode=1;
					//show param
					ChangeRunMode("RunTWaits");
					resetPaint();//reset paint
					CurrentMode=RadioButtonRunTWaits;//change current radiobutton pointer
				}};
		}
		else
		{
			ActionListenerRunTWaits=new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					CPanel.setActionEventRunTWaits(e);
					if(RunStatus!=0)
					{
						//roll back
						RadioButtonRunTWaits.setSelected(false);
						CurrentMode.setSelected(true);
						//alert
						JOptionPane.showMessageDialog(null, "Please Stop Current Analyze First!");
						return;
					}
					
					System.out.println("RunMode change to 1");
					RunMode=1;
					//show param
					ChangeRunMode("RunTWaits");
					resetPaint();//reset paint
					CurrentMode=RadioButtonRunTWaits;//change current radiobutton pointer
				}};
		}
		
		RadioButtonRunTWaits.addActionListener(ActionListenerRunTWaits);
		//param
		LabelRunMode1Param=new JLabel("Till Waits");
		SpinnerRunMode1Param=new JSpinner();
		LabelRunMode1Param.setBounds(860,110,150,20);
		SpinnerRunMode1Param.setBounds(860,130,70,20);
		SpinnerRunMode1Param.setValue(1);
		AddSimpleSpinnerRange(SpinnerRunMode1Param,1,1000);
		switchRunTWaitsParam(false);
		APanel.add(LabelRunMode1Param);
		APanel.add(SpinnerRunMode1Param);
			
		//Run Mode run till steps
		LabelRunTSteps=new JLabel("Run Till Steps");
		LabelRunTSteps.setBounds(845,60,100,20);
		LabelRunTSteps.setFont(new Font("Lao UI", Font.PLAIN, 15));
		APanel.add(LabelRunTSteps);
		
		RadioButtonRunTSteps=new JRadioButton("Run Till Steps");	
		RadioButtonRunTSteps.setBounds(820,60,20,20);
		RadioButtonRunTSteps.setOpaque(false);
		ButtonGroupRunMode.add(RadioButtonRunTSteps);
		APanel.add(RadioButtonRunTSteps);
		//add listener
		if(closeable)
		{
			ActionListenerRunTSteps=new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(RunStatus!=0)
					{
						//roll back
						RadioButtonRunTSteps.setSelected(false);
						CurrentMode.setSelected(true);
						//alert
						JOptionPane.showMessageDialog(null, "Please Stop Current Analyze First!");
						return;
					}
					System.out.println("RunMode change to 2");
					RunMode=2;
					//shwo param
					ChangeRunMode("RunTSteps");
					resetPaint();//reset paint
					CurrentMode=RadioButtonRunTSteps;//change current radiobutton pointer
				}};
		}
		else
		{
			ActionListenerRunTSteps=new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					CPanel.setActionEventRunTSteps(e);
					if(RunStatus!=0)
					{
						//roll back
						RadioButtonRunTSteps.setSelected(false);
						CurrentMode.setSelected(true);
						//alert
						JOptionPane.showMessageDialog(null, "Please Stop Current Analyze First!");
						return;
					}
					System.out.println("RunMode change to 2");
					RunMode=2;
					//shwo param
					ChangeRunMode("RunTSteps");
					resetPaint();//reset paint
					CurrentMode=RadioButtonRunTSteps;//change current radiobutton pointer
				}};
		}
		
		RadioButtonRunTSteps.addActionListener(ActionListenerRunTSteps);
		//param
		
		LabelRunMode2Param=new JLabel("Till Steps");
		SpinnerRunMode2Param=new JSpinner();
		LabelRunMode2Param.setBounds(860,110,150,20);
		SpinnerRunMode2Param.setBounds(860,130,70,20);
		SpinnerRunMode2Param.setValue(1);
		AddSimpleSpinnerRange(SpinnerRunMode2Param,1,6000);
		switchRunTStepsParam(false);
		APanel.add(LabelRunMode2Param);
		APanel.add(SpinnerRunMode2Param);
			
			//button (start(continue)/pause)(next)/stop
			//button start(continue)
		ImageIconStart=new ImageIcon(AnalyzePanel.class.getResource("/UI/Start.png"));
		LabelStart=new JLabel(ImageIconStart);
		LabelStart.setBounds(680, 160, ImageIconStart.getIconWidth(), ImageIconStart.getIconHeight());
		APanel.add(LabelStart);
		if(closeable)
		{
			MouseAdapterStart=new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
					if(RunStatus==0)//before is stop
					{
	//==================start action					
						if(RunMode!=0)//not single step mode
						{
							//calculate formula type
							int formulatype=0;
							//get size random param
							int sizeparam1;
							int sizeparam2;
							if(ComboSizeRandomMode.getSelectedItem().toString().equals("Average"))
							{
								sizeparam1=(int)SpinnerSizeMin.getValue();
								sizeparam2=(int)SpinnerSizeMax.getValue();
							}
							else
							{
								sizeparam1=(int)SpinnerSizeFormulaParam1.getValue();
								sizeparam2=(int)SpinnerSizeFormulaParam2.getValue();
								formulatype=2;//calculate formula type
							}
							
							//get processing time param
							int processingtimeparam1;
							int processingtimeparam2;
							if(ComboProcessingTimeRandomMode.getSelectedItem().toString().equals("Average"))
							{
								processingtimeparam1=(int)SpinnerProcessingTimeMin.getValue();
								processingtimeparam2=(int)SpinnerProcessingTimeMax.getValue();
							}
							else
							{
								processingtimeparam1=(int)SpinnerProcessingTimeFormulaParam1.getValue();
								processingtimeparam2=(int)SpinnerProcessingTimeFormulaParam2.getValue();
								formulatype++;//calculate formula type
							}
							
							//get RunMode param
							int RunModeparam;
							switch(RunMode)
							{
							case 1:RunModeparam=(int)SpinnerRunMode1Param.getValue();break;
							case 2:RunModeparam=(int)SpinnerRunMode2Param.getValue();break;
							default:System.out.println("RunMode error!");RunModeparam=0;
							}
							AlgorithmContainer AC=new AlgorithmContainer(AnalyzePanel.this,ComboAlgorithm.getSelectedItem().toString(),RunMode,RunModeparam,(int)SpinnerLoadTime.getValue(),formulatype,sizeparam1,sizeparam2,processingtimeparam1,processingtimeparam2);
							CurrentAC=AC;//set current algorithm
						}
						else
						{
							int sizefixed=(int)SpinnerRunMode0Param1.getValue();
							int processingtimefixed=(int)SpinnerRunMode0Param2.getValue();
							AlgorithmContainer AC=new AlgorithmContainer(AnalyzePanel.this,ComboAlgorithm.getSelectedItem().toString(),RunMode,(int)SpinnerLoadTime.getValue(),sizefixed,processingtimefixed);
							CurrentAC=AC;//set current algorithm
						}
						resetResult();//reset result
						resetPaint();//reset all rectangle
						Thread t=new Thread(CurrentAC);
						CurrentThread=t;
						t.start();
						setEnableStart(false);
						System.out.println("start");
					}
					else//pause or next
					{
	//==================continue action
						System.out.println("continue");
						if(CurrentAC!=null)
						{
							if(RunMode==0)//single step
							{
								CurrentAC.Continue((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
								System.out.println("sizefixed="+(int)SpinnerRunMode0Param1.getValue()+" processingtimefixed="+(int)SpinnerRunMode0Param2.getValue());
							}
							else
							{
								CurrentAC.Continue();
							}
						}
						else
						{
							System.out.println("Algorithm Error!!!");//error
						}
					}
					changeRunStatus(1);//change run state
					System.out.println("RunStatus change to 1");
					if(RunMode!=0)//not single step
					{
						LabelStart.setVisible(false);
						LabelPause.setVisible(true);
						System.out.println("Change Icon");
					}
				}
			};
		}
		else
		{
			MouseAdapterStart=new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
					CPanel.setMouseEventStart(e);
					if(RunStatus==0)//before is stop
					{
	//==================start action					
						if(RunMode!=0)//not single step mode
						{
							//calculate formula type
							int formulatype=0;
							//get size random param
							int sizeparam1;
							int sizeparam2;
							if(ComboSizeRandomMode.getSelectedItem().toString().equals("Average"))
							{
								sizeparam1=(int)SpinnerSizeMin.getValue();
								sizeparam2=(int)SpinnerSizeMax.getValue();
							}
							else
							{
								sizeparam1=(int)SpinnerSizeFormulaParam1.getValue();
								sizeparam2=(int)SpinnerSizeFormulaParam2.getValue();
								formulatype=2;//calculate formula type
							}
							
							//get processing time param
							int processingtimeparam1;
							int processingtimeparam2;
							if(ComboProcessingTimeRandomMode.getSelectedItem().toString().equals("Average"))
							{
								processingtimeparam1=(int)SpinnerProcessingTimeMin.getValue();
								processingtimeparam2=(int)SpinnerProcessingTimeMax.getValue();
							}
							else
							{
								processingtimeparam1=(int)SpinnerProcessingTimeFormulaParam1.getValue();
								processingtimeparam2=(int)SpinnerProcessingTimeFormulaParam2.getValue();
								formulatype++;//calculate formula type
							}
							
							//get RunMode param
							int RunModeparam;
							switch(RunMode)
							{
							case 1:RunModeparam=(int)SpinnerRunMode1Param.getValue();break;
							case 2:RunModeparam=(int)SpinnerRunMode2Param.getValue();break;
							default:System.out.println("RunMode error!");RunModeparam=0;
							}
							AlgorithmContainer AC=new AlgorithmContainer(AnalyzePanel.this,ComboAlgorithm.getSelectedItem().toString(),RunMode,RunModeparam,(int)SpinnerLoadTime.getValue(),formulatype,sizeparam1,sizeparam2,processingtimeparam1,processingtimeparam2);
							CurrentAC=AC;//set current algorithm
						}
						else
						{
							int sizefixed=(int)SpinnerRunMode0Param1.getValue();
							int processingtimefixed=(int)SpinnerRunMode0Param2.getValue();
							AlgorithmContainer AC=new AlgorithmContainer(AnalyzePanel.this,ComboAlgorithm.getSelectedItem().toString(),RunMode,(int)SpinnerLoadTime.getValue(),sizefixed,processingtimefixed);
							CurrentAC=AC;//set current algorithm
						}
						resetResult();//reset result
						resetPaint();//reset all rectangle
						Thread t=new Thread(CurrentAC);
						CurrentThread=t;
						t.start();
						setEnableStart(false);
						System.out.println("start");
					}
					else//pause or next
					{
	//==================continue action
						System.out.println("continue");
						if(CurrentAC!=null)
						{
							if(RunMode==0)//single step
							{
								CurrentAC.Continue((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
								System.out.println("sizefixed="+(int)SpinnerRunMode0Param1.getValue()+" processingtimefixed="+(int)SpinnerRunMode0Param2.getValue());
							}
							else
							{
								CurrentAC.Continue();
							}
						}
						else
						{
							System.out.println("Algorithm Error!!!");//error
						}
					}
					changeRunStatus(1);//change run state
					System.out.println("RunStatus change to 1");
					if(RunMode!=0)//not single step
					{
						LabelStart.setVisible(false);
						LabelPause.setVisible(true);
						System.out.println("Change Icon");
					}
				}
			};
		}
		
		LabelStart.addMouseListener(MouseAdapterStart);		
			//button pause
		ImageIconPause=new ImageIcon(AnalyzePanel.class.getResource("/UI/Pause.png"));
		LabelPause=new JLabel(ImageIconPause);
		LabelPause.setBounds(680, 160, ImageIconPause.getIconWidth(), ImageIconPause.getIconHeight());
		LabelPause.setVisible(false);
		APanel.add(LabelPause);
		if(closeable)
		{
			MouseAdapterPause=new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
	//==============pause action
					//change ui and data
					System.out.println("pause");
					changeRunStatus(2);//run state
					System.out.println("RunStatus change to 2");
					
					LabelPause.setVisible(false);
					LabelStart.setVisible(true);
					System.out.println("Change Icon");
					
					if(CurrentAC!=null)
					{
						CurrentAC.Pause();
					}
					else
					{
						System.out.println("Algorithm Error!!!");
					}
				}
			};
		}
		else
		{
			MouseAdapterPause=new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
					CPanel.setMouseEventPause(e);
	//==============pause action
					//change ui and data
					System.out.println("pause");
					changeRunStatus(2);//run state
					System.out.println("RunStatus change to 2");
					
					LabelPause.setVisible(false);
					LabelStart.setVisible(true);
					System.out.println("Change Icon");
					
					if(CurrentAC!=null)
					{
						CurrentAC.Pause();
					}
					else
					{
						System.out.println("Algorithm Error!!!");
					}
				}
			};
		}
		
		LabelPause.addMouseListener(MouseAdapterPause);
		
			//button stop
		ImageIconStop=new ImageIcon(AnalyzePanel.class.getResource("/UI/Stop.png"));
		LabelStop=new JLabel(ImageIconStop);
		LabelStop.setBounds(720, 160, ImageIconStop.getIconWidth(), ImageIconStop.getIconHeight());
		APanel.add(LabelStop);
		LabelStop.setVisible(false);
		if(closeable)
		{
			MouseAdapterStop=new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
	//==============stop action
					ACFinish();
				}
			};
		}
		else
		{
			MouseAdapterStop=new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
					CPanel.setMouseEventStop(e);
	//==============stop action
					ACFinish();
				}
			};
		}
		
		LabelStop.addMouseListener(MouseAdapterStop);
//algorithm select
		LabelAlgorithm=new JLabel("Algorithm");
		LabelAlgorithm.setBounds(600, 20, 80, 25);
		LabelAlgorithm.setFont(new Font("Lao UI",0,15));
		APanel.add(LabelAlgorithm);
		
		ComboModelAlgorithm=new DefaultComboBoxModel(StringAlgorithm);
		ComboAlgorithm=new JComboBox(ComboModelAlgorithm);
		ComboAlgorithm.setBounds(680, 20, 100, 25);
		ComboAlgorithm.setFont(new Font("Lao UI",0,15));
		ComboAlgorithm.setSelectedIndex(0);
		APanel.add(ComboAlgorithm);
//spinner LoadTime
		LabelLoadTime=new JLabel("Load Time");
		LabelLoadTime.setBounds(590, 50, 90, 25);
		LabelLoadTime.setFont(new Font("Lao UI",0,15));
		APanel.add(LabelLoadTime);
		
		SpinnerLoadTime=new JSpinner();
		SpinnerLoadTime.setValue(1000);
		SpinnerLoadTime.setBounds(680, 50, 100, 25);
		AddSimpleSpinnerRange(SpinnerLoadTime,5,2000);
		APanel.add(SpinnerLoadTime);
		
//random block size
		LabelSizeRandomMode=new JLabel("<html>Random<br>Block<br>Size</html>");
		LabelSizeRandomMode.setBounds(10, 45, 100, 60);
		LabelSizeRandomMode.setFont(new Font("Lao UI",0,15));
		APanel.add(LabelSizeRandomMode);
		ComboModelSizeRandomMode=new DefaultComboBoxModel(StringSizeRandomMode);
		ComboSizeRandomMode=new JComboBox(ComboModelSizeRandomMode);
		ComboSizeRandomMode.setBounds(10, 10, 130, 30);
		ComboSizeRandomMode.setFont(new Font("Lao UI",0,15));
		APanel.add(ComboSizeRandomMode);
		//add listener
		if(closeable)
		{
			ActionListenerSizeRandomMode=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					switchSizeFormulaNormalDistribution(ComboSizeRandomMode.getSelectedItem().toString().equals("Normal Distribution"));
					switchSizeAverage(ComboSizeRandomMode.getSelectedItem().toString().equals("Average"));
				}
			};
		}
		else
		{
			ActionListenerSizeRandomMode=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					CPanel.setActionEventSizeRandomMode(e);
					switchSizeFormulaNormalDistribution(ComboSizeRandomMode.getSelectedItem().toString().equals("Normal Distribution"));
					switchSizeAverage(ComboSizeRandomMode.getSelectedItem().toString().equals("Average"));
				}
			};
		}
		
		ComboSizeRandomMode.addActionListener(ActionListenerSizeRandomMode);
		
		//average max and min
		LabelSizeMaxRandom=new JLabel("Max=");
		LabelSizeMinRandom=new JLabel("Min=");
		SpinnerSizeMax=new JSpinner();
		SpinnerSizeMin=new JSpinner();
		LabelSizeMaxRandom.setFont(new Font("Lao UI",0,15));
		LabelSizeMinRandom.setFont(new Font("Lao UI",0,15));
		SpinnerSizeMax.setFont(new Font("Lao UI",0,15));
		SpinnerSizeMin.setFont(new Font("Lao UI",0,15));
		SpinnerSizeMax.setValue(1024);
		SpinnerSizeMin.setValue(1);
		LabelSizeMinRandom.setBounds(155, 40, 50, 30);
		LabelSizeMaxRandom.setBounds(155, 70, 50, 30);
		SpinnerSizeMin.setBounds(190, 40, 80, 25);
		SpinnerSizeMax.setBounds(190, 70, 80, 25);
		APanel.add(LabelSizeMinRandom);
		APanel.add(LabelSizeMaxRandom);
		APanel.add(SpinnerSizeMin);
		APanel.add(SpinnerSizeMax);
		switchSizeAverage(true);
		if(closeable)
		{
			ChangeListenerSizeMin=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerSizeMin.getValue()>1024)
					{
						SpinnerSizeMin.setValue(1024);
					}
					if((int)SpinnerSizeMin.getValue()<1)
					{
						SpinnerSizeMin.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerSizeMax.getValue()<(int)SpinnerSizeMin.getValue())
					{
						SpinnerSizeMax.setValue((int)SpinnerSizeMin.getValue());
					}
				}
			};
		}
		else
		{
			ChangeListenerSizeMin=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSizeMin(e);
					if((int)SpinnerSizeMin.getValue()>1024)
					{
						SpinnerSizeMin.setValue(1024);
					}
					if((int)SpinnerSizeMin.getValue()<1)
					{
						SpinnerSizeMin.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerSizeMax.getValue()<(int)SpinnerSizeMin.getValue())
					{
						SpinnerSizeMax.setValue((int)SpinnerSizeMin.getValue());
					}
				}
			};
		}
		
		SpinnerSizeMin.addChangeListener(ChangeListenerSizeMin);
		
		if(closeable)
		{
			ChangeListenerSizeMax=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerSizeMax.getValue()>1024)
					{
						SpinnerSizeMax.setValue(1024);
					}
					if((int)SpinnerSizeMax.getValue()<1)
					{
						SpinnerSizeMax.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerSizeMax.getValue()<(int)SpinnerSizeMin.getValue())
					{
						SpinnerSizeMin.setValue((int)SpinnerSizeMax.getValue());
					}
				}
			};
		}
		else
		{
			ChangeListenerSizeMax=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSizeMax(e);
					if((int)SpinnerSizeMax.getValue()>1024)
					{
						SpinnerSizeMax.setValue(1024);
					}
					if((int)SpinnerSizeMax.getValue()<1)
					{
						SpinnerSizeMax.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerSizeMax.getValue()<(int)SpinnerSizeMin.getValue())
					{
						SpinnerSizeMin.setValue((int)SpinnerSizeMax.getValue());
					}
				}
			};
		}
		
		SpinnerSizeMax.addChangeListener(ChangeListenerSizeMax);
		
		//normal distribution formula
		LabelSizeFormula=new JLabel("X~N(512,171^2)");
		LabelSizeFormulaParam1=new JLabel("¦Ì=");
		LabelSizeFormulaParam2=new JLabel("¦Ò=");
		SpinnerSizeFormulaParam1=new JSpinner();
		SpinnerSizeFormulaParam2=new JSpinner();
		LabelSizeFormula.setFont(new Font("Lao UI",0,18));
		LabelSizeFormulaParam1.setFont(new Font("Lao UI",0,15));
		LabelSizeFormulaParam2.setFont(new Font("Lao UI",0,15));
		SpinnerSizeFormulaParam1.setFont(new Font("Lao UI",0,15));
		SpinnerSizeFormulaParam2.setFont(new Font("Lao UI",0,15));
		LabelSizeFormula.setBounds(150, 10, 150, 30);
		LabelSizeFormulaParam1.setBounds(160, 40, 50, 30);
		LabelSizeFormulaParam2.setBounds(160, 70, 50, 30);
		SpinnerSizeFormulaParam1.setBounds(190, 40, 80, 25);
		SpinnerSizeFormulaParam2.setBounds(190, 70, 80, 25);
		SpinnerSizeFormulaParam1.setValue(512);
		SpinnerSizeFormulaParam2.setValue(171);
		switchSizeFormulaNormalDistribution(false);//set invisible
		APanel.add(LabelSizeFormula);
		APanel.add(LabelSizeFormulaParam1);
		APanel.add(LabelSizeFormulaParam2);
		APanel.add(SpinnerSizeFormulaParam1);
		APanel.add(SpinnerSizeFormulaParam2);
		if(closeable)
		{
			ChangeListenerSizeFormulaParam1=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerSizeFormulaParam1.getValue()>1024)
					{
						SpinnerSizeFormulaParam1.setValue(1024);
					}
					if((int)SpinnerSizeFormulaParam1.getValue()<0)
					{
						SpinnerSizeFormulaParam1.setValue(0);
					}
					LabelSizeFormula.setText("X~N("+(int)SpinnerSizeFormulaParam1.getValue()+","+(int)SpinnerSizeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		else
		{
			ChangeListenerSizeFormulaParam1=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSizeFormulaParam1(e);
					if((int)SpinnerSizeFormulaParam1.getValue()>1024)
					{
						SpinnerSizeFormulaParam1.setValue(1024);
					}
					if((int)SpinnerSizeFormulaParam1.getValue()<0)
					{
						SpinnerSizeFormulaParam1.setValue(0);
					}
					LabelSizeFormula.setText("X~N("+(int)SpinnerSizeFormulaParam1.getValue()+","+(int)SpinnerSizeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		SpinnerSizeFormulaParam1.addChangeListener(ChangeListenerSizeFormulaParam1);
		
		if(closeable)
		{
			ChangeListenerSizeFormulaParam2=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerSizeFormulaParam2.getValue()>171)
					{
						SpinnerSizeFormulaParam2.setValue(171);
					}
					if((int)SpinnerSizeFormulaParam2.getValue()<0)
					{
						SpinnerSizeFormulaParam2.setValue(0);
					}
					LabelSizeFormula.setText("X~N("+(int)SpinnerSizeFormulaParam1.getValue()+","+(int)SpinnerSizeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		else
		{
			ChangeListenerSizeFormulaParam2=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSizeFormulaParam2(e);
					if((int)SpinnerSizeFormulaParam2.getValue()>171)
					{
						SpinnerSizeFormulaParam2.setValue(171);
					}
					if((int)SpinnerSizeFormulaParam2.getValue()<0)
					{
						SpinnerSizeFormulaParam2.setValue(0);
					}
					LabelSizeFormula.setText("X~N("+(int)SpinnerSizeFormulaParam1.getValue()+","+(int)SpinnerSizeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		
		SpinnerSizeFormulaParam2.addChangeListener(ChangeListenerSizeFormulaParam2);
		
		
//random block processing time
		LabelProcessTimeRandomMode=new JLabel("<html>Random<br>Processing<br>Time</html>");
		LabelProcessTimeRandomMode.setBounds(300, 45, 100, 60);
		LabelProcessTimeRandomMode.setFont(new Font("Lao UI",0,15));
		APanel.add(LabelProcessTimeRandomMode);
		ComboModelProcessingTimeRandomMode=new DefaultComboBoxModel(StringProcessingTimeRandomMode);
		ComboProcessingTimeRandomMode=new JComboBox(ComboModelProcessingTimeRandomMode);
		ComboProcessingTimeRandomMode.setBounds(300, 10, 130, 30);
		ComboProcessingTimeRandomMode.setFont(new Font("Lao UI",0,15));
		APanel.add(ComboProcessingTimeRandomMode);
		//add listener
		if(closeable)
		{
			ActionListenerProcessingTimeRandomMode=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					switchProcessingTimeFormulaNormalDistribution(ComboProcessingTimeRandomMode.getSelectedItem().toString().equals("Normal Distribution"));
					switchProcessingTimeAverage(ComboProcessingTimeRandomMode.getSelectedItem().toString().equals("Average"));
				}
			};
		}
		else
		{
			ActionListenerProcessingTimeRandomMode=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					CPanel.setActionEventProcessingTimeRandomMode(e);
					switchProcessingTimeFormulaNormalDistribution(ComboProcessingTimeRandomMode.getSelectedItem().toString().equals("Normal Distribution"));
					switchProcessingTimeAverage(ComboProcessingTimeRandomMode.getSelectedItem().toString().equals("Average"));
				}
			};
		}
		
		ComboProcessingTimeRandomMode.addActionListener(ActionListenerProcessingTimeRandomMode);
		
		//average max and min
		LabelProcessingTimeMaxRandom=new JLabel("Max=");
		LabelProcessingTimeMinRandom=new JLabel("Min=");
		SpinnerProcessingTimeMax=new JSpinner();
		SpinnerProcessingTimeMin=new JSpinner();
		SpinnerProcessingTimeMax.setValue(5000);
		SpinnerProcessingTimeMin.setValue(1);
		LabelProcessingTimeMaxRandom.setFont(new Font("Lao UI",0,15));
		LabelProcessingTimeMinRandom.setFont(new Font("Lao UI",0,15));
		SpinnerProcessingTimeMax.setFont(new Font("Lao UI",0,15));
		SpinnerProcessingTimeMin.setFont(new Font("Lao UI",0,15));
		LabelProcessingTimeMinRandom.setBounds(445, 40, 50, 30);
		LabelProcessingTimeMaxRandom.setBounds(445, 70, 50, 30);
		SpinnerProcessingTimeMin.setBounds(480, 40, 80, 25);
		SpinnerProcessingTimeMax.setBounds(480, 70, 80, 25);
		APanel.add(LabelProcessingTimeMinRandom);
		APanel.add(LabelProcessingTimeMaxRandom);
		APanel.add(SpinnerProcessingTimeMin);
		APanel.add(SpinnerProcessingTimeMax);
		switchProcessingTimeAverage(true);
		
		if(closeable)
		{
			ChangeListenerProcessingTimeMin=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerProcessingTimeMin.getValue()>5000)
					{
						SpinnerProcessingTimeMin.setValue(5000);
					}
					if((int)SpinnerProcessingTimeMin.getValue()<1)
					{
						SpinnerProcessingTimeMin.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerProcessingTimeMax.getValue()<(int)SpinnerProcessingTimeMin.getValue())
					{
						SpinnerProcessingTimeMax.setValue((int)SpinnerProcessingTimeMin.getValue());
					}
				}
			};
		}
		else
		{
			ChangeListenerProcessingTimeMin=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventProcessingTimeMin(e);
					if((int)SpinnerProcessingTimeMin.getValue()>5000)
					{
						SpinnerProcessingTimeMin.setValue(5000);
					}
					if((int)SpinnerProcessingTimeMin.getValue()<1)
					{
						SpinnerProcessingTimeMin.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerProcessingTimeMax.getValue()<(int)SpinnerProcessingTimeMin.getValue())
					{
						SpinnerProcessingTimeMax.setValue((int)SpinnerProcessingTimeMin.getValue());
					}
				}
			};
		}
		
		SpinnerProcessingTimeMin.addChangeListener(ChangeListenerProcessingTimeMin);
		
		if(closeable)
		{
			ChangeListenerProcessingTimeMax=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerProcessingTimeMax.getValue()>5000)
					{
						SpinnerProcessingTimeMax.setValue(5000);
					}
					if((int)SpinnerProcessingTimeMax.getValue()<1)
					{
						SpinnerProcessingTimeMax.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerProcessingTimeMax.getValue()<(int)SpinnerProcessingTimeMin.getValue())
					{
						SpinnerProcessingTimeMin.setValue((int)SpinnerProcessingTimeMax.getValue());
					}
				}
			};
		}
		else
		{
			ChangeListenerProcessingTimeMax=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventProcessingTimeMax(e);
					if((int)SpinnerProcessingTimeMax.getValue()>5000)
					{
						SpinnerProcessingTimeMax.setValue(5000);
					}
					if((int)SpinnerProcessingTimeMax.getValue()<1)
					{
						SpinnerProcessingTimeMax.setValue(1);
					}
					//check spinner size max
					if((int)SpinnerProcessingTimeMax.getValue()<(int)SpinnerProcessingTimeMin.getValue())
					{
						SpinnerProcessingTimeMin.setValue((int)SpinnerProcessingTimeMax.getValue());
					}
				}
			};
		}
		SpinnerProcessingTimeMax.addChangeListener(ChangeListenerProcessingTimeMax);
		
		//normal distribution formula
		LabelProcessingTimeFormula=new JLabel("X~N(1000,100^2)");
		LabelProcessingTimeFormulaParam1=new JLabel("¦Ì=");
		LabelProcessingTimeFormulaParam2=new JLabel("¦Ò=");
		SpinnerProcessingTimeFormulaParam1=new JSpinner();
		SpinnerProcessingTimeFormulaParam2=new JSpinner();
		LabelProcessingTimeFormula.setFont(new Font("Lao UI",0,18));
		LabelProcessingTimeFormulaParam1.setFont(new Font("Lao UI",0,15));
		LabelProcessingTimeFormulaParam2.setFont(new Font("Lao UI",0,15));
		SpinnerProcessingTimeFormulaParam1.setFont(new Font("Lao UI",0,15));
		SpinnerProcessingTimeFormulaParam2.setFont(new Font("Lao UI",0,15));
		LabelProcessingTimeFormula.setBounds(440, 10, 150, 30);
		LabelProcessingTimeFormulaParam1.setBounds(450, 40, 50, 30);
		LabelProcessingTimeFormulaParam2.setBounds(450, 70, 50, 30);
		SpinnerProcessingTimeFormulaParam1.setBounds(480, 40, 80, 25);
		SpinnerProcessingTimeFormulaParam2.setBounds(480, 70, 80, 25);
		SpinnerProcessingTimeFormulaParam1.setValue(1000);
		SpinnerProcessingTimeFormulaParam2.setValue(100);
		switchProcessingTimeFormulaNormalDistribution(false);//set invisible
		APanel.add(LabelProcessingTimeFormula);
		APanel.add(LabelProcessingTimeFormulaParam1);
		APanel.add(LabelProcessingTimeFormulaParam2);
		APanel.add(SpinnerProcessingTimeFormulaParam1);
		APanel.add(SpinnerProcessingTimeFormulaParam2);
		if(closeable)
		{
			ChangeListenerProcessingTimeFormulaParam1=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerProcessingTimeFormulaParam1.getValue()>5000)
					{
						SpinnerProcessingTimeFormulaParam1.setValue(5000);
					}
					if((int)SpinnerProcessingTimeFormulaParam1.getValue()<0)
					{
						SpinnerProcessingTimeFormulaParam1.setValue(0);
					}
					LabelProcessingTimeFormula.setText("X~N("+(int)SpinnerProcessingTimeFormulaParam1.getValue()+","+(int)SpinnerProcessingTimeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		else
		{
			ChangeListenerProcessingTimeFormulaParam1=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSizeFormulaParam1(e);
					if((int)SpinnerProcessingTimeFormulaParam1.getValue()>5000)
					{
						SpinnerProcessingTimeFormulaParam1.setValue(5000);
					}
					if((int)SpinnerProcessingTimeFormulaParam1.getValue()<0)
					{
						SpinnerProcessingTimeFormulaParam1.setValue(0);
					}
					LabelProcessingTimeFormula.setText("X~N("+(int)SpinnerProcessingTimeFormulaParam1.getValue()+","+(int)SpinnerProcessingTimeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		
		SpinnerProcessingTimeFormulaParam1.addChangeListener(ChangeListenerProcessingTimeFormulaParam1);
		
		if(closeable)
		{
			ChangeListenerProcessingTimeFormulaParam2=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if((int)SpinnerProcessingTimeFormulaParam2.getValue()>1000)
					{
						SpinnerProcessingTimeFormulaParam2.setValue(1000);
					}
					if((int)SpinnerProcessingTimeFormulaParam2.getValue()<0)
					{
						SpinnerProcessingTimeFormulaParam2.setValue(0);
					}
					LabelProcessingTimeFormula.setText("X~N("+(int)SpinnerProcessingTimeFormulaParam1.getValue()+","+(int)SpinnerProcessingTimeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		else
		{
			ChangeListenerProcessingTimeFormulaParam2=new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					CPanel.setChangeEventSizeFormulaParam2(e);
					if((int)SpinnerProcessingTimeFormulaParam2.getValue()>1000)
					{
						SpinnerProcessingTimeFormulaParam2.setValue(1000);
					}
					if((int)SpinnerProcessingTimeFormulaParam2.getValue()<0)
					{
						SpinnerProcessingTimeFormulaParam2.setValue(0);
					}
					LabelProcessingTimeFormula.setText("X~N("+(int)SpinnerProcessingTimeFormulaParam1.getValue()+","+(int)SpinnerProcessingTimeFormulaParam2.getValue()+"^2)");
				}
			};
		}
		
		SpinnerProcessingTimeFormulaParam2.addChangeListener(ChangeListenerProcessingTimeFormulaParam2);
		
//result pane
		//status
		LabelResultStatus=new JLabel("Status : Stop");
		LabelResultStatus.setBounds(1010, 10, 200, 20);
		LabelResultStatus.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultStatus);
		
		//time
		LabelResultTime=new JLabel("Time : 0.0 s");
		LabelResultTime.setBounds(1010, 36, 200, 20);
		LabelResultTime.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultTime);
		
		//insert times
		LabelResultInsertTimes=new JLabel("Insert Times : 0");
		LabelResultInsertTimes.setBounds(1010, 62, 200, 20);
		LabelResultInsertTimes.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultInsertTimes);
		
		//wait times
		LabelResultWaitTimes=new JLabel("Wait Times : 0");
		LabelResultWaitTimes.setBounds(1010, 88, 200, 20);
		LabelResultWaitTimes.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultWaitTimes);
		
		//total size
		LabelResultTotalSize=new JLabel("Total Size : 0");
		LabelResultTotalSize.setBounds(1010, 114, 200, 20);
		LabelResultTotalSize.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultTotalSize);
		
		//total processing time
		LabelResultTotalProcessingTime=new JLabel("Total Processing Time : 0");
		LabelResultTotalProcessingTime.setBounds(1010, 140, 290, 20);
		LabelResultTotalProcessingTime.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultTotalProcessingTime);
		
		//max debris when wait
		LabelResultMaxDebris=new JLabel("Max Debris : 0%");
		LabelResultMaxDebris.setBounds(1010, 166, 200, 20);
		LabelResultMaxDebris.setFont(new Font("Lao UI",0,20));
		APanel.add(LabelResultMaxDebris);
	}
	
//result set below
	public void resetResult()
	{
		setResultStatus("Stop");
		setResultInsertTimes(0);
		setResultWaitTimes(0);
		setResultTotalSize(0);
		setResultTotalProcessingTime(0);
		setResultMaxDebris(0);
	}
	
	public void setResultStatus(final String Status)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LabelResultStatus.setText("Status : "+Status);
			}}).start();
	}
	
	public void beginResultTime(final long starttime)
	{
		ResultIsCounting=true;
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				while(ResultIsCounting)
				{
					Date endtime=new Date();
					LabelResultTime.setText("Time : "+new DecimalFormat("0.00").format((endtime.getTime()-starttime)/1000.0)+" s");
				}
			}}).start();
		
	}
	
	public void stopResultTime()
	{
		ResultIsCounting=false;
	}
	
	public void setResultInsertTimes(final long times)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LabelResultInsertTimes.setText("Insert Times : "+times);
			}}).start();
		
	}
	
	public void setResultWaitTimes(final long times)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LabelResultWaitTimes.setText("Wait Times : "+times);
			}}).start();
		
	}
	
	public void setResultTotalSize(final long size)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LabelResultTotalSize.setText("Total Size : "+size);
			}}).start();
		
	}
	
	public void setResultTotalProcessingTime(final long processingtime)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LabelResultTotalProcessingTime.setText("Processing Time : "+new DecimalFormat("0.00").format(processingtime/1000.0)+" s");
			}}).start();
		
	}
	
	public void setResultMaxDebris(final double debris)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LabelResultMaxDebris.setText("Max Debris : "+new DecimalFormat("0.00").format(debris)+"%");
			}}).start();
		
	}
//result set above
	
//widget help to add spinner min and max
	private void AddSimpleSpinnerRange(final JSpinner js,final int min,final int max)
	{
		js.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if((int)js.getValue()>max)
				{
					js.setValue(max);
				}
				if((int)js.getValue()<min)
				{
					js.setValue(min);
				}
			}
		});
	}
	
//frame configuration
	private void FrameConfig()
	{
		setUndecorated(true);//remove border
		setResizable(false);//disable resize
		this.setSize(1300, 200);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		APanel=(JPanel)getContentPane();
		APanel.setOpaque(false);
		APanel.setLayout(null);
	}
	
	public synchronized void paint(Graphics g)
	{
		super.paint(g);
		//paint Disk
		g.setColor(Color.BLACK);
	    g.fillRect(Disk.x, Disk.y, Disk.width, Disk.height);
	    
	    //paint next partition
	    
    	if(NextPartition!=null)
	    {
    		synchronized(NextPartition)
    	    {	
		    	int rgb=(int)(200*(NextProcessingTime/10000.0));
		    	g.setColor(new Color(255-rgb/2,200-rgb,200-rgb));
		    	//g.setColor(Color.PINK);
		    	g.fillRect(NextPartition.x, NextPartition.y, NextPartition.width, NextPartition.height);
    	    }
	    }
	    
	    //paint used block
	    //g.setColor(Color.LIGHT_GRAY);
	    synchronized(OccupiedPartition)
	    {
	    	for(int i=0;i<OccupiedPartition.size();i++)
		    {
		    	g.setColor(Color.WHITE);
		    	g.fillRect(OccupiedPartition.get(i).x, OccupiedPartition.get(i).y, 1, OccupiedPartition.get(i).height);
		    	g.setColor(Color.LIGHT_GRAY);
		    	g.fillRect(OccupiedPartition.get(i).x+1, OccupiedPartition.get(i).y, OccupiedPartition.get(i).width-2, OccupiedPartition.get(i).height);
		    	g.setColor(Color.WHITE);
		    	g.fillRect(OccupiedPartition.get(i).x+OccupiedPartition.get(i).width-1, OccupiedPartition.get(i).y, 1, OccupiedPartition.get(i).height);
		    }
	    }
	    
	    //current block
	    
    	if(CurrentPartition!=null)
	    {
    		synchronized(CurrentPartition)
    	    {
		    	g.setColor(Color.WHITE);
		    	g.fillRect(CurrentPartition.x, CurrentPartition.y, 1, CurrentPartition.height);
		    	g.setColor(Color.ORANGE);
			    g.fillRect(CurrentPartition.x+1, CurrentPartition.y, CurrentPartition.width-2, CurrentPartition.height);
			    g.setColor(Color.WHITE);
		    	g.fillRect(CurrentPartition.x+CurrentPartition.width-1, CurrentPartition.y, 1, CurrentPartition.height);
		    }
	    }
	    
	    //draw scaleplate
	    g.setColor(Color.BLACK);
	    Graphics2D g2d=(Graphics2D)g;
	    g2d.setStroke(new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
	    	//botton line
	    g2d.drawLine(10, DiskY-5, 522, DiskY-5);
	    	//rulers
	    for(int i=0;i<=512;i+=16)
	    {
	    	if(i%64==0)
	    	{
	    		String value=String.valueOf(i*2);
		    	g2d.drawChars(value.toCharArray(), 0, value.toCharArray().length, 5+i, DiskY-20);
		    	g2d.drawLine(10+i, DiskY-4, 10+i, DiskY-15);
		    	continue;
	    	}
	    	g2d.drawLine(10+i, DiskY-5, 10+i, DiskY-10);
	    }
	}
	
//update UI current partition
	public synchronized void setCurrentPartition(int address,int size)
	{
		CurrentPartition=new Rectangle(Disk.x+(int)(address/2.0+0.5),DiskY,(int)(size/2.0+0.5),DiskHeight);
		OccupiedPartition.addLast(CurrentPartition);
		//System.out.println("CurrentPartition="+(Disk.x+(int)(address/2.0+0.5))+"-"+DiskY+"-"+(int)(size/2.0+0.5)+"-"+DiskHeight);
		//System.out.println("=======insertPartition======");
		//printOccupiedPartition();
		repaint();
	}
	
//update UI next partition
	public synchronized void setNextPartition(int size,int time)
	{
		NextPartition=new Rectangle(Disk.x,190,(int)(size/2.0+0.5),5);
		NextProcessingTime=time;
		repaint();
	}
	
//clear next partition and all rectangle
	public void resetPaint()
	{
		if(RunMode==0)//single step
		{
			setNextPartition((int)SpinnerRunMode0Param1.getValue(),(int)SpinnerRunMode0Param2.getValue());
		}
		else
		{
			NextPartition=null;
			NextProcessingTime=0;
		}
		CurrentPartition=null;
		OccupiedPartition=new LinkedList<Rectangle>();
		repaint();
	}
	
	public synchronized void removePartition(int address,int size)
	{
		Rectangle r=new Rectangle(Disk.x+(int)(address/2.0+0.5),DiskY,(int)(size/2.0+0.5),DiskHeight);
		if(!OccupiedPartition.remove(r))
		{
			System.out.println("AnalyzePanel Position:"+CPanel.getAPanelList().indexOf(AnalyzePanel.this));
			System.out.println("Remove Failed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! x="+r.x+" y="+r.y+" width="+r.width+" height="+r.height);
			System.out.println("Failed Information: address="+address+" size="+size);
			if(CurrentAC.getAlgorithmSelector().getClass().equals(BuddySystem.class))
			{
				CurrentAC.getAlgorithmSelector().printLinkedAvailableTable();
			}
			else
			{
				CurrentAC.getAlgorithmSelector().printAvaliableTable();
			}
			CurrentAC.getAlgorithmSelector().printOccupiedTable();
		}
		if(r.equals(CurrentPartition))
		{
			//System.out.println("Current remove");
			CurrentPartition=null;
		}
		//System.out.println("=======removePartition======");
		//printOccupiedPartition();
		repaint();
	}

//RunStatus manage
	private void changeRunStatus(int state)//0-stop; 1-running; 2-pause;
	{
		if(state==0)//stop
		{
			LabelStart.setVisible(true);
			LabelStop.setVisible(false);
			LabelPause.setVisible(false);
		}
		else if(state==1)//running
		{
			LabelStop.setVisible(true);
			if(RunMode!=0)//not single step
			{
				LabelStart.setVisible(false);
				LabelPause.setVisible(true);
			}
		}
		else if(state==2)//pause
		{
			LabelStart.setVisible(true);
			LabelStop.setVisible(true);
			LabelPause.setVisible(false);
		}
		RunStatus=state;
	}
//take over below
	public void takeoverRandomBlockSize(boolean s)
	{}
	public void takeoverRandomProcessingTime(boolean s)
	{}
	public void takeoverAlgorithm(boolean s)
	{}
	public void takeoverRunMode(boolean s)
	{}
	public void takeoverRunStatus(boolean s)
	{}
//take over above
	
//switch function below
	private void switchSizeFormulaNormalDistribution(boolean s)
	{
		LabelSizeFormula.setVisible(s);
		LabelSizeFormulaParam1.setVisible(s);
		LabelSizeFormulaParam2.setVisible(s);
		SpinnerSizeFormulaParam1.setVisible(s);
		SpinnerSizeFormulaParam2.setVisible(s);
	}
	
	private void switchSizeAverage(boolean s)
	{
		LabelSizeMaxRandom.setVisible(s);
		LabelSizeMinRandom.setVisible(s);
		SpinnerSizeMax.setVisible(s);
		SpinnerSizeMin.setVisible(s);

	}
	
	private void switchProcessingTimeFormulaNormalDistribution(boolean s)
	{
		LabelProcessingTimeFormula.setVisible(s);
		LabelProcessingTimeFormulaParam1.setVisible(s);
		LabelProcessingTimeFormulaParam2.setVisible(s);
		SpinnerProcessingTimeFormulaParam1.setVisible(s);
		SpinnerProcessingTimeFormulaParam2.setVisible(s);
	}
	
	private void switchProcessingTimeAverage(boolean s)
	{
		LabelProcessingTimeMaxRandom.setVisible(s);
		LabelProcessingTimeMinRandom.setVisible(s);
		SpinnerProcessingTimeMax.setVisible(s);
		SpinnerProcessingTimeMin.setVisible(s);
	}
	
	private void switchSingleStep(boolean s)
	{
		LabelRunMode0Param1.setVisible(s);
		LabelRunMode0Param2.setVisible(s);
		SpinnerRunMode0Param1.setVisible(s);
		SpinnerRunMode0Param2.setVisible(s);
		
		//shield
		ComboSizeRandomMode.setEnabled(!s);
		ComboProcessingTimeRandomMode.setEnabled(!s);
		SpinnerSizeMax.setEnabled(!s);
		SpinnerSizeMin.setEnabled(!s);
		SpinnerProcessingTimeMax.setEnabled(!s);
		SpinnerProcessingTimeMin.setEnabled(!s);
		SpinnerSizeFormulaParam1.setEnabled(!s);
		SpinnerSizeFormulaParam2.setEnabled(!s);
		SpinnerProcessingTimeFormulaParam1.setEnabled(!s);
		SpinnerProcessingTimeFormulaParam2.setEnabled(!s);
	}
	
	private void switchRunTWaitsParam(boolean s)
	{
		LabelRunMode1Param.setVisible(s);
		SpinnerRunMode1Param.setVisible(s);
	}
	
	private void switchRunTStepsParam(boolean s)
	{
		LabelRunMode2Param.setVisible(s);
		SpinnerRunMode2Param.setVisible(s);
	}
	
	private void ChangeRunMode(String RunMode)
	{
		switchSingleStep(false);
		switchRunTWaitsParam(false);
		switchRunTStepsParam(false);
		switch(RunMode)
		{
		case "SingleStep":switchSingleStep(true);break;
		case "RunTWaits":switchRunTWaitsParam(true);break;
		case "RunTSteps":switchRunTStepsParam(true);break;
		}
	}
//switch function above	

//disable components
	private void setEnableStart(boolean s)
	{
		if(RunMode!=0)//single step
		{
			ComboSizeRandomMode.setEnabled(s);
			ComboProcessingTimeRandomMode.setEnabled(s);
			SpinnerSizeMax.setEnabled(s);
			SpinnerSizeMin.setEnabled(s);
			SpinnerProcessingTimeMax.setEnabled(s);
			SpinnerProcessingTimeMin.setEnabled(s);
			SpinnerSizeFormulaParam1.setEnabled(s);
			SpinnerSizeFormulaParam2.setEnabled(s);
			SpinnerProcessingTimeFormulaParam1.setEnabled(s);
			SpinnerProcessingTimeFormulaParam2.setEnabled(s);
		}		
		ComboAlgorithm.setEnabled(s);
		SpinnerLoadTime.setEnabled(s);
		SpinnerRunMode1Param.setEnabled(s);
		SpinnerRunMode2Param.setEnabled(s);
	}
	
//thread finish
	public void ACFinish()
	{
		System.out.println("Finish");
	//result
			stopResultTime();
	//data
		if(CurrentThread!=null)
		{
			CurrentThread.interrupt();
			CurrentThread=null;
		}
		if(CurrentAC!=null)
		{
			CurrentAC.Stop();
			CurrentAC=null;
		}
	//ui
		setEnableStart(true);
		changeRunStatus(0);//run state
		//clear all rectangle
		//resetPaint();
		repaint();
		System.out.println("stop : RunStatus cahnge to 0");
	
	}
//get
	public int getRunStatus()
	{
		return RunStatus;
	}
	
	public MouseAdapter getMouseAdapterStart() {
		return MouseAdapterStart;
	}

	public MouseAdapter getMouseAdapterPause() {
		return MouseAdapterPause;
	}

	public MouseAdapter getMouseAdapterStop() {
		return MouseAdapterStop;
	}

	public ActionListener getActionListenerSingleStep() {
		return ActionListenerSingleStep;
	}

	public ActionListener getActionListenerRunTWaits() {
		return ActionListenerRunTWaits;
	}

	public ActionListener getActionListenerRunTSteps() {
		return ActionListenerRunTSteps;
	}

	public ActionListener getActionListenerSizeRandomMode() {
		return ActionListenerSizeRandomMode;
	}

	public ActionListener getActionListenerProcessingTimeRandomMode() {
		return ActionListenerProcessingTimeRandomMode;
	}

	public ChangeListener getChangeListenerSpinnerRunMode0Param1() {
		return ChangeListenerSpinnerRunMode0Param1;
	}

	public ChangeListener getChangeListenerSpinnerRunMode0Param2() {
		return ChangeListenerSpinnerRunMode0Param2;
	}

	public ChangeListener getChangeListenerSizeMin() {
		return ChangeListenerSizeMin;
	}

	public ChangeListener getChangeListenerSizeMax() {
		return ChangeListenerSizeMax;
	}

	public ChangeListener getChangeListenerSizeFormulaParam1() {
		return ChangeListenerSizeFormulaParam1;
	}

	public ChangeListener getChangeListenerSizeFormulaParam2() {
		return ChangeListenerSizeFormulaParam2;
	}

	public ChangeListener getChangeListenerProcessingTimeMin() {
		return ChangeListenerProcessingTimeMin;
	}

	public ChangeListener getChangeListenerProcessingTimeMax() {
		return ChangeListenerProcessingTimeMax;
	}

	public ChangeListener getChangeListenerProcessingTimeFormulaParam1() {
		return ChangeListenerProcessingTimeFormulaParam1;
	}

	public ChangeListener getChangeListenerProcessingTimeFormulaParam2() {
		return ChangeListenerProcessingTimeFormulaParam2;
	}

	//test print occupied partition
	public void printOccupiedPartition()
	{
		for(int i=0;i<OccupiedPartition.size();i++)
		{
			System.out.println("                                           OccupiedPartirion <"+i+"> x="+OccupiedPartition.get(i).x+" y="+OccupiedPartition.get(i).y+" width="+OccupiedPartition.get(i).width+" height="+OccupiedPartition.get(i).height);
		}
	}
}
