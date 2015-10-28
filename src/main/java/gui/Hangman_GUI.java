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
import utilities.StringUtilities;

/**
 * The {@code Hangman_GUI} class provides for a user interface for the
 * {@code hangman} package.
 *
 * @author Oliver Abdulrahim
 */
public final class Hangman_GUI
    extends javax.swing.JFrame
{

    /**
     * The cerealVersionUID for this class.
     */
    private static final long serialVersionUID = -4227892083846427803L;

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
     * Enables or disables all components contained within the given
     * {@code Container}.
     *
     * @param container The {@code Container} whose components to set the state
     *        of.
     * @param state The state to set every {@code Component} to.
     */
    public static void setStateOf(Container container, boolean state) {
        applyTo(container, component -> component.setEnabled(state));
    }
    
    /**
     * Displays a {@code JOptionPane} confirmation dialog using the given
     * arguments.
     *
     * @param message The message to display on the gameOperationsPanel.
     * @param title The title of the gameOperationsPanel.
     * @return The outcome of the user input.
     */
    private static int showConfirmPane(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title,
                JOptionPane.OK_CANCEL_OPTION);
    }

    /**
     * Displays a {@code JOptionPane} information window using the given
     * arguments.
     *
     * @param message The message to display on the gameOperationsPanel.
     * @param title The title of the gameOperationsPanel.
     */
    private static void showMessagePane(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a {@code JOptionPane} information window using the given
     * arguments.
     *
     * @param message The message to display on the gameOperationsPanel.
     * @param title The title of the gameOperationsPanel.
     */
    private static void showErrorPane(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Default behavior for invalid input is to emit a system beep.
     */
    private static void invalidInputReceived() {
        Toolkit.getDefaultToolkit().beep();
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
            
        }
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Hangman_GUI.class.getName())
                    .log(Level.SEVERE,
                            "Error with look and feel settings. "
                                    + "Check if they are installed correctly",
                            ex);
        }
        SwingUtilities.invokeLater(() -> {
            Hangman_GUI gui = new Hangman_GUI();
            gui.setVisible(true);
            gui.newGameDialog("Would you like to start a new game?", "New " +
                    "Game");
        });
    }

// Game variables
    
    /**
     * Stores the game manager for this user interface.
     */
    private Hangman game;
    
    /**
     * Stores the amount of games that this user interface has played. May be
     * reset at any time using {@link #resetGameMenuItem}.
     */
    private int gamesPlayed;
    
    /**
     * Stores the amount of games that this user interface has won. May be
     * reset at any time using {@link #resetGameMenuItem}.
     */
    private int gamesWon;
    
    // The indented list of attributes below represents the hierarchy of the 
    // Component objects contained within this class. Components with higher
    // indentation are layered over those with less indentation. Additionally,
    // items are presented in the general top-to-bottom, left-to-right order in 
    // which they appear on the actual GUI.
    
