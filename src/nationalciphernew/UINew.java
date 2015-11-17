package nationalciphernew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ProgressMonitor;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import javalibrary.Output;
import javalibrary.cipher.ColumnarRow;
import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.cipher.permentate.Permentations;
import javalibrary.cipher.stats.TraverseTree;
import javalibrary.cipher.stats.WordSplit;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.listener.CustomMouseListener;
import javalibrary.math.ArrayHelper;
import javalibrary.math.MathHelper;
import javalibrary.math.Rounder;
import javalibrary.math.Statistics;
import javalibrary.math.Units.Time;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.string.ValueFormat;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ImageUtil;
import javalibrary.swing.LayoutUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.swing.SwingHelper;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import javalibrary.thread.Threads;
import javalibrary.util.MapHelper;
import nationalcipher.Main;
import nationalciphernew.cipher.manage.DecryptionManager;
import nationalciphernew.cipher.manage.DecryptionMethod;
import nationalciphernew.cipher.manage.IDecrypt;
import nationalciphernew.cipher.manage.Solution;
import nationalciphernew.cipher.stats.StatCalculator;
import nationalciphernew.cipher.stats.StatisticType;

/**
 *
 * @author Alex
 */
public class UINew extends JFrame {

	public static String BEST_SOULTION = "";
	public static ShowTopSolutionsAction topSolutions;
	public KeyPanel keyPanel;
	
	public Settings settings;
	public Output output;

	private Thread thread;
	private Timer threadTimer;
	private List<JDialog> dialogs;
	private List<JDialog> lastStates;
	
    public UINew() {
    	super("Cryptography Solver");
    	this.settings = new Settings();
    	this.threadTimer = new Timer();
    	this.dialogs = new ArrayList<JDialog>();
    	this.lastStates = new ArrayList<JDialog>();
    	
        initComponents();
        finishComponents();
        loadDataFiles();
    }

