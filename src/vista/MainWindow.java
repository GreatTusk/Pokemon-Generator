package vista;

import ImageManagement.ImageIconCellRenderer;
import ActionListeners.CustomChangeListener;
import ActionListeners.SliderMouseListener;
import ImageManagement.ImageArray;
import Interface.IDefaultAttributes;
import Interface.IPokemonCalculator;
import Storage.PokemonFileUtility;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelo.Ability;
import modelo.Item;
import modelo.Move;
import modelo.Nature;
import modelo.Pokemon;
import modelo.PokemonPaste;
import modelo.PokemonType;
import modelo.Gender;
import modelo.PokemonPasteInfo;
import soundManagement.SoundPlayer;
import static vista.MainWindow.addLimit;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author F776
 */
public class MainWindow extends javax.swing.JFrame implements IPokemonCalculator, IDefaultAttributes, Serializable {

    private int counter;
    private int toUpdatePkmnId;
    private final Gender gender = new Gender(3, "Genderless");
    private static ArrayList<PokemonPaste> listPokemonPaste = new ArrayList<>();
    private static ArrayList<Item> listaItems;
    private static ArrayList<Pokemon> listaPokemon;
    private static ArrayList<Ability> listaAbility;
    private static ArrayList<PokemonType> listaPkmnType;
    private static ArrayList<Nature> listaNature;
    private static ArrayList<Move> listaMove;
    private static HashMap<Integer, Integer[]> listPokemonTyping = new HashMap<>();
    private static HashMap<Integer, ImageIcon> listPokemonImage = new HashMap<>();
    private static HashMap<Integer, ImageIcon> listTypeImage = new HashMap<>();
    private static HashMap<Integer, List<Ability>> listPokemonAbility = new HashMap<>();
    private static final ImageArray ia = new ImageArray() {
    };
    private JLabel[] listStatLabels;
    private JSpinner[] listIvSPN;
    private JComboBox[] listMoveCBOXS;
    private JSlider[] listStatSliders;
    private static MainWindow instance;
    private static final ImageIcon pokeball = POKEBALL_IMAGE;
    private static MovesTable movesTable;
    private static ItemsTable itemsTable;
    private static AbilityTable abilityTable;
    private static final double LOAD_AMOUNT = 100 / 11;

    private static void updateProgressBar(String message) {
        SwingUtilities.invokeLater(() -> {
            LoadingScreen.getProgressBar().setValue((int) (LoadingScreen.getProgressBar().getValue() + LOAD_AMOUNT));
            LoadingScreen.getProgressLabel().setText(message);
        });
    }

    /**
     * Creates new form MainWindow
     *
     * @throws java.net.URISyntaxException
     */
    public MainWindow() throws URISyntaxException {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        initComponents();
        initializeArrays();
        createDataDirectory();
        loadAllObjects();
        initializeComponents();
        refresh();

    }

