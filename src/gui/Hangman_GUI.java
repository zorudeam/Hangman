package gui;

import game.Actor;
import game.Hangman;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import language.Difficulty;
import language.Word;
import utilities.functions.StringUtilities;

/**
 * The {@code Hangman_GUI} class provides for a user interface for the 
 * {@code hangman} package.
 * 
 * @author Oliver Abdulrahim
 */
public class Hangman_GUI extends JFrame {
    
    /**
     * The cerealVersionUID for this class.
     */
    private static final long serialVersionUID = -4227892083846427803L;

    private JButton aButton;
    private JComboBox<String> actorComboBox;
    private JLabel actorLabel;
    private JButton bButton;
    private JButton cButton;
    private JLabel currentWordLabel;
    private JPanel currentWordPanel;
    private JButton dButton;
    private JList<Word> dictionaryList;
    private ButtonGroup difficultyButtonGroup;
    private JLabel difficultyLabel;
    private JButton eButton;
    private JRadioButton easyRadioButton;
    private JPanel editPanel;
    private JButton fButton;
    private JMenu fileMenu;
    private JMenuItem fileMenuItem;
    private JPopupMenu.Separator fileSeparator;
    private JButton gButton;
    private JPanel gamePanel;
    private JButton giveUpButton;
    private JTextField alreadyGuessedField;
    private JLabel guessedLabel;
    private JTextField guessesLeftField;
    private JLabel guessesLeftLabel;
    private JButton hButton;
    private JRadioButton hardRadioButton;
    private JButton hintButton;
    private JButton iButton;
    private JLabel imageLabel;
    private JButton jButton;
    private JButton kButton;
    private JPanel keyboardPanel;
    private JButton lButton;
    private JButton mButton;
    private JRadioButton mediumRadioButton;
    private JMenuBar menuBar;
    private JButton nButton;
    private JMenuItem newMenuItem;
    private JButton newWordButton;
    private JButton oButton;
    private JButton okButton;
    private JPanel okPanel;
    private JButton pButton;
    private JButton qButton;
    private JButton rButton;
    private JMenuItem resetGameMenuItem;
    private JButton sButton;
    private JFrame settingsFrame;
    private JMenu settingsMenu;
    private JMenuItem settingsMenuItem;
    private JPanel statisticsPanel;
    private JButton tButton;
    private JButton uButton;
    private JButton vButton;
    private JButton wButton;
    private JTextField winRateField;
    private JLabel winRateLabel;
    private JPanel dictionaryDisplayPanel;
    private JScrollPane dictionaryScrollPane;
    private JButton xButton;
    private JButton yButton;
    private JButton zButton;
    private final Hangman game;
    private int gamesPlayed;
    private int gamesWon;
    
    /**
     * Creates new, default {@code Hangman_GUI} form.
     */
    public Hangman_GUI() {
        game = new Hangman();
        gamesPlayed = 0;
        gamesWon = 0;
        initComponents();
        addTypingListeners();
        addButtonListeners();
        setUpDictionaryList();
    }
    
    /**
     * Adds action listeners for keyboard input to each component contained 
     * within this object.
     */
    private void addTypingListeners() {
        applyToAll((Component c) -> {
            c.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent evt) {
                    parseGuess(evt);
                }
            });
        });
    }
    
    /**
     * Adds action listeners for click input to each button on the soft keyboard
     * of the GUI.
     */
    private void addButtonListeners() {
        // keyboardPanel only has JButtons - okay to cast
        applyTo(keyboardPanel, (Component c) -> {
            ((AbstractButton) c).addActionListener(this :: parseGuess);
        });
    }
    
    /**
     * Recursively returns all components within a given container, including
     * any children that are also {@code Container}s.
     * 
     * @param container The component whose components to retrieve.
     * @return All components within a given container.
     */
    public static List<Component> getAllComponents(Container container) {
        Component[] components = container.getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component c : components) {
            compList.add(c);
            if (c instanceof Container) {
                compList.addAll(getAllComponents((Container) c));
            }
        }
        return compList;
    }
    
    /**
     * Applies a given {@code Consumer} to every {@code Component} contained in
     * this object.
     * 
     * @param action The {@code Consumer} to apply to every {@code Component} 
     *        contained in this object.
     */
    private void applyToAll(Consumer<? super Component> action) {
        List<Component> allComponents = getAllComponents(this);
        allComponents.stream().forEach(action);
    }
    
    /**
     * Applies a given {@code Consumer} to every {@code Component} contained in
     * the specified {@code Container}.
     * 
     * @param action The {@code Consumer} to apply to every {@code Component} 
     *        contained in the specified {@code Container}.
     */
    private static void applyTo(Container container, 
            Consumer<? super Component> action)
    {
        Stream.of(container.getComponents()).forEach(action);
    }
    
    /**
     * Enables or disables all components contained within this object.
     * 
     * @param state The state to set every {@code Component} to.
     */
    public void setStateOfAll(boolean state) {
        applyToAll((Component c) -> {
            c.setEnabled(state);
        });
    }
    
    /**
     * Enables or disables all components contained within the given 
     * {@code Container}.
     * 
     * @param container The {@code Container} whose components to set the state
     *        of.
     * @param state The state to set every {@code Component} to.
     */
    public static void setStateOf(Container container, boolean state) {
        applyTo(container, (Component c) -> {
            c.setEnabled(state);
        });
    }
    
    /**
     * Sets up the list of words that display each item in the game's 
     * dictionary.
     */
    private void setUpDictionaryList() {
        dictionaryList.setModel(new DefaultListModel<Word>() {
            
            private static final long serialVersionUID = 938467039846L;
            
            @Override
            public int size() {
                return game.getWords().cacheList().size(); 
            }
            
            @Override
            public int getSize() {
                return size();
            }
            
            @Override
            public Word get(int index) {
                return getList().get(index);
            }
            
            @Override
            public Word elementAt(int index) {
                return get(index);
            }
            
            @Override
            public Word getElementAt(int i) { 
                return get(i);
            }
            
            @Override
            public Word remove(int index) {
                return getList().remove(index);
            }

            @Override
            public void removeElementAt(int index) {
                remove(index);
            }

            @Override
            public void add(int index, Word element) {
                game.getWords().cacheList().add(index, element);
            }
            
            @Override
            public void addElement(Word element) {
                add(0, element);
            }
            
            private List<Word> getList() {
                return game.getWords().cacheList();
            }
        });
    }
    
    /**
     * Called from within the constructor to initialize the form.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        settingsFrame = new JFrame();
        okPanel = new JPanel();
        okButton = new JButton();
        dictionaryDisplayPanel = new JPanel();
        dictionaryScrollPane = new JScrollPane();
        dictionaryList = new JList<>();
        editPanel = new JPanel();
        difficultyLabel = new JLabel();
        easyRadioButton = new JRadioButton();
        mediumRadioButton = new JRadioButton();
        hardRadioButton = new JRadioButton();
        actorLabel = new JLabel();
        actorComboBox = new JComboBox<>();
        difficultyButtonGroup = new ButtonGroup();
        keyboardPanel = new JPanel();
        qButton = new JButton();
        wButton = new JButton();
        eButton = new JButton();
        rButton = new JButton();
        tButton = new JButton();
        yButton = new JButton();
        uButton = new JButton();
        iButton = new JButton();
        oButton = new JButton();
        pButton = new JButton();
        aButton = new JButton();
        sButton = new JButton();
        dButton = new JButton();
        fButton = new JButton();
        gButton = new JButton();
        hButton = new JButton();
        jButton = new JButton();
        kButton = new JButton();
        lButton = new JButton();
        zButton = new JButton();
        xButton = new JButton();
        cButton = new JButton();
        vButton = new JButton();
        bButton = new JButton();
        nButton = new JButton();
        mButton = new JButton();
        gamePanel = new JPanel();
        imageLabel = new JLabel();
        currentWordPanel = new JPanel();
        currentWordLabel = new JLabel();
        statisticsPanel = new JPanel();
        guessesLeftLabel = new JLabel();
        guessesLeftField = new JTextField();
        winRateLabel = new JLabel();
        winRateField = new JTextField();
        giveUpButton = new JButton();
        alreadyGuessedField = new JTextField();
        guessedLabel = new JLabel();
        hintButton = new JButton();
        newWordButton = new JButton();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        newMenuItem = new JMenuItem();
        resetGameMenuItem = new JMenuItem();
        fileSeparator = new JPopupMenu.Separator();
        fileMenuItem = new JMenuItem();
        settingsMenu = new JMenu();
        settingsMenuItem = new JMenuItem();

        settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        settingsFrame.setTitle("Settings");
        settingsFrame.setLocationByPlatform(true);
        settingsFrame.setMaximumSize(new Dimension(374, 309));
        settingsFrame.setMinimumSize(new Dimension(374, 309));
        settingsFrame.setPreferredSize(new Dimension(374, 309));
        settingsFrame.setResizable(false);
        settingsFrame.setType(Window.Type.POPUP);

        okPanel.setBorder(BorderFactory.createTitledBorder(""));

        okButton.setText("OK");
        okButton.addActionListener((ActionEvent e) -> {
            newGame();
            settingsFrame.dispose();
        });

        GroupLayout okPanelLayout = new GroupLayout(okPanel);
        okPanel.setLayout(okPanelLayout);
        okPanelLayout.setHorizontalGroup(
            okPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(okPanelLayout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(okButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        okPanelLayout.setVerticalGroup(
            okPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(okPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(okButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dictionaryDisplayPanel.setBorder(BorderFactory.createTitledBorder("Dictionary Words"));
        dictionaryDisplayPanel.setToolTipText("");

        dictionaryList.setFont(new Font("Consolas", 0, 11));
        dictionaryList.setToolTipText(NumberFormat.getInstance().format(game.getWords().size()) + " words in this dictionary.");
        dictionaryScrollPane.setViewportView(dictionaryList);

        GroupLayout wordDisplayPanelLayout = new GroupLayout(dictionaryDisplayPanel);
        dictionaryDisplayPanel.setLayout(wordDisplayPanelLayout);
        wordDisplayPanelLayout.setHorizontalGroup(
            wordDisplayPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(wordDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dictionaryScrollPane, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
        );
        wordDisplayPanelLayout.setVerticalGroup(
            wordDisplayPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(wordDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dictionaryScrollPane)
                .addContainerGap())
        );

        editPanel.setBorder(BorderFactory.createTitledBorder("Game Options"));
        editPanel.setToolTipText("");

        difficultyLabel.setText("<html><p>Select word difficulty.</p></html>");

        difficultyButtonGroup.add(easyRadioButton);
        easyRadioButton.setText("Easy");
        easyRadioButton.addActionListener((e) -> updateGameSettings());

        difficultyButtonGroup.add(mediumRadioButton);
        mediumRadioButton.setSelected(true);
        mediumRadioButton.setText("Medium");
        mediumRadioButton.addActionListener((e) -> updateGameSettings());

        difficultyButtonGroup.add(hardRadioButton);
        hardRadioButton.setText("Hard");
        hardRadioButton.addActionListener((e) -> updateGameSettings());

        actorLabel.setText("<html><p>Select a set of images to use.</p></html>");

        actorComboBox.setModel(new DefaultComboBoxModel<>(Actor.allNames()));

        GroupLayout editPanelLayout = new GroupLayout(editPanel);
        editPanel.setLayout(editPanelLayout);
        editPanelLayout.setHorizontalGroup(
            editPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(actorComboBox, 0, 104, Short.MAX_VALUE)
                    .addComponent(hardRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(easyRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(difficultyLabel)
                    .addComponent(mediumRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(actorLabel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        editPanelLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {actorComboBox, difficultyLabel, easyRadioButton, hardRadioButton, mediumRadioButton});

        editPanelLayout.setVerticalGroup(
            editPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(difficultyLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(easyRadioButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mediumRadioButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hardRadioButton)
                .addGap(13, 13, 13)
                .addComponent(actorLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(actorComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        editPanelLayout.linkSize(SwingConstants.VERTICAL, new Component[] {easyRadioButton, hardRadioButton, mediumRadioButton});

        GroupLayout settingsFrameLayout = new GroupLayout(settingsFrame.getContentPane());
        settingsFrame.getContentPane().setLayout(settingsFrameLayout);
        settingsFrameLayout.setHorizontalGroup(
            settingsFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(settingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(okPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(settingsFrameLayout.createSequentialGroup()
                        .addComponent(dictionaryDisplayPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editPanel, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsFrameLayout.setVerticalGroup(
            settingsFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(settingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsFrameLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(dictionaryDisplayPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hangman");
        setLocationByPlatform(true);
        setResizable(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                parseGuess(evt);
            }
        });

        keyboardPanel.setBorder(BorderFactory.createTitledBorder("Keyboard"));
        keyboardPanel.setEnabled(false);
        keyboardPanel.setLayout(new GridBagLayout());

        qButton.setFont(new Font("Consolas", 0, 12));
        qButton.setText("Q");
        qButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(qButton, gridBagConstraints);

        wButton.setFont(new Font("Consolas", 0, 12));
        wButton.setText("W");
        wButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(wButton, gridBagConstraints);

        eButton.setFont(new Font("Consolas", 0, 12));
        eButton.setText("E");
        eButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(eButton, gridBagConstraints);

        rButton.setFont(new Font("Consolas", 0, 12));
        rButton.setText("R");
        rButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(rButton, gridBagConstraints);

        tButton.setFont(new Font("Consolas", 0, 12));
        tButton.setText("T");
        tButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(tButton, gridBagConstraints);

        yButton.setFont(new Font("Consolas", 0, 12));
        yButton.setText("Y");
        yButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(yButton, gridBagConstraints);

        uButton.setFont(new Font("Consolas", 0, 12));
        uButton.setText("U");
        uButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(uButton, gridBagConstraints);

        iButton.setFont(new Font("Consolas", 0, 12));
        iButton.setText("I");
        iButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(iButton, gridBagConstraints);

        oButton.setFont(new Font("Consolas", 0, 12));
        oButton.setText("O");
        oButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(oButton, gridBagConstraints);

        pButton.setFont(new Font("Consolas", 0, 12));
        pButton.setText("P");
        pButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        keyboardPanel.add(pButton, gridBagConstraints);

        aButton.setFont(new Font("Consolas", 0, 12));
        aButton.setText("A");
        aButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(aButton, gridBagConstraints);

        sButton.setFont(new Font("Consolas", 0, 12));
        sButton.setText("S");
        sButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(sButton, gridBagConstraints);

        dButton.setFont(new Font("Consolas", 0, 12));
        dButton.setText("D");
        dButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(dButton, gridBagConstraints);

        fButton.setFont(new Font("Consolas", 0, 12));
        fButton.setText("F");
        fButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(fButton, gridBagConstraints);

        gButton.setFont(new Font("Consolas", 0, 12));
        gButton.setText("G");
        gButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(gButton, gridBagConstraints);

        hButton.setFont(new Font("Consolas", 0, 12));
        hButton.setText("H");
        hButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(hButton, gridBagConstraints);

        jButton.setFont(new Font("Consolas", 0, 12));
        jButton.setText("J");
        jButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(jButton, gridBagConstraints);

        kButton.setFont(new Font("Consolas", 0, 12));
        kButton.setText("K");
        kButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(kButton, gridBagConstraints);

        lButton.setFont(new Font("Consolas", 0, 12));
        lButton.setText("L");
        lButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(lButton, gridBagConstraints);

        zButton.setFont(new Font("Consolas", 0, 12));
        zButton.setText("Z");
        zButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(zButton, gridBagConstraints);

        xButton.setFont(new Font("Consolas", 0, 12));
        xButton.setText("X");
        xButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(xButton, gridBagConstraints);

        cButton.setFont(new Font("Consolas", 0, 12));
        cButton.setText("C");
        cButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(cButton, gridBagConstraints);

        vButton.setFont(new Font("Consolas", 0, 12));
        vButton.setText("V");
        vButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(vButton, gridBagConstraints);

        bButton.setFont(new Font("Consolas", 0, 12));
        bButton.setText("B");
        bButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(bButton, gridBagConstraints);

        nButton.setFont(new Font("Consolas", 0, 12));
        nButton.setText("N");
        nButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(nButton, gridBagConstraints);

        mButton.setFont(new Font("Consolas", 0, 12));
        mButton.setText("M");
        mButton.setEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, -35, 0, 0);
        keyboardPanel.add(mButton, gridBagConstraints);

        gamePanel.setBorder(BorderFactory.createTitledBorder("Hangman"));
        gamePanel.setEnabled(false);
        gamePanel.setPreferredSize(new Dimension(248, 180));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout gamePanelLayout = new GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addContainerGap())
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        currentWordPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentWordPanel.setToolTipText("");
        currentWordPanel.setEnabled(false);

        currentWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentWordLabel.setText("<html><p>Welcome to Hangman. To begin, press "
                + "<font face = Consolas color=\"black\">File → New Game</font>,"
                + " or you can just stare at the screen.</p></html>");
        currentWordLabel.setEnabled(false);

        GroupLayout currentPanelLayout = new GroupLayout(currentWordPanel);
        currentWordPanel.setLayout(currentPanelLayout);
        currentPanelLayout.setHorizontalGroup(
            currentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(currentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentWordLabel, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        currentPanelLayout.setVerticalGroup(
            currentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(currentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentWordLabel, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
        );

        statisticsPanel.setBorder(BorderFactory.createTitledBorder("Statistics and Options"));
        statisticsPanel.setEnabled(false);

        guessesLeftLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        guessesLeftLabel.setText("Guesses Left");
        guessesLeftLabel.setEnabled(false);

        guessesLeftField.setEditable(false);
        guessesLeftField.setBackground(new Color(255, 255, 255));
        guessesLeftField.setHorizontalAlignment(JTextField.RIGHT);
        guessesLeftField.setText("0");
        guessesLeftField.setEnabled(false);

        winRateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        winRateLabel.setText("Win Rate");
        winRateLabel.setEnabled(false);

        winRateField.setEditable(false);
        winRateField.setBackground(new Color(255, 255, 255));
        winRateField.setHorizontalAlignment(JTextField.RIGHT);
        winRateField.setText("0");
        winRateField.setEnabled(false);

        giveUpButton.setText("Give Up");
        giveUpButton.setEnabled(false);
        giveUpButton.addActionListener((e) -> attemptGiveUp());

        alreadyGuessedField.setEditable(false);
        alreadyGuessedField.setBackground(new Color(255, 255, 255));
        alreadyGuessedField.setText("None.");
        alreadyGuessedField.setEnabled(false);

        guessedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        guessedLabel.setText("Guessed Letters");
        guessedLabel.setEnabled(false);

        hintButton.setText("Hint");
        hintButton.setEnabled(false);
        hintButton.addActionListener((e) -> doHint());

        newWordButton.setText("New Word");
        newWordButton.setEnabled(false);
        newWordButton.addActionListener((e) -> newGame());

        GroupLayout statisticsPanelLayout = new GroupLayout(statisticsPanel);
        statisticsPanel.setLayout(statisticsPanelLayout);
        statisticsPanelLayout.setHorizontalGroup(
            statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(statisticsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(statisticsPanelLayout.createSequentialGroup()
                        .addComponent(newWordButton, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(giveUpButton, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
                    .addGroup(statisticsPanelLayout.createSequentialGroup()
                        .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(statisticsPanelLayout.createSequentialGroup()
                                .addComponent(winRateLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(winRateField, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
                            .addGroup(statisticsPanelLayout.createSequentialGroup()
                                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(guessesLeftLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(guessedLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(alreadyGuessedField, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(guessesLeftField, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)))
                            .addComponent(hintButton, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        statisticsPanelLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {guessedLabel, guessesLeftLabel, winRateLabel});

        statisticsPanelLayout.setVerticalGroup(
            statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(statisticsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(guessedLabel)
                    .addComponent(alreadyGuessedField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(guessesLeftLabel)
                    .addComponent(guessesLeftField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(winRateLabel)
                    .addComponent(winRateField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hintButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statisticsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(newWordButton)
                    .addComponent(giveUpButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statisticsPanelLayout.linkSize(SwingConstants.VERTICAL, new Component[] {guessedLabel, guessesLeftLabel, winRateLabel});

        statisticsPanelLayout.linkSize(SwingConstants.VERTICAL, new Component[] {giveUpButton, hintButton, newWordButton});

        fileMenu.setText("File");

        newMenuItem.setText("New Game");
        newMenuItem.addActionListener((e) -> showSettingsFrame());
        fileMenu.add(newMenuItem);

        resetGameMenuItem.setText("Reset Game");
        resetGameMenuItem.addActionListener((e) -> tryResetGame());
        fileMenu.add(resetGameMenuItem);
        fileMenu.add(fileSeparator);

        fileMenuItem.setText("Exit");
        fileMenuItem.addActionListener((e) -> System.exit(0));
        fileMenu.add(fileMenuItem);

        menuBar.add(fileMenu);

        settingsMenu.setText("Options");

        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener((e) -> showSettingsFrame());
        settingsMenu.add(settingsMenuItem);

        menuBar.add(settingsMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gamePanel, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(currentWordPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(statisticsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(keyboardPanel, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(currentWordPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statisticsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(gamePanel, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keyboardPanel, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addContainerGap())
        );
        pack();
    }
    
    /**
     * Serves as a common method for parsing and handling the value of an input 
     * event. 
     * 
     * <p> If given event is a {@code KeyEvent}, but the key pressed is not 
     * alphabetic, this method emits an error beep and does nothing. Otherwise, 
     * this method attempts to make the given move.
     * 
     * <p> If the given event is not {@code KeyEvent}, this method assumes that
     * it is an {@code ActionEvent}, which may only originate from a 
     * {@code AbstractButton} instance within this class. This method attempts 
     * to make the given move based on the text contained by the button at which
     * the event originated.
     * 
     * @param evt The input event at which the guess occurred. This event is a
     *        common superclass of {@link KeyEvent} and {@link ActionEvent}.
     * @see #makeMove(char) The next method in the chain of events that occur
     *      when a guess is received from the GUI.
     */
    private void parseGuess(EventObject evt) {
        // Each button on the GUI soft-keyboard has a String with length one 
        // with its representative character. If the given input is an 
        // ActionEvent, then it is safe to assume that it originated from a
        // {@code JButton}.
        char guess;
        if (evt instanceof ActionEvent) {
            AbstractButton button = (AbstractButton) evt.getSource();
            guess = button.getText().charAt(0);
            makeMove(guess);
            button.setEnabled(false);
        }
        else { // evt is an instance of KeyEvent
            guess = ((KeyEvent) evt).getKeyChar();
            if (Character.isAlphabetic(guess)) {
                makeMove(guess);
                disableButton(guess);
            }
            else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    
    /**
     * Finds the {@code JButton} in the {@code keyboardPanel} that represents 
     * the given character parameter and disables it.
     * 
     * @param guess The character guess.
     */
    private void disableButton(char guess) {
        char sanitizedGuess = Word.sanitizeCharacter(guess);
        for (int i = 0; i < keyboardPanel.getComponentCount(); i++) {
            AbstractButton button = (AbstractButton) keyboardPanel.getComponent(i);
            char buttonText = Word.sanitizeCharacter(button.getText().charAt(0));
            if (sanitizedGuess == buttonText) {
                button.setEnabled(false);
                break; // Only one button for each character, so okay to break
            }
        }
    }
    
    /**
     * Attempts to make a move on the game board. This method updates the game 
     * board appropriately depending on the validity of the guess.
     * 
     * @param guess The character to attempt to guess.
     */
    private void makeMove(char guess) {
        boolean valid = game.makeGuess(guess);
        if (valid) {
            updateCurrentLabel();
        }
        else {
            updateImages();
        }
        updateStatistics();
        checkGameState();
    }
    
    /**
     * Resets the game and all game variables.
     */
    private void newGame() {
        // Special clase for when "New Word" is requested when the user has 
        // already guessed characters.
        if (game.hasGuessed() && !game.hasWon()) {
            lostGame(true);
        }
        updateGameSettings();
        updateCurrentLabel();
        updateImages();
        updateStatistics();
        setStateOfAll(true);
    }
    
    /**
     * Asks for user input to reset the game, including game statistics such as
     * win rate.
     */
    private void tryResetGame() {
        int reply = showConfirmPane("<html><p>Reset the all scores and the game"
                + " board back to default?</p></html>",
                                   "Reset Confirmation");
        if (reply == JOptionPane.YES_OPTION) {
            alreadyGuessedField.setText("None.");
            gamesPlayed = 0;
            gamesWon = 0;
            newGame();
        }
    }
    
     /**
     * Attempts to get user input on whether or not to "give up" or throw this 
     * current game. If the user specifies yes, the current game is considered a
     * loss and the user is shown the correct word. Otherwise, nothing happens.
     */
    private void attemptGiveUp() {
        int response = showConfirmPane("Really give up and show the word?", "Give Up?");
        if (response == JOptionPane.YES_OPTION) {
            lostGame(false);
        }
    }                      
    
    /**
     * Attempts to give the user a hint.
     */
    private void doHint() {
        if (game.giveHint()) {
            disableButton(game.lastGuess());
            updateCurrentLabel();
            updateImages();
            updateStatistics();
            checkGameState();
        }
    }
    
    /**
     * Returns the currently selected difficulty.
     * 
     * @return The currently selected difficulty.
     */
    private Difficulty getUserSelectedDifficulty() {
        Difficulty difficulty = Difficulty.HARD;
        if (easyRadioButton.isSelected()) {
            difficulty = Difficulty.EASY;
        } 
        else if (mediumRadioButton.isSelected()) {
            difficulty = Difficulty.MEDIUM;
        }
        return difficulty;
    }

    /**
     * Returns the actor currently selected by the user.
     * 
     * @return The actor currently selected by the user.
     */
    private Actor getUserSelectedActor() {
        Actor actor = Actor.values()[0];
        String selected = actorComboBox.getSelectedItem().toString();
        for (Actor a : Actor.values()) {
            if (a.getName().equals(selected)) {
                actor = a;
                break; // Each actor is enumerated only once, so okay to break.
            }
        }
        return actor;
    }
    
    /**
     * Displays the settings {@code JFrame}.
     */
    private void showSettingsFrame() {
        settingsFrame.setVisible(true);
    }
    
    /**
     * Updates the game with the currently given set of user settings.
     */
    private void updateGameSettings() {
        Difficulty difficulty = getUserSelectedDifficulty();
        Actor actor = getUserSelectedActor();
        game.resetGame(difficulty);
        game.setActor(actor);
        dictionaryList.updateUI();
    }
    
    /**
     * Updates the current label which displays the current word to accurately
     * reflect the state of the game.
     */
    private void updateCurrentLabel() {
        String formatted = StringUtilities.delimit(game.getCorrectGuesses(), ' ');
        currentWordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        currentWordLabel.setText(formatted);
    }
    
    /**
     * Updates the current set of images on the game panel.
     */
    private void updateImages() {
        int index = game.maxGuesses() - game.getGuessesLeft();
        imageLabel.setIcon(game.getActor().getImageArray()[index]);
    }
    
    /**
     * Updates the statistics display panel with information reflecting the 
     * current state of the game.
     */
    private void updateStatistics() {
        if (!game.hasGuessed()) {
            alreadyGuessedField.setText("None.");
        }
        else {
            String guessed = StringUtilities.sort(game.getPreviouslyGuessed().toUpperCase());
            guessed = StringUtilities.formattedToString(guessed);
            alreadyGuessedField.setText(guessed);
        }
        
        int remaining = game.getGuessesLeft();
        guessesLeftField.setText(remaining + "");
        
        String winRate = StringUtilities.doubleAsPercent((double) gamesWon / gamesPlayed);
        winRateField.setText(winRate);
        String gameInfo = "Games won/played : " + gamesWon + '/' + gamesPlayed + '.';
        winRateField.setToolTipText(gameInfo);
        winRateLabel.setToolTipText(gameInfo);
        
        String cheaterWord = "The current word is " + game.getCurrentWord() + '.';
        currentWordLabel.setToolTipText(cheaterWord);
        
        String hintText = hintButton.getText();
        int hintsLeft = game.getHintsLeft();
        if (hintsLeft > 0) {
            hintText = "Hint " + '(' + hintsLeft + ')';
            if ((game.correctGuessesToWin() == 1 || game.getGuessesLeft() == 1)) {
                hintText = "No hints on the last move!";
                hintButton.setEnabled(false);
            }
        }
        else if (hintButton.isEnabled()) {
            hintText = "Out of hints!";
            hintButton.setEnabled(false);
        }
        hintButton.setText(hintText);
    }
    
    /**
     * Checks if the user has won or lost the game.
     */
    private void checkGameState() {
        if (game.hasWon()) {
            wonGame(false);
        } 
        else if (!game.canGuess()) {
            lostGame(false);
        }
    }

    /**
     * Handles the winner state of the game, making the necessary increments to
     * the games won and played.
     * 
     * @param quietMode Flag for displaying a message pane.
     */
    private void wonGame(boolean quietMode) {
        gamesWon++;
        gamesPlayed++;
        if (!quietMode) {
            gameEnded("Nice guessing! \"" + StringUtilities.asSentence(game.getCurrentWord())
                    + "\" was the correct word!", "Winner!");
        }
    }
    
    /**
     * Handles the loser state of the game, making the necessary increments to
     * the games played.
     * 
     * @param quietMode Flag for displaying a message pane.
     */
    private void lostGame(boolean quietMode) {
        imageLabel.setIcon(game.getActor().getImageArray()[game.maxGuesses()]);
        gamesPlayed++;
        if (!quietMode) {
        gameEnded("Sorry! \"" + StringUtilities.asSentence(game.getCurrentWord())
                + "\" was the correct word!", "Loser!");
        }
    }
    
    /**
     * Ensures the GUI is kept properly updated at the end of a game.
     * 
     * @param message The message to display a message pane with.
     * @param title The title of the message pane to display.
     */
    private void gameEnded(String message, String title) {
        String actual = StringUtilities.delimit(game.getCurrentWord(), ' ');
        updateStatistics();
        currentWordLabel.setText(actual);
        setStateOf(keyboardPanel, false);
        giveUpButton.setEnabled(false);
        hintButton.setEnabled(false);
        hintButton.setText("Hint");
        showMessagePane(message, title);
    }
    
    /**
     * Displays a {@code JOptionPane} confirmation dialog using the given 
     * arguments.
     * 
     * @param message The message to display on the pane.
     * @param title The title of the pane.
     * @return The outcome of the user input.
     */
    private static int showConfirmPane(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title, 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Displays a {@code JOptionPane} information window using the given 
     * arguments.
     * 
     * @param message The message to display on the pane.
     * @param title The title of the pane.
     */
    private static void showMessagePane(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Displays a {@code JOptionPane} information window using the given 
     * arguments.
     * 
     * @param message The message to display on the pane.
     * @param title The title of the pane.
     */
    private static void showErrorPane(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, 
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Prompts the user if they would like to start a new game.
     * 
     * @param message The message to display on the pane.
     * @param title The title of the pane.
     */
    protected void newGameDialog(String message, String title) {
        int response = showConfirmPane(message, title);
        if (response == JOptionPane.YES_OPTION) {
            showSettingsFrame();
        }
    }
    
    /**
     * The main method for this package. Creates and displays a 
     * {@code Hangman_GUI} form.
     * 
     * @param args The command-line arguments.
     */
    public static void main(String args[]) {
        // Sets the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        } catch (ClassNotFoundException | InstantiationException 
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Hangman_GUI.class.getName())
                    .log(Level.SEVERE, 
                            "Error with look and feel settings. "
                          + "Check if look and feels are installed correctly",
                            ex);
        }
        SwingUtilities.invokeLater(() -> {
            Hangman_GUI gui = new Hangman_GUI();
            gui.setVisible(true);
            gui.newGameDialog("Would you like to start a new game?",
                    "New Game");
        });
    }

}