    public void loadDataFiles() {
    	final Map<Component, Boolean> stateMap = SwingHelper.disableAllChildComponents((JComponent)getContentPane(), menuBar);
        final JProgressBar loadBar = new JProgressBar(0, Languages.languages.size() + 3);
        loadBar.setStringPainted(true);
        loadBar.setPreferredSize(new Dimension(500, 60));
		
        Object[] options = {"Cancel"};

		JOptionPane optionPane = new JOptionPane(loadBar, JOptionPane.PLAIN_MESSAGE, JOptionPane.CANCEL_OPTION, null, options, options[0]);
		final JDialog dialog = optionPane.createDialog(this, "Loading...");
		dialog.setModal(false);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(this);
		
		//Loading
		Threads.runTask(new Runnable() {
			@Override
			public void run() {
				dialog.setTitle("Loading... TranverseTree");
				TraverseTree.onLoad();
				loadBar.setValue(loadBar.getValue() + 1);
				dialog.setTitle("Loading... Dictinary");
				Dictionary.onLoad();
				loadBar.setValue(loadBar.getValue() + 1);
				dialog.setTitle("Loading... Word statitics");
				WordSplit.loadFile();
				loadBar.setValue(loadBar.getValue() + 1);
				
				
				for(ILanguage language : Languages.languages) {
					dialog.setTitle("Loading... Lang(" + language.getName() + ")");
					language.loadNGramData();
					loadBar.setValue(loadBar.getValue() + 1);
				}
				
				  
				BufferedReader updateReader3 = new BufferedReader(new InputStreamReader(TraverseTree.class.getResourceAsStream("/javalibrary/cipher/stats/trigraph.txt")));

				String[] split = null;
				try {
					split = updateReader3.readLine().split(",");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = 0;
				for(String s : split) {
					StatCalculator.bstd[i] = Integer.valueOf(s);
					i += 1;
				}
				
				SwingHelper.rewindAllChildComponents(stateMap);
				dialog.dispose();
			}
		});
		
	}

	public void finishComponents() {
		this.addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent event) {
				int newState = event.getNewState();
				if((newState & Frame.ICONIFIED) == Frame.ICONIFIED) {
					for(JDialog dialog : dialogs) {
						dialog.setVisible(false);
					}
				}
			}
		});
		this.addWindowListener(new WindowAdapter() {
			
            @Override
            public void windowDeactivated(WindowEvent e) {
            	for(JDialog dialog : dialogs) {
            		dialog.setVisible(false);
            	}
            }

            @Override
            public void windowActivated(WindowEvent e) {
            	for(JDialog dialog : dialogs)
            		dialog.setVisible(lastStates.contains(dialog));
            }
        });
		
		this.output = new Output.TextComponent(this.outputTextArea);
        this.pack();
		this.setSize(900, 800);
        this.setVisible(true);
    }
                     
    private void initComponents() {
    	this.cipherSelect = new JComboBox<String>(DecryptionManager.getNames());
    	this.decryptionType = new JComboBox<DecryptionMethod>();
    	this.inputPanel = new JPanel();
        this.inputTextScroll = new JScrollPane();
        this.inputTextArea = new JTextArea();
        this.statTextArea = new JTextArea();
    	this.outputTextScroll = new JScrollPane();
        this.outputTextArea = new JTextArea();
        this.progressBar = new JProgressBar();
        this.toolBar = new JToolBar();
        this.toolBarSettings = new JButton();
        this.toolBarStart = new JButton();
        this.toolBarStop = new JButton();
        this.menuBar = new JMenuBar();
        this.menuItemFile = new JMenu();
        this.menuItemFullScreen = new JMenuItem();
        this.menuItemExit = new JMenuItem();
        this.menuItemEdit = new JMenu();
        this.menuItemPaste = new JMenuItem();
        this.menuItemCopySolution = new JMenuItem();
        this.menuItemShowTopSolutions = new JMenuItem();
        this.menuItemBinary = new JMenuItem();
        this.menuItemTools = new JMenu();
        this.menuItemNGram = new JMenuItem();
        this.menuItemLetterFrequency = new JMenuItem();
        this.menuItemIoC = new JMenu();
        this.menuItemIoCADFGX = new JMenuItem();
        this.menuItemIoCNormal = new JMenuItem();
        this.menuItemIoCBifid = new JMenuItem();
        this.menuItemIoCNicodemus = new JMenuItem();
        this.menuItemIoCNihilist = new JMenuItem();
        this.menuItemIoCPorta = new JMenuItem();
        this.menuItemIoCVigenere = new JMenuItem();
        this.menuItemIdentify = new JMenuItem();
        this.menuItemWordSplit = new JMenuItem();
        this.menuItemInfo = new JMenuItem();
        this.menuItemSettings = new JMenu();
        this.menuItemLanguage = new JMenu();
        this.menuItemCurrentLanguage = new JMenuItem();
        this.menuItemKeyword = new JMenu();
		this.menuItemKeywordNormal = new JCheckBoxMenuItem();
		this.menuItemKeywordHalf = new JCheckBoxMenuItem();
		this.menuItemKeywordReverse = new JCheckBoxMenuItem();
		this.menuItemSimulatedAnnealing = new JMenu();
        this.menuItemSAPreset = new JMenu();
        
		this.setLayout(new GridBagLayout());

		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_open.png")));
		//this.setIconImage(image);
	

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMinimumSize(new Dimension(0, 25));
        panel.setPreferredSize(new Dimension(0, 25));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
 
     
		
        this.toolBar.setFloatable(false);
        

      	JButton nospaces = new JButton(" Remove _ ");
		nospaces.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = inputTextArea.getText();
				inputTextArea.setText(s.replaceAll("\\s+", ""));
			}
		});
		this.toolBar.add(nospaces);
		
		
		JButton nonletters = new JButton(" Keep A-Z ");
		nonletters.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = inputTextArea.getText();
				inputTextArea.setText(s.replaceAll("[^a-zA-Z]+", ""));
			}
		});
		this.toolBar.add(nonletters);
      	
		
        
        this.cipherSelect.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
        this.cipherSelect.addActionListener(new CipherSelectAction());
        this.toolBar.add(this.cipherSelect);
     		 
     	this.decryptionType.setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
		List<DecryptionMethod> methods = getDecryptManager().getDecryptionMethods();
		
		for(DecryptionMethod method : methods)
			decryptionType.addItem(method);
      	this.toolBar.add(this.decryptionType);

      	this.toolBarSettings.setText("Settings");
        this.toolBarSettings.setIcon(ImageUtil.createImageIcon("/image/cog_edit.png", "Settings"));
        this.toolBarSettings.setFocusPainted(false);
        this.toolBarSettings.setToolTipText("Edits the settings for the current selected cipher.");
        this.toolBarSettings.addActionListener(new CipherSettingsAction());
        this.toolBar.add(this.toolBarSettings);
      	
        this.toolBarStart.setText("Execute");
        this.toolBarStart.setIcon(ImageUtil.createImageIcon("/image/accept.png", "Start"));
        this.toolBarStart.setFocusPainted(false);
        this.toolBarStart.setToolTipText("Tries to decrypt the given text.");
        this.toolBarStart.addActionListener(new ExecuteAction());
        this.toolBar.add(this.toolBarStart);
        
        this.toolBarStop.setText("Terminate");
        this.toolBarStop.setIcon(ImageUtil.createImageIcon("/image/stop.png", "Terminate"));
        this.toolBarStop.setFocusPainted(false);
        this.toolBarStop.setEnabled(false);
        this.toolBarStop.setToolTipText("Terminates the current process.");
        this.toolBarStop.addActionListener(new TerminateAction());
        this.toolBar.add(this.toolBarStop);
   
        //this.toolBar.add(ButtonUtil.createIconButton(ImageUtil.createImageIcon("/image/text_letterspacing.png", "Remove Letters")));
		
		panel.add(this.toolBar);
        this.add(panel, LayoutUtil.createConstraints(0, 0, 1, 0));
        
		this.inputPanel.setLayout(new BoxLayout(this.inputPanel, BoxLayout.X_AXIS));
		this.inputTextArea.setBackground(new Color(255, 255, 220));
  
        this.inputTextArea.setLineWrap(true);
        this.inputTextArea.getDocument().addDocumentListener(new InputTextChange());
        DocumentUtil.addUndoManager(this.inputTextArea);
        this.inputTextScroll.setViewportView(this.inputTextArea);
        this.inputTextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ((AbstractDocument)this.inputTextArea.getDocument()).setDocumentFilter(new DocumentUtil.DocumentUpperCaseInput());

        this.inputPanel.add(this.inputTextScroll);
        
        this.statTextArea.setLineWrap(true);
        this.statTextArea.setEditable(false);
        this.statTextArea.setFont(this.statTextArea.getFont().deriveFont(12F));
        this.statTextArea.setText("Statistics");
	    JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scrollPane.setMinimumSize(new Dimension(300, 300));
        //scrollPane.setPreferredSize(new Dimension(300, 0));
        //scrollPane.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
    	scrollPane.setViewportView(this.statTextArea);
    	
	    this.inputPanel.add(scrollPane);
	    this.add(this.inputPanel, LayoutUtil.createConstraints(0, 1, 1, 0.2));
	    
	    
	    this.keyPanel = new KeyPanel();

	    this.add(this.keyPanel, LayoutUtil.createConstraints(0, 2, 0, 0));
	    
	    
	    
	    
	    
	    
	    //Output panel
        this.outputTextArea.setEditable(false);
        this.outputTextScroll.setViewportView(this.outputTextArea);
        this.outputTextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(this.outputTextScroll, LayoutUtil.createConstraints(0, 3, 1, 0.5));
        
        
        this.progressBar = new JProgressBar(0, 10);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		this.progressBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				progressBar.setString(Rounder.round(progressBar.getPercentComplete() * 100, 1) + "%");
			}
	    });
		this.add(this.progressBar, LayoutUtil.createConstraints(0, 4, 1, 0.01));
        
        this.menuItemFile.setText("File");
        
        this.menuItemFullScreen.setText("Full Screen");
        this.menuItemFullScreen.addActionListener(new FullScreenAction());
        this.menuItemFullScreen.setIcon(ImageUtil.createImageIcon("/image/page_white_magnify.png", "Full Screen"));
        this.menuItemFile.add(this.menuItemFullScreen);
        
        this.menuItemExit.setText("Exit");
        this.menuItemExit.addActionListener(new ExitAction());
        this.menuItemFile.add(this.menuItemExit);
        
        this.menuBar.add(this.menuItemFile);

        this.menuItemEdit.setText("Edit");
        
        this.menuItemPaste.setText("Paste");
        this.menuItemPaste.setIcon(ImageUtil.createImageIcon("/image/paste_plain.png", "Paste"));
        this.menuItemPaste.addActionListener(new PasteAction());
        this.menuItemPaste.setToolTipText("Pastes the text from the system clipboard.");
        this.menuItemEdit.add(this.menuItemPaste);
        
        this.menuItemCopySolution.setText("Copy Solution");
        this.menuItemCopySolution.setIcon(ImageUtil.createImageIcon("/image/page_copy.png", "Copy Solution"));
        this.menuItemCopySolution.addActionListener(new CopySolutionAction());
        this.menuItemCopySolution.setToolTipText("Copies the best lastest solution to the system clipboard.");
        this.menuItemEdit.add(this.menuItemCopySolution);
        
        this.menuItemShowTopSolutions.setText("Top Solutions");
       // this.menuItemShowTopSolutions.setIcon(ImageUtil.createImageIcon("/image/page_copy.png", "Top Solutions"));
        this.menuItemShowTopSolutions.addActionListener(topSolutions = new ShowTopSolutionsAction());
        this.menuItemShowTopSolutions.setToolTipText("Shows the top solutions.");
        this.menuItemEdit.add(this.menuItemShowTopSolutions);
        
        this.menuItemEdit.addSeparator();
        
        this.menuItemBinary.setText("Binary to Text");
        this.menuItemBinary.setIcon(ImageUtil.createImageIcon("/image/page_white_text.png", "Binary Convert"));
        this.menuItemBinary.addActionListener(new BinaryConvertAction());
        this.menuItemBinary.setEnabled(false);
        this.menuItemEdit.add(this.menuItemBinary);
        
        this.menuBar.add(this.menuItemEdit);

        this.menuItemTools.setText("Tools");
        this.menuItemLetterFrequency.setText("Letter Frequency");
        this.menuItemLetterFrequency.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Letter Frequency"));
        this.menuItemLetterFrequency.addActionListener(new LetterFrequencyAction());
        this.menuItemTools.add(this.menuItemLetterFrequency);
        
        
        this.menuItemNGram.setText("N-Gram Frequency");
        this.menuItemNGram.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "N-Gram Frequency"));
        this.menuItemNGram.addActionListener(new NGramFrequencyAction());
        this.menuItemTools.add(this.menuItemNGram);
        
        
        this.menuItemIoC.setText("Index of Coincidence");
        //this.menuItemIoC.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Letter Frequency"));
        this.menuItemTools.add(this.menuItemIoC);
        
        this.menuItemIoCADFGX.setText("ADFGX");
        this.menuItemIoCADFGX.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "ADFGX IoC"));
        this.menuItemIoCADFGX.addActionListener(new ADFGXIoCAction());
        this.menuItemIoC.add(this.menuItemIoCADFGX);
        
        this.menuItemIoCNormal.setText("Normal");
        this.menuItemIoCNormal.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Normal IoC"));
        this.menuItemIoCNormal.addActionListener(new NormalIoCAction());
        this.menuItemIoC.add(this.menuItemIoCNormal);
        
        this.menuItemIoCBifid.setText("Bifid");
        this.menuItemIoCBifid.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Bifid IoC"));
        this.menuItemIoCBifid.addActionListener(new BifidIoCAction());
        this.menuItemIoC.add(this.menuItemIoCBifid);
        
        this.menuItemIoCNicodemus.setText("Nicodemus");
        this.menuItemIoCNicodemus.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Nicodemus"));
        this.menuItemIoCNicodemus.addActionListener(new NicodemusIoCAction());
        this.menuItemIoC.add(this.menuItemIoCNicodemus);
        
        this.menuItemIoCNihilist.setText("Nihilist");
        this.menuItemIoCNihilist.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Nihilist"));
        this.menuItemIoCNihilist.addActionListener(new NihilistIoCAction());
        this.menuItemIoC.add(this.menuItemIoCNihilist);
        
        this.menuItemIoCPorta.setText("Porta");
        this.menuItemIoCPorta.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Porta"));
        this.menuItemIoCPorta.addActionListener(new PortaIoCAction());
        this.menuItemIoC.add(this.menuItemIoCPorta);
 
        
        this.menuItemIoCVigenere.setText("Vigenere");
        this.menuItemIoCVigenere.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Vigenere"));
        this.menuItemIoCVigenere.addActionListener(new VigenereIoCAction());
        this.menuItemIoC.add(this.menuItemIoCVigenere);
        
        this.menuItemTools.addSeparator();
        
        this.menuItemWordSplit.setText("Word Split");
        this.menuItemWordSplit.setIcon(ImageUtil.createImageIcon("/image/spellcheck.png", "Paste"));
        this.menuItemWordSplit.addActionListener(new WordSplitAction());
        this.menuItemTools.add(this.menuItemWordSplit);
        
        this.menuItemIdentify.setText("Identify Cipher");
        this.menuItemIdentify.setIcon(ImageUtil.createImageIcon("/image/page_white_find.png", "Idenifty Cipher"));
        this.menuItemIdentify.addActionListener(new IdentifyAction());
        this.menuItemTools.add(this.menuItemIdentify);
        
        this.menuItemInfo.setText("Text Information");
        this.menuItemInfo.setIcon(ImageUtil.createImageIcon("/image/information.png", "Information"));
        this.menuItemInfo.addActionListener(new TextInformationAction());
        this.menuItemTools.add(this.menuItemInfo);
        
        this.menuBar.add(this.menuItemTools);

        this.menuItemSettings.setText("Settings");
        
        this.menuItemLanguage.setText("Language");

		this.menuItemLanguage.setIcon(ImageUtil.createImageIcon("/image/globe.png", "Language"));
		this.menuItemCurrentLanguage.setText("Current: " + settings.language.getName());
		
		
		this.menuItemLanguage.add(this.menuItemCurrentLanguage);
		this.menuItemLanguage.addSeparator();
		ButtonGroup group = new ButtonGroup();
		for(ILanguage language : Languages.languages) {
			JMenuItem jmi = new JCheckBoxMenuItem(language.getName(), ImageUtil.createImageIcon(language.getImagePath(), language.getName()));
			jmi.addActionListener(new LanguageChangeAction(language));
			this.menuItemLanguage.add(jmi);
			group.add(jmi);
			if(language == this.settings.language) jmi.setSelected(true);
		}
		this.menuItemSettings.add(this.menuItemLanguage);

		this.menuItemKeyword.setText("Keyword");
		
		this.menuItemKeywordNormal.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		this.menuItemKeywordHalf.setText("NOPQRSTUVWXYZABCDEFGHIJKLM");
		this.menuItemKeywordReverse.setText("ZYXWVUTSRQPONMLKJIHGFEDCBA");
		
		new JMenuItem[]{this.menuItemKeywordNormal, this.menuItemKeywordHalf, this.menuItemKeywordReverse}[this.settings.keywordCreation].setSelected(true);
		
		this.settings.keywordCreationGroup = new ButtonGroup();
		this.settings.keywordCreationGroup.add(this.menuItemKeywordNormal);
		this.settings.keywordCreationGroup.add(this.menuItemKeywordHalf);
		this.settings.keywordCreationGroup.add(this.menuItemKeywordReverse);
		
		this.menuItemKeywordNormal.addActionListener(new KeywordCreationAction(0));
		this.menuItemKeywordHalf.addActionListener(new KeywordCreationAction(1));
		this.menuItemKeywordReverse.addActionListener(new KeywordCreationAction(2));
		
		this.menuItemKeyword.add(this.menuItemKeywordNormal);
		this.menuItemKeyword.add(this.menuItemKeywordHalf);
		this.menuItemKeyword.add(this.menuItemKeywordReverse);
		
		this.menuItemSettings.add(this.menuItemKeyword);
		
		this.menuItemSimulatedAnnealing.setText("Simulated Annealing");
		
		JTextField tempSetting = new JTextField(ValueFormat.getNumber(this.settings.simulatedAnnealing.get(0)));
		((AbstractDocument)tempSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentDoubleInput(tempSetting));
		tempSetting.addKeyListener(new SimulatedAnnealingAction(tempSetting, 0));
		this.menuItemSimulatedAnnealing.add(new JLabel("Temperature Value"));
		this.menuItemSimulatedAnnealing.add(tempSetting);
		this.menuItemSimulatedAnnealing.addSeparator();
		JTextField tempStepSetting = new JTextField(ValueFormat.getNumber(this.settings.simulatedAnnealing.get(1)));
		((AbstractDocument)tempStepSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentDoubleInput(tempStepSetting));
		tempStepSetting.addKeyListener(new SimulatedAnnealingAction(tempStepSetting, 1));
		this.menuItemSimulatedAnnealing.add(new JLabel("Temperature Step"));
		this.menuItemSimulatedAnnealing.add(tempStepSetting);
		this.menuItemSimulatedAnnealing.addSeparator();
		JTextField countSetting = new JTextField(ValueFormat.getNumber(this.settings.simulatedAnnealing.get(2)));
		((AbstractDocument)countSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		countSetting.addKeyListener(new SimulatedAnnealingAction(countSetting, 2));
		this.menuItemSimulatedAnnealing.add(new JLabel("Count"));
		this.menuItemSimulatedAnnealing.add(countSetting);
		
		this.menuItemSAPreset.setText("Presets");
		JMenuItem saPreset1 = new JMenuItem("Substitution");
		saPreset1.addActionListener(new PresetAction(tempSetting, tempStepSetting, countSetting, 20D, 0.1D, 100));
		this.menuItemSAPreset.add(saPreset1);
		JMenuItem saPreset2 = new JMenuItem("Playfair");
		saPreset2.addActionListener(new PresetAction(tempSetting, tempStepSetting, countSetting, 20D, 0.1D, 10000));
		this.menuItemSAPreset.add(saPreset2);
		//JMenuItem saPreset3 = new JMenuItem("Substitution");
		//saPreset3.addActionListener(new PresetAction(tempSetting, tempStepSetting, countSetting, 20D, 0.1D, 100));
		//this.menuItemSAPreset.add(saPreset3);
		
		
		
		this.menuItemSimulatedAnnealing.add(this.menuItemSAPreset);
		
		this.menuItemSettings.add(this.menuItemSimulatedAnnealing);
		
		
        this.menuBar.add(this.menuItemSettings);

        
        
        
        this.setJMenuBar(this.menuBar);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        //this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        ((JPanel)this.getContentPane()).setBorder(new EmptyBorder(0, 5, 5, 5));
    }
    
    private class InputTextChange extends DocumentUtil.DocumentChangeAdapter {

		@Override
		public void onUpdate(DocumentEvent event) {
			try {
				String inputText = event.getDocument().getText(0, event.getDocument().getLength());
				String statText = "";
				
				List<Integer> factors = MathHelper.getFactors(inputText.length());
				Collections.sort(factors);
				statText +=  "Length: " + inputText.length() + " " + factors;
				statText += "\n A-Z: " + StringTransformer.countLetterChars(inputText);
				statText += "\n 0-9: " + StringTransformer.countDigitChars(inputText);
				statText += "\n ___: " + StringTransformer.countSpacesChars(inputText);
				statText += "\n *?!: " + StringTransformer.countOtherChars(inputText);
				statText += "\nSuggested Fitness: " + TextFitness.getEstimatedFitness(inputText, settings.language);
				statText += "\nActual Fitness: " + TextFitness.scoreFitnessQuadgrams(inputText, settings.language);
				statTextArea.setText(statText);
				
				menuItemBinary.setEnabled(inputText.length() != 0 && inputText.replaceAll("[^0-1]", "").length() == inputText.length());
			} 
			catch(BadLocationException e) {
				e.printStackTrace();
			}
		}
    }
    
    private class CipherSelectAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		DecryptionMethod lastMethod = (DecryptionMethod)decryptionType.getSelectedItem();
    		decryptionType.removeAllItems();
    		IDecrypt decrypt = getDecryptManager();
    		List<DecryptionMethod> methods = decrypt.getDecryptionMethods();
    		
    		for(DecryptionMethod method : methods)
    			decryptionType.addItem(method);
    		
    		if(methods.contains(lastMethod))
    			decryptionType.setSelectedItem(lastMethod);
    	}
    }
    
    private class CipherSettingsAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		final JDialog dialog = new JDialog();
    		dialog.setTitle("Cipher Settings");
    		
    		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/cog.png")));
    		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		dialog.setLocationRelativeTo(UINew.this);
    		dialog.setResizable(false);
    		dialog.setMinimumSize(new Dimension(400, 200));
    		
    		ActionListener escListener = new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	        	dialog.dispatchEvent(new WindowEvent( 
    	                    dialog, WindowEvent.WINDOW_CLOSING 
    	                ));
    	        }
    	    };

    	    dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    		
    		
	        IDecrypt force = getDecryptManager();
	        force.createSettingsUI(dialog);
	   
			dialog.setModal(true);
			dialog.setVisible(true);
   
    	}
    }
    
    private class ExecuteAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			final String text = inputTextArea.getText();
			
			if(text == null || text.isEmpty())
				return;
			
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					threadTimer.restart();
					UINew.BEST_SOULTION = "";
					IDecrypt force = getDecryptManager();
					output.println("Cipher: " + force.getName());
					
					DecryptionMethod method = (DecryptionMethod)decryptionType.getSelectedItem();
					
					try {
						force.attemptDecrypt(text, settings, method, output, keyPanel, new ProgressValue(1000, progressBar));
					}
					catch(Exception e) {
						output.println(e.toString());
						e.printStackTrace();
					}
					
					DecimalFormat df = new DecimalFormat("#.#");
					output.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
					output.println("");
					toolBarStart.setEnabled(true);
					toolBarStop.setEnabled(false);
					menuItemSettings.setEnabled(true);
					try {
						Thread.sleep(1000L);
					} 
					catch(InterruptedException e) {
						e.printStackTrace();
					}
					progressBar.setIndeterminate(false);
					progressBar.setMaximum(10);
					progressBar.setValue(0);
				}
				
			});
			thread.start();
			toolBarStart.setEnabled(false);
			toolBarStop.setEnabled(true);
			menuItemSettings.setEnabled(false);
		}
    }
    
    public class TerminateAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if(thread != null)
				thread.stop();
			
			DecimalFormat df = new DecimalFormat("#.#");
			output.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
			output.println("");
			toolBarStart.setEnabled(true);
			toolBarStop.setEnabled(false);
			menuItemSettings.setEnabled(true);
			
			try {
				Thread.sleep(500L);
			} 
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			progressBar.setIndeterminate(false);
			progressBar.setMaximum(10);
			progressBar.setValue(0);
		}
    }
    
    private class FullScreenAction implements ActionListener {

    	public Dimension lastSize;
    	public Point lastLocation;
    	
		@Override
		public void actionPerformed(ActionEvent event) {
			dispose();
			if(!isUndecorated()) {
				setExtendedState(Frame.MAXIMIZED_BOTH);
		    	setUndecorated(true);
			}
			else {
				setSize(this.lastSize);
				setLocation(this.lastLocation);
				setExtendedState(Frame.NORMAL);
				setUndecorated(false);
			}
			
			this.lastSize = getSize();
			this.lastLocation = getLocation();
			setVisible(true);
		}
    }
    
    private class ExitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			System.exit(128);
		}
    }
    
    public class PasteAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		try {
				String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				inputTextArea.setText(data);
			} 
    		catch(Exception e) {
				e.printStackTrace();
			}
		}
    }

    public class CopySolutionAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		if(!BEST_SOULTION.isEmpty()) {
	    		StringSelection selection = new StringSelection(BEST_SOULTION);
	    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    		}
		}
    }
    
    public class ShowTopSolutionsAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JTextArea textOutput;
    	
    	public ShowTopSolutionsAction() {
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Top Solutions");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(900, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        this.textOutput = new JTextArea();

	        JScrollPane scrollPane = new JScrollPane(this.textOutput);
	
	        panel.add(scrollPane);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
		}
    	
    	public void updateDialog(List<Solution> solutions) {
    		String text = "";
    		
    		for(int i = 0; i < Math.min(100, solutions.size()); i++)
    			text += String.format(i + " Fitness: %f, Key: %s, Plaintext: %s\n", solutions.get(i).score, solutions.get(i).keyString, new String(solutions.get(i).text));	
    		textOutput.setText(text);
    		textOutput.revalidate();
    	}
    }
    
    public class BinaryConvertAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = inputTextArea.getText();
			List<String> split = StringTransformer.splitInto(binaryText, (int)5);
			
			String cipherText = "";
			for(String binary : split) {
				try {
					int decimal = Integer.parseInt(binary, 2);
					if(decimal < 0 || decimal > 25) {
						cipherText = "ERROR: Binary number \'" + binary + "' dec(" + decimal + ") is valid letter"; 
						break;
					}
					char letter = (char)(decimal + 'A');
					cipherText += letter;
				}
				catch(NumberFormatException e) {
					cipherText = "ERROR: Cannot parse \'" + binary + "'"; 
					break;
				}
			}
			inputTextArea.setText(cipherText);
		}
    }
    
    public class WordSplitAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JTextArea textOutput;
    	
    	public WordSplitAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Word Split");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 200));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        
	        this.textOutput = new JTextArea();
	        this.textOutput.setLineWrap(true);

	        JScrollPane scrollPane = new JScrollPane(this.textOutput);
	
	        panel.add(scrollPane);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		String split = WordSplit.splitText(inputTextArea.getText().replaceAll(" ", ""));
    		textOutput.setText(split);
    		textOutput.revalidate();
    	}
    }
    
    private class JDialogCloseEvent extends WindowAdapter {
    	
    	private JDialog dialog;
    	
    	public JDialogCloseEvent(JDialog dialog) {
    		this.dialog = dialog;
    		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	}
    	
		@Override
		public void windowClosed(WindowEvent event) {
			lastStates.remove(dialog);
		}
    }
    
    public class LetterFrequencyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chartAlphabeticly;
    	
    	public LetterFrequencyAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}
				}
    			
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Letter Frequency");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
	        panel.add(this.chart);
	        
	        this.chartAlphabeticly = new JBarChart(new ChartList());
	        this.chartAlphabeticly.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered Alphabeticly"));
	        panel.add(this.chartAlphabeticly);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
	        this.chartAlphabeticly.resetAll();
    		
    		String text = getInputTextOnlyAlpha();
			
    		if(!text.isEmpty()) {
	    		
	    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(text, 1, 1, false);
				
				List<String> asendingOrder = new ArrayList<String>(counts.keySet());
				Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
				Collections.reverse(asendingOrder);
				
		        for(String letterCount : asendingOrder)
		        	this.chart.values.add(new ChartData(letterCount, (double)counts.get(letterCount)));
				
				
		        if(!counts.isEmpty())
		        	for(char ch = 'A'; ch <= 'Z'; ch++)
		        		this.chartAlphabeticly.values.add(new ChartData("" + ch, (double)(counts.containsKey("" + ch) ? counts.get("" + ch) : 0)));
    		}
    		
			this.chart.repaint();
			this.chartAlphabeticly.repaint();
    	}
    }
    
    private class NGramFrequencyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JComboBox<String> comboBox;
    	
    	public NGramFrequencyAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}
				}
    			
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("N-Gram Frequency");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(900, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart();
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
	        panel.add(this.chart);

	        this.comboBox = new JComboBox<String>(new String[] {"ALL", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
	        this.comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	        this.comboBox.addItemListener(new ItemListener() {
				@Override
			    public void itemStateChanged(ItemEvent event) {
					if(event.getStateChange() == ItemEvent.SELECTED) {
						updateDialog();
			       }
			    }       
			});
	        panel.add(this.comboBox);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
    		
    		String text = getInputTextOnlyAlpha();
			
    		if(!text.isEmpty()) {
	    		
	    		
	    		String label = (String)this.comboBox.getSelectedItem();
				int minlength = 2;
	      		int maxlength = 15;
	      		if(!label.contains("ALL")) {
	      			minlength = Integer.valueOf(label);
	      			maxlength = minlength;
	      		}
    			
	    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(text, minlength, maxlength, true);
				
	    		List<String> asendingOrder = new ArrayList<String>(counts.keySet());
				Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
				Collections.reverse(asendingOrder);
	    		
		        for(String ngram : asendingOrder)
		        	if(this.chart.values.size() < 40)
		        		this.chart.values.add(new ChartData(ngram, (double)counts.get(ngram)));
    		}
    		
			this.chart.repaint();
    	}
    }
    
    public class ADFGXIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public ADFGXIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("ADFGX IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Step Calculation"));
	        panel.add(this.chart);
	        
	        this.chart2 = new JBarChart(new ChartList());
	        this.chart2.setHasBarText(false);
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart2);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		this.chart.resetAll();
    		this.chart2.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			
    			Figure figure = new Figure(text.toCharArray(), settings.getLanguage());
    			
    			for(int length = 2; length <= 6; length++)
    				Permentations.permutate(figure, ArrayHelper.range(0, length));
    			
    			String s = "";
    			
    			s += "Like english: " + figure.smallest + "\n";
    			for(Integer[] i : figure.orders1) {
    				s += Arrays.toString(i) + "\n";
    			}
    			
    			s += "\nIOC: " + figure.closestIC + " off normal\n";
    			
    			for(Integer[] i : figure.orders2) {
    				s += Arrays.toString(i) + "\n";
    			}
    			
    			  output.println("----------- ADFGVX ----------- ");
    				output.print(s);
    			  //  output.println(" IoC Calculation: " + bestPeriod);
    			    output.println("");
    			
    			/**
    			int bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 20; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateEvenDiagrahpicIC(StringTransformer.getEveryNthBlock(text, 2, i, period));
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.pow(total - settings.language.getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.chart2.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.chart2.setSelected(bestPeriod - 2);**/
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    public static class Figure implements PermentateArray {

		public char[] text;
		public ILanguage language;
		public double smallest = Double.MAX_VALUE;
		public List<Integer[]> orders1 = new ArrayList<Integer[]>();
		public double closestIC = Double.MAX_VALUE;
		public List<Integer[]> orders2 = new ArrayList<Integer[]>();
		
		
		public Figure(char[] text, ILanguage language) {
			this.text = text;
			this.language = language;
		}
		
		@Override
		public void onPermentate(int[] array) {
			System.out.println(""+ Arrays.toString(array));
			char[] s = ColumnarRow.decode(this.text, array);
			String str = new String(s);
			double n = calculate(str, this.language);
	    	double evenDiagraphicIC = StatCalculator.calculateEvenDiagrahpicIC(str);
	    	
	    	Integer[] arr = new Integer[array.length];
	    	for(int i = 0; i <array.length; i++)
	    		arr[i] = Integer.valueOf(array[i]);
	    	
	    	
	    	if(n <= smallest) {
	    		if(n != smallest)
	    			orders1.clear();
	    		orders1.add(arr);
	    		smallest = n;
	    	}
	    	double sqDiff = Math.pow(this.language.getNormalCoincidence() - evenDiagraphicIC, 2);
	    	if(sqDiff <= closestIC) {
	    		if(n != smallest)
	    			orders2.clear();
	    		orders2.add(arr);
	    		
	    		closestIC = sqDiff;
	    	}
		}
		
	}
	
	public static double calculate(String text, ILanguage language) {
		Map<String, Integer> letters = MapHelper.sortMapByValue(StringAnalyzer.getEmbeddedStrings(text, 2, 2, false), false);
		double total = 0.0D;
		
		List<Double> normalOrder = language.getFrequencyLargestFirst();
		
		int index = 0;
		for(String letter : letters.keySet()) {
			
			double count = letters.get(letter);
			double expectedCount = normalOrder.get(index) * (text.length() / 2) / 100;
			
			double sum = Math.abs(count - expectedCount);
			index += 1;
			total += sum;
			if(index >= normalOrder.size())
				break;
		}
		
		return total;
	}
    
    private class NormalIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public NormalIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Normal IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Normal IoC Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 0; period <= Math.min(40, text.length()); ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.language.getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    			
    		    this.chart.setSelected(bestPeriod);
    		}
    		
    		this.chart.repaint();
    	}
    }
    
    public class BifidIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public BifidIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Bifid IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Step Calculation"));
	        panel.add(this.chart);
	        
	        this.chart2 = new JBarChart(new ChartList());
	        this.chart2.setHasBarText(false);
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart2);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		this.chart.resetAll();
    		this.chart2.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
				
				Map<Integer, Double> values = new HashMap<Integer, Double>();
				double maxValue = Double.MIN_VALUE;
				int maxStep = -1;
				
				double secondValue = Double.MIN_VALUE;
				
				for(int step = 1; step <= Math.min(40, text.length()); step++) {
					HashMap<String, Integer> counts = new HashMap<String, Integer>();
					for(int i = 0; i < text.length() - step; i++) {
						String s = text.charAt(i) + "" + text.charAt(i + step);
						counts.put(s, counts.containsKey(s) ? counts.get(s) + 1 : 1);
					}
					
					Statistics stats = new Statistics(counts.values());
				    double variance = stats.getVariance();
				 
				    this.chart.values.add(new ChartData("Step: " + step, variance));
					values.put(step, variance);
					
					if(variance > maxValue) {
						secondValue = maxValue;
						maxValue = variance;
						maxStep = step;
					}
					else if(variance > secondValue) {
						secondValue = variance;
					}
				}
	
				
				int periodGuess = -1;
	
				if(maxStep != -1) {
					if(maxValue - maxValue / 4 > secondValue)
						periodGuess = maxStep * 2;
					else {
						double max = Double.MAX_VALUE;
						int bestStep = 0;
						
						for(int step = maxStep - 1; step <= maxStep + 1; step++) {
							if(!values.containsKey(step) || step == maxStep)
								continue;
							
							double diff = Math.abs(values.get(maxStep) - values.get(step));
							if(diff < max) {
								max = diff;
								bestStep = step;
							}
						}
						this.chart.setSelected(bestStep - 1);
						
						periodGuess = Math.min(bestStep, maxStep) * 2 + Math.abs(bestStep - maxStep);
					}
				}
				
				int bestPeriod = -1;
				double bestIC = Double.MIN_VALUE;
			    for(int period = 0; period <= 40; period++) {
			    	if(period == 1) continue;
			    	
			        double score = StatCalculator.calculateBifidDiagraphicIC(text, period);
			        this.chart2.values.add(new ChartData("Period: " + period, score));
			        if(bestIC < score)
			        	bestPeriod = period;
			        
			        bestIC = Math.max(bestIC, score);
			    }
			    
			    this.chart.setSelected(maxStep - 1);
				this.chart2.setSelected(bestPeriod > 0 ? bestPeriod - 1 : 0);
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    private class NicodemusIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public NicodemusIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Nicodemus IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestIC = Double.POSITIVE_INFINITY;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateNicodemusIC(text, 5, period) - settings.language.getNormalCoincidence(), 2) * 10000;
    		    	
    		    	if(sqDiff < bestIC)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIC = Math.min(bestIC, sqDiff);
    		    }
    			
    		    this.chart.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    	}
    }
    
    private class NihilistIoCAction implements ActionListener {

    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public NihilistIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Nihilist IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Nihilist IoC"));
	        panel.add(this.chart);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyDigits();
    		if(!text.isEmpty() && text.length() % 2 == 0) {
    			int bestPeriod = -1;
    		    double bestIC = Double.POSITIVE_INFINITY;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateDiagraphicKappaIC(text, period * 2) - settings.language.getNormalCoincidence(), 2) * 10000;
    		    	
    		    	if(sqDiff < bestIC)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIC = Math.min(bestIC, sqDiff);
    		    }
    			
    		    this.chart.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    	}
    }
    
    public class PortaIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public PortaIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Porta IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		
    		this.chart.repaint();
    	}
    }
    
    private class VigenereIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public VigenereIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Vigenere IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kappa IoC Calculation"));
	        panel.add(this.chart);
	        
	        this.chart2 = new JBarChart(new ChartList());
	        this.chart2.setHasBarText(false);
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Average IoC Calculation"));
	        panel.add(this.chart2);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			this.chart2.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.language.getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    		    
    		    this.chart.setSelected(bestPeriod - 2);
    		    
    		    bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period));
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.pow(total - settings.language.getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.chart2.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.chart2.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    private class IdentifyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JPanel cipherInfoPanel;
    	private JPanel cipherScorePanel;
    	private  JScrollPane scrollPane;
    	
    	public IdentifyAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Identify Cipher");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	          
	        JPanel basePanel = new JPanel();
	        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
	        scrollPane = new JScrollPane(basePanel);
		    scrollPane.getVerticalScrollBar().setUnitIncrement(12);
	        
	        
	        this.cipherInfoPanel = new JPanel();
		    this.cipherInfoPanel.setLayout(new BoxLayout(this.cipherInfoPanel, BoxLayout.Y_AXIS));
		    basePanel.add(this.cipherInfoPanel);
		    
		    this.cipherScorePanel = new JPanel();
		    this.cipherScorePanel.setLayout(new BoxLayout(this.cipherScorePanel, BoxLayout.Y_AXIS));
		    basePanel.add(this.cipherScorePanel);
		    
		    
		    panel.add(scrollPane);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		this.cipherInfoPanel.removeAll();
    		this.cipherScorePanel.removeAll();
    		
    		String text = getInputText();
    		
    		if(!text.isEmpty()) {
    			
    			Object[] statValues = new Object[38];
			    
			    statValues[0] = StatCalculator.calculateIC(text) * 1000.0D;
				statValues[1] = StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D;
				statValues[2] = StatCalculator.calculateMaxKappaIC(text, 1, 15);
				statValues[3] = StatCalculator.calculateDiagrahpicIC(text) * 10000.0D;
				statValues[4] = StatCalculator.calculateEvenDiagrahpicIC(text) * 10000;
				statValues[5] = StatCalculator.calculateLR(text);
				statValues[6] = StatCalculator.calculateROD(text);
				statValues[7] = StatCalculator.calculateLDI(text);
				statValues[8] = StatCalculator.calculateSDD(text);
			    
				statValues[9] = "CIPHER";
			    
				statValues[10] = StatCalculator.isLengthDivisible2(text);
				statValues[11] = StatCalculator.isLengthDivisible3(text);
				statValues[12] = StatCalculator.isLengthDivisible5(text);
				statValues[13] = StatCalculator.isLengthDivisible25(text);
				statValues[14] = StatCalculator.isLengthDivisible4_15(text);
				statValues[15] = StatCalculator.isLengthDivisible4_30(text);
				statValues[16] = StatCalculator.isLengthPerfectSquare(text);
				statValues[17] = StatCalculator.containsLetter(text);
				statValues[18] = StatCalculator.containsDigit(text);
				statValues[19] = StatCalculator.containsJ(text);
				statValues[20] = StatCalculator.containsHash(text);
				statValues[21] = StatCalculator.calculateDBL(text);
				
				
				statValues[22] = StatCalculator.calculateALDI(text);
				statValues[23] = StatCalculator.calculateBLDI(text);
				statValues[24] = StatCalculator.calculatePLDI(text);
				statValues[25] = StatCalculator.calculateSLDI(text);
				statValues[26] = StatCalculator.calculateVLDI(text);
				statValues[27] = StatCalculator.calculateNormalOrder(text, settings.getLanguage());
				statValues[28] = StatCalculator.calculateRDI(text);
				statValues[29] = StatCalculator.calculatePTX(text);
				statValues[30] = StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
				statValues[31] = StatCalculator.calculatePHIC(text);
				statValues[32] = StatCalculator.calculateHAS0(text);
				statValues[33] = StatCalculator.calculateMaxBifidDiagraphicIC(text, 3, 15);
				statValues[34] = StatCalculator.calculateCDD(text);
				statValues[35] = StatCalculator.calculateSSTD(text);
				statValues[36] = StatCalculator.calculateMPIC(text);
				statValues[37] = StatCalculator.calculateSeriatedPlayfair(text);
			    
				Map<String, Integer> answers = new HashMap<String, Integer>();
				
				for(Map map : TraverseTree.trees) {
					String answer = TraverseTree.traverse_tree(map, statValues);
					answers.put(answer, 1 + (answers.containsKey(answer) ? answers.get(answer) : 0));
				}
				answers = MapHelper.sortMapByValue(answers, false);
				
				JLabel cipherScoreLabel = new JLabel("" + answers);
		    	cipherScoreLabel.setFont(cipherScoreLabel.getFont().deriveFont(20F));
		    	this.cipherScorePanel.add(cipherScoreLabel);
				System.out.println(answers);
    			
    			/**
	    		HashMap<StatisticType, Double> currentData = new HashMap<StatisticType, Double>();
			    
			    currentData.put(StatisticType.INDEX_OF_COINCIDENCE, StatCalculator.calculateIC(text) * 1000.0D);
			    currentData.put(StatisticType.MAX_IOC, StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D);
			    currentData.put(StatisticType.MAX_KAPPA, StatCalculator.calculateMaxKappaIC(text, 1, 15));
			    currentData.put(StatisticType.DIGRAPHIC_IOC, StatCalculator.calculateDiagrahpicIC(text) * 10000.0D);
			    currentData.put(StatisticType.EVEN_DIGRAPHIC_IOC, StatCalculator.calculateEvenDiagrahpicIC(text) * 10000);
			    currentData.put(StatisticType.LONG_REPEAT_3, StatCalculator.calculateLR(text));
			    currentData.put(StatisticType.LONG_REPEAT_ODD, StatCalculator.calculateROD(text));
			    currentData.put(StatisticType.LOG_DIGRAPH, StatCalculator.calculateLDI(text));
			    currentData.put(StatisticType.SINGLE_LETTER_DIGRAPH, StatCalculator.calculateSDD(text));
	    		
	    		List<List<Object>> num_dev = StatCalculator.getResultsFromStats(currentData);
			    
			    Comparator<List<Object>> comparator = new Comparator<List<Object>>() {
			    	@Override
			        public int compare(List<Object> c1, List<Object> c2) {
			        	double diff = (double)c1.get(1) - (double)c2.get(1);
			        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
			        }
			    };
	
			    Collections.sort(num_dev, comparator);
			    
			    JLabel titleLabel = new JLabel("Cipher");
			    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
			    this.cipherInfoPanel.add(titleLabel);
			    
			    JLabel titleScoreLabel = new JLabel("Likelyhood");
			    titleScoreLabel.setFont(titleScoreLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
			    this.cipherScorePanel.add(titleScoreLabel);
			    
			    for(int i = 0; i < num_dev.size(); i++) {
			    	JLabel cipherInfoLabel = new JLabel(num_dev.get(i).get(0) + ":            ");
			    	cipherInfoLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	this.cipherInfoPanel.add(cipherInfoLabel);
			    	
			    	
			    	JLabel cipherScoreLabel = new JLabel("" + Rounder.round((double)num_dev.get(i).get(1), 2));
			    	cipherScoreLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	this.cipherScorePanel.add(cipherScoreLabel);
			    }**/
    		}
    		this.cipherInfoPanel.revalidate();
    	}
    }
     
    private class TextInformationAction implements ActionListener {
    	
    	private JDialog dialog;
    	public JTextArea output;
    	
    	public TextInformationAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Text Statistics");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(375, 600));
	       
	        this.output = new JTextArea();
			JScrollPane outputScrollPanel = new JScrollPane(this.output);
			outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			outputScrollPanel.setPreferredSize(new Dimension(1000, 200));
			this.output.setEditable(false);
			this.output.setLineWrap(true);
		
	
    		this.dialog.add(outputScrollPanel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		String text = getInputTextOnlyAlpha();
    		int length = text.length();
    		
    		String outputText = "Length: " + length;
			outputText += "\nEstimated Fitness for length: " + Rounder.round(TextFitness.getEstimatedFitness(text, settings.language), 4);
    		
    		if(!text.isEmpty()) {

    			outputText += "\n IC: " + StatCalculator.calculateIC(text) * 1000.0D;
    			outputText += "\n MIC: " + StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D;
    		    outputText += "\n MKA: " + StatCalculator.calculateMaxKappaIC(text, 1, 15);
    		    outputText += "\n DIC: " + StatCalculator.calculateDiagrahpicIC(text) * 10000.0D;
    		    outputText += "\n EDI: " + StatCalculator.calculateEvenDiagrahpicIC(text) * 10000;
    		    outputText += "\n LR: " + StatCalculator.calculateLR(text);
    		    outputText += "\n ROD: " + StatCalculator.calculateROD(text);
    		    outputText += "\n LDI: " + StatCalculator.calculateLDI(text);
    		    outputText += "\n SDD: " + StatCalculator.calculateSDD(text);

    		    outputText += "\n A_LDI: " + StatCalculator.calculateALDI(text);
    		    outputText += "\n B_LDI: " + StatCalculator.calculateBLDI(text);
    		    outputText += "\n P_LDI: " + StatCalculator.calculatePLDI(text);
    		    outputText += "\n S_LDI: " + StatCalculator.calculateSLDI(text);
    		    outputText += "\n V_LDI: " + StatCalculator.calculateVLDI(text);
    		    
    		    outputText += "\n NOMOR: " + StatCalculator.calculateNormalOrder(text, settings.getLanguage());
    		    outputText += "\n RDI: " + StatCalculator.calculateRDI(text);
    		    outputText += "\n PTX: " + StatCalculator.calculatePTX(text);
    		    outputText += "\n NIC: " +  StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
    		    outputText += "\n PHIC: " + StatCalculator.calculatePHIC(text);
    		    outputText += "\n BDI: " +  StatCalculator.calculateMaxBifidDiagraphicIC(text, 3, 15);
    		    outputText += "\n CDD: " +  StatCalculator.calculateCDD(text);
    		    outputText += "\n SSTD: " +  StatCalculator.calculateSSTD(text);
    		    outputText += "\n MPIC: " + StatCalculator.calculateMPIC(text);
    		    outputText += "\n SERP: " + StatCalculator.calculateSeriatedPlayfair(text);
    		    
    		    
    		    outputText += "\n DIV_2: " + StatCalculator.isLengthDivisible2(text);
    		    outputText += "\n DIV_3: " + StatCalculator.isLengthDivisible3(text);
    		    outputText += "\n DIV_5: " + StatCalculator.isLengthDivisible5(text);
    		    outputText += "\n DIV_25: " + StatCalculator.isLengthDivisible25(text);
    		    outputText += "\n DIV_4_15: " + StatCalculator.isLengthDivisible4_15(text);
    		    outputText += "\n DIV_4_30: " + StatCalculator.isLengthDivisible4_30(text);
    		    List<Integer> factors =  MathHelper.getFactors(length);
    		    Collections.sort(factors);
    		    outputText += "\n DIV_N: " + factors;
    		    outputText += "\n PSQ: " + StatCalculator.isLengthPerfectSquare(text);
    		    outputText += "\n HAS_LETTERS: " + StatCalculator.containsLetter(text);
    		    outputText += "\n HAS_DIGITS: " + StatCalculator.containsDigit(text);
    		    outputText += "\n HAS_J: " + StatCalculator.containsJ(text);
    		    outputText += "\n HAS_#: " + StatCalculator.containsHash(text);
    		    outputText += "\n HAS_0: " + StatCalculator.calculateHAS0(text);
    		    outputText += "\n DBL: " + StatCalculator.calculateDBL(text);
    		    int lastPos = this.output.getCaretPosition();
    		    this.output.setText(outputText);
    		    this.output.setCaretPosition(lastPos);
    		}
    		else {
    			outputText += "\n IC: 0.0";
    			outputText += "\n MIC: 0.0";
    			outputText += "\n MKA: 0.0";
    			outputText += "\n DIC: 0.0";
    			outputText += "\n EDI: 0.0";
    			outputText += "\n LR: 0.0";
    			outputText += "\n ROD: 0.0";
    			outputText += "\n LDI: 0.0";
    		    
    			outputText += "\n SDD: 0.0";
    		    
    			outputText += "\n A_LDI: 0.0";
    			outputText += "\n B_LDI: 0.0";
    			outputText += "\n P_LDI: 0.0";
    			outputText += "\n S_LDI: 0.0";
    			outputText += "\n V_LDI: 0.0";
    		    
    			outputText += "\n NOMOR: 0.0";
    			outputText += "\n RDI: 0.0";
    			outputText += "\n PTX: 0.0";
    			outputText += "\n NIC: 0.0";
    			outputText += "\n PHIC: 0.0";
    			outputText += "\n BDI: 0.0";
    			outputText += "\n CDD: 0.0";
    		    outputText += "\n SSTD: 0.0";
    		    outputText += "\n MPIC: 0.0";
    		    outputText += "\n SERP: 0.0";
    		    
    		    
    		    outputText += "\n DIV_2: true";
    		    outputText += "\n DIV_3: true";
    		    outputText += "\n DIV_5: true";
    		    outputText += "\n DIV_25: true";
    		    outputText += "\n DIV_4_15: true";
    		    outputText += "\n DIV_4_30: true";
    		    outputText += "\n DIV_N: []";
    		    outputText += "\n PSQ: true";
    		    outputText += "\n HAS_LETTERS: false";
    		    outputText += "\n HAS_DIGITS: false";
    		    outputText += "\n HAS_J: false";
    		    outputText += "\n HAS_#: false";
    		    outputText += "\n HAS_0: false";
    		    outputText += "\n DBL: false";
    		}
    		int lastPos = this.output.getCaretPosition();
 		    this.output.setText(outputText);
 		    this.output.setCaretPosition(lastPos);
    	}
    }
    
    private class LanguageChangeAction implements ActionListener {
    	
    	public ILanguage language;
    	
    	public LanguageChangeAction(ILanguage language) {
    		this.language = language;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			settings.language = language;
			menuItemCurrentLanguage.setText("Current: " + language.getName());
		}
    }
    
    private class KeywordCreationAction implements ActionListener {
    	
    	public int id;
    	
    	public KeywordCreationAction(int id) {
    		this.id = id;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			settings.keywordCreation = id;
		}
    }
    
    private class SimulatedAnnealingAction implements KeyListener {
    	
    	public JTextComponent textComponent;
    	public int id;
    	
    	public SimulatedAnnealingAction(JTextComponent textComponent, int id) {
    		this.textComponent = textComponent;
    		this.id = id;
    	}
    	
		@Override
		public void keyPressed(KeyEvent arg0) {
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			settings.simulatedAnnealing.set(this.id, Double.valueOf(this.textComponent.getText()));
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
    }
    
    public class PresetAction implements ActionListener {
    	
    	public JTextField tempSetting;
    	public JTextField tempStepSetting;
    	public JTextField countSetting;
    	public double tempStart;
    	public double tempStep;
    	public int count;
    	
    	public PresetAction(JTextField tempSetting, JTextField tempStepSetting, JTextField countSetting, double tempStart, double tempStep, int count) {
    		this.tempSetting = tempSetting;
    		this.tempStepSetting = tempStepSetting;
    		this.countSetting = countSetting;
    		this.tempStart = tempStart;
    		this.tempStep = tempStep;
    		this.count = count;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			settings.simulatedAnnealing.set(0, this.tempStart);
			settings.simulatedAnnealing.set(1, this.tempStep);
			settings.simulatedAnnealing.set(2, (double)this.count);
			tempSetting.setText(ValueFormat.getNumber(this.tempStart));

			tempStepSetting.setText(ValueFormat.getNumber(this.tempStep));

			countSetting.setText(ValueFormat.getNumber(this.count));	
		}
    }
    
    public String getInputText() {
    	return this.inputTextArea.getText();
    }
    
    public String getInputTextOnlyAlpha() {
    	return this.inputTextArea.getText().replaceAll("[^a-zA-Z]+", "");
    }
    
    public String getInputTextOnlyDigits() {
    	return this.inputTextArea.getText().replaceAll("[^0-9]+", "");
    }
    
    public IDecrypt getDecryptManager() {
    	return DecryptionManager.ciphers.get(cipherSelect.getSelectedIndex());
    }
     
    private JComboBox<String> cipherSelect;
    private JComboBox<DecryptionMethod> decryptionType;
    private JPanel inputPanel;
    private JScrollPane inputTextScroll;
    private JTextArea inputTextArea;
    private JTextArea statTextArea;
    private JScrollPane outputTextScroll;
    private JTextArea outputTextArea;
    private JProgressBar progressBar;
    private JToolBar toolBar;
    private JButton toolBarSettings;
    private JButton toolBarStart;
    private JButton toolBarStop;
    private JMenuBar menuBar;
    private JMenu menuItemFile;
	private JMenuItem menuItemFullScreen;
	private JMenuItem menuItemExit;
    private JMenu menuItemEdit;
    private JMenuItem menuItemPaste;
    private JMenuItem menuItemCopySolution;
    private JMenuItem menuItemShowTopSolutions;
    private JMenuItem menuItemBinary;
    private JMenu menuItemTools; 
    private JMenuItem menuItemLetterFrequency;
    private JMenuItem menuItemNGram;
    private JMenu menuItemIoC;
    private JMenuItem menuItemIoCADFGX;
    private JMenuItem menuItemIoCNormal;
    private JMenuItem menuItemIoCBifid;
    private JMenuItem menuItemIoCNicodemus;
    private JMenuItem menuItemIoCNihilist;
    private JMenuItem menuItemIoCPorta;
    private JMenuItem menuItemIoCVigenere;
    private JMenuItem menuItemIdentify;
    private JMenuItem menuItemWordSplit;
    private JMenuItem menuItemInfo;
    private JMenu menuItemSettings;
    private JMenu menuItemLanguage;
    private JMenuItem menuItemCurrentLanguage;
    private JMenu menuItemKeyword;
    private JMenuItem menuItemKeywordNormal;
    private JMenuItem menuItemKeywordHalf;
    private JMenuItem menuItemKeywordReverse;
    public JMenu menuItemSimulatedAnnealing;
    public JMenu menuItemSAPreset;
}