    @SuppressWarnings("unchecked")
    private <T> T loadObjectFromResource(String resourceName) {
        try (InputStream inputStream = MainWindow.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                    // The cast is safe because we are reading an object of type T from the stream
                    return (T) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    // Handle the exception appropriately

                }
            } else {
                System.err.println("Resource not found: " + resourceName);
            }
        } catch (IOException e) {
            // Handle the exception appropriately

        }
        return null;
    }

    private void loadAllObjects() {
        LoadingScreen.getLoadingScreen().setVisible(true);

        Thread thread = new Thread(() -> {
            // Getting Pokémon
            MainWindow.listaPokemon = loadObjectFromResource("Objects/listaPokemon.ser");
            updateProgressBar(PROGRESS_POKEMON);

            // Getting natures
            MainWindow.listaNature = loadObjectFromResource("Objects/listaNature.ser");
            updateProgressBar(PROGRESS_NATURES);

            // Getting moves
            MainWindow.listaMove = loadObjectFromResource("Objects/listaMove.ser");
            updateProgressBar(PROGRESS_MOVES);

            // Retrieve Pokemon types
            MainWindow.listaPkmnType = loadObjectFromResource("Objects/listaPkmnType.ser");
            updateProgressBar(PROGRESS_TYPES);

            // Getting abilities
            MainWindow.listaAbility = loadObjectFromResource("Objects/listaAbility.ser");
            updateProgressBar(PROGRESS_ABILITIES);

            // Retrieve Items
            MainWindow.listaItems = loadObjectFromResource("Objects/listaItems.ser");
            updateProgressBar(PROGRESS_ITEMS);

            // Retrieve Pokemon Typings
            MainWindow.listPokemonTyping = loadObjectFromResource("Objects/listPokemonTyping.ser");
            updateProgressBar(PROGRESS_TYPINGS);

            // Getting Pokemon Abilities
            MainWindow.listPokemonAbility = loadObjectFromResource("Objects/listPokemonAbility.ser");
            updateProgressBar(PROGRESS_POKEMON_ABILITIES);

            // Initialize image arrays
            MainWindow.listPokemonImage = loadObjectFromResource("Objects/listPokemonImage.ser");
            ia.setPokemonIconArray(MainWindow.listaPokemon);
            updateProgressBar(PROGRESS_POKEMON_IMAGES);

            MainWindow.listTypeImage = loadObjectFromResource("Objects/listTypeImage.ser");
            ia.setTypeIconArray();
            updateProgressBar(PROGRESS_TYPE_IMAGES);
        });
        thread.start(); // Corrected to start the thread

        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

//    private void exportAllHashMaps() {
//
//        exportObject(listaPokemon, "src/main/resources/Objects/listaPokemon");
//        exportObject(listaNature, "src/main/resources/Objects/listaNature");
//        exportObject(listaMove, "src/main/resources/Objects/listaMove");
//        exportObject(listaPkmnType, "src/main/resources/Objects/listaPkmnType");
//        exportObject(listaAbility, "src/main/resources/Objects/listaAbility");
//        exportObject(listaItems, "src/main/resources/Objects/listaItems");
//        exportObject(listPokemonTyping, "src/main/resources/Objects/listPokemonTyping");
//        exportObject(listPokemonAbility, "src/main/resources/Objects/listPokemonAbility");
//        exportObject(listPokemonImage, "src/main/resources/Objects/listPokemonImage");
//        exportObject(listTypeImage, "src/main/resources/Objects/listTypeImage");
//    }
//
//    private void exportObject(Object o, String path) {
//
//        try (FileOutputStream fileOut = new FileOutputStream(path + ".ser"); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
//
//            out.writeObject(o);
//            System.out.println("Object " + path + " saved!");
//            fileOut.close();
//            out.close();
//
//        } catch (IOException i) {
//        }
//    }

    private void initializeArrays() {
        // Then, initialize the statSliders array after the Swing components are created
        this.listStatSliders = new JSlider[]{sldHP, sldATK, sldDEF, sldSPATK, sldSPDEF, sldSPD};
        this.listStatLabels = new JLabel[]{lblHP, lblATK, lblDEF, lblSPATK, lblSPDEF, lblSPD};
        this.listIvSPN = new JSpinner[]{spnIVHP, spnIVATK, spnIVDEF, spnIVSPATK, spnIVSPDEF, spnIVSPD};
        this.listMoveCBOXS = new JComboBox[]{cboMove1, cboMove2, cboMove3, cboMove4};
        this.btnUpdatePokepaste.setVisible(false);
        lblLogoUpdate.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void initializeComponents() {
        initializeSpinners(listIvSPN);
        addLimit(listStatSliders);
        setLabels(listStatLabels, listStatSliders);
        llenarCboMoves(listMoveCBOXS);
        llenarCboItem();
        llenarCboSpecies();
        counter++;
        llenarPkmnType();
        llenarNature();
        llenarTabla();
        counter++;
        updateRemainingEvs();
        initializeEventListeners();
        this.setLocationRelativeTo(null);
        Thread thread = new Thread(() -> {
            updateProgressBar(PROGRESS_COMPONENTS);
            LoadingScreen.getProgressBar().setValue(100);
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Dispose the loading screen on the EDT
            SwingUtilities.invokeLater(() -> {

                LoadingScreen.disposeFrame();
            });
        }

    }

    // ImageIcon created for the frame
    ImageIcon greatTuskIcon = GREAT_TUSK_ICON;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgpGender = new javax.swing.ButtonGroup();
        jSlider1 = new javax.swing.JSlider();
        jtpPokemonGeneration = new javax.swing.JTabbedPane();
        jPanelPokemon = new javax.swing.JPanel();
        lblNickname = new javax.swing.JLabel();
        lblSpecies = new javax.swing.JLabel();
        lblMove1 = new javax.swing.JLabel();
        lblMove2 = new javax.swing.JLabel();
        lblMove4 = new javax.swing.JLabel();
        lblMove3 = new javax.swing.JLabel();
        lblItems = new javax.swing.JLabel();
        txtNickname = new javax.swing.JTextField();
        lblAbility = new javax.swing.JLabel();
        lblTeraType = new javax.swing.JLabel();
        cboTeraType = new javax.swing.JComboBox<>();
        lblNature = new javax.swing.JLabel();
        cboNature = new javax.swing.JComboBox<>();
        rbnFemale = new javax.swing.JRadioButton();
        rbnMale = new javax.swing.JRadioButton();
        rdnGenderless = new javax.swing.JRadioButton();
        cboSpecies = new javax.swing.JComboBox<>();
        cboMove1 = new javax.swing.JComboBox<>();
        cboMove2 = new javax.swing.JComboBox<>();
        cboMove3 = new javax.swing.JComboBox<>();
        cboMove4 = new javax.swing.JComboBox<>();
        cboItem = new javax.swing.JComboBox<>();
        cboAbility = new javax.swing.JComboBox<>();
        btnGeneratePokepaste = new javax.swing.JButton();
        spnLevel = new javax.swing.JSpinner();
        lblLevel = new javax.swing.JLabel();
        ckbShiny = new javax.swing.JCheckBox();
        btnSavePokepaste = new javax.swing.JButton();
        btnImportPaste = new javax.swing.JButton();
        btnAbilityDesc = new javax.swing.JButton();
        btnMove1Desc = new javax.swing.JButton();
        btnMove2Desc = new javax.swing.JButton();
        btnMove3Desc = new javax.swing.JButton();
        btnMove4Desc = new javax.swing.JButton();
        btnItemDesc = new javax.swing.JButton();
        btnResetFields = new javax.swing.JButton();
        lblPokepaste = new javax.swing.JLabel();
        lblPokemonSprite = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPokepaste = new javax.swing.JTextArea();
        btnUpdatePokepaste = new javax.swing.JButton();
        lblType1 = new javax.swing.JLabel();
        lblType2 = new javax.swing.JLabel();
        lbllogoGenerate = new javax.swing.JLabel();
        lblLogoReset = new javax.swing.JLabel();
        lblLogoImport = new javax.swing.JLabel();
        lblLogoSave = new javax.swing.JLabel();
        lblLogoUpdate = new javax.swing.JLabel();
        btnPokemonCry = new javax.swing.JButton();
        lblPokemonImage = new javax.swing.JLabel();
        jpnStats = new javax.swing.JPanel();
        lblDEF = new javax.swing.JLabel();
        lblSPDEF = new javax.swing.JLabel();
        sldATK = new javax.swing.JSlider();
        sldDEF = new javax.swing.JSlider();
        lblSPATK = new javax.swing.JLabel();
        sldSPD = new javax.swing.JSlider();
        lblSPD = new javax.swing.JLabel();
        lblHP = new javax.swing.JLabel();
        sldSPDEF = new javax.swing.JSlider();
        lblATK = new javax.swing.JLabel();
        sldHP = new javax.swing.JSlider();
        lblEVs = new javax.swing.JLabel();
        lblBaseHp = new javax.swing.JLabel();
        sldSPATK = new javax.swing.JSlider();
        lblBaseAtk = new javax.swing.JLabel();
        lblBaseDef = new javax.swing.JLabel();
        lblBaseSpatk = new javax.swing.JLabel();
        lblBaseSPDef = new javax.swing.JLabel();
        lblBaseSpd = new javax.swing.JLabel();
        pgbHP = new javax.swing.JProgressBar();
        pgbATK = new javax.swing.JProgressBar();
        pgbDEF = new javax.swing.JProgressBar();
        pgbSPATK = new javax.swing.JProgressBar();
        pgbSPDEF = new javax.swing.JProgressBar();
        pgbSPD = new javax.swing.JProgressBar();
        lblFinalHP = new javax.swing.JLabel();
        lblFinalATK = new javax.swing.JLabel();
        lblFinalDEF = new javax.swing.JLabel();
        lblFinalSPATK = new javax.swing.JLabel();
        lblFinalSPDEF = new javax.swing.JLabel();
        lblFinalSPD = new javax.swing.JLabel();
        lblIVs = new javax.swing.JLabel();
        spnIVHP = new javax.swing.JSpinner();
        spnIVATK = new javax.swing.JSpinner();
        spnIVDEF = new javax.swing.JSpinner();
        spnIVSPATK = new javax.swing.JSpinner();
        spnIVSPDEF = new javax.swing.JSpinner();
        spnIVSPD = new javax.swing.JSpinner();
        lblEVSum = new javax.swing.JLabel();
        lblRemainingEvs = new javax.swing.JLabel();
        lblSPDEv = new javax.swing.JLabel();
        lblHPEV = new javax.swing.JLabel();
        lblATKEV = new javax.swing.JLabel();
        lblDEFEV = new javax.swing.JLabel();
        lblSPATKEV = new javax.swing.JLabel();
        lblSPDEFEV = new javax.swing.JLabel();
        lblPkdexImage = new javax.swing.JLabel();
        SavedPokemon = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPokemonPaste = new javax.swing.JTable();
        btnConsultar = new javax.swing.JButton();
        btnUpdateRow = new javax.swing.JButton();
        btnDeletePokepaste = new javax.swing.JButton();
        lblRecuperarPokemon = new javax.swing.JLabel();
        lblLogoConsultar = new javax.swing.JLabel();
        PokeballDelete = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();
        jpnMovesTable = new javax.swing.JPanel();
        btnTableMoves = new javax.swing.JButton();
        btnPokemonPokedex = new javax.swing.JButton();
        btnItemPokedex = new javax.swing.JButton();
        btnAbilityPokedex = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pokemon Generator");
        setIconImage(greatTuskIcon.getImage());
        setName("mainFrame"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1920, 1080));
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtpPokemonGeneration.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        jtpPokemonGeneration.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtpPokemonGeneration.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jtpPokemonGenerationMouseEntered(evt);
            }
        });

        jPanelPokemon.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPokemon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNickname.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblNickname.setForeground(new java.awt.Color(255, 255, 255));
        lblNickname.setText("Nickname:");
        jPanelPokemon.add(lblNickname, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 160, -1, -1));

        lblSpecies.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblSpecies.setForeground(new java.awt.Color(255, 255, 255));
        lblSpecies.setText("Species:");
        jPanelPokemon.add(lblSpecies, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 210, -1, -1));

        lblMove1.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblMove1.setForeground(new java.awt.Color(255, 255, 255));
        lblMove1.setText("Move 1:");
        jPanelPokemon.add(lblMove1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 310, -1, -1));

        lblMove2.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblMove2.setForeground(new java.awt.Color(255, 255, 255));
        lblMove2.setText("Move 2:");
        jPanelPokemon.add(lblMove2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 360, -1, -1));

        lblMove4.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblMove4.setForeground(new java.awt.Color(255, 255, 255));
        lblMove4.setText("Move 4:");
        jPanelPokemon.add(lblMove4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 460, -1, -1));

        lblMove3.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblMove3.setForeground(new java.awt.Color(255, 255, 255));
        lblMove3.setText("Move 3:");
        jPanelPokemon.add(lblMove3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 410, -1, -1));

        lblItems.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblItems.setForeground(new java.awt.Color(255, 255, 255));
        lblItems.setText("Items:");
        jPanelPokemon.add(lblItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 510, -1, -1));
        jPanelPokemon.add(txtNickname, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, 180, -1));

        lblAbility.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblAbility.setForeground(new java.awt.Color(255, 255, 255));
        lblAbility.setText("Ability:");
        jPanelPokemon.add(lblAbility, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, -1, -1));

        lblTeraType.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblTeraType.setForeground(new java.awt.Color(255, 255, 255));
        lblTeraType.setText("Tera Type:");
        jPanelPokemon.add(lblTeraType, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 560, -1, -1));

        cboTeraType.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboTeraType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTeraTypeItemStateChanged(evt);
            }
        });
        cboTeraType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTeraTypeActionPerformed(evt);
            }
        });
        jPanelPokemon.add(cboTeraType, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 580, 300, -1));

        lblNature.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblNature.setForeground(new java.awt.Color(255, 255, 255));
        lblNature.setText("Nature:");
        jPanelPokemon.add(lblNature, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 610, -1, -1));

        cboNature.setToolTipText("");
        cboNature.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboNature.setName(""); // NOI18N
        cboNature.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNatureItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboNature, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 630, 300, -1));

        rbnFemale.setBackground(new java.awt.Color(189, 15, 52));
        bgpGender.add(rbnFemale);
        rbnFemale.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        rbnFemale.setForeground(new java.awt.Color(255, 255, 255));
        rbnFemale.setText("Female");
        rbnFemale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbnFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnFemaleActionPerformed(evt);
            }
        });
        jPanelPokemon.add(rbnFemale, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 520, 70, -1));

        rbnMale.setBackground(new java.awt.Color(189, 15, 52));
        bgpGender.add(rbnMale);
        rbnMale.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        rbnMale.setForeground(new java.awt.Color(255, 255, 255));
        rbnMale.setText("Male");
        rbnMale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbnMale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbnMaleActionPerformed(evt);
            }
        });
        jPanelPokemon.add(rbnMale, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 560, -1, -1));

        rdnGenderless.setBackground(new java.awt.Color(189, 15, 52));
        bgpGender.add(rdnGenderless);
        rdnGenderless.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        rdnGenderless.setForeground(new java.awt.Color(255, 255, 255));
        rdnGenderless.setSelected(true);
        rdnGenderless.setText("Random");
        rdnGenderless.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdnGenderless.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdnGenderlessActionPerformed(evt);
            }
        });
        jPanelPokemon.add(rdnGenderless, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 600, 80, -1));

        cboSpecies.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboSpecies.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSpeciesItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboSpecies, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 230, 300, -1));

        cboMove1.setSelectedItem(new String(""));
        cboMove1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboMove1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMove1ItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboMove1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 330, 300, -1));

        cboMove2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboMove2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMove2ItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboMove2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, 300, -1));

        cboMove3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboMove3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMove3ItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboMove3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 430, 300, -1));

        cboMove4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboMove4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMove4ItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboMove4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 480, 300, -1));

        cboItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboItemItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 530, 300, -1));

        cboAbility.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboAbility.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cboAbility.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAbilityItemStateChanged(evt);
            }
        });
        jPanelPokemon.add(cboAbility, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 280, 300, -1));

        btnGeneratePokepaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnGeneratePokepaste.setText("Generate");
        btnGeneratePokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGeneratePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGeneratePokepasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnGeneratePokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, 90, -1));

        spnLevel.setModel(new javax.swing.SpinnerNumberModel(100, 1, 100, 1));
        spnLevel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spnLevel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnLevelStateChanged(evt);
            }
        });
        jPanelPokemon.add(spnLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 520, -1, -1));

        lblLevel.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblLevel.setForeground(new java.awt.Color(255, 255, 255));
        lblLevel.setText("Level:");
        jPanelPokemon.add(lblLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 500, -1, -1));

        ckbShiny.setBackground(new java.awt.Color(189, 15, 52));
        ckbShiny.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        ckbShiny.setForeground(new java.awt.Color(255, 255, 255));
        ckbShiny.setText("Shiny");
        ckbShiny.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ckbShiny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbShinyActionPerformed(evt);
            }
        });
        jPanelPokemon.add(ckbShiny, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 580, -1, -1));

        btnSavePokepaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnSavePokepaste.setText("Save");
        btnSavePokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSavePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePokepasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnSavePokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 90, -1));

        btnImportPaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnImportPaste.setText("Import");
        btnImportPaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImportPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportPasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnImportPaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 620, 90, -1));

        btnAbilityDesc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnAbilityDesc.setForeground(new java.awt.Color(206, 17, 49));
        btnAbilityDesc.setText("?");
        btnAbilityDesc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAbilityDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbilityDescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnAbilityDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 280, 40, 20));

        btnMove1Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove1Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove1Desc.setText("?");
        btnMove1Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove1Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove1DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove1Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 40, 20));

        btnMove2Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove2Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove2Desc.setText("?");
        btnMove2Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove2Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove2DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove2Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 380, 40, 20));

        btnMove3Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove3Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove3Desc.setText("?");
        btnMove3Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove3Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove3DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove3Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 430, 40, 20));

        btnMove4Desc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnMove4Desc.setForeground(new java.awt.Color(206, 17, 49));
        btnMove4Desc.setText("?");
        btnMove4Desc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMove4Desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMove4DescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnMove4Desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 480, 40, 20));

        btnItemDesc.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        btnItemDesc.setForeground(new java.awt.Color(206, 17, 49));
        btnItemDesc.setText("?");
        btnItemDesc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDescActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnItemDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 530, 40, 20));

        btnResetFields.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnResetFields.setText("Reset");
        btnResetFields.setToolTipText("");
        btnResetFields.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetFieldsActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnResetFields, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 540, 90, -1));

        lblPokepaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        lblPokepaste.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPokepaste.setText("Pokepaste:");
        lblPokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblPokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, -1, -1));

        lblPokemonSprite.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblPokemonSprite, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 110, 100));

        txtPokepaste.setBackground(new java.awt.Color(239, 240, 208));
        txtPokepaste.setColumns(20);
        txtPokepaste.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        txtPokepaste.setRows(5);
        txtPokepaste.setBorder(null);
        txtPokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(txtPokepaste);

        jPanelPokemon.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 310, 220));

        btnUpdatePokepaste.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        btnUpdatePokepaste.setText("Update");
        btnUpdatePokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdatePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePokepasteActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnUpdatePokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, 90, -1));

        lblType1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblType1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, -1, -1));

        lblType2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblType2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 130, -1, -1));

        lbllogoGenerate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbllogoGenerate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/logoGenerate.png"))); // NOI18N
        lbllogoGenerate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lbllogoGenerate, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 500, 30, 40));

        lblLogoReset.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/logoReset.png"))); // NOI18N
        lblLogoReset.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblLogoReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 530, 30, 40));

        lblLogoImport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/logoImport.png"))); // NOI18N
        lblLogoImport.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblLogoImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 610, 30, 40));

        lblLogoSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/logoSave.png"))); // NOI18N
        lblLogoSave.setToolTipText("");
        lblLogoSave.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblLogoSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 560, 30, 60));

        lblLogoUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/logoUpdate.png"))); // NOI18N
        lblLogoUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblLogoUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 570, 30, 40));

        btnPokemonCry.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnPokemonCry.setForeground(new java.awt.Color(206, 17, 49));
        btnPokemonCry.setText("▶");
        btnPokemonCry.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPokemonCry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPokemonCryActionPerformed(evt);
            }
        });
        jPanelPokemon.add(btnPokemonCry, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 230, 40, 20));

        lblPokemonImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/PokedexOpenInsert - copia.jpg"))); // NOI18N
        lblPokemonImage.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelPokemon.add(lblPokemonImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 920, 700));

        jtpPokemonGeneration.addTab("Pokémon", jPanelPokemon);

        jpnStats.setBackground(new java.awt.Color(255, 255, 255));
        jpnStats.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDEF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jpnStats.add(lblDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 360, 42, 54));

        lblSPDEF.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jpnStats.add(lblSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 500, 42, 54));

        sldATK.setMajorTickSpacing(36);
        sldATK.setMaximum(252);
        sldATK.setPaintLabels(true);
        sldATK.setValue(0);
        sldATK.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jpnStats.add(sldATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 290, 220, 46));

        sldDEF.setMajorTickSpacing(36);
        sldDEF.setMaximum(252);
        sldDEF.setPaintLabels(true);
        sldDEF.setValue(0);
        sldDEF.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jpnStats.add(sldDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 360, 220, 46));

        lblSPATK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jpnStats.add(lblSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 430, 42, 54));

        sldSPD.setMajorTickSpacing(36);
        sldSPD.setMaximum(252);
        sldSPD.setPaintLabels(true);
        sldSPD.setValue(0);
        sldSPD.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jpnStats.add(sldSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 570, 220, 46));

        lblSPD.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jpnStats.add(lblSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 570, 42, 54));

        lblHP.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jpnStats.add(lblHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 42, 54));

        sldSPDEF.setMajorTickSpacing(36);
        sldSPDEF.setMaximum(252);
        sldSPDEF.setPaintLabels(true);
        sldSPDEF.setValue(0);
        sldSPDEF.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jpnStats.add(sldSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 496, 220, 50));

        lblATK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jpnStats.add(lblATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 290, 42, 54));

        sldHP.setMajorTickSpacing(36);
        sldHP.setMaximum(252);
        sldHP.setPaintLabels(true);
        sldHP.setValue(0);
        sldHP.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        sldHP.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sldHPMouseDragged(evt);
            }
        });
        jpnStats.add(sldHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 220, 220, 46));

        lblEVs.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        lblEVs.setForeground(new java.awt.Color(255, 255, 255));
        lblEVs.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEVs.setText("EVs: ");
        jpnStats.add(lblEVs, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 180, -1, 20));

        lblBaseHp.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblBaseHp.setText(" HP");
        jpnStats.add(lblBaseHp, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 20, 30));

        sldSPATK.setMajorTickSpacing(36);
        sldSPATK.setMaximum(252);
        sldSPATK.setPaintLabels(true);
        sldSPATK.setValue(0);
        sldSPATK.setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        jpnStats.add(sldSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 430, 220, 46));

        lblBaseAtk.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblBaseAtk.setText("ATK");
        jpnStats.add(lblBaseAtk, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 30, 30));

        lblBaseDef.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblBaseDef.setText("DEF");
        jpnStats.add(lblBaseDef, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 30, 30));

        lblBaseSpatk.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblBaseSpatk.setText("SPATK");
        jpnStats.add(lblBaseSpatk, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, 40, 30));

        lblBaseSPDef.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblBaseSPDef.setText("SPDEF");
        jpnStats.add(lblBaseSPDef, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, -1, 30));

        lblBaseSpd.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblBaseSpd.setText("SPD");
        jpnStats.add(lblBaseSpd, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 360, 30, 30));

        pgbHP.setMaximum(255);
        pgbHP.setString("");
        pgbHP.setStringPainted(true);
        jpnStats.add(pgbHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 100, 40));

        pgbATK.setMaximum(255);
        pgbATK.setString("");
        pgbATK.setStringPainted(true);
        jpnStats.add(pgbATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 100, 40));

        pgbDEF.setMaximum(255);
        pgbDEF.setString("");
        pgbDEF.setStringPainted(true);
        jpnStats.add(pgbDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 100, 40));

        pgbSPATK.setMaximum(255);
        pgbSPATK.setString("");
        pgbSPATK.setStringPainted(true);
        jpnStats.add(pgbSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 100, 40));

        pgbSPDEF.setMaximum(255);
        pgbSPDEF.setRequestFocusEnabled(false);
        pgbSPDEF.setString("");
        pgbSPDEF.setStringPainted(true);
        jpnStats.add(pgbSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 100, 40));

        pgbSPD.setMaximum(255);
        pgbSPD.setString("");
        pgbSPD.setStringPainted(true);
        jpnStats.add(pgbSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, 100, 40));
        jpnStats.add(lblFinalHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 220, 30, 46));
        jpnStats.add(lblFinalATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 290, 30, 46));
        jpnStats.add(lblFinalDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 360, 30, 46));
        jpnStats.add(lblFinalSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 430, 30, 46));
        jpnStats.add(lblFinalSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 500, 30, 46));
        jpnStats.add(lblFinalSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 570, 30, 46));

        lblIVs.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        lblIVs.setForeground(new java.awt.Color(255, 255, 255));
        lblIVs.setText("IVs");
        jpnStats.add(lblIVs, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 190, -1, -1));

        spnIVHP.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnIVHPStateChanged(evt);
            }
        });
        jpnStats.add(spnIVHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 220, 60, 46));

        spnIVATK.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnIVATKStateChanged(evt);
            }
        });
        jpnStats.add(spnIVATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 290, 60, 46));

        spnIVDEF.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnIVDEFStateChanged(evt);
            }
        });
        jpnStats.add(spnIVDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 360, 60, 46));

        spnIVSPATK.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnIVSPATKStateChanged(evt);
            }
        });
        jpnStats.add(spnIVSPATK, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 430, 60, 46));

        spnIVSPDEF.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnIVSPDEFStateChanged(evt);
            }
        });
        jpnStats.add(spnIVSPDEF, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 496, 60, 50));

        spnIVSPD.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnIVSPDStateChanged(evt);
            }
        });
        jpnStats.add(spnIVSPD, new org.netbeans.lib.awtextra.AbsoluteConstraints(784, 570, 60, 46));

        lblEVSum.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        lblEVSum.setText("508");
        jpnStats.add(lblEVSum, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 430, 40, 20));

        lblRemainingEvs.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        lblRemainingEvs.setText("Remaining EV's");
        jpnStats.add(lblRemainingEvs, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, -1, -1));

        lblSPDEv.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblSPDEv.setForeground(new java.awt.Color(255, 255, 255));
        lblSPDEv.setText("SPD:");
        jpnStats.add(lblSPDEv, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 560, -1, -1));

        lblHPEV.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblHPEV.setForeground(new java.awt.Color(255, 255, 255));
        lblHPEV.setText("HP:");
        jpnStats.add(lblHPEV, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, -1, -1));

        lblATKEV.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblATKEV.setForeground(new java.awt.Color(255, 255, 255));
        lblATKEV.setText("ATK:");
        jpnStats.add(lblATKEV, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 270, -1, -1));

        lblDEFEV.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblDEFEV.setForeground(new java.awt.Color(255, 255, 255));
        lblDEFEV.setText("DEF:");
        jpnStats.add(lblDEFEV, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, -1, -1));

        lblSPATKEV.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblSPATKEV.setForeground(new java.awt.Color(255, 255, 255));
        lblSPATKEV.setText("SPATK:");
        jpnStats.add(lblSPATKEV, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 410, -1, -1));

        lblSPDEFEV.setFont(new java.awt.Font("Eras Demi ITC", 0, 12)); // NOI18N
        lblSPDEFEV.setForeground(new java.awt.Color(255, 255, 255));
        lblSPDEFEV.setText("SPDEF:");
        jpnStats.add(lblSPDEFEV, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 480, -1, -1));

        lblPkdexImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/PokedexOpenEVs.jpg"))); // NOI18N
        lblPkdexImage.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jpnStats.add(lblPkdexImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 920, 700));

        jtpPokemonGeneration.addTab("EV's", jpnStats);

        SavedPokemon.setBackground(new java.awt.Color(255, 204, 255));
        SavedPokemon.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SavedPokemon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPokemonPaste.setAutoCreateRowSorter(true);
        tblPokemonPaste.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Icon", "Tera Type", "Ability", "HP", "ATK", "DEF", "SPATK", "SPDEF", "SPD", "Level", "Shiny"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPokemonPaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblPokemonPaste.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblPokemonPaste);

        SavedPokemon.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 880, 489));

        btnConsultar.setText("Refresh");
        btnConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        SavedPokemon.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 30, -1, -1));

        btnUpdateRow.setText("Retrieve Pokémon");
        btnUpdateRow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateRowActionPerformed(evt);
            }
        });
        SavedPokemon.add(btnUpdateRow, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, -1, -1));

        btnDeletePokepaste.setText("Delete Pokémon");
        btnDeletePokepaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeletePokepaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePokepasteActionPerformed(evt);
            }
        });
        SavedPokemon.add(btnDeletePokepaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        lblRecuperarPokemon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRecuperarPokemon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/RecuperarPokemon.png"))); // NOI18N
        SavedPokemon.add(lblRecuperarPokemon, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 40, 60));

        lblLogoConsultar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/logoConsultar.png"))); // NOI18N
        SavedPokemon.add(lblLogoConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, 40));

        PokeballDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/pokeballDelete.png"))); // NOI18N
        SavedPokemon.add(PokeballDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 40, 40));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/wp8813120.jpg"))); // NOI18N
        lblFondo.setMaximumSize(new java.awt.Dimension(3840, 2160));
        SavedPokemon.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(-150, -310, 2860, 1310));

        jtpPokemonGeneration.addTab("Saved Pokémon", SavedPokemon);

        jpnMovesTable.setBackground(new java.awt.Color(204, 255, 204));
        jpnMovesTable.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTableMoves.setText("Moves Pokedex");
        btnTableMoves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTableMovesActionPerformed(evt);
            }
        });
        jpnMovesTable.add(btnTableMoves, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, 200, 70));

        btnPokemonPokedex.setText("Pokémon Pokedex");
        btnPokemonPokedex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPokemonPokedexActionPerformed(evt);
            }
        });
        jpnMovesTable.add(btnPokemonPokedex, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 360, 200, 70));

        btnItemPokedex.setText("Item Pokedex");
        btnItemPokedex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemPokedexActionPerformed(evt);
            }
        });
        jpnMovesTable.add(btnItemPokedex, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 520, 200, 70));

        btnAbilityPokedex.setText("Ability Pokedex");
        btnAbilityPokedex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbilityPokedexActionPerformed(evt);
            }
        });
        jpnMovesTable.add(btnAbilityPokedex, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 520, 200, 70));

        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pokedex/rotomdexpng.png"))); // NOI18N
        jpnMovesTable.add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(-150, -200, 2000, 1120));

        jtpPokemonGeneration.addTab("Pokédex", jpnMovesTable);

        getContentPane().add(jtpPokemonGeneration, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 690));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbnFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnFemaleActionPerformed
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        gender.setId(2);
        gender.setName("Female");
    }//GEN-LAST:event_rbnFemaleActionPerformed

    private void rbnMaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbnMaleActionPerformed
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        gender.setId(1);
        gender.setName("Male");
    }//GEN-LAST:event_rbnMaleActionPerformed

    private void rdnGenderlessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdnGenderlessActionPerformed
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        gender.setId(3);
        gender.setName("Genderless");
    }//GEN-LAST:event_rdnGenderlessActionPerformed

    private void cboTeraTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTeraTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTeraTypeActionPerformed

    private void btnGeneratePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGeneratePokepasteActionPerformed
        // TODO add your handling code here:
        if (areMovesValid()) {
            SoundPlayer.optionSelected();
            String generatePoketext = generatePokepaste();
            txtPokepaste.setText(generatePoketext);
        } else if (isFirstMoveNull()) {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "No move has been selected. Please set moves for your Pokémon", "Insufficient Data", JOptionPane.ERROR_MESSAGE);
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "Review your selected moves. They cannot be repeated", "Attempted Registration of Duplicate Moves", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGeneratePokepasteActionPerformed

    private void cboSpeciesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSpeciesItemStateChanged
        // TODO add your handling code here:
        llenarCboAbility();
        setPokemonTypingLabels();
        lblPokemonSprite.setIcon(getPokemonSprite());
        //setFinalStatLabels();
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String selectedPokemonName = cboSpecies.getSelectedItem().toString();
            if (counter != 0) {
                playPokemonCry(selectedPokemonName);
            }
            for (Pokemon p : MainWindow.listaPokemon) {
                if (p.getName().equals(selectedPokemonName)) {
                    updateProgressBars(p);
                    //setFinalStatLabels();
                    break;  // You need to break here after updating the progress bars for the selected Pokemon
                }
            }
        }
    }//GEN-LAST:event_cboSpeciesItemStateChanged

    private void ckbShinyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbShinyActionPerformed
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        this.ckbShiny.isSelected();
    }//GEN-LAST:event_ckbShinyActionPerformed

    private void cboNatureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNatureItemStateChanged
        // TODO add your handling code here:
        if (counter > 1) {
            SoundPlayer.optionSelected();
            setFinalStatLabels();
        }


    }//GEN-LAST:event_cboNatureItemStateChanged

    private void btnSavePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePokepasteActionPerformed
        // TODO add your handling code here:
        if (areMovesValid()) {
            String pokepaste = generatePokepaste();
            try {
                PokemonFileUtility.writePokemonData(pokepaste);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred. Unable to store your Pokémon. Please review your data and try again", "Pokémon Entry", JOptionPane.ERROR_MESSAGE);
                SoundPlayer.wrongBuzzer();
            }
            SoundPlayer.playSave();
            JOptionPane.showMessageDialog(this, "Your Pokémon has been stored successfully", "Pokémon Entry", JOptionPane.INFORMATION_MESSAGE);
            refresh();
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "Review your selected moves. They cannot be repeated", "Attempted Registration of Duplicate Moves", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnSavePokepasteActionPerformed

    private void spnLevelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnLevelStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        setFinalStatLabels();
    }//GEN-LAST:event_spnLevelStateChanged

    private void sldHPMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sldHPMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_sldHPMouseDragged

    private void btnImportPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportPasteActionPerformed
        // TODO add your handling code here:
        if (!txtPokepaste.getText().isEmpty()) {
            SoundPlayer.optionSelected();
            resetFields();
            importPokepaste(txtPokepaste.getText());
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "Please insert a Pokepaste", "No Pokepaste Inserted", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnImportPasteActionPerformed

    private void btnAbilityDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbilityDescActionPerformed
        // TODO add your handling code here:

        String abilityDescription = getAbilityDescription();
        if (!"".equals(abilityDescription)) {
            SoundPlayer.menuOpened();
            JOptionPane.showMessageDialog(this, abilityDescription, "Ability Description", JOptionPane.INFORMATION_MESSAGE);
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "No ability has been selected", "Ability Description", JOptionPane.ERROR_MESSAGE);
        }
        // Show a pop-up message with the ability description

    }//GEN-LAST:event_btnAbilityDescActionPerformed

    private void btnMove1DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove1DescActionPerformed
        // TODO add your handling code here:
        if (getMoveDescription(cboMove1)) {
            SoundPlayer.optionSelected();
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "No move has been selected", "Move Description", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnMove1DescActionPerformed

    private void btnMove2DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove2DescActionPerformed
        // TODO add your handling code here:
        if (getMoveDescription(cboMove2)) {
            SoundPlayer.optionSelected();
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "No move has been selected", "Move Description", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMove2DescActionPerformed

    private void btnMove3DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove3DescActionPerformed
        // TODO add your handling code here:
        if (getMoveDescription(cboMove3)) {
            SoundPlayer.optionSelected();
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "No move has been selected", "Move Description", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMove3DescActionPerformed

    private void btnMove4DescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMove4DescActionPerformed
        // TODO add your handling code here:
        if (getMoveDescription(cboMove4)) {
            SoundPlayer.optionSelected();
        } else {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "No move has been selected", "Move Description", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMove4DescActionPerformed

    private void btnItemDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDescActionPerformed
        // TODO add your handling code here:
        SoundPlayer.menuOpened();
        String itemDesc = getItemDescription();
        // Show a pop-up message with the ability description
        JOptionPane.showMessageDialog(this, itemDesc, "Item Description", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnItemDescActionPerformed

    private void btnResetFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetFieldsActionPerformed
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        if (!this.btnSavePokepaste.isVisible()) {
            this.btnUpdatePokepaste.setVisible(false);
            this.btnSavePokepaste.setVisible(true);
        }
        resetFields();
    }//GEN-LAST:event_btnResetFieldsActionPerformed

    private void btnUpdatePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePokepasteActionPerformed
        // TODO add your handling code here:
        PokemonPaste original = matchPokepaste(toUpdatePkmnId);
        String newPokemon = generatePokepaste();
        String originalMon = generatePokepaste(original);

        try {
            PokemonFileUtility.updatePokemonData(originalMon, newPokemon);
            SoundPlayer.playSave();
            JOptionPane.showMessageDialog(this, "Your Pokémon has been modified successfully", "Pokémon Modification", JOptionPane.INFORMATION_MESSAGE);
            lblLogoUpdate.setVisible(false);
            btnUpdatePokepaste.setVisible(false);
            btnSavePokepaste.setVisible(true);
            lblLogoSave.setVisible(true);
            llenarTabla();
        } catch (HeadlessException e) {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "An error occurred. Unable to modify your Pokémon. Please review your data and try again", "Pokémon Modification", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnUpdatePokepasteActionPerformed

    private void btnDeletePokepasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePokepasteActionPerformed
        // TODO add your handling code here:

        toUpdatePkmnId = getSelectedRowFromPokepaste();
        PokemonPaste p = matchPokepaste(toUpdatePkmnId);
        String generatePokepaste = generatePokepaste(p);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this Pokémon? This action cannot be undone.",
                "Deletion Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.YES_OPTION) {
            try {
                PokemonFileUtility.deletePokemonData(generatePokepaste);
                SoundPlayer.playSave();
                JOptionPane.showMessageDialog(this, "Your Pokémon has been deleted successfully", "Pokémon Deletion", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } catch (HeadlessException e) {
                SoundPlayer.wrongBuzzer();
                JOptionPane.showMessageDialog(this, "An error occurred. Unable to delete your Pokémon. Please select it and try again", "Pokémon Deletion", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            SoundPlayer.menuClosed();
        }
        //Conexion.getInstance().releaseConnection(connection);
        // Use the itemList as needed


    }//GEN-LAST:event_btnDeletePokepasteActionPerformed

    private void btnUpdateRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateRowActionPerformed

        // se consigue la id del pokemon en pokemon_pokepaste
        toUpdatePkmnId = getSelectedRowFromPokepaste();
        PokemonPaste p = matchPokepaste(toUpdatePkmnId);

        // crear objeto pokepaste desde los datos de la base de datos
//            PokemonPaste p = ip.getPokepasteAtId(connection, toUpdatePkmnId);
        // crear texto pokepaste a partir del objeto pokepaste anterior
        String pokepaste = generatePokepaste(p);
        if (pokepaste.equals("")) {
            try {
                throw new Exception("Pokepaste is empty");
            } catch (Exception ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // imprimir al textfield
        txtPokepaste.setText(pokepaste);
        // resetear todo
        resetFields();
        // set valores del pokepaste en los componentes del programa
        importPokepaste(pokepaste);
        btnSavePokepaste.setVisible(false);
        lblLogoSave.setVisible(false);
        btnUpdatePokepaste.setVisible(true);
        lblLogoUpdate.setVisible(true);
        //SoundPlayer.playSave();
        JOptionPane.showMessageDialog(this, "Pokémon retrieved successfully. You may modify or alter it if you wish", "Pokémon retrieved", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_btnUpdateRowActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
        refresh();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnTableMovesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTableMovesActionPerformed
        // TODO add your handling code here:
        if (movesTable == null) {
            movesTable = new MovesTable();
        }
        SoundPlayer.menuOpened();
        movesTable.setVisible(true);
    }//GEN-LAST:event_btnTableMovesActionPerformed

    private void btnPokemonPokedexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPokemonPokedexActionPerformed

        if (PokedexClose.pokemonTable == null) {
            PokedexClose.pokemonTable = new PokemonTable();
        }
        SoundPlayer.menuOpened();
        PokedexClose.pokemonTable.setVisible(true);
    }//GEN-LAST:event_btnPokemonPokedexActionPerformed

    private void btnItemPokedexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemPokedexActionPerformed
        // TODO add your handling code here:
        if (itemsTable == null) {
            itemsTable = new ItemsTable();
        }
        SoundPlayer.menuOpened();
        itemsTable.setVisible(true);
    }//GEN-LAST:event_btnItemPokedexActionPerformed

    private void btnAbilityPokedexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbilityPokedexActionPerformed
        // TODO add your handling code here:
        if (abilityTable == null) {
            abilityTable = new AbilityTable();
        }
        SoundPlayer.menuOpened();
        abilityTable.setVisible(true);
    }//GEN-LAST:event_btnAbilityPokedexActionPerformed

    private void jtpPokemonGenerationMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtpPokemonGenerationMouseEntered
        // TODO add your handling code here:
        if (PokedexClose.pokemonTable == null) {
            PokedexClose.pokemonTable = new PokemonTable();
        }

    }//GEN-LAST:event_jtpPokemonGenerationMouseEntered

    private void cboAbilityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAbilityItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboAbilityItemStateChanged

    private void cboMove1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMove1ItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboMove1ItemStateChanged

    private void cboMove2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMove2ItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboMove2ItemStateChanged

    private void cboMove3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMove3ItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboMove3ItemStateChanged

    private void cboMove4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMove4ItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboMove4ItemStateChanged

    private void cboItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboItemItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboItemItemStateChanged

    private void cboTeraTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTeraTypeItemStateChanged
        // TODO add your handling code here:
//        SoundPlayer.optionSelected();
    }//GEN-LAST:event_cboTeraTypeItemStateChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosed

    private void spnIVHPStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnIVHPStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
    }//GEN-LAST:event_spnIVHPStateChanged

    private void spnIVATKStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnIVATKStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();

    }//GEN-LAST:event_spnIVATKStateChanged

    private void spnIVDEFStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnIVDEFStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
    }//GEN-LAST:event_spnIVDEFStateChanged

    private void spnIVSPATKStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnIVSPATKStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
    }//GEN-LAST:event_spnIVSPATKStateChanged

    private void spnIVSPDEFStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnIVSPDEFStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
    }//GEN-LAST:event_spnIVSPDEFStateChanged

    private void spnIVSPDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnIVSPDStateChanged
        // TODO add your handling code here:
        SoundPlayer.optionSelected();
    }//GEN-LAST:event_spnIVSPDStateChanged

    private void btnPokemonCryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPokemonCryActionPerformed
        // TODO add your handling code here:
        String selectedPokemonName = cboSpecies.getSelectedItem().toString();
        playPokemonCry(selectedPokemonName);
    }//GEN-LAST:event_btnPokemonCryActionPerformed

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        // TODO add your handling code here:
        //SoundPlayer.menuClosed();
    }//GEN-LAST:event_formWindowStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel PokeballDelete;
    private javax.swing.JPanel SavedPokemon;
    private javax.swing.ButtonGroup bgpGender;
    private javax.swing.JButton btnAbilityDesc;
    private javax.swing.JButton btnAbilityPokedex;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnDeletePokepaste;
    private javax.swing.JButton btnGeneratePokepaste;
    private javax.swing.JButton btnImportPaste;
    private javax.swing.JButton btnItemDesc;
    private javax.swing.JButton btnItemPokedex;
    private javax.swing.JButton btnMove1Desc;
    private javax.swing.JButton btnMove2Desc;
    private javax.swing.JButton btnMove3Desc;
    private javax.swing.JButton btnMove4Desc;
    private javax.swing.JButton btnPokemonCry;
    private javax.swing.JButton btnPokemonPokedex;
    private javax.swing.JButton btnResetFields;
    private javax.swing.JButton btnSavePokepaste;
    private javax.swing.JButton btnTableMoves;
    private javax.swing.JButton btnUpdatePokepaste;
    private javax.swing.JButton btnUpdateRow;
    private javax.swing.JComboBox<String> cboAbility;
    private javax.swing.JComboBox<String> cboItem;
    private javax.swing.JComboBox<String> cboMove1;
    private javax.swing.JComboBox<String> cboMove2;
    private javax.swing.JComboBox<String> cboMove3;
    private javax.swing.JComboBox<String> cboMove4;
    private javax.swing.JComboBox<String> cboNature;
    private javax.swing.JComboBox<String> cboSpecies;
    private javax.swing.JComboBox<String> cboTeraType;
    private javax.swing.JCheckBox ckbShiny;
    private javax.swing.JPanel jPanelPokemon;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JPanel jpnMovesTable;
    private javax.swing.JPanel jpnStats;
    private javax.swing.JTabbedPane jtpPokemonGeneration;
    private javax.swing.JLabel lblATK;
    private javax.swing.JLabel lblATKEV;
    private javax.swing.JLabel lblAbility;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblBaseAtk;
    private javax.swing.JLabel lblBaseDef;
    private javax.swing.JLabel lblBaseHp;
    private javax.swing.JLabel lblBaseSPDef;
    private javax.swing.JLabel lblBaseSpatk;
    private javax.swing.JLabel lblBaseSpd;
    private javax.swing.JLabel lblDEF;
    private javax.swing.JLabel lblDEFEV;
    private javax.swing.JLabel lblEVSum;
    private javax.swing.JLabel lblEVs;
    private javax.swing.JLabel lblFinalATK;
    private javax.swing.JLabel lblFinalDEF;
    private javax.swing.JLabel lblFinalHP;
    private javax.swing.JLabel lblFinalSPATK;
    private javax.swing.JLabel lblFinalSPD;
    private javax.swing.JLabel lblFinalSPDEF;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblHP;
    private javax.swing.JLabel lblHPEV;
    private javax.swing.JLabel lblIVs;
    private javax.swing.JLabel lblItems;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JLabel lblLogoConsultar;
    private javax.swing.JLabel lblLogoImport;
    private javax.swing.JLabel lblLogoReset;
    private javax.swing.JLabel lblLogoSave;
    private javax.swing.JLabel lblLogoUpdate;
    private javax.swing.JLabel lblMove1;
    private javax.swing.JLabel lblMove2;
    private javax.swing.JLabel lblMove3;
    private javax.swing.JLabel lblMove4;
    private javax.swing.JLabel lblNature;
    private javax.swing.JLabel lblNickname;
    private javax.swing.JLabel lblPkdexImage;
    private javax.swing.JLabel lblPokemonImage;
    private javax.swing.JLabel lblPokemonSprite;
    private javax.swing.JLabel lblPokepaste;
    private javax.swing.JLabel lblRecuperarPokemon;
    private javax.swing.JLabel lblRemainingEvs;
    private javax.swing.JLabel lblSPATK;
    private javax.swing.JLabel lblSPATKEV;
    private javax.swing.JLabel lblSPD;
    private javax.swing.JLabel lblSPDEF;
    private javax.swing.JLabel lblSPDEFEV;
    private javax.swing.JLabel lblSPDEv;
    private javax.swing.JLabel lblSpecies;
    private javax.swing.JLabel lblTeraType;
    private javax.swing.JLabel lblType1;
    private javax.swing.JLabel lblType2;
    private javax.swing.JLabel lbllogoGenerate;
    private javax.swing.JProgressBar pgbATK;
    private javax.swing.JProgressBar pgbDEF;
    private javax.swing.JProgressBar pgbHP;
    private javax.swing.JProgressBar pgbSPATK;
    private javax.swing.JProgressBar pgbSPD;
    private javax.swing.JProgressBar pgbSPDEF;
    private javax.swing.JRadioButton rbnFemale;
    private javax.swing.JRadioButton rbnMale;
    private javax.swing.JRadioButton rdnGenderless;
    private javax.swing.JSlider sldATK;
    private javax.swing.JSlider sldDEF;
    private javax.swing.JSlider sldHP;
    private javax.swing.JSlider sldSPATK;
    private javax.swing.JSlider sldSPD;
    private javax.swing.JSlider sldSPDEF;
    private javax.swing.JSpinner spnIVATK;
    private javax.swing.JSpinner spnIVDEF;
    private javax.swing.JSpinner spnIVHP;
    private javax.swing.JSpinner spnIVSPATK;
    private javax.swing.JSpinner spnIVSPD;
    private javax.swing.JSpinner spnIVSPDEF;
    private javax.swing.JSpinner spnLevel;
    private javax.swing.JTable tblPokemonPaste;
    private javax.swing.JTextField txtNickname;
    private javax.swing.JTextArea txtPokepaste;
    // End of variables declaration//GEN-END:variables

    /**
     * Este método permite añadir las limitaciones a los sliders de EVs. Sea el
     * mouse clickeado o el knob de los sliders arrastrados, la suma de los
     * sliders no será nunca mayor a 508. Además, al ser el mouse soltado, se
     * asegura que el valor de EVs seleccionado para cada estadística siempre
     * será múltiplo de 4.
     *
     * @param statSliders
     */
    public static void addLimit(JSlider[] statSliders) {
        SliderMouseListener sliderMouseListener = new SliderMouseListener(statSliders);
        for (JSlider statSlider : statSliders) {
            statSlider.addMouseListener(sliderMouseListener);
            statSlider.addMouseMotionListener(sliderMouseListener);
        }
    }

    /**
     * Este método usa los datos de la tabla Item para llenar su combobox
     */
    private void llenarCboItem() {
        // Definición de variables para rescatar datos
        String nombre;
        DefaultComboBoxModel<String> cboModelo = (DefaultComboBoxModel<String>) this.cboItem.getModel();
        for (Item i : MainWindow.listaItems) {
            nombre = i.getName();
            cboModelo.addElement(nombre);
        }
    }

    /**
     * Este método usa los datos de la tabla pokémon para llenar su combobox
     */
    private void llenarCboSpecies() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel<String> cboModelo = (DefaultComboBoxModel<String>) this.cboSpecies.getModel();
        for (Pokemon p : MainWindow.listaPokemon) {
            nombre = p.getName();
            cboModelo.addElement(nombre);
        }
    }

    /**
     * Este método sirve para llenar un combobox con todos los movimientos de
     * Pokémon
     *
     * @param cbo un combobox
     */
    private void obtenerCBOMoves(JComboBox<String> cbo) {
        // Definition of variables to retrieve data
        String nombre = "";
        DefaultComboBoxModel<String> cboModelo = (DefaultComboBoxModel<String>) cbo.getModel();

        cboModelo.addElement(nombre);

        for (Move m : MainWindow.listaMove) {
            nombre = m.getName();
            cboModelo.addElement(nombre);
        }
    }

    /**
     * Este método itera sobre todos los combobox de movimientos para pasarles
     * todos los movimientos.
     *
     * @param cboxes lista que almacena todos los combobox
     */
    @SuppressWarnings("unchecked")
    private void llenarCboMoves(JComboBox<String>[] cboxes) {
        for (JComboBox cboxe : cboxes) {
            obtenerCBOMoves(cboxe);
        }
    }

    /**
     * Este método llena el combobox de habilidades con todas las habilidades.
     */
    private void llenarCboAbility() {
        //definicion variables para rescatar datos
        Pokemon p = matchPokemon();
        int pokemonId = p.getId();
        List<Ability> listAbility = MainWindow.listPokemonAbility.get(pokemonId);
        String nombre;
        DefaultComboBoxModel<String> cboModelo = (DefaultComboBoxModel<String>) this.cboAbility.getModel();
        cboModelo.removeAllElements();
        try {
            for (Ability a : listAbility) {
                nombre = a.getName();
                cboModelo.addElement(nombre);
            }
        } catch (Exception e) {
            System.out.println("No ability for " + p.getName());
        }

    }

    /**
     * Este método llena el combobox de tipos de Pokémon con todos los tipos.
     */
    private void llenarPkmnType() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel<String> cboModelo = (DefaultComboBoxModel<String>) this.cboTeraType.getModel();
        for (PokemonType p : MainWindow.listaPkmnType) {
            nombre = p.getName();
            cboModelo.addElement(nombre);
        }
    }

    /**
     * Este método llena el combobox de naturalezas con todas las naturalezas.
     */
    private void llenarNature() {
        //definicion variables para rescatar datos
        String nombre;
        DefaultComboBoxModel<String> cboModelo = (DefaultComboBoxModel<String>) this.cboNature.getModel();
        for (Nature n : MainWindow.listaNature) {
            nombre = n.getName();
            cboModelo.addElement(nombre);
        }
    }

    private void updateProgressBars(Pokemon p) {
        // variables a reutilizar
        int hp = p.getHp();
        int atk = p.getAtk();
        int def = p.getDef();
        int spatk = p.getSpatk();
        int spdef = p.getSpdef();
        int spd = p.getSpd();
        pgbHP.setValue(hp);
        pgbHP.setString(String.valueOf(hp));
        pgbATK.setValue(atk);
        pgbATK.setString(String.valueOf(atk));
        pgbDEF.setValue(def);
        pgbDEF.setString(String.valueOf(def));
        pgbSPATK.setValue(spatk);
        pgbSPATK.setString(String.valueOf(spatk));
        pgbSPDEF.setValue(spdef);
        pgbSPDEF.setString(String.valueOf(spdef));
        pgbSPD.setValue(spd);
        pgbSPD.setString(String.valueOf(spd));
    }

    /**
     * Este método calcula y publica en los FinalStatLabels las estadísticas
     * reales y finales de los Pokémon. Primero se calculan sin tomar en cuenta
     * las naturalezas, y luego se les aplica el modificador de las naturalezas
     * definidos en la IPokemonCalculator
     */
    private void setFinalStatLabels() {
        int hp;
        int atk;
        int def;
        int spatk;
        int spdef;
        int spd;
        int level = (int) spnLevel.getValue();
        int natureID = matchNature().getId();

        hp = calculateHpStat(pgbHP.getValue(), (int) spnIVHP.getValue(), sldHP.getValue(), level);
        atk = calculateModifiedStat(pgbATK.getValue(), (int) spnIVATK.getValue(), sldATK.getValue(), level);
        def = calculateModifiedStat(pgbDEF.getValue(), (int) spnIVDEF.getValue(), sldDEF.getValue(), level);
        spatk = calculateModifiedStat(pgbSPATK.getValue(), (int) spnIVSPATK.getValue(), sldSPATK.getValue(), level);
        spdef = calculateModifiedStat(pgbSPDEF.getValue(), (int) spnIVSPDEF.getValue(), sldSPDEF.getValue(), level);
        spd = calculateModifiedStat(pgbSPD.getValue(), (int) spnIVSPD.getValue(), sldSPD.getValue(), level);

        int[] newStats = modifyStatNature(natureID, atk, def, spatk, spdef, spd);

        lblFinalHP.setText(String.valueOf(hp));
        lblFinalATK.setText(String.valueOf(newStats[0]));
        lblFinalDEF.setText(String.valueOf(newStats[1]));
        lblFinalSPATK.setText(String.valueOf(newStats[2]));
        lblFinalSPDEF.setText(String.valueOf(newStats[3]));
        lblFinalSPD.setText(String.valueOf(newStats[4]));
    }

    private int[] setFinalStatLabels(PokemonPaste p) {
        int hp;
        int atk;
        int def;
        int spatk;
        int spdef;
        int spd;
        int level = p.getLevel();
        int natureID = p.getNature().getId();

        hp = calculateHpStat(p.getPokemon().getHp(), p.getHpIv(), p.getHpEv(), level);
        atk = calculateModifiedStat(p.getPokemon().getAtk(), p.getAtkIv(), p.getAtkEv(), level);
        def = calculateModifiedStat(p.getPokemon().getDef(), p.getDefIv(), p.getDefEv(), level);
        spatk = calculateModifiedStat(p.getPokemon().getSpatk(), p.getSpatkIv(), p.getSpatkEv(), level);
        spdef = calculateModifiedStat(p.getPokemon().getSpdef(), p.getSpdefIv(), p.getSpdefEv(), level);
        spd = calculateModifiedStat(p.getPokemon().getSpd(), p.getSpdIv(), p.getSpdEv(), level);

        int[] newStats = modifyStatNature(natureID, atk, def, spatk, spdef, spd);
        int[] finalStats = {hp, newStats[0], newStats[1], newStats[2], newStats[3], newStats[4]};
        return finalStats;
    }

    /**
     * Este método modifica las estadísticas de los Pokémon dependiendo en sus
     * naturalezas.
     *
     * @param natureID el id de la naturaleza en la base de datos.
     * @param atk el stat de ataque
     * @param def el stat de defensa
     * @param spatk el stat de ataque especial
     * @param spdef el stat de defensa especial
     * @param spd el stat de velocidad
     * @return devuelve la lista de stats ya modificados tras pasar por el
     * switch. Nota: el stat de HP (puntos de vida) no es afectado por ninguna
     * naturaleza.
     */
    private int[] modifyStatNature(int natureID, int atk, int def, int spatk, int spdef, int spd) {
        int[] effects = {atk, def, spatk, spdef, spd};
        double h = HINDERING_NATURE;
        double b = BENEFICIAL_NATURE;

        switch (natureID) {
            case 1 -> {
                // Hardy
                return effects;
            }

            case 2 -> {
                // Lonely
                effects[0] = (int) (effects[0] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;
            }

            case 3 -> {
                // Brave
                effects[0] = (int) (effects[0] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;
            }

            case 4 -> {
                // Adamant
                effects[0] = (int) (effects[0] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;
            }

            case 5 -> {
                // Naughty
                effects[0] = (int) (effects[0] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;
            }

            case 6 -> {
                // Bold
                effects[1] = (int) (effects[1] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;
            }

            case 7 -> {
                // Docile
                return effects;
            }

            case 8 -> {
                // Relaxed
                effects[1] = (int) (effects[1] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;
            }

            case 9 -> {
                // Impish
                effects[1] = (int) (effects[1] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;
            }

            case 10 -> {
                // Lax
                effects[1] = (int) (effects[1] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;
            }

            case 11 -> {
                // Timid
                effects[4] = (int) (effects[4] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;
            }

            case 12 -> {
                // Hasty
                effects[4] = (int) (effects[4] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;
            }

            case 13 -> {
                // Serious
                return effects;
            }

            case 14 -> {
                // Jolly
                effects[4] = (int) (effects[4] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;
            }

            case 15 -> {
                // Naive
                effects[4] = (int) (effects[4] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;
            }

            case 16 -> {
                // Modest
                effects[2] = (int) (effects[2] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;
            }

            case 17 -> {
                // Mild
                effects[2] = (int) (effects[2] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;
            }

            case 18 -> {
                // Quiet
                effects[2] = (int) (effects[2] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;
            }

            case 19 -> {
                // Bashful
                return effects;
            }

            case 20 -> {
                // Rash
                effects[2] = (int) (effects[2] * b);
                effects[3] = (int) (effects[3] * h);
                return effects;
            }

            case 21 -> {
                // Calm
                effects[3] = (int) (effects[3] * b);
                effects[0] = (int) (effects[0] * h);
                return effects;
            }

            case 22 -> {
                // Gentle
                effects[3] = (int) (effects[3] * b);
                effects[1] = (int) (effects[1] * h);
                return effects;
            }

            case 23 -> {
                // Sassy
                effects[3] = (int) (effects[3] * b);
                effects[4] = (int) (effects[4] * h);
                return effects;
            }

            case 24 -> {
                // Careful
                effects[3] = (int) (effects[3] * b);
                effects[2] = (int) (effects[2] * h);
                return effects;
            }

            case 25 -> {
                // Quirky
                return effects;
            }
            default ->
                throw new AssertionError("Invalid nature ID: " + natureID);
        }
    }

    /**
     * Método que al ser invocado actualiza un stat entero y sus JLabel basado
     * en el cálculo del método calculateModifiedStat(int baseStat, int IV, int
     * EV, int level, double natureValue) de la interfaz.
     *
     * @param listaPokemon
     * @param cbo
     * @param finalStat
     * @param iv
     * @param slider
     * @param level
     *
     */
//    public void updateStats(ArrayList<Pokemon> listaPokemon, JComboBox cbo, JLabel finalStat, JSpinner iv, JSlider slider, int level){
//        for (Pokemon p : listaPokemon) {
//            if (p.getName().equals(cbo.getSelectedItem().toString())) {   
//                finalStat.setText(String.valueOf(calculateModifiedStat(p.getHp(), (int) iv.getValue(), slider.getValue(), level)));
//                break;  // You need to break here after updating the progress bars for the selected Pokemon
//            }
//        }
//    }
    /**
     * Método de la interfaz que permite calcular las estadísticas reales de un
     * Pokémon basado en las siguientes variables.
     *
     * @param baseStat las estadísticas base de un Pokémon para un stat {ATK,
     * DEF, SPATK, SPDEF, SPD}.
     * @param IV los valores individuales de un Pokémon (genes) para un stat.
     * @param EV los valores de esfuerzo de un Pokémon para un stat.
     * @param level el nivel del Pokémon.
     * @return
     */
    @Override
    public int calculateModifiedStat(int baseStat, int IV, int EV, int level) {
        return (int) Math.floor((Math.floor(2 * baseStat + IV + Math.floor(EV / 4)) * level / 100 + 5));
    }

    /**
     * Método de la interfaz que permite calcular las estadísticas reales (HP)
     * de un Pokémon basado en las siguientes variables.
     *
     * @param baseStat las estadísticas base de un Pokémon para un stat {HP}.
     * @param IV los valores individuales de un Pokémon (genes) para un stat.
     * @param EV los valores de esfuerzo de un Pokémon para un stat.
     * @param level el nivel del Pokémon.
     * @return
     */
    @Override
    public int calculateHpStat(int baseStat, int IV, int EV, int level) {
        if (!cboSpecies.getSelectedItem().toString().equals("Shedinja")) {
            return (int) Math.floor((2 * baseStat + IV + Math.floor(EV / 4)) * level / 100 + level + 10);
        } else {
            return 1;
        }
    }

    /**
     * Este método inicializa los ChangeListeners y aplica los modelos definidos
     * a cada spinner de IVs.
     *
     * @param spinners la lista de spinners de IVs.
     */
    private void initializeSpinners(JSpinner[] spinners) {
        for (JSpinner spinner : spinners) {
            CustomChangeListener customChangeListener = new CustomChangeListener();
            SpinnerNumberModel model = new SpinnerNumberModel(31, 0, 31, 1);
            spinner.addChangeListener(customChangeListener);
            spinner.setModel(model);
        }
    }

    /**
     * Este método calcula y publica en los finalStatSliders las estadísticas
     * finales de los Pokémon, basadas en sus características base, nivel, EVs,
     * IVs y naturaleza. Estos serán actualizados cada vez que se interactúe con
     * los sliders de EVs, los spinners de IVs y los movimientos? eso es raro
     */
    private void initializeEventListeners() {
        // Sliders
        for (JSlider slider : listStatSliders) {
            slider.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    setFinalStatLabels();
                }

                @Override
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    setFinalStatLabels();
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    setFinalStatLabels();
                }
            });
        }

        // ComboBoxes
        for (JComboBox cbo : listMoveCBOXS) {
            cbo.addItemListener((java.awt.event.ItemEvent evt) -> {
                setFinalStatLabels();
            });
        }

        // Spinners
        for (JSpinner spinner : listIvSPN) {
            spinner.addChangeListener((javax.swing.event.ChangeEvent evt) -> {
                setFinalStatLabels();
            });
        }
    }

    /**
     * Este método halla el objeto naturaleza que está seleccionado en el
     * combobox.
     *
     * @return el objeto naturaleza en el combobox.
     */
    private Nature matchNature() {
        Nature nature = null;
        for (Nature n : listaNature) {
            if (n.getName().equals(cboNature.getSelectedItem().toString())) {
                nature = n;
                return nature;
            }
        }
        return nature;
    }

    /**
     * Este método halla el objeto pokemon que está seleccionado en el combobox.
     *
     * @return el objeto pokemon en el combobox.
     */
    private Pokemon matchPokemon() {
        Pokemon pokemon = null;
        for (Pokemon p : listaPokemon) {
            if (p.getName().equals(cboSpecies.getSelectedItem().toString())) {
                pokemon = p;
                return pokemon;
            }
        }
        return pokemon;
    }

    /**
     * Este método halla el objeto Item que está seleccionado en el combobox.
     *
     * @return el objeto item en el combobox.
     */
    private Item matchItem() {
        Item item = null;
        for (Item i : listaItems) {
            if (i.getName().equals(cboItem.getSelectedItem().toString())) {
                item = i;
                return item;
            }
        }
        return item;
    }

    /**
     * Este método halla el objeto tipo pokemon que está seleccionado en el
     * combobox.
     *
     * @return el objeto tipo pokemon en el combobox.
     */
    private PokemonType matchTeraType() {
        PokemonType type = null;
        for (PokemonType p : listaPkmnType) {
            if (p.getName().equals(cboTeraType.getSelectedItem().toString())) {
                type = p;
                return type;
            }
        }
        return type;
    }

    /**
     * Este método halla el objeto tipo ability que está seleccionado en el
     * combobox.
     *
     * @return el objeto tipo ability en el combobox.
     */
    private Ability matchAbility() {
        Ability ability = null;
        for (Ability a : listaAbility) {
            if (a.getName().equals(cboAbility.getSelectedItem().toString())) {
                ability = a;
                return ability;
            }
        }
        return ability;
    }

    /**
     * Este método halla el objeto move que está seleccionado en el combobox.
     *
     * @return el objeto move en el combobox.
     */
    private Move matchMove(JComboBox cboMove) {
        Move Move = null;
        for (Move m : listaMove) {
            if (m.getName().equals(cboMove.getSelectedItem().toString())) {
                Move = m;
                return Move;
            }
        }
        return Move;
    }

    /**
     * Este método recoge todos los objetos y atributos seleccionados en la
     * interfaz gráfica para armar un objeto PokemonPaste, que contiene los
     * datos para poblar la tabla pokemon_paste en la base de datos, que es la
     * única en la que el usuario puede insertar, borrar y actualizar datos.
     *
     * @return un objeto PokemonPaste
     */
    private PokemonPaste buildPokepaste() {
        PokemonPaste pkpaste = new PokemonPaste();
        pkpaste.setPokemon(matchPokemon());
        pkpaste.setItem(matchItem());
        pkpaste.setNickname(txtNickname.getText());
        pkpaste.setLevel((int) spnLevel.getValue());
        pkpaste.setGender(gender);
        pkpaste.setIsShiny(ckbShiny.isSelected());
        pkpaste.setTeraType(matchTeraType());
        pkpaste.setAbility(matchAbility());

        pkpaste.setMove1(matchMove(cboMove1));
        pkpaste.setMove2(matchMove(cboMove2));
        pkpaste.setMove3(matchMove(cboMove3));
        pkpaste.setMove4(matchMove(cboMove4));

        pkpaste.setHpEv(sldHP.getValue());
        pkpaste.setAtkEv(sldATK.getValue());
        pkpaste.setDefEv(sldDEF.getValue());
        pkpaste.setSpatkEv(sldSPATK.getValue());
        pkpaste.setSpdefEv(sldSPDEF.getValue());
        pkpaste.setSpdEv(sldSPD.getValue());

        pkpaste.setHpIv((int) spnIVHP.getValue());
        pkpaste.setAtkIv((int) spnIVATK.getValue());
        pkpaste.setDefIv((int) spnIVDEF.getValue());
        pkpaste.setSpatkIv((int) spnIVSPATK.getValue());
        pkpaste.setSpdefIv((int) spnIVSPDEF.getValue());
        pkpaste.setSpdIv((int) spnIVSPD.getValue());
        pkpaste.setNature(matchNature());
        pkpaste.setHp(Integer.parseInt(lblFinalHP.getText()));
        pkpaste.setAtk(Integer.parseInt(lblFinalATK.getText()));
        pkpaste.setDef(Integer.parseInt(lblFinalDEF.getText()));
        pkpaste.setSpatk(Integer.parseInt(lblFinalSPATK.getText()));
        pkpaste.setSpdef(Integer.parseInt(lblFinalSPDEF.getText()));
        pkpaste.setSpd(Integer.parseInt(lblFinalSPD.getText()));
        return pkpaste;
    }

    private Pokemon matchPokemon(String name) {
        for (Pokemon pokemon : listaPokemon) {
            if (pokemon.getName().equals(name)) {
                return pokemon;
            }
        }
        return null;
    }

    private Item matchItem(String name) {
        for (Item i : listaItems) {
            if (i.getName().equals(name)) {
                return i;
            }
        }
        return null;
    }

    private Ability matchAbility(String name) {
        for (Ability a : listaAbility) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    private Nature matchNature(String name) {
        for (Nature n : listaNature) {
            if (n.getName().equals(name)) {
                return n;
            }
        }
        return null;
    }

    private PokemonType matchTeraType(String name) {
        for (PokemonType p : listaPkmnType) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private ArrayList<Move> matchMove(List<String> name) {
        ArrayList<Move> moves = new ArrayList<>();

        for (String s : name) {
            for (Move move : listaMove) {
                if (s.equals(move.getName())) {
                    moves.add(move);
                    break;
                }
            }
        }

        return moves;
    }

    private PokemonPaste buildPokepaste(Object[] p) {
        PokemonPaste pkpaste = new PokemonPaste();

        pkpaste.setGender(gender);
        pkpaste.setPokemon(matchPokemon(String.valueOf(p[0])));
        pkpaste.setItem(matchItem(String.valueOf(p[2])));
        pkpaste.setNickname(String.valueOf(p[1]));
        pkpaste.setLevel((int) p[5]);

        pkpaste.setIsShiny((boolean) p[6]);
        pkpaste.setTeraType(matchTeraType(String.valueOf(p[7])));
        pkpaste.setAbility(matchAbility(String.valueOf(p[4])));
        pkpaste.setNature(matchNature(String.valueOf(p[14])));

        @SuppressWarnings("unchecked")
        ArrayList<Move> moves = matchMove((List<String>) p[21]);

        pkpaste.setMove1(moves.get(0));
        pkpaste.setMove2(moves.get(1));
        pkpaste.setMove3(moves.get(2));
        pkpaste.setMove4(moves.get(3));

        pkpaste.setHpEv((int) p[8]);
        pkpaste.setAtkEv((int) p[9]);
        pkpaste.setDefEv((int) p[10]);
        pkpaste.setSpatkEv((int) p[11]);
        pkpaste.setSpdefEv((int) p[12]);
        pkpaste.setSpdEv((int) p[13]);

        pkpaste.setHpIv((int) p[15]);
        pkpaste.setAtkIv((int) p[16]);
        pkpaste.setDefIv((int) p[17]);
        pkpaste.setSpatkIv((int) p[18]);
        pkpaste.setSpdefIv((int) p[19]);
        pkpaste.setSpdIv((int) p[20]);

        int[] finalStats = setFinalStatLabels(pkpaste);

        pkpaste.setHp(finalStats[0]);
        pkpaste.setAtk(finalStats[1]);
        pkpaste.setDef(finalStats[2]);
        pkpaste.setSpatk(finalStats[3]);
        pkpaste.setSpdef(finalStats[4]);
        pkpaste.setSpd(finalStats[5]);
        return pkpaste;
    }

    private void readPokepaste() {
        List<String> readPokemonData = PokemonFileUtility.readPokemonData();

        for (String string : readPokemonData) {
            Object[] pokepasteData = extractDataFromPokepaste(string);
            listPokemonPaste.add(buildPokepaste(pokepasteData));
        }
    }

    /**
     * Este método permite consultar los datos de la tabla pokemon_paste de la
     * base de datos y guardarlos en la JTable tblPokemon. Los datos de la tabla
     * pokemon_paste son previamente "traducidos" en el método listPokepaste de
     * la clase InteractuarPokepaste en nombres (en lugar de claves primarias
     * int).
     */
    private void llenarTabla() {
        //definicion variables para rescatar datos
        DefaultTableModel tbl = (DefaultTableModel) this.tblPokemonPaste.getModel();
        //para que no se dupliquela información
        tbl.setRowCount(0);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tbl);
        this.tblPokemonPaste.setRowSorter(sorter);

        for (PokemonPaste p : listPokemonPaste) {
            tbl.addRow(new Object[]{
                p.getId(), p.getPokemon().getName(), getPokemonSprite(p), getTypeImages(p.getTeraType().getId()),
                p.getAbility().getName(), p.getHp(), p.getAtk(), p.getDef(), p.getSpatk(),
                p.getSpdef(), p.getSpd(), p.getLevel(), p.getIsShiny() == false ? "No" : "Yes"
            });
        }

        tblPokemonPaste.getColumnModel().getColumn(2).setCellRenderer(new ImageIconCellRenderer());
        tblPokemonPaste.getColumnModel().getColumn(3).setCellRenderer(new ImageIconCellRenderer());
    }

    /**
     * Este método permite obtener la descripcion de un movimiento seleccionado.
     *
     * @param cbo el combobox del cual se quiere sacar el movimiento.
     * @return la descripcion en formato String.
     */
    private boolean getMoveDescription(JComboBox cbo) {
        try {
            Move move = matchMove(cbo);
            if (move != null) {
                new MovesTable(move).setVisible(true);
                return true;
            }
        } catch (NullPointerException e) {
        }
        return false;
    }

    /**
     * Este método permite obtener la descripcion del item seleccionado.
     *
     * @return la descripcion del item.
     */
    private String getItemDescription() {
        String description = "";
        try {
            Item item = matchItem();
            if (item != null) {
                description = item.getDescription();
            }
        } catch (NullPointerException e) {
        }
        return description;
    }

    /**
     * Este método permite obtener la descripcion de la habilidad seleccionada.
     *
     * @return la descripcion de la habilidad.
     */
    private String getAbilityDescription() {
        String description = "";
        try {
            Ability ability = matchAbility();
            if (ability != null) {
                description = ability.getDescription();
            }
        } catch (NullPointerException e) {
        }
        return description;
    }

    /**
     * Este método permite resetear todos los campos combobox para poder
     * seleccionar nuevas opciones.
     */
    private void resetFields() {
        String empty = "";
        for (JComboBox cbo : listMoveCBOXS) {
            cbo.setSelectedItem(empty);
        }
        for (JSlider s : listStatSliders) {
            s.setValue(0);
        }
        for (JSpinner j : listIvSPN) {
            j.setValue(31);
        }
        spnLevel.setValue(100);
        rdnGenderless.setSelected(true);
        ckbShiny.setSelected(false);
        txtNickname.setText(empty);
        cboAbility.setSelectedItem(empty);
        cboItem.setSelectedItem(empty);
        cboSpecies.setSelectedItem(empty);
    }

    /**
     * Este método permite obtener un ícono con la imagen de un Pokémon basado
     * en su nombre. Se obtiene el nombre del Pokémon seleccionado en la
     * cboSpecies y se busca en el array listPokemonImage.
     *
     * @return la imagen del Pokemon
     */
    private ImageIcon getPokemonSprite() {
        int pokemonId = matchPokemon().getId();
        if (listPokemonImage.containsKey(pokemonId)) {
            ImageIcon sprite = MainWindow.listPokemonImage.get(pokemonId);
            Image img = sprite.getImage();
            Image resizedImg = img.getScaledInstance(112, 84, Image.SCALE_SMOOTH);
            sprite = new ImageIcon(resizedImg);
            return sprite;
        } else {
            return new ImageIcon(pokeball.getImage().getScaledInstance(112, 84, Image.SCALE_SMOOTH));
        }
    }

    private ImageIcon getPokemonSprite(PokemonPaste p) {
        String pokemonName = p.getPokemon().getName();
        int pokemonId = 0;
        for (Pokemon pokemon : listaPokemon) {
            if (pokemon.getName().equals(pokemonName)) {
                pokemonId = pokemon.getId();
                break;
            }
        }
        ImageIcon sprite = MainWindow.listPokemonImage.get(pokemonId);
        Image img = sprite.getImage();
        Image resizedImg = img.getScaledInstance(33, 21, Image.SCALE_SMOOTH);
        sprite = new ImageIcon(resizedImg);
        return sprite;
        //return new ImageIcon(pokeball.getImage().getScaledInstance(66,42, Image.SCALE_SMOOTH));
    }

    /**
     * Método que permite recibir un pokepaste en el txtPokepaste e importarlo
     * al sistema para poder interactuar con él y poder subirlo a la base de
     * datos.
     *
     * @param pokepaste
     */
    public void importPokepaste(String pokepaste) {
        // Split the pokepaste string into lines
        String[] lines = pokepaste.split("\n");

        // Parse the species and nickname if present
        String speciesAndNickname = lines[0];
        String[] speciesAndNicknameParts = speciesAndNickname.split(" @ ");
        String species = "";
        String nickname = "";
        String item = ""; // Added item variable

        // The first line is divided into two: the species + nickname + gender
        if (speciesAndNicknameParts.length == 2) {
            // left part of the first line. It is also trimmed
            species = speciesAndNicknameParts[0].trim();

            // Further split the speciesAndNicknameParts[0] to separate species and nickname
            String[] speciesParts = speciesAndNicknameParts[0].trim().split("\\(");
            // For "Creamy (Alcremie) (F) @ Assault Vest"  it would result in:
            // [Creamy , Alcremie) , F)]
            if (speciesParts.length > 1) {
                if (speciesParts[1].contentEquals("M)") || speciesParts[1].contentEquals("F)")) {
                    String[] newSpeciesParts = {"", speciesParts[0]};
                    speciesParts = newSpeciesParts;
                }
                nickname = speciesParts[0].trim();
                species = speciesParts[1].replace(")", "").trim();
            }
            item = speciesAndNicknameParts[1].trim(); // Set item from the second part
        } else {
            species = speciesAndNickname.trim();
        }

        // Set the species, nickname, and item
        cboSpecies.setSelectedItem(species);
        txtNickname.setText(nickname);
        cboItem.setSelectedItem(item); // Set the item
        // Set gender radio buttons
        // Extract gender information from the nickname

        String genderLine = lines[0];
        if (genderLine.contains("(M)")) {
            rbnMale.setSelected(true);
        } else if (genderLine.contains("(F)")) {
            rbnFemale.setSelected(true);
        } else {
            rdnGenderless.setSelected(true);
        }

        // Process each line
        for (String line : lines) {
            if (line.startsWith("Ability:")) {
                // Set ability combobox value
                String ability = line.substring("Ability: ".length()).trim();
                cboAbility.setSelectedItem(ability);
            } else if (line.startsWith("Level:")) {
                // Set level spinner value
                String levelString = line.substring("Level: ".length()).trim();
                int level = Integer.parseInt(levelString);
                spnLevel.setValue(level);
            } else if (line.startsWith("Shiny:") && line.contains(": Y")) {
                // Set shiny checkbox value
                ckbShiny.setSelected(true);
            } else if (line.startsWith("Tera Type:")) {
                // Set tera type combobox value
                String teraType = line.substring("Tera Type: ".length()).trim();
                cboTeraType.setSelectedItem(teraType);
            } else if (line.startsWith("EVs:")) {
                // Set EV sliders
                // Parse and set values for HP, Atk, Def, SpA, SpDef, and Spe
                // Example: EVs: 252 HP / 252 Atk / 4 Def
                String evLine = line.substring("EVs: ".length());
                String[] evValues = evLine.split(" / ");
                for (String ev : evValues) {
                    String[] parts = ev.trim().split(" ");
                    int value = Integer.parseInt(parts[0]);
                    String stat = parts[1];
                    switch (stat) {
                        case "HP" ->
                            sldHP.setValue(value);
                        case "Atk" ->
                            sldATK.setValue(value);
                        case "Def" ->
                            sldDEF.setValue(value);
                        case "SpA" ->
                            sldSPATK.setValue(value);
                        case "SpD" ->
                            sldSPDEF.setValue(value);
                        case "Spe" ->
                            sldSPD.setValue(value);
                        default -> {
                        }
                    }
                    // Handle unknown stat
                }
            } else if (line.contains(" Nature")) {
                // Set nature combobox value
                String natureLine = line.replace("Nature", "").trim();
                String[] parts = natureLine.split("\\s+");
                if (parts.length > 0) {
                    String nature = parts[parts.length - 1];
                    cboNature.setSelectedItem(nature);
                }

            } else if (line.startsWith("IVs:")) {
                // Set IV spinners
                // Parse and set values for HP, Atk, Def, SpA, SpDef, and Spe
                // Example: IVs: 31 HP / 31 Atk / 31 Def
                String ivLine = line.substring("IVs: ".length());
                String[] ivValues = ivLine.split(" / ");
                for (String iv : ivValues) {
                    String[] parts = iv.trim().split(" ");
                    int value = Integer.parseInt(parts[0]);
                    String stat = parts[1];
                    switch (stat) {
                        case "HP" ->
                            spnIVHP.setValue(value);
                        case "Atk" ->
                            spnIVATK.setValue(value);
                        case "Def" ->
                            spnIVDEF.setValue(value);
                        case "SpA" ->
                            spnIVSPATK.setValue(value);
                        case "SpD" ->
                            spnIVSPDEF.setValue(value);
                        case "Spe" ->
                            spnIVSPD.setValue(value);
                        default -> {
                        }
                    }
                    // Handle unknown stat
                }
            } else if (line.startsWith("- ")) {
                // Set move combobox values
                String move = line.substring(2).trim();
                // Check which move slot (1, 2, 3, 4) and set the corresponding combobox
                if (cboMove1.getSelectedItem().toString().isEmpty()) {
                    cboMove1.setSelectedItem(move);
                } else if (cboMove2.getSelectedItem().toString().isEmpty()) {
                    cboMove2.setSelectedItem(move);
                } else if (cboMove3.getSelectedItem().toString().isEmpty()) {
                    cboMove3.setSelectedItem(move);
                } else if (cboMove4.getSelectedItem().toString().isEmpty()) {
                    cboMove4.setSelectedItem(move);
                }
                // Add more conditions for other lines as needed
            }
        }

    }

    private Object[] extractDataFromPokepaste(String pokepaste) {
        // Split the pokepaste string into lines
        String[] lines = pokepaste.split("\n");

        // Parse the species and nickname if present
        String speciesAndNickname = lines[0];
        String[] speciesAndNicknameParts = speciesAndNickname.split(" @ ");
        String species = "";
        String nickname = "";
        String item = ""; // Added item variable

        // The first line is divided into two: the species + nickname + gender
        if (speciesAndNicknameParts.length == 2) {
            // left part of the first line. It is also trimmed
            species = speciesAndNicknameParts[0].trim();

            // Further split the speciesAndNicknameParts[0] to separate species and nickname
            String[] speciesParts = speciesAndNicknameParts[0].trim().split("\\(");
            // For "Creamy (Alcremie) (F) @ Assault Vest"  it would result in:
            // [Creamy , Alcremie) , F)]
            if (speciesParts.length > 1) {
                if (speciesParts[1].contentEquals("M)") || speciesParts[1].contentEquals("F)")) {
                    String[] newSpeciesParts = {"", speciesParts[0]};
                    speciesParts = newSpeciesParts;
                }
                nickname = speciesParts[0].trim();
                species = speciesParts[1].replace(")", "").trim();
            }
            item = speciesAndNicknameParts[1].trim(); // Set item from the second part
        } else {
            species = speciesAndNickname.trim();
        }

        // Extract gender information from the nickname
        String genderLine = lines[0];
        if (genderLine.contains("(M)")) {
            gender.setId(1);
            gender.setName("Male");
        } else if (genderLine.contains("(F)")) {
            gender.setId(2);
            gender.setName("Female");
        } else {
            gender.setId(3);
            gender.setName("Genderless");
        }

        String ability = "";
        int level = 100;
        boolean shiny = false;
        String teraType = "";
        int hpEv = 0;
        int atkEv = 0;
        int defEv = 0;
        int spatkEv = 0;
        int spdefEv = 0;
        int spdEv = 0;
        String nature = "";
        int hpIv = 31;
        int atkIv = 31;
        int defIv = 31;
        int spatkIv = 31;
        int spdefIv = 31;
        int spdIv = 31;
        List<String> moves = new ArrayList<>();

        // Process each line
        for (String line : lines) {
            if (line.startsWith("Ability:")) {
                // Set ability combobox value
                ability = line.substring("Ability: ".length()).trim();

            } else if (line.startsWith("Level:")) {
                // Set level spinner value
                String levelString = line.substring("Level: ".length()).trim();
                level = Integer.parseInt(levelString);

            } else if (line.startsWith("Shiny:") && line.contains(": Y")) {
                // Set shiny checkbox value
                shiny = true;
            } else if (line.startsWith("Tera Type:")) {
                // Set tera type combobox value
                teraType = line.substring("Tera Type: ".length()).trim();

            } else if (line.startsWith("EVs:")) {
                // Set EV sliders
                // Parse and set values for HP, Atk, Def, SpA, SpDef, and Spe
                // Example: EVs: 252 HP / 252 Atk / 4 Def
                String evLine = line.substring("EVs: ".length());
                String[] evValues = evLine.split(" / ");
                for (String ev : evValues) {
                    String[] parts = ev.trim().split(" ");
                    int value = Integer.parseInt(parts[0]);
                    String stat = parts[1];
                    switch (stat) {
                        case "HP" ->
                            hpEv = value;
                        case "Atk" ->
                            atkEv = value;
                        case "Def" ->
                            defEv = value;
                        case "SpA" ->
                            spatkEv = value;
                        case "SpD" ->
                            spdefEv = value;
                        case "Spe" ->
                            spdEv = value;
                        default -> {
                        }
                    }
                    // Handle unknown stat
                }
            } else if (line.contains(" Nature")) {
                // Set nature combobox value
                String natureLine = line.replace("Nature", "").trim();
                String[] parts = natureLine.split("\\s+");
                if (parts.length > 0) {
                    nature = parts[parts.length - 1];

                }

            } else if (line.startsWith("IVs:")) {
                // Set IV spinners
                // Parse and set values for HP, Atk, Def, SpA, SpDef, and Spe
                // Example: IVs: 31 HP / 31 Atk / 31 Def
                String ivLine = line.substring("IVs: ".length());
                String[] ivValues = ivLine.split(" / ");
                for (String iv : ivValues) {
                    String[] parts = iv.trim().split(" ");
                    int value = Integer.parseInt(parts[0]);
                    String stat = parts[1];
                    switch (stat) {
                        case "HP" ->
                            hpIv = value;
                        case "Atk" ->
                            atkIv = value;
                        case "Def" ->
                            defIv = value;
                        case "SpA" ->
                            spatkIv = value;
                        case "SpD" ->
                            spdefIv = value;
                        case "Spe" ->
                            spdIv = value;
                        default -> {
                        }
                    }
                    // Handle unknown stat
                }
            } else if (line.startsWith("- ")) {
                // Set move combobox values
                String move = line.substring(2).trim();
                moves.add(move);
            }
        }

        return new Object[]{species, nickname, item, genderLine, ability, level, shiny, teraType,
            hpEv, atkEv, defEv, spatkEv, spdefEv, spdEv, nature, hpIv, atkIv, defIv, spatkIv, spdefIv,
            spdIv, moves};
    }

    /**
     * Este método permite actualizar los labels de EVs para que se modifiquen
     * de acuerdo a los nuevos valores que vayan tomando los sliders de EVs.
     *
     * @param statLabels lista de labels que muestran los EVs asignados.
     * @param statSliders lista de sliders que reciben los valores de EVs.
     */
    private void setLabels(JLabel[] statLabels, JSlider[] statSliders) {
        for (int i = 0; i < statLabels.length; i++) {
            int index = i; // Final or effectively final variable
            statLabels[i].setText(String.valueOf(statSliders[i].getValue()));
            statSliders[i].addChangeListener(e -> {
                statLabels[index].setText(String.valueOf(statSliders[index].getValue()));
            });
        }
    }

    /**
     * Este método permite averiguar si se han asignado o no valores a los
     * spinners de IVs (por defecto 31).
     *
     * @return true si hay alguno distinto a 31. false si todos son 31.
     *
     */
    private boolean isSpnIV31() {
        for (JSpinner i : listIvSPN) {
            if ((int) i.getValue() != 31) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este método permite averiguar si se han asignado o no valores a los
     * sliders de EVs.
     *
     * @return true si hay alguno distinto a 0. false si todos son 0.
     *
     */
    private boolean isSldEV0() {
        for (JSlider s : listStatSliders) {
            if (s.getValue() != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este método asigna a cada slider de EVs un Change Listener que ejecuta la
     * actualización del label lblEVSum.
     */
    private void updateRemainingEvs() {
        for (JSlider slider : listStatSliders) {
            slider.addChangeListener(e -> {
                if (!slider.getValueIsAdjusting()) {
                    updateLabel();
                }
            });
        }
    }

    /**
     * Este método actualiza el label en el que se guarda la cantidad restante
     * de EVs que se pueden asignar a un Pokémon.
     */
    private void updateLabel() {
        int hp = sldHP.getValue();
        int atk = sldATK.getValue();
        int def = sldDEF.getValue();
        int spatk = sldSPATK.getValue();
        int spdef = sldSPDEF.getValue();
        int spd = sldSPD.getValue();
        int evSum = hp + atk + def + spatk + spdef + spd;
        lblEVSum.setText(String.valueOf(508 - evSum));
    }

    /**
     * Este método obtiene los valores de los campos en la GUI para generar un
     * pokepaste. Un pokepaste es la representación textual de los atributos de
     * un Pokémon que se usan en el simulador de batallas Pokémon Showdown para
     * importar o exportar sets.
     *
     * @return la String del pokepaste.
     */
    public String generatePokepaste() {
        int hp = sldHP.getValue();
        int atk = sldATK.getValue();
        int def = sldDEF.getValue();
        int spatk = sldSPATK.getValue();
        int spdef = sldSPDEF.getValue();
        int spd = sldSPD.getValue();

        StringBuilder pokepaste = new StringBuilder();

        if (!txtNickname.getText().isEmpty()) { // if the pokemon has a nickname
            pokepaste.append(txtNickname.getText()).append(' ');
            pokepaste.append("(").append(cboSpecies.getSelectedItem().toString()).append(")");
        } else { // else just show the species' name
            pokepaste.append(cboSpecies.getSelectedItem().toString());
        }
        if (gender.getId() != 3) {
            pokepaste.append(" (").append(gender.getName().charAt(0)).append(")");
        }
        pokepaste.append(" @ ").append(cboItem.getSelectedItem().toString());
        pokepaste.append("\n").append("Ability: ").append(cboAbility.getSelectedItem().toString());
        if ((int) spnLevel.getValue() != 100) {
            pokepaste.append("\nLevel: ").append(spnLevel.getValue());
        }
        if (ckbShiny.isSelected()) {
            pokepaste.append("\nShiny: Yes");
        }
        pokepaste.append("\nTera Type: ").append(cboTeraType.getSelectedItem().toString());
        if (isSldEV0()) {
            pokepaste.append("\nEVs: ");
            if (sldHP.getValue() != 0) {
                pokepaste.append(String.valueOf(hp)).append(" HP / ");
            }
            if (sldATK.getValue() != 0) {
                pokepaste.append(String.valueOf(atk)).append(" Atk / ");
            }
            if (sldDEF.getValue() != 0) {
                pokepaste.append(String.valueOf(def)).append(" Def / ");
            }
            if (sldSPATK.getValue() != 0) {
                pokepaste.append(String.valueOf(spatk)).append(" SpA / ");
            }
            if (sldSPDEF.getValue() != 0) {
                pokepaste.append(String.valueOf(spdef)).append(" SpD / ");
            }
            if (sldSPD.getValue() != 0) {
                pokepaste.append(String.valueOf(spd)).append(" Spe");
            }
            if (pokepaste.substring(pokepaste.length() - 3).equals(" / ")) {
                pokepaste.delete(pokepaste.length() - 3, pokepaste.length());
            }
        }

        pokepaste.append("\n").append(cboNature.getSelectedItem().toString()).append(" Nature");
        // 

        if (isSpnIV31()) {

            pokepaste.append("\nIVs: ");
            if ((int) spnIVHP.getValue() != 31) {
                pokepaste.append(String.valueOf(spnIVHP.getValue())).append(" HP / ");
            }
            if ((int) spnIVATK.getValue() != 31) {
                pokepaste.append(String.valueOf(spnIVATK.getValue())).append(" Atk / ");
            }
            if ((int) spnIVDEF.getValue() != 31) {
                pokepaste.append(String.valueOf(spnIVDEF.getValue())).append(" Def / ");
            }
            if ((int) spnIVSPATK.getValue() != 31) {
                pokepaste.append(String.valueOf(spnIVSPATK.getValue())).append(" SpA / ");
            }
            if ((int) spnIVSPDEF.getValue() != 31) {
                pokepaste.append(String.valueOf(spnIVSPDEF.getValue())).append(" SpD / ");
            }
            if ((int) spnIVSPD.getValue() != 31) {
                pokepaste.append(String.valueOf(spnIVSPD.getValue())).append(" Spe");
            }
            if (pokepaste.substring(pokepaste.length() - 3).equals(" / ")) {
                pokepaste.delete(pokepaste.length() - 3, pokepaste.length());
            }
        }
        if (!cboMove1.getSelectedItem().toString().isEmpty()) {
            pokepaste.append("\n- ").append(cboMove1.getSelectedItem().toString());
        }
        if (!cboMove2.getSelectedItem().toString().isEmpty()) {
            pokepaste.append("\n- ").append(cboMove2.getSelectedItem().toString());
        }
        if (!cboMove3.getSelectedItem().toString().isEmpty()) {
            pokepaste.append("\n- ").append(cboMove3.getSelectedItem().toString());
        }
        if (!cboMove4.getSelectedItem().toString().isEmpty()) {
            pokepaste.append("\n- ").append(cboMove4.getSelectedItem().toString());
        }

        return String.valueOf(pokepaste);
    }

    /**
     * Este método comprueba si hay algún movimiento que se repita.
     *
     * @return
     */
    private boolean areMovesValid() {
        Set<Object> selectedItems = new HashSet<>();
        boolean isFirstComboBox = true;

        for (JComboBox move : listMoveCBOXS) {
            Object selectedItem = move.getSelectedItem();

            // Skip null values after the first JComboBox
            if (!isFirstComboBox && selectedItem == null) {
                continue;
            }

            isFirstComboBox = false;

            // Check if the selected item is already in the set
            if (!selectedItems.add(selectedItem)) {
                // Duplicate found
                return false;
            }
        }
        // No duplicates found
        return true;
    }

    /**
     * Este método comprueba que el primer movimiento no sea nulo.
     *
     * @return
     */
    private boolean isFirstMoveNull() {
        return listMoveCBOXS[0].getSelectedItem().toString().equals("");
    }

    /**
     * Este método permite obtener el id de una fila de la tabla pokepaste.
     *
     * @return
     */
    private int getSelectedRowFromPokepaste() {
        int rowIndex = tblPokemonPaste.getSelectedRow();
        int columnIndex = 0;
        try {
            Object id = tblPokemonPaste.getValueAt(rowIndex, columnIndex);
            return (int) id;
        } catch (Exception e) {
            SoundPlayer.wrongBuzzer();
            JOptionPane.showMessageDialog(this, "You have not selected a row. Please choose a row and try again.", "No Pokémon Selected",
                    JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    /**
     * Este método sobrecargado permite generar un Pokepaste a partir de un
     * objeto PokemonPaste, a diferencia del método sin parámetros que lo arma a
     * base de los varios componentes de la GUI.
     *
     * @param p un objeto PokemonPaste
     * @return string pokepaste
     */
    public String generatePokepaste(PokemonPaste p) {
        //EVs
        try {
            int hp = p.getHpEv();
            int atk = p.getAtkEv();
            int def = p.getDefEv();
            int spatk = p.getSpatkEv();
            int spdef = p.getSpdefEv();
            int spd = p.getSpdEv();
            //IVs
            int hpI = p.getHpIv();
            int atkI = p.getAtkIv();
            int defI = p.getDefIv();
            int spatkI = p.getSpatkIv();
            int spdefI = p.getSpdefIv();
            int spdI = p.getSpdIv();
            //Moves
            String move1 = p.getMove1().getName();
            String move2 = p.getMove2().getName();
            String move3 = p.getMove3().getName();
            String move4 = p.getMove4().getName();
            // Stringbuilder creation
            StringBuilder pokepaste = new StringBuilder();

            if (!p.getNickname().equals("")) { // if the pokemon has a nickname
                pokepaste.append(p.getNickname()).append(' ');
                pokepaste.append("(").append(p.getPokemon().getName()).append(")");
            } else { // else just show the species' name
                pokepaste.append(p.getPokemon().getName());
            }
            if (p.getGender().getId() != 3) {
                pokepaste.append(" (").append(p.getGender().getName().charAt(0)).append(")");
            }
            pokepaste.append(" @ ").append(p.getItem().getName());
            pokepaste.append("\n").append("Ability: ").append(p.getAbility().getName());
            if (p.getLevel() != 100) {
                pokepaste.append("\nLevel: ").append(String.valueOf(p.getLevel()));
            }
            if (p.getIsShiny()) {
                pokepaste.append("\nShiny: Yes");
            }
            pokepaste.append("\nTera Type: ").append(p.getTeraType().getName());
            if (hp != 0 || atk != 0 || def != 0
                    || spatk != 0 || spdef != 0 || spd != 0) {
                pokepaste.append("\nEVs: ");
                if (hp != 0) {
                    pokepaste.append(String.valueOf(hp)).append(" HP / ");
                }
                if (atk != 0) {
                    pokepaste.append(String.valueOf(atk)).append(" Atk / ");
                }
                if (def != 0) {
                    pokepaste.append(String.valueOf(def)).append(" Def / ");
                }
                if (spatk != 0) {
                    pokepaste.append(String.valueOf(spatk)).append(" SpA / ");
                }
                if (spdef != 0) {
                    pokepaste.append(String.valueOf(spdef)).append(" SpD / ");
                }
                if (spd != 0) {
                    pokepaste.append(String.valueOf(spd)).append(" Spe");
                }
                if (pokepaste.substring(pokepaste.length() - 3).equals(" / ")) {
                    pokepaste.delete(pokepaste.length() - 3, pokepaste.length());
                }
            }

            pokepaste.append("\n").append(p.getNature().getName()).append(" Nature");
            // 

            if (hpI != 31 || atkI != 31 || defI != 31
                    || spatkI != 31 || spdefI != 31 || spdI != 31) {

                pokepaste.append("\nIVs: ");
                if (hpI != 31) {
                    pokepaste.append(String.valueOf(hpI)).append(" HP / ");
                }
                if (atkI != 31) {
                    pokepaste.append(String.valueOf(atkI)).append(" Atk / ");
                }
                if (defI != 31) {
                    pokepaste.append(String.valueOf(defI)).append(" Def / ");
                }
                if (spatkI != 31) {
                    pokepaste.append(String.valueOf(spatkI)).append(" SpA / ");
                }
                if (spdefI != 31) {
                    pokepaste.append(String.valueOf(spdefI)).append(" SpD / ");
                }
                if (spdI != 31) {
                    pokepaste.append(String.valueOf(spdI)).append(" Spe");
                }
                if (pokepaste.substring(pokepaste.length() - 3).equals(" / ")) {
                    pokepaste.delete(pokepaste.length() - 3, pokepaste.length());
                }
            }
            if (!move1.isEmpty()) {
                pokepaste.append("\n- ").append(move1);
            }
            if (!move2.isEmpty()) {
                pokepaste.append("\n- ").append(move2);
            }
            if (!move3.isEmpty()) {
                pokepaste.append("\n- ").append(move3);
            }
            if (!move4.isEmpty()) {
                pokepaste.append("\n- ").append(move4);
            }

            return String.valueOf(pokepaste);
        } catch (Exception e) {
        }
        return "";
    }

    private PokemonPaste matchPokepaste(int i) {
        for (PokemonPaste p : listPokemonPaste) {
            if (p.getId() == i) {
                return p;
            }

        }
        return null;
    }

    public ArrayList<Pokemon> getPokemonArray() {
        return MainWindow.listaPokemon;
    }

    public ArrayList<Item> getItemArray() {
        return MainWindow.listaItems;
    }

    public ArrayList<Ability> getAbilityArray() {
        return MainWindow.listaAbility;
    }

    public HashMap<Integer, Integer[]> getPokemonTyping() {
        return MainWindow.listPokemonTyping;
    }

    public static ImageIcon getPokeballImage() {
        return pokeball;
    }

    public HashMap<Integer, ImageIcon> getPokemonImages() {
        return listPokemonImage;
    }

    public HashMap<Integer, ImageIcon> getTypeImages() {
        return listTypeImage;
    }

    public ArrayList<Move> getMoves() {
        return listaMove;
    }

    private ImageIcon getTypeImages(int id) {
        return new ImageIcon(listTypeImage.get(id).getImage().getScaledInstance(75, 15, Image.SCALE_SMOOTH));
    }

    public static MainWindow getInstance() {
        if (instance == null) {
            try {
                instance = new MainWindow();
            } catch (URISyntaxException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    private ImageIcon[] getPokemonTypingImages() {

        int pokemonId = matchPokemon().getId();

        ImageIcon t1 = null;
        ImageIcon t2 = null;

        Integer[] types = listPokemonTyping.get(pokemonId);
        if (types != null) {
            //Type1 and type2 obtained from each PokemonTyping object.
            int type1 = types[0];
            int type2 = types[1];

            // Get the scaled instance of the image
            Image typeImage1 = listTypeImage.get(type1).getImage().getScaledInstance(100, 20, Image.SCALE_SMOOTH);
            t1 = new ImageIcon(typeImage1);

            if (listTypeImage.containsKey(type2)) {
                // Get the scaled instance of the image
                Image typeImage2 = listTypeImage.get(type2).getImage().getScaledInstance(100, 20, Image.SCALE_SMOOTH);
                t2 = new ImageIcon(typeImage2);
            }
        }
        return new ImageIcon[]{t1, t2};
    }

    private void setPokemonTypingLabels() {

        // Image list is retrieved
        ImageIcon[] pokemonTypingImages = getPokemonTypingImages();

        // Set label with the type's image
        if (pokemonTypingImages[0] != null) {
            this.lblType1.setIcon(pokemonTypingImages[0]);
            this.lblType1.setBounds(520, 100, 100, 20);
        }
        if (pokemonTypingImages[1] != null) {
            this.lblType2.setIcon(pokemonTypingImages[1]);
            this.lblType2.setBounds(520, 130, 100, 20);
        } else {
            this.lblType2.setIcon(null);
            this.lblType1.setBounds(520, 115, 100, 20);
        }

    }

    private void playPokemonCry(String pokemonName) {
        String formattedPokemonName = pokemonName.toLowerCase().replace(" ", "").replace(":", "-");
        if (containsExcludedSuffix(formattedPokemonName)) {
            formattedPokemonName = formattedPokemonName.substring(0, formattedPokemonName.indexOf("-"));
        }
        String soundPath = "soundManagement/pokemonCries/" + formattedPokemonName + ".wav";
        SoundPlayer.playSound(soundPath, -5f, false);
    }

    private boolean containsExcludedSuffix(String pokemonName1) {
        String[] excludedSuffixes = {
            "-alola", "-galar", "-hisui", "paldea", "deoxys-", "rotom-",
            "-origin", "-small", "-large", "wormadam-", "basculegion-",
            "-meteor", "-pirou", "-zen", "-ash", "-blade", "-wellspring",
            "-hearthflame", "-cornerstone", "-teal-tera", "-wellspring-tera",
            "-hearthflame-tera", "-cornerstone-tera", "-terastal", "-stellar"
        };

        for (String suffix : excludedSuffixes) {
            if (pokemonName1.contains(suffix)) {
                return true;
            }
        }

        return false;
    }

    private void createDataDirectory() {
        String FILE_NAME = "storedPokemon.txt";
        try {
            // Get the current working directory
            Path currentDir = Paths.get(System.getProperty("user.dir"));

            // Create the "Data" directory if it doesn't exist
            Path dataDir = currentDir.resolve("Data");
            if (Files.notExists(dataDir)) {
                Files.createDirectory(dataDir);
                //System.out.println("Data directory created successfully.");
            }

            // Create the "storedPokemon.txt" file if it doesn't exist
            Path pokemonFile = dataDir.resolve(FILE_NAME);
            if (Files.notExists(pokemonFile)) {
                Files.createFile(pokemonFile);
                //System.out.println(FILE_NAME + " created successfully.");
            }
        } catch (IOException e) {
            // Handle the exception as needed    
        }
    }

    private void refresh() {
        listPokemonPaste.clear();
        readPokepaste();
        llenarTabla();
    }

//    private void missingCry() {
//        for (Pokemon pokemon : listaPokemon) {
//            String pokemonName = pokemon.getName();
//            String pokemonName1 = pokemonName.toLowerCase().replace(" ", "").replace(":", "-");
//
//            if (pokemonName1.contains("-alola") || pokemonName1.contains("-galar") || pokemonName1.contains("-hisui") 
//                    || pokemonName1.contains("paldea") || pokemonName1.contains("deoxys-") || pokemonName1.contains("rotom-")
//                    || pokemonName1.contains("-origin") || pokemonName1.contains("-small") || pokemonName1.contains("-large")
//                    || pokemonName1.contains("wormadam-") || pokemonName1.contains("basculegion-") || pokemonName1.contains("-meteor")
//                    || pokemonName1.contains("-pirou") || pokemonName1.contains("-origin") || pokemonName1.contains("-zen")
//                    || pokemonName1.contains("-ash") || pokemonName1.contains("-blade")) {
//                pokemonName1 = pokemonName1.substring(0, pokemonName1.indexOf("-"));
//            }
//
//            File file = new File("src/main/resources/soundManagement/pokemonCries/" + pokemonName1 + ".wav");
//
//            if (!file.exists()) {
//                System.out.println("src/main/resources/soundManagement/pokemonCries/" + pokemonName1 + ".wav");
//            }
//        }
//    }
}
