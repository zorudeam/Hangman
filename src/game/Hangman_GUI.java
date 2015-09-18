package game;

import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ColorPicker;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import language.Dictionary;
import language.Word;
import language.WordProperties;
import utilities.functions.StringUtilities;

/**
 * The {@code Hangman_GUI} class provides for a user interface framework for the
 * {@code hangman} package.
 * 
 * @author Oliver Abdulrahim
 */
public class Hangman_GUI extends JFrame {
    
    private static final long serialVersionUID = -4227892083846427803L;

    /**
     * Creates new, default {@code Hangman_GUI} form.
     */
    public Hangman_GUI() {
        game = new Hangman();
        gamesPlayed = 0;
        gamesWon = 0;
        initComponents();

        /*
         * Add the action listener for keyboard input to each relevant component 
         * in this form.
         */
        addPanelListeners(keyboardPanel);
        addPanelListeners(statisticsPanel);
        addPanelListeners(gameOptionsPanel);
        
        wordDisplayList.setModel(new javax.swing.AbstractListModel() {
            List<Word> wordList = new ArrayList<>(game.getWords().keySet());
            
            @Override
            public int getSize() {
                return wordList.size(); 
            }
            
            @Override
            public Object getElementAt(int i) { 
                return wordList.get(i); 
            }
            
        });
    }
    