//  private final JFrame this;
        private JMenuBar menuBar;
            private JMenu fileMenu;
                private JMenuItem newGameMenuItem;
                private JMenuItem resetGameMenuItem;
                private JPopupMenu.Separator fileSeparator;
                private JMenuItem exitMenuItem;
            private JMenu settingsMenu;
                private JMenuItem gameSettingsMenuItem;
        private JPanel imagePanel;
            private JLabel imageLabel;
        private JPanel currentWordPanel;
            private JLabel currentWordLabel;
        private JPanel gameOperationsPanel;
            private JLabel guessedLabel;
            private JTextField guessedField;
            private JLabel guessesLeftLabel;
            private JTextField guessesLeftField;
            private JLabel winRateLabel;
            private JTextField winRateField;
            private JButton hintButton;
            private JButton newWordButton;
            private JButton giveUpButton;
        private JPanel keyboardPanel;
            private static final String KEYBOARD = "QWERTYUIOPASDFGHJKLZXCVBNM";
            // Has 26 JButton objects for each character [A-Z] added in initComponents()

    private JFrame settingsFrame;
        private JPanel dictionaryDisplayPanel;
            private JScrollPane dictionaryScrollPane;
                private JList<Word> dictionaryList;
        private JPanel gameOptionsPanel;
            private JLabel difficultyLabel;
            private ButtonGroup difficultyButtonGroup;
                private JRadioButton easyRadioButton;
                private JRadioButton mediumRadioButton;
                private JRadioButton hardRadioButton;
            private JLabel actorLabel;
            private JComboBox<String> actorComboBox;
        private JPanel okPanel;
            private JButton okButton;

    /**
     * Creates new, default {@code Hangman_GUI} form.
     */
    public Hangman_GUI() {
        resetGame();
        initComponents();
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
     * Enables or disables all components contained within this object.
     *
     * @param state The state to set every {@code Component} to.
     */
    public void setStateOfAll(boolean state) {
        // *GASP* IT'S NOT STATELESS
        applyToAll((component) -> component.setEnabled(state));
    }

    /**
     * Adds action listeners for keyboard input to each component contained
     * within this object.
     */
    private void addTypingListeners() {
        KeyAdapter listener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                parseGuess(evt);
            }
        };
        applyToAll(component -> component.addKeyListener(listener));
        // Add the listener to this object here in this method rather than in
        // applyToAll(Consumer) to prevent a bug. The applyToAll(Consumer) 
        // method uses the getAllComponents(Container) method, which returns a
        // List containing all Component objects in the given container. Adding 
        // the given Container to the returned List may be problematic: certain 
        // repaint operations, such as Component#setEnabled() may trigger an 
        // undesired minimize operation on Window objects. Adding the listener
        // here is a workaround to this.
        this.addKeyListener(listener);
    }

    /**
     * Adds action listeners for click input to each button on the soft keyboard
     * of the GUI.
     */
    private void addButtonListeners() {
        // keyboardPanel only has JButtons - okay to perform weak cast to 
        // AbstractButton
        applyTo(keyboardPanel, component-> {
            ((AbstractButton) component).addActionListener(this :: parseGuess);
        });
    }

    /**
     * Called from within the constructor to initialize the form.
     * 
     * HERE THERE BE DRAGONS. TURN BACK, VALIANT PROGRAMMER, SAVE YOURSELF FROM
     * THE BOILERPLATE OF GROUPLAYOUT! (Should switch to GridBagLayout)
     */
    private void initComponents() {
        GroupLayout layout;
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        newGameMenuItem = new JMenuItem();
        resetGameMenuItem = new JMenuItem();
        fileSeparator = new JPopupMenu.Separator();
        exitMenuItem = new JMenuItem();
        settingsMenu = new JMenu();
        gameSettingsMenuItem = new JMenuItem();

        imagePanel = new JPanel();
        imageLabel = new JLabel();

        currentWordPanel = new JPanel();
        currentWordLabel = new JLabel();

        gameOperationsPanel = new JPanel();
        guessedLabel = new JLabel();
        guessedField = new JTextField();
        guessesLeftLabel = new JLabel();
        guessesLeftField = new JTextField();
        winRateLabel = new JLabel();
        winRateField = new JTextField();
        hintButton = new JButton();
        newWordButton = new JButton();
        giveUpButton = new JButton();

        keyboardPanel = new JPanel();

        settingsFrame = new JFrame();

        dictionaryDisplayPanel = new JPanel();
        dictionaryScrollPane = new JScrollPane();
        dictionaryList = new JList<>();

        gameOptionsPanel = new JPanel();
        difficultyLabel = new JLabel();
        difficultyButtonGroup = new ButtonGroup();
        easyRadioButton = new JRadioButton();
        mediumRadioButton = new JRadioButton();
        hardRadioButton = new JRadioButton();
        actorLabel = new JLabel();
        actorComboBox = new JComboBox<>();
        okPanel = new JPanel();
        okButton = new JButton();

    // menuBar setup
        fileMenu.setText("File");

        newGameMenuItem.setText("New Game");
        newGameMenuItem.addActionListener(e -> showSettingsFrame());
        fileMenu.add(newGameMenuItem);

        resetGameMenuItem.setText("Reset Game");
        resetGameMenuItem.addActionListener(e -> tryResetGame());
        fileMenu.add(resetGameMenuItem);
        fileMenu.add(fileSeparator);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(e -> quit());
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        settingsMenu.setText("Options");

        gameSettingsMenuItem.setText("Settings");
        gameSettingsMenuItem.addActionListener(e -> showSettingsFrame());
        settingsMenu.add(gameSettingsMenuItem);

        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);

    // imagePanel setup
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setIcon(game.images()[0]);
        
        imagePanel.setBorder(BorderFactory.createTitledBorder("Hangman"));
        imagePanel.setPreferredSize(new Dimension(248, 180));
        imagePanel.add(imageLabel);
        
    // currentWordPanel setup
        currentWordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentWordLabel.setText("<html><p>Welcome to Hangman. To begin, press "
                + "<font face = Consolas color=\"black\">File → New Game</font>,"
                + " or you can just stare at the screen.</p></html>");
        // Set all three sizes to prevent resizing
        final Dimension dim = new Dimension(215, 55);
        currentWordLabel.setMinimumSize(dim);
        currentWordLabel.setMaximumSize(dim);
        currentWordLabel.setPreferredSize(dim);
        
        currentWordPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentWordPanel.add(currentWordLabel);
        
    // gameOperationsPanel setup
        final Color fieldBackground = Color.WHITE;
        gameOperationsPanel.setBorder(BorderFactory.createTitledBorder
                        ("Statistics and Options"));
        
        guessesLeftLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        guessesLeftLabel.setText("Guesses Left");

        guessesLeftField.setEditable(false);
        guessesLeftField.setBackground(fieldBackground);
        guessesLeftField.setHorizontalAlignment(JTextField.RIGHT);
        guessesLeftField.setText("0");

        winRateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        winRateLabel.setText("Win Rate");

        winRateField.setEditable(false);
        winRateField.setBackground(fieldBackground);
        winRateField.setHorizontalAlignment(JTextField.RIGHT);
        winRateField.setText("0");

        newWordButton.setText("New Word");
        newWordButton.addActionListener(e -> newGame());
        
        giveUpButton.setText("Give Up");
        giveUpButton.addActionListener(e -> attemptGiveUp());

        guessedField.setEditable(false);
        guessedField.setBackground(fieldBackground);
        guessedField.setText("None.");

        guessedLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        guessedLabel.setText("Guessed Letters");

        hintButton.setText("Hint");
        hintButton.addActionListener(e -> doHint());


        /* Layout for gameOperationsPanel should look something like the 
         * following:
         *        ╭─────────────────────╮
         *        │┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈│
         *        │┈┈ ■■■■■■ ┈ ■■■■■■ ┈┈│
         *        │┈┈ ■■■■■■ ┈ ■■■■■■ ┈┈│
         *        │┈┈ ■■■■■■ ┈ ■■■■■■ ┈┈│
         *        │┈┈ ■■■■■■■■■■■■■■■ ┈┈│
         *        │┈┈ ■■■■■■ ┈ ■■■■■■ ┈┈│
         *        │┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈│
         *        ╰─────────────────────╯
         *   Character/Item     Representation
         *     - solid line     panel edges
         *     - ┈              padding
         *     - ■              component
         */
        Component[][] members = {
            {guessedLabel, guessedField}, 
            {guessesLeftLabel, guessesLeftField},
            {winRateLabel, winRateField},
            {hintButton},
            {newWordButton, giveUpButton}
        };
        gameOperationsPanel.setLayout(new GridBagLayout());
        
        // Desired horizontal/vertical distances for each component in the layout
        final int horizontalFill = 57;
        final int fromEdge = 11;
        final int between = 4;
        final int memberRows = members.length;
        GridBagConstraints c;
        Insets i;
        for (int y = 0; y < memberRows; y++) {
            for (int x = 0; x < members[y].length; x++) {
                c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.anchor = GridBagConstraints.CENTER;
                c.gridx = x;
                c.gridy = y;
                i = new Insets(0, fromEdge, between, between);
                // Special case for hintButton, needs to span two columns
                if (members[y][x].equals(hintButton)) {
                    i = new Insets(between, fromEdge, between, fromEdge);
                    c.gridwidth = 2;
                }
                else {
                    if (y == 0) {
                        i.top = fromEdge;
                    }
                    else if (y == memberRows - 1) {
                        i.bottom = fromEdge;
                    }
                    if (x == members[y].length - 1) {
                        i.left = 0;
                        i.right = fromEdge;
                        c.ipadx = horizontalFill;
                    }
                }
                c.insets = i;
                gameOperationsPanel.add(members[y][x], c);
            }
        }
        
    // keyboardPanel setup

        // Let me tell you about our savior, GridBagLayout
        keyboardPanel.setBorder(BorderFactory.createTitledBorder("Keyboard"));
        keyboardPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints;

        // Rows and columns should be zero-offset
        final int rows = 2, columns = 9;
        final int padding = 35;
        final Insets leadingInset = new Insets(0, -padding, 0, 0);

        // The rows of the keyboard have different amount of "keys" and are
        // center-padded. Add this padding to every row after the first. Also 
        // add y to x for every row additional offset.
        int keyboardIndex = 0;
        for (int y = 0; y <= rows; y++) {
            for (int x = y; x <= columns && keyboardIndex < KEYBOARD.length(); x++) {
                constraints = new GridBagConstraints();
                constraints.gridx = x;
                constraints.gridy = y;
                if (y > 0) {
                    constraints.insets = leadingInset;
                }
                String text = KEYBOARD.substring(keyboardIndex, ++keyboardIndex);
                keyboardPanel.add(buildButton(text), constraints);
            }
        }
        
        dictionaryList.setModel(new DefaultListModel<Word>() {
            private static final long serialVersionUID = 938467039846L;
            private List<Word> getList() {
                return game.getWords().cacheList();
            }
            @Override
            public int getSize() {
                return getList().size();
            }
            @Override
            public Word getElementAt(int i) {
                return getList().get(i);
            }
        });
        
        settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        settingsFrame.setTitle("Settings");
        settingsFrame.setLocationByPlatform(true);
        settingsFrame.setMinimumSize(new Dimension(374, 309));
        settingsFrame.setResizable(false);
        settingsFrame.setType(Window.Type.POPUP);

        okPanel.setBorder(BorderFactory.createTitledBorder(""));

        okButton.setText("OK");
        okButton.addActionListener(e -> {
            newGame();
            settingsFrame.dispose();
        });

        layout = new GroupLayout(okPanel);
        okPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(okButton, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(okButton)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dictionaryDisplayPanel.setBorder(BorderFactory.createTitledBorder("Dictionary Words"));

        dictionaryList.setFont(new Font("Consolas", 0, 11));
        dictionaryList.setToolTipText(NumberFormat.getInstance().format(game.getWords().size()) + " words in this dictionary.");
        dictionaryScrollPane.setViewportView(dictionaryList);

        layout = new GroupLayout(dictionaryDisplayPanel);
        dictionaryDisplayPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(dictionaryScrollPane, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(dictionaryScrollPane)
                                .addContainerGap())
        );

        gameOptionsPanel.setBorder(BorderFactory.createTitledBorder("Game Options"));

        difficultyLabel.setText("<html><p>Select word difficulty.</p></html>");

        difficultyButtonGroup.add(easyRadioButton);
        easyRadioButton.setText("Easy");
        easyRadioButton.addActionListener(e -> updateGameSettings());

        difficultyButtonGroup.add(mediumRadioButton);
        mediumRadioButton.setSelected(true);
        mediumRadioButton.setText("Medium");
        mediumRadioButton.addActionListener(e -> updateGameSettings());

        difficultyButtonGroup.add(hardRadioButton);
        hardRadioButton.setText("Hard");
        hardRadioButton.addActionListener(e -> updateGameSettings());

        actorLabel.setText("<html><p>Select a set of images to use.</p></html>");

        actorComboBox.setModel(new DefaultComboBoxModel<>(Actor.allNames()));

        layout = new GroupLayout(gameOptionsPanel);
        gameOptionsPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(actorComboBox, 0, 104, Short.MAX_VALUE)
                                        .addComponent(hardRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(easyRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(difficultyLabel)
                                        .addComponent(mediumRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(actorLabel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        layout.linkSize(SwingConstants.HORIZONTAL, actorComboBox, difficultyLabel, easyRadioButton, hardRadioButton, mediumRadioButton);

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
        
        layout.linkSize(SwingConstants.VERTICAL, easyRadioButton, hardRadioButton, mediumRadioButton);
        
        layout = new GroupLayout(settingsFrame.getContentPane());
        settingsFrame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(okPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(dictionaryDisplayPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(gameOptionsPanel, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dictionaryDisplayPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(gameOptionsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(okPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(imagePanel, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(currentWordPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(gameOperationsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                                                .addComponent(gameOperationsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(imagePanel, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(keyboardPanel, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                .addContainerGap())
        );
        setStateOfAll(false);
        setStateOf(menuBar, true);
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hangman");
        setLocationByPlatform(true);
        setResizable(false);

        addTypingListeners();
        addButtonListeners();
        
        pack();
    }

    /**
     * Builder for keyboard buttons. The buttons returned by this method are
     * disabled, use the "Consolas" font (12pt), and have the given text.
     *
     * @param text The text of the button.
     * @return A button with the given text.
     */
    private JButton buildButton(String text) {
        final Font consolas = new Font("Consolas", 0, 12);
        JButton button = new JButton(text);
        button.setFont(consolas);
        button.setEnabled(false);
        return button;
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
        else { // Assume evt is an instance of KeyEvent
            guess = ((KeyEvent) evt).getKeyChar();
            if (Character.isAlphabetic(guess)) {
                makeMove(guess);
                disableButton(guess);
            }
            else {
                invalidInputReceived();
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
            guessedField.setText("None.");
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
        String formatted = StringUtilities.delimit(game.getCorrectGuesses(),
                ' ');
        currentWordLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        currentWordLabel.setText(formatted);
    }

    /**
     * Updates the current set of images on the game gameOperationsPanell.
     */
    private void updateImages() {
        int index = game.maxGuesses() - game.getGuessesLeft();
        imageLabel.setIcon(game.images()[index]);
    }

    /**
     * Updates the statistics display gameOperationsPanell with information reflecting the
     * current state of the game.
     */
    private void updateStatistics() {
        if (!game.hasGuessed()) {
            guessedField.setText("None.");
        }
        else {
            String guessed = game.getPreviouslyGuessed().toUpperCase();
            guessed = StringUtilities.sort(guessed);
            guessed = StringUtilities.formattedToString(guessed);
            guessedField.setText(guessed);
        }

        int remaining = game.getGuessesLeft();
        guessesLeftField.setText(remaining + "");

        // Must cast this result to a double - division by zero with integral
        // types will throw ArithmeticException
        double rate = (double) gamesWon / gamesPlayed;
        String formattedRate = StringUtilities.doubleAsPercent(rate);
        winRateField.setText(formattedRate);
        
        String gameInfo = "Games won/played : " + gamesWon + '/' + gamesPlayed + '.';
        winRateField.setToolTipText(gameInfo);
        winRateLabel.setToolTipText(gameInfo);

        String cheaterWord = "The current word is " + game.getCurrentWord() + '.';
        currentWordLabel.setToolTipText(cheaterWord);

        String hintText = hintButton.getText();
        int hintsLeft = game.getHintsLeft();
        if (hintsLeft > 0) {
            hintText = "Hint " + '(' + hintsLeft + ')';
            if (game.correctGuessesToWin() == 1 || game.getGuessesLeft() == 1) {
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
     * @param quietMode Flag for displaying a message gameOperationsPanel.
     */
    private void wonGame(boolean quietMode) {
        gamesWon++;
        gamesPlayed++;
        if (!quietMode) {
            String actual = StringUtilities.asSentence(game.getCurrentWord());
            gameEnded("Nice guessing! \"" + actual + "\" was the correct word!", "Winner!");
        }
    }

    /**
     * Handles the loser state of the game, making the necessary increments to
     * the games played.
     *
     * @param quietMode Flag for displaying a message gameOperationsPanel.
     */
    private void lostGame(boolean quietMode) {
        imageLabel.setIcon(game.images()[game.maxGuesses()]);
        gamesPlayed++;
        if (!quietMode) {
        String actual = StringUtilities.asSentence(game.getCurrentWord());
        gameEnded("Sorry! \"" + actual + "\" was the correct word!", "Loser!");
        }
    }

    /**
     * Ensures the GUI is kept properly updated at the end of a game.
     *
     * @param message The message to display a message gameOperationsPanel with.
     * @param title The title of the message gameOperationsPanel to display.
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
     * Prompts the user if they would like to start a new game.
     *
     * @param message The message to display on the gameOperationsPanel.
     * @param title The title of the gameOperationsPanel.
     */
    private void newGameDialog(String message, String title) {
        int response = showConfirmPane(message, title);
        if (response == JOptionPane.YES_OPTION) {
            showSettingsFrame();
        }
    }
    
    /**
     * Quits the the GUI, closing everything and ending the program execution.
     */
    private void quit() {
        System.exit(0);
    }

    private void resetGame() {
        game = new Hangman();
        gamesPlayed = 0;
        gamesWon = 0;
    }
    
}