    /**
     * Should only be called in the constructor. Adds action listeners for 
     * keyboard input to each relevant component from the given {@code JPanel}.
     */
    private void addPanelListeners(JPanel panel) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            panel.getComponent(i).addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent evt) {
                    formKeyReleased(evt);
                }
            });
        }
    }
    
    /**
     * Should only be called in the constructor. Adds action listeners for 
     * keyboard input to each button in the keyboard {@code JPanel}.
     */
    private void addButtonListeners() {
        for (int i = 0; i < keyboardPanel.getComponentCount(); i++) {
            keyboardPanel.getComponent(i).addKeyListener(new AWTEventMulticaster() {
                @Override
                public void keyReleased(KeyEvent evt) {
                    parseGuess(evt);
                }
            });
        }
    }
    
    
    
    /**
     * Recursively returns all components within a given container, including
     * any children who are also {@code Container}s.
     * 
     * @param comp The component whose components to retrieve.
     * @return All components within a given container.
     */
    public static List<Component> getAllComponents(Container comp) {
        Component[] components = comp.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component c : components) {
            compList.add(c);
            if (c instanceof Container) {
                compList.addAll(getAllComponents((Container) c));
            }
        }
        return compList;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fileChooser = new javax.swing.JFileChooser();
        settingsFrame = new javax.swing.JFrame();
        wordDisplayPanel = new javax.swing.JPanel();
        wordScrollPane = new javax.swing.JScrollPane();
        wordDisplayList = new javax.swing.JList();
        editPanel = new javax.swing.JPanel();
        difficultyLabel = new javax.swing.JLabel();
        easyRadioButton = new javax.swing.JRadioButton();
        mediumRadioButton = new javax.swing.JRadioButton();
        hardRadioButton = new javax.swing.JRadioButton();
        anyRadioButton = new javax.swing.JRadioButton();
        actorLabel = new javax.swing.JLabel();
        actorComboBox = new javax.swing.JComboBox();
        loadPanel = new javax.swing.JPanel();
        loadLabel = new javax.swing.JLabel();
        customRadioButton = new javax.swing.JRadioButton();
        defaultRadioButton = new javax.swing.JRadioButton();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        updatePanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        addFrame = new javax.swing.JFrame();
        addPanel = new javax.swing.JPanel();
        wordField = new javax.swing.JTextField();
        wordLabel = new javax.swing.JLabel();
        cancelAddButton = new javax.swing.JButton();
        confirmAddButton = new javax.swing.JButton();
        loadButtonGroup = new javax.swing.ButtonGroup();
        difficultyButtonGroup = new javax.swing.ButtonGroup();
        keyboardPanel = new javax.swing.JPanel();
        aButton = new javax.swing.JButton();
        bButton = new javax.swing.JButton();
        cButton = new javax.swing.JButton();
        dButton = new javax.swing.JButton();
        eButton = new javax.swing.JButton();
        fButton = new javax.swing.JButton();
        gButton = new javax.swing.JButton();
        hButton = new javax.swing.JButton();
        iButton = new javax.swing.JButton();
        jButton = new javax.swing.JButton();
        kButton = new javax.swing.JButton();
        lButton = new javax.swing.JButton();
        mButton = new javax.swing.JButton();
        nButton = new javax.swing.JButton();
        oButton = new javax.swing.JButton();
        pButton = new javax.swing.JButton();
        qButton = new javax.swing.JButton();
        rButton = new javax.swing.JButton();
        sButton = new javax.swing.JButton();
        tButton = new javax.swing.JButton();
        uButton = new javax.swing.JButton();
        vButton = new javax.swing.JButton();
        wButton = new javax.swing.JButton();
        xButton = new javax.swing.JButton();
        yButton = new javax.swing.JButton();
        zButton = new javax.swing.JButton();
        gamePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        currentPanel = new javax.swing.JPanel();
        currentLabel = new javax.swing.JLabel();
        statisticsPanel = new javax.swing.JPanel();
        guessedLabel = new javax.swing.JLabel();
        guessedField = new javax.swing.JTextField();
        remainingLabel = new javax.swing.JLabel();
        remainingField = new javax.swing.JTextField();
        gamesWonLabel = new javax.swing.JLabel();
        gamesWonField = new javax.swing.JTextField();
        gamesPlayedLabel = new javax.swing.JLabel();
        gamesPlayedField = new javax.swing.JTextField();
        gameOptionsPanel = new javax.swing.JPanel();
        giveUpButton = new javax.swing.JButton();
        newWordButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        fileSeparator = new javax.swing.JPopupMenu.Separator();
        fileMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        settingsMenuItem = new javax.swing.JMenuItem();

        fileChooser.setCurrentDirectory(new java.io.File("C:\\Users\\Ali\\Documents\\Papers\\11th Grade\\Computer Science\\Dictionary\\dist\\bin"));

        settingsFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        settingsFrame.setTitle("Settings");
        settingsFrame.setAlwaysOnTop(true);
        settingsFrame.setLocationByPlatform(true);
        settingsFrame.setResizable(false);

        wordDisplayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Dictionary Words"));
        wordDisplayPanel.setToolTipText("");

        wordDisplayList.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        wordScrollPane.setViewportView(wordDisplayList);

        javax.swing.GroupLayout wordDisplayPanelLayout = new javax.swing.GroupLayout(wordDisplayPanel);
        wordDisplayPanel.setLayout(wordDisplayPanelLayout);
        wordDisplayPanelLayout.setHorizontalGroup(
            wordDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wordDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addContainerGap())
        );
        wordDisplayPanelLayout.setVerticalGroup(
            wordDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wordDisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordScrollPane)
                .addContainerGap())
        );

        editPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Options"));
        editPanel.setToolTipText("");

        difficultyLabel.setText("<html><p>Select word difficulty.</p></html>");

        difficultyButtonGroup.add(easyRadioButton);
        easyRadioButton.setText("Easy");

        difficultyButtonGroup.add(mediumRadioButton);
        mediumRadioButton.setSelected(true);
        mediumRadioButton.setText("Medium");

        difficultyButtonGroup.add(hardRadioButton);
        hardRadioButton.setText("Hard");

        difficultyButtonGroup.add(anyRadioButton);
        anyRadioButton.setText("Any Difficulty");

        actorLabel.setText("Choose an actor.");

        actorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(Actor.allNames()));

        javax.swing.GroupLayout editPanelLayout = new javax.swing.GroupLayout(editPanel);
        editPanel.setLayout(editPanelLayout);
        editPanelLayout.setHorizontalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(actorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(editPanelLayout.createSequentialGroup()
                        .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hardRadioButton)
                            .addComponent(easyRadioButton)
                            .addComponent(difficultyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mediumRadioButton)
                            .addComponent(actorLabel)
                            .addComponent(anyRadioButton))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        editPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {easyRadioButton, hardRadioButton, mediumRadioButton});

        editPanelLayout.setVerticalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(difficultyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(easyRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mediumRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hardRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(anyRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(actorLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        editPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {easyRadioButton, hardRadioButton, mediumRadioButton});

        loadPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Word Settings"));
        loadPanel.setToolTipText("");

        loadLabel.setText("<html><p>Select which dictionary to use.</p></html>");

        loadButtonGroup.add(customRadioButton);
        customRadioButton.setText("Custom (select a file)");
        customRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRadioButtonActionPerformed(evt);
            }
        });

        loadButtonGroup.add(defaultRadioButton);
        defaultRadioButton.setSelected(true);
        defaultRadioButton.setText("Default (use existing)");
        defaultRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultRadioButtonActionPerformed(evt);
            }
        });

        addButton.setText("Add Word");
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addButtonMouseReleased(evt);
            }
        });
        addButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                addButtonKeyReleased(evt);
            }
        });

        removeButton.setText("Remove Word");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loadPanelLayout = new javax.swing.GroupLayout(loadPanel);
        loadPanel.setLayout(loadPanelLayout);
        loadPanelLayout.setHorizontalGroup(
            loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loadPanelLayout.createSequentialGroup()
                        .addGroup(loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customRadioButton)
                            .addComponent(defaultRadioButton)
                            .addComponent(loadLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        loadPanelLayout.setVerticalGroup(
            loadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(loadLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(defaultRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeButton)
                .addContainerGap())
        );

        updatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updatePanelLayout = new javax.swing.GroupLayout(updatePanel);
        updatePanel.setLayout(updatePanelLayout);
        updatePanelLayout.setHorizontalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        updatePanelLayout.setVerticalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout settingsFrameLayout = new javax.swing.GroupLayout(settingsFrame.getContentPane());
        settingsFrame.getContentPane().setLayout(settingsFrameLayout);
        settingsFrameLayout.setHorizontalGroup(
            settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(updatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(settingsFrameLayout.createSequentialGroup()
                        .addComponent(wordDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loadPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsFrameLayout.setVerticalGroup(
            settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(settingsFrameLayout.createSequentialGroup()
                        .addComponent(loadPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(wordDisplayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addFrame.setTitle("Add A Word");
        addFrame.setAlwaysOnTop(true);
        addFrame.setLocationByPlatform(true);
        addFrame.setResizable(false);

        addPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Add a Word"));

        wordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                wordFieldKeyReleased(evt);
            }
        });

        wordLabel.setText("<html><p>Enter the word you would like to add to the dictionary.</p></html>");

        cancelAddButton.setText("Cancel");
        cancelAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelAddButtonActionPerformed(evt);
            }
        });

        confirmAddButton.setText("OK");
        confirmAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAddButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addPanelLayout = new javax.swing.GroupLayout(addPanel);
        addPanel.setLayout(addPanelLayout);
        addPanelLayout.setHorizontalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(wordField)
                    .addComponent(wordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(addPanelLayout.createSequentialGroup()
                        .addComponent(confirmAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );
        addPanelLayout.setVerticalGroup(
            addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(wordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmAddButton)
                    .addComponent(cancelAddButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addFrameLayout = new javax.swing.GroupLayout(addFrame.getContentPane());
        addFrame.getContentPane().setLayout(addFrameLayout);
        addFrameLayout.setHorizontalGroup(
            addFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addFrameLayout.setVerticalGroup(
            addFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hangman");
        setLocationByPlatform(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        keyboardPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Keyboard"));
        keyboardPanel.setEnabled(false);
        keyboardPanel.setLayout(new java.awt.GridBagLayout());

        aButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        aButton.setText("A");
        aButton.setEnabled(false);
        aButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(aButton, new java.awt.GridBagConstraints());

        bButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        bButton.setText("B");
        bButton.setEnabled(false);
        bButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(bButton, new java.awt.GridBagConstraints());

        cButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        cButton.setText("C");
        cButton.setEnabled(false);
        cButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(cButton, new java.awt.GridBagConstraints());

        dButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        dButton.setText("D");
        dButton.setEnabled(false);
        dButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(dButton, new java.awt.GridBagConstraints());

        eButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        eButton.setText("E");
        eButton.setEnabled(false);
        eButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(eButton, new java.awt.GridBagConstraints());

        fButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        fButton.setText("F");
        fButton.setEnabled(false);
        fButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(fButton, new java.awt.GridBagConstraints());

        gButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        gButton.setText("G");
        gButton.setEnabled(false);
        gButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(gButton, new java.awt.GridBagConstraints());

        hButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        hButton.setText("H");
        hButton.setEnabled(false);
        hButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(hButton, new java.awt.GridBagConstraints());

        iButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        iButton.setText("I");
        iButton.setEnabled(false);
        iButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(iButton, new java.awt.GridBagConstraints());

        jButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jButton.setText("J");
        jButton.setEnabled(false);
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(jButton, new java.awt.GridBagConstraints());

        kButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        kButton.setText("K");
        kButton.setEnabled(false);
        kButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(kButton, new java.awt.GridBagConstraints());

        lButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        lButton.setText("L");
        lButton.setEnabled(false);
        lButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(lButton, new java.awt.GridBagConstraints());

        mButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        mButton.setText("M");
        mButton.setEnabled(false);
        mButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mButtonActionPerformed(evt);
            }
        });
        keyboardPanel.add(mButton, new java.awt.GridBagConstraints());

        nButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        nButton.setText("N");
        nButton.setEnabled(false);
        nButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(nButton, gridBagConstraints);

        oButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        oButton.setText("O");
        oButton.setEnabled(false);
        oButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(oButton, gridBagConstraints);

        pButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        pButton.setText("P");
        pButton.setEnabled(false);
        pButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(pButton, gridBagConstraints);

        qButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        qButton.setText("Q");
        qButton.setEnabled(false);
        qButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(qButton, gridBagConstraints);

        rButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        rButton.setText("R");
        rButton.setEnabled(false);
        rButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(rButton, gridBagConstraints);

        sButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        sButton.setText("S");
        sButton.setEnabled(false);
        sButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(sButton, gridBagConstraints);

        tButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        tButton.setText("T");
        tButton.setEnabled(false);
        tButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(tButton, gridBagConstraints);

        uButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        uButton.setText("U");
        uButton.setEnabled(false);
        uButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(uButton, gridBagConstraints);

        vButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        vButton.setText("V");
        vButton.setEnabled(false);
        vButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(vButton, gridBagConstraints);

        wButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        wButton.setText("W");
        wButton.setEnabled(false);
        wButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(wButton, gridBagConstraints);

        xButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        xButton.setText("X");
        xButton.setEnabled(false);
        xButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(xButton, gridBagConstraints);

        yButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        yButton.setText("Y");
        yButton.setEnabled(false);
        yButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(yButton, gridBagConstraints);

        zButton.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        zButton.setText("Z");
        zButton.setEnabled(false);
        zButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 1;
        keyboardPanel.add(zButton, gridBagConstraints);

        gamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Hangman"));
        gamePanel.setEnabled(false);

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        currentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Current Word"));
        currentPanel.setToolTipText("");
        currentPanel.setEnabled(false);

        currentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentLabel.setText("<html><p>Welcome to Hangman. To begin, press <font face = Consolas color=\"black\">File → New Game</font>, or you can just stare at these panels.</p></html>");
        currentLabel.setEnabled(false);

        javax.swing.GroupLayout currentPanelLayout = new javax.swing.GroupLayout(currentPanel);
        currentPanel.setLayout(currentPanelLayout);
        currentPanelLayout.setHorizontalGroup(
            currentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(currentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );
        currentPanelLayout.setVerticalGroup(
            currentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(currentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
        );

        statisticsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Statistics"));
        statisticsPanel.setEnabled(false);

        guessedLabel.setText("Guessed Letters");
        guessedLabel.setEnabled(false);

        guessedField.setEditable(false);
        guessedField.setBackground(new java.awt.Color(255, 255, 255));
        guessedField.setText("None.");
        guessedField.setEnabled(false);

        remainingLabel.setText("Guesses Remaining");
        remainingLabel.setEnabled(false);

        remainingField.setEditable(false);
        remainingField.setBackground(new java.awt.Color(255, 255, 255));
        remainingField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        remainingField.setText("0");
        remainingField.setEnabled(false);

        gamesWonLabel.setText("Games Won");
        gamesWonLabel.setEnabled(false);

        gamesWonField.setEditable(false);
        gamesWonField.setBackground(new java.awt.Color(255, 255, 255));
        gamesWonField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gamesWonField.setText("0");
        gamesWonField.setEnabled(false);

        gamesPlayedLabel.setText("Games Played");
        gamesPlayedLabel.setEnabled(false);

        gamesPlayedField.setEditable(false);
        gamesPlayedField.setBackground(new java.awt.Color(255, 255, 255));
        gamesPlayedField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gamesPlayedField.setText("0");
        gamesPlayedField.setEnabled(false);

        javax.swing.GroupLayout statisticsPanelLayout = new javax.swing.GroupLayout(statisticsPanel);
        statisticsPanel.setLayout(statisticsPanelLayout);
        statisticsPanelLayout.setHorizontalGroup(
            statisticsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statisticsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statisticsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(guessedField, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guessedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(remainingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(remainingField)
                    .addComponent(gamesPlayedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gamesWonLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gamesWonField)
                    .addComponent(gamesPlayedField))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        statisticsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {gamesPlayedField, gamesPlayedLabel, gamesWonField, gamesWonLabel, guessedField, guessedLabel, remainingField, remainingLabel});

        statisticsPanelLayout.setVerticalGroup(
            statisticsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statisticsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(guessedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guessedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(remainingLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(remainingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gamesWonLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gamesWonField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(gamesPlayedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gamesPlayedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statisticsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {gamesPlayedField, gamesPlayedLabel, gamesWonField, gamesWonLabel, guessedField, guessedLabel, remainingField, remainingLabel});

        gameOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Options"));
        gameOptionsPanel.setEnabled(false);

        giveUpButton.setText("Give Up");
        giveUpButton.setEnabled(false);
        giveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                giveUpButtonActionPerformed(evt);
            }
        });

        newWordButton.setText("New Word");
        newWordButton.setEnabled(false);
        newWordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWordButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.setEnabled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gameOptionsPanelLayout = new javax.swing.GroupLayout(gameOptionsPanel);
        gameOptionsPanel.setLayout(gameOptionsPanelLayout);
        gameOptionsPanelLayout.setHorizontalGroup(
            gameOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(giveUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newWordButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gameOptionsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {giveUpButton, newWordButton, resetButton});

        gameOptionsPanelLayout.setVerticalGroup(
            gameOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameOptionsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(giveUpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newWordButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton)
                .addContainerGap())
        );

        gameOptionsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {giveUpButton, newWordButton, resetButton});

        fileMenu.setText("File");

        newMenuItem.setText("New Game");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);
        fileMenu.add(fileSeparator);

        fileMenuItem.setText("Exit");
        fileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(settingsMenuItem);

        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(keyboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statisticsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(currentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gameOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {currentPanel, gameOptionsPanel, statisticsPanel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(currentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statisticsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gameOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keyboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {currentPanel, keyboardPanel});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void customRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRadioButtonActionPerformed
        fileChooser();
        updateWordList(fileChooser.getSelectedFile().getAbsolutePath());
    }//GEN-LAST:event_customRadioButtonActionPerformed

    private void fileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_fileMenuItemActionPerformed

    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsMenuItemActionPerformed
        showSettingsFrame();
    }//GEN-LAST:event_settingsMenuItemActionPerformed

    private void addButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addButtonMouseReleased
        showAddFrame();
    }//GEN-LAST:event_addButtonMouseReleased

    private void addButtonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addButtonKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            showAddFrame();
        }
    }//GEN-LAST:event_addButtonKeyReleased

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        removeItemFromList();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void wordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_wordFieldKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addItemToList();
            addFrame.dispose();
        }
    }//GEN-LAST:event_wordFieldKeyReleased

    private void confirmAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAddButtonActionPerformed
        addItemToList();
        addFrame.dispose();
    }//GEN-LAST:event_confirmAddButtonActionPerformed

    private void cancelAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAddButtonActionPerformed
        addFrame.dispose();
    }//GEN-LAST:event_cancelAddButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        initGame();
        settingsFrame.dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void defaultRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultRadioButtonActionPerformed
        updateWordList();
    }//GEN-LAST:event_defaultRadioButtonActionPerformed

    private void aButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_aButtonActionPerformed

    private void bButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_bButtonActionPerformed

    private void cButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_cButtonActionPerformed

    private void dButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_dButtonActionPerformed

    private void eButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_eButtonActionPerformed

    private void fButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_fButtonActionPerformed

    private void gButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_gButtonActionPerformed

    private void hButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_hButtonActionPerformed

    private void iButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_iButtonActionPerformed

    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_jButtonActionPerformed

    private void kButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_kButtonActionPerformed

    private void lButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_lButtonActionPerformed

    private void mButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_mButtonActionPerformed

    private void nButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_nButtonActionPerformed

    private void oButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_oButtonActionPerformed

    private void pButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_pButtonActionPerformed

    private void qButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_qButtonActionPerformed

    private void rButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_rButtonActionPerformed

    private void sButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_sButtonActionPerformed

    private void tButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_tButtonActionPerformed

    private void uButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_uButtonActionPerformed

    private void vButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vButtonActionPerformed
        parseGuess(evt);
    }//GEN-LAST:event_vButtonActionPerformed

    private void wButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wButtonActionPerformed
        makeMove('w');
    }//GEN-LAST:event_wButtonActionPerformed

    private void xButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xButtonActionPerformed
        makeMove('x');
    }//GEN-LAST:event_xButtonActionPerformed

    private void yButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yButtonActionPerformed
        makeMove('y');
    }//GEN-LAST:event_yButtonActionPerformed

    private void zButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zButtonActionPerformed
        makeMove('z');
    }//GEN-LAST:event_zButtonActionPerformed

    private void newWordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWordButtonActionPerformed
        initGame();
    }//GEN-LAST:event_newWordButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        tryResetGame();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        parseGuess(evt);
    }//GEN-LAST:event_formKeyReleased

    private void giveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giveUpButtonActionPerformed
        attemptGiveUp();
    }//GEN-LAST:event_giveUpButtonActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        showSettingsFrame();
    }//GEN-LAST:event_newMenuItemActionPerformed
    
    /**
     * Asks for user input to reset the game.
     */
    private void tryResetGame() {
        int reply = showConfirmPane("<html><p>Reset the all scores and the game "
                + "board back to default?</p></html>",
                                   "Reset Confirmation");
        if(reply == javax.swing.JOptionPane.YES_OPTION) {
            guessedField.setText("None.");
            gamesPlayed = 0;
            gamesWon = 0;
            initGame();
        }
    }
    
     /**
     * Attempts to get user input on whether or not to "give up" or throw this 
     * current game. If the user specifies yes, the current game is considered a
     * loss and the user is shown the correct word. Otherwise, nothing happens.
     */
    private void attemptGiveUp() {
        int response = showConfirmPane("Really give up and show the word?", "Give Up?");
        if (response == javax.swing.JOptionPane.YES_OPTION) {
            lostGame();
        }
    }
    
    /**
     * Returns the currently selected difficulty.
     * 
     * @return The currently selected difficulty.
     */
    private WordProperties getUserSelectedDifficulty() {
        WordProperties difficulty = WordProperties.HARD_WORD;
        if (easyRadioButton.isSelected()) {
            difficulty = WordProperties.EASY_WORD;
        } 
        else if (mediumRadioButton.isSelected()) {
            difficulty = WordProperties.MEDIUM_WORD;
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
                break; // Each actor appears only once, so okay to break.
            }
        }
        return actor;
    }
    
    /**
     * Displays the word adding {@code JFrame}.
     */
    private void showAddFrame() {
        addFrame.setBounds(getX() + 10, getY() + 10, 220, 188);
        addFrame.setVisible(true);
    }
    
    /**
     * Displays the settings {@code JFrame}.
     */
    private void showSettingsFrame() {
        settingsFrame.setBounds(getX() + 10, getY() + 10, 457, 485);
        settingsFrame.setVisible(true);
    }
    
    /**
     * Parses and handles the value an input event. If the key pressed is not 
     * alphabetic, this method emits an error beep and does nothing. Otherwise,
     * this method attempts to make the given move.
     * 
     * @param evt The input event at which the guess occurred. This event is of
     *        common superclass of {@link KeyEvent} and {@link ActionEvent}, 
     *        allowing for the use of .
     * @see #makeMove(char) The next method in the chain of events that occur
     *      when a guess is received from the GUI.
     */
    private void parseGuess(EventObject evt) {
        // Each button on the GUI soft-keyboard has a String with length one 
        // with its representative character. Also works with key inputs.
        char guess = ((AbstractButton) evt.getSource()).getText().charAt(0);
        if (Character.isAlphabetic(guess)) {
            makeMove(guess);
        }
    }
    
    /**
     * Attempts to make a move on the game board. This method updates the game 
     * board appropriately depending on the validity of the guess.
     * 
     * @param guess The character to attempt to guess.
     * @see #correctGuess() Called by this method if the given guess is correct.
     * @see #incorrectGuess() Called by this method if the given guess is not
     *      correct.
     * 
     */
    private void makeMove(char guess) {
        boolean valid = game.makeGuess(guess);
        if (valid) {
            correctGuess();
            disableRepresentative(guess);
        }
        else {
            incorrectGuess();
        }
        updateStatistics();
    }
    
    /**
     * Finds the {@code JButton} in the {@code keyboardPanel} that represents 
     * the given character parameter and disables it.
     * 
     * @param guess The character guess.
     */
    private void disableRepresentative(char guess) {
        char sanitizedGuess = Word.sanitizeWord(guess);
        for (int i = 0; i < keyboardPanel.getComponentCount(); i++) {
            AbstractButton button = (AbstractButton) keyboardPanel.getComponent(i);
            char buttonText = Word.sanitizeWord(button.getText().charAt(0));
            if (sanitizedGuess == buttonText) {
                button.setEnabled(false);
                break; // Only one button for each character, so okay to break
            }
        }
    }
    
    /**
     * Initializes all game variables.
     */
    private void initGame() {
        updateGameSettings();
        updateCurrentLabel();
        updateImages();
        updateStatistics();
        setStateOfAll(true);
    }
    
    /**
     * Updates the game with the currently given set of user settings.
     */
    private void updateGameSettings() {
        Actor actor = getUserSelectedActor();
        Dictionary dictionary = Dictionary.NULL_DICTIONARY;
        WordProperties difficulty = getUserSelectedDifficulty();
        game = new Hangman(difficulty, actor, dictionary);
    }
    
    /**
     * Updates the current label which displays the current word to accurately
     * reflect the state of the game.
     */
    private void updateCurrentLabel() {
        String formatted = StringUtilities.expand(game.getCorrectGuesses());
        currentLabel.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));
        currentLabel.setText(formatted);
    }
    
    /**
     * Updates the current set of images on the game panel.
     */
    private void updateImages() {
        int index = game.getActor().getImageArray().length - game.getGuessesRemaining();
        if (index <= game.maxGuesses()) {
            
        }
        imageLabel.setIcon(game.getActor().getImageArray()[index]);
    }
    
    /**
     * Enables or disables all components contained within the object.
     * 
     * @param state The state to set every {@code Component} to.
     */
    private void setStateOfAll(boolean state) {
        List<Component> allComponents = getAllComponents(this);
        allComponents.stream().forEach((Component c) -> {
            c.setEnabled(state);
        });
    }
    
    /**
     * Updates the statistics display panel.
     */
    private void updateStatistics() {
        String guessed = game.getAlreadyGuessed().toUpperCase();
        guessed = StringUtilities.toFormattedCharArray(guessed);
        guessedField.setText(guessed);
        
        int remaining = game.getGuessesRemaining();
        remainingField.setText(remaining + "");
        
        gamesWonField.setText(gamesWon + "");
        gamesPlayedField.setText(gamesPlayed + "");
    }
    
    /**
     * Method for updating the game board with a valid move.
     */
    private void correctGuess() {
        updateStatistics();
        String current = StringUtilities 
                .expandString(game.getCorrectGuesses(), " ");
        currentLabel.setText(current);
        checkGameState();
    }
    
    /**
     * Method for updating the game board with an invalid move.
     */
    private void incorrectGuess() {
        updateStatistics();
        updateImages();
        checkGameState();
    }
    
    /**
     * Method that checks if the user has won or lost the game.
     */
    private void checkGameState() {
        if (game.hasWon()) {
            wonGame();
        } 
        else if (game.getGuessesMade() >= game.getMaxGuesses()) {
            lostGame();
        }

    }

    /**
     * Method that handles the winner state of the game.
     */
    private void wonGame() {
        gamesWon++;
        gamesPlayed++;
        endOfGameChecks("Nice guessing! \""
                + StringUtilities.formatAsSentence(game.getCurrentWord())
                + "\" was the correct word!",
                "Winner!");
    }
    
    /**
     * Method that handles the loser state of the game.
     */
    private void lostGame() {
        gamesPlayed++;
        endOfGameChecks("Sorry! \""
                + StringUtilities.formatAsSentence(game.getCurrentWord())
                + "\" was the correct word!",
                "Loser!");
    }
    
    /**
     * Method that ensures the GUI is kept properly updated at the end of a
     * game.
     * 
     * @param message   The message to display a message pane with.
     * @param title     The title of the message pane to display.
     */
    private void endOfGameChecks(String message, String title) {
        String actual = StringUtilities
                .expandString(game.getCurrentWord(), " ");
        currentLabel.setText(actual);
        updateStatistics();
        resetKeyboard(false);
        giveUpButton.setEnabled(false);
        showMessagePane(message, title);
    }
    
    /**
     * Method that removes a user-specified item from the dictionary.
     */
    private void removeItemFromList() {
        int index = wordDisplayList.getSelectedIndex();
        int response = showConfirmPane("Really remove \""
                + game.getWords().getList().get(index)
                + "\" from the dictionary?",
                "Word Removal");
        if(response == JFileChooser.APPROVE_OPTION) {
            game.getWords().remove(wordDisplayList.getSelectedIndex());
            wordDisplayList.updateUI();
        }
    }
    
    /**
     * Method that adds a user-defined item to the dictionary in alphabetical
     * order.
     */
    private void addItemToList() {
        String add = wordField.getText();
        add = add.toLowerCase();
        if (!game.getWords().getList().contains(add)) {
            game.getWords().add(wordField.getText());
            wordField.setText("");
            wordDisplayList.updateUI();
        }
        else {
            showMessagePane("The given word \""
                          + add
                          + "\" is in the dictionary.", "Add Word Error");
        }
    }
    
    /**
     * Method that allows for the user to pick a file to assign to the 
     * dictionary.
     */
    private int fileChooser() {
        int response = fileChooser.showOpenDialog(null);
        if (response != javax.swing.JFileChooser.APPROVE_OPTION) {
            defaultRadioButton.setSelected(true);
        }
        return response;
    }
    
    /**
     * Method for reinstantiating {@code gameManager.getList()} with the default
     * word repository.
     */
    private void updateWordList() {
        Dictionary list = new Dictionary("bin/default.txt");
        game.setWords(list);
    }
    
    /**
     * Method for reinstantiating {@code gameManager.getList()} with the 
     * specified path word repository.
     * 
     * @param path  The location of the file to retrieve.
     */
    private void updateWordList(String path) {
        if (path == null) {
            showErrorPane("The selected file is empty or unreadable. "
                    + "Try a different file.",
                    "File Chooser Error");
            fileChooser();
        }
        else {
            Dictionary list = new Dictionary(new File(path));
            game = new Hangman(getUserSelectedDifficulty(), getUserSelectedActor(), list);
            wordDisplayList.updateUI();
        }
    }
    
    /**
     * Method for displaying a custom {@code JOptionPane} confirmation dialog.
     * 
     * @param message   The message to display on the pane.
     * @param title     The title of the pane.
     * @return          Return the outcome of the user input.
     */
    private int showConfirmPane(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title, 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Method for displaying a custom {@code JOptionPane} information window.
     * 
     * @param message   The message to display on the pane.
     * @param title     The title of the pane.
     */
    private void showMessagePane(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Method for displaying a custom {@code JOptionPane} information window.
     * 
     * @param message   The message to display on the pane.
     * @param title     The title of the pane.
     */
    private void showErrorPane(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, 
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Method that should be called at the start of program execution. Prompts
     * the user if they would like to automatically start a new game.
     * 
     * @param message   The message to display on the pane.
     * @param title     The title of the pane.
     */
    public void newGameDialog(String message, String title) {
        int response = showConfirmPane(message, title);
        if (response == JOptionPane.YES_OPTION) {
            showSettingsFrame();
        }
    }
    
    /**
     * The main method for this class, creates and shows a new form of this
     * class.
     * 
     * @param args  The command line arguments.
     */
    public static void main(String args[]) {
        // Sets the Windows look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException 
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ColorPicker.class.getName())
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aButton;
    private javax.swing.JComboBox actorComboBox;
    private javax.swing.JLabel actorLabel;
    private javax.swing.JButton addButton;
    private javax.swing.JFrame addFrame;
    private javax.swing.JPanel addPanel;
    private javax.swing.JRadioButton anyRadioButton;
    private javax.swing.JButton bButton;
    private javax.swing.JButton cButton;
    private javax.swing.JButton cancelAddButton;
    private javax.swing.JButton confirmAddButton;
    private javax.swing.JLabel currentLabel;
    private javax.swing.JPanel currentPanel;
    private javax.swing.JRadioButton customRadioButton;
    private javax.swing.JButton dButton;
    private javax.swing.JRadioButton defaultRadioButton;
    private javax.swing.ButtonGroup difficultyButtonGroup;
    private javax.swing.JLabel difficultyLabel;
    private javax.swing.JButton eButton;
    private javax.swing.JRadioButton easyRadioButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPanel editPanel;
    private javax.swing.JButton fButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem fileMenuItem;
    private javax.swing.JPopupMenu.Separator fileSeparator;
    private javax.swing.JButton gButton;
    private javax.swing.JPanel gameOptionsPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JTextField gamesPlayedField;
    private javax.swing.JLabel gamesPlayedLabel;
    private javax.swing.JTextField gamesWonField;
    private javax.swing.JLabel gamesWonLabel;
    private javax.swing.JButton giveUpButton;
    private javax.swing.JTextField guessedField;
    private javax.swing.JLabel guessedLabel;
    private javax.swing.JButton hButton;
    private javax.swing.JRadioButton hardRadioButton;
    private javax.swing.JButton iButton;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JButton jButton;
    private javax.swing.JButton kButton;
    private javax.swing.JPanel keyboardPanel;
    private javax.swing.JButton lButton;
    private javax.swing.ButtonGroup loadButtonGroup;
    private javax.swing.JLabel loadLabel;
    private javax.swing.JPanel loadPanel;
    private javax.swing.JButton mButton;
    private javax.swing.JRadioButton mediumRadioButton;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton nButton;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JButton newWordButton;
    private javax.swing.JButton oButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton pButton;
    private javax.swing.JButton qButton;
    private javax.swing.JButton rButton;
    private javax.swing.JTextField remainingField;
    private javax.swing.JLabel remainingLabel;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton sButton;
    private javax.swing.JFrame settingsFrame;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JPanel statisticsPanel;
    private javax.swing.JButton tButton;
    private javax.swing.JButton uButton;
    private javax.swing.JPanel updatePanel;
    private javax.swing.JButton vButton;
    private javax.swing.JButton wButton;
    private javax.swing.JList wordDisplayList;
    private javax.swing.JPanel wordDisplayPanel;
    private javax.swing.JTextField wordField;
    private javax.swing.JLabel wordLabel;
    private javax.swing.JScrollPane wordScrollPane;
    private javax.swing.JButton xButton;
    private javax.swing.JButton yButton;
    private javax.swing.JButton zButton;
    // End of variables declaration//GEN-END:variables
    private Hangman game;
    private int gamesPlayed;
    private int gamesWon;
    
}