/*
 * Copyright 2017 theman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package theman;

import java.awt.event.KeyEvent;
import theman.interfaces.EInputTypes;
import theman.interfaces.ILogInfo;
import theman.interfaces.IControlerPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import theman.interfaces.EBlockerInputs;
import theman.interfaces.IPublish;
import jota.model.Bundle;
import jota.utils.Pair;

/**
 *
 * @author theman
 */
public class ControlerPanel extends javax.swing.JFrame implements IControlerPanel, ILogInfo {

    /**
     * Creates new form ControlerPanel
     */
    private SpinnerNumberModel snm;
    private SpinnerNumberModel minSpinner;
    private SpinnerNumberModel maxSpinner;
    private Thread job;
   
    private final Ticker ticker;
    private final Job GUIPublisher;
    private final Controler con;

    private TrunkBanchListModel trunkBranchModel;
    private BundleListModel bundleListModel;
    private boolean paused = false;

    public ControlerPanel() {
        initComponents();
        initCustoms();
        GUIPublisher = new Job();
        GUIPublisher.execute();
        

        con = new Controler((IControlerPanel) this, GUIPublisher.getPublisherMethod());
        ticker = new Ticker(con, GUIPublisher.getPublisherMethod());
        new Thread(ticker, "Listener Thread").start();

//        snm.setMaximum(1);
//        jSpinner4.setModel(new SpinnerNumberModel(1, //initial value
//         15, //min
//         200, //max
//         1));
    }

    private void initCustoms() {
        trunkBranchModel = new TrunkBanchListModel();
        bundleListModel = new BundleListModel();
        TrunkList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        BranchList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        TrunkList.setModel(trunkBranchModel);// TODO add your handling code here:
        BranchList.setModel(trunkBranchModel);
        jList3.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList3.setModel(bundleListModel);

        SharedListSelectionHandler l = new SharedListSelectionHandler(this, TrunkList);
        SharedListSelectionHandler l2 = new SharedListSelectionHandler(this, BranchList);
        TrunkList.addListSelectionListener(l);
        BranchList.addListSelectionListener(l2);
        snm = new SpinnerNumberModel(1, //initial value
                0, //min
                300, //max
                1);
        jSpinner3.setModel(snm);
        buttonGroup1.add(jRadioButtonMenuItemSingle);
        buttonGroup1.add(jRadioButtonMenuItemMultiple);
        minSpinner = new SpinnerNumberModel(0, 0, 14, 1);
        maxSpinner = new SpinnerNumberModel(15, minSpinner.getMaximum(), 1000, 1);
        jSpinner3.setModel(minSpinner);
        jSpinner4.setModel(maxSpinner);
    }

    @Override
    public String getReciverAddress() {
        return jTextFieldReceiver.getText();
    }

    @Override
    public int getZeroValueSpamCount() {

        return (int) jSpinner1.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float[] getSliderValues() {
        return new float[]{
            (float) jSlider1.getValue() / 100f,
            (float) jSlider2.getValue() / 100f,
            (float) jSlider3.getValue() / 100f,
            (float) jSlider4.getValue() / 100f,
            (float) jSlider5.getValue() / 100f,
            (float) jSlider6.getValue() / 100f,
            (float) jSlider7.getValue() / 100f,
            (float) jSlider8.getValue() / 100f,
            (float) jSlider9.getValue() / 100f,
            (float) jSlider10.getValue() / 100f};

    }

    @Override
    public String getTransactionSeed() {
        return jTextFieldInput.getText();
    }

    @Override
    public int getTransactionAmount() {
        System.out.println((int) jSpinner2.getValue());
        return (int) jSpinner2.getValue();
    }

    @Override
    public boolean getBeepStat() {
        return jRadioButton2.isSelected();
    }

    @Override
    public String[] getHashesToBuildOn() {
        return new String[]{trunkBranchModel.getHashAt(TrunkList.getSelectedIndex()), trunkBranchModel.getHashAt(BranchList.getSelectedIndex())};
    }

    @Override
    public EInputTypes getInputType() {
        return (EInputTypes) jComboBox1.getSelectedItem();
    }

    @Override
    public boolean getQuick() {
        return jRadioButtonMenuItemSingle.isSelected();
    }

    @Override
    public String getListentoHash() {
        return jTextFieldListener.getText();
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public EBlockerInputs getBlockerType() {
        return (EBlockerInputs) jComboBox2.getSelectedItem();

    }

    @Override
    public void logInfo(String info) {
        jTextAreaInfoScreen.selectAll();
        jTextAreaInfoScreen.replaceSelection("");
        jTextAreaInfoScreen.append(info);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jSlider3 = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        jSlider4 = new javax.swing.JSlider();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSlider5 = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        jButtonStartSpam = new javax.swing.JButton();
        SpinnerModel spinnerModel = new SpinnerNumberModel(50, //initial value
            0, //min
            200, //max
            1);//step
        jSpinner1 = new javax.swing.JSpinner(spinnerModel);
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TrunkList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        BranchList = new javax.swing.JList<>();
        jSlider6 = new javax.swing.JSlider();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jButtonPause = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSlider7 = new javax.swing.JSlider();
        jLabel11 = new javax.swing.JLabel();
        jSlider8 = new javax.swing.JSlider();
        jLabel12 = new javax.swing.JLabel();
        jSlider9 = new javax.swing.JSlider();
        jLabel14 = new javax.swing.JLabel();
        jSlider10 = new javax.swing.JSlider();
        jButtonClearHistory = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        manualHashTextField = new javax.swing.JTextField();
        manualHash1Label = new javax.swing.JLabel();
        AddManualHashButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSpinner2 = new javax.swing.JSpinner(new SpinnerNumberModel(1,
            0,
            10000000,
            10));
    jButtonStartTransaction = new javax.swing.JButton();
    jTextFieldReceiver = new javax.swing.JTextField();
    jButtonInsertDonAddress = new javax.swing.JButton();
    jLabel13 = new javax.swing.JLabel();
    jButtonPrepareTransaction = new javax.swing.JButton();
    jPanel3 = new javax.swing.JPanel();
    jTextFieldInput = new javax.swing.JTextField();
    jButtonShowState = new javax.swing.JButton();
    jLableBalance = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JComboBox<>();
    jSpinner3 = new javax.swing.JSpinner();
    jSpinner4 = new javax.swing.JSpinner();
    jLabel15 = new javax.swing.JLabel();
    jLabel16 = new javax.swing.JLabel();
    jPanel4 = new javax.swing.JPanel();
    jScrollPane3 = new javax.swing.JScrollPane();
    jTextAreaLogScreen = new javax.swing.JTextArea();
    jPanel5 = new javax.swing.JPanel();
    jScrollPane4 = new javax.swing.JScrollPane();
    jTextAreaInfoScreen = new javax.swing.JTextArea();
    jPanel6 = new javax.swing.JPanel();
    jComboBox2 = new javax.swing.JComboBox<>();
    jTextFieldListener = new javax.swing.JTextField();
    jButtonStartListener = new javax.swing.JButton();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    jRadioButtonMenuItemSingle = new javax.swing.JRadioButtonMenuItem();
    jRadioButtonMenuItemMultiple = new javax.swing.JRadioButtonMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("IOTA Confirmation Booster");
    addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowClosed(java.awt.event.WindowEvent evt) {
            formWindowClosed(evt);
        }
    });

    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Spam Controler"));

    jSlider1.setToolTipText("");
    jSlider1.setValue(0);

    jLabel2.setText("zero tip + new tip ");

    jLabel3.setText("zero tip + zero tip");

    jSlider2.setToolTipText("");
    jSlider2.setValue(0);

    jLabel4.setText("start tip + zero tip");

    jSlider3.setToolTipText("");
    jSlider3.setValue(0);

    jLabel5.setText("start tip + new tip");

    jSlider4.setToolTipText("");
    jSlider4.setValue(0);

    jRadioButton2.setSelected(true);
    jRadioButton2.setText("Beep");
    jRadioButton2.setToolTipText("Makes a beep tone, once a transaction has been broadcastet");

    jSlider5.setToolTipText("");
    jSlider5.setValue(0);

    jLabel6.setText("last 2 zero tips");

    jButtonStartSpam.setText("Start Spam");
    jButtonStartSpam.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonStartSpamActionPerformed(evt);
        }
    });

    jLabel1.setText("Spam Amount");

    TrunkList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    TrunkList.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            TrunkListKeyReleased(evt);
        }
    });
    jScrollPane1.setViewportView(TrunkList);

    BranchList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    BranchList.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            BranchListKeyReleased(evt);
        }
    });
    jScrollPane2.setViewportView(BranchList);

    jSlider6.setToolTipText("");
    jSlider6.setValue(0);

    jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

    jLabel7.setText("Manual <Trunk + Branch>");

    jButtonPause.setText("Pause Spam");
    jButtonPause.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonPauseActionPerformed(evt);
        }
    });

    jLabel8.setText("Trunk");

    jLabel9.setText("Branch");

    jLabel10.setText("latest tip + new tip");

    jSlider7.setToolTipText("");
    jSlider7.setValue(0);

    jLabel11.setText("new + new tip");

    jSlider8.setToolTipText("");
    jSlider8.setValue(0);

    jLabel12.setText("Pyramid");

    jSlider9.setToolTipText("");
    jSlider9.setValue(0);

    jLabel14.setText("Manual <Trunk + new tip>");

    jSlider10.setToolTipText("");
    jSlider10.setValue(0);

    jButtonClearHistory.setText("Clear History");
    jButtonClearHistory.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonClearHistoryActionPerformed(evt);
        }
    });

    jScrollPane5.setViewportView(jList3);

    manualHashTextField.setToolTipText("");
    manualHashTextField.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            manualHashTextFieldKeyReleased(evt);
        }
    });

    manualHash1Label.setText("Manual Hash");

    AddManualHashButton.setText("Add");
    AddManualHashButton.setToolTipText("Add a specific hash to the Trunk /Branch List");
    AddManualHashButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            AddManualHashButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel2)
                .addComponent(jLabel3)
                .addComponent(jLabel11)
                .addComponent(jLabel4)
                .addComponent(jLabel5)
                .addComponent(jLabel6)
                .addComponent(jLabel10)
                .addComponent(jLabel12))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jSlider4, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSlider5, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSlider7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jSlider2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSlider8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jSlider9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jButtonClearHistory)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButtonPause)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButtonStartSpam, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(34, 34, 34)
                                    .addComponent(jRadioButton2))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addGap(37, 37, 37)
                                    .addComponent(jSlider10, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(jLabel8))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(2, 2, 2)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(39, 39, 39)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(manualHash1Label)
                                    .addGap(18, 18, 18)
                                    .addComponent(manualHashTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(AddManualHashButton))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(42, 42, 42)
                                    .addComponent(jSlider6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jSlider4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addComponent(jSlider5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel10)
                        .addComponent(jSlider7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSlider8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addGap(27, 27, 27)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12)
                        .addComponent(jSlider9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(15, 15, 15))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jSlider6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel8))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(manualHash1Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(manualHashTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AddManualHashButton))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jRadioButton2)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonStartSpam)
                                .addComponent(jButtonPause)
                                .addComponent(jButtonClearHistory)))
                        .addComponent(jSlider10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
    );

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Receiver"));

    jSpinner2.setToolTipText("Donation amount in i");

    jButtonStartTransaction.setText("Start Transaction");
    jButtonStartTransaction.setToolTipText("Prepares, validates and broadcasts a transaction");
    jButtonStartTransaction.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonStartTransactionActionPerformed(evt);
        }
    });

    jTextFieldReceiver.setToolTipText("");
    jTextFieldReceiver.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextFieldReceiverActionPerformed(evt);
        }
    });

    jButtonInsertDonAddress.setText("Insert Donation Address");
    jButtonInsertDonAddress.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonInsertDonAddressActionPerformed(evt);
        }
    });

    jLabel13.setText("Transfer Amount");

    jButtonPrepareTransaction.setText("Prepare");
    jButtonPrepareTransaction.setToolTipText("prepares a transaction without PoW");
    jButtonPrepareTransaction.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonPrepareTransactionActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTextFieldReceiver)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addComponent(jButtonInsertDonAddress)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13)
                    .addGap(18, 18, 18)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jButtonPrepareTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(17, 17, 17)
                    .addComponent(jButtonStartTransaction)))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTextFieldReceiver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(9, 9, 9)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonInsertDonAddress)
                .addComponent(jLabel13))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonPrepareTransaction)
                .addComponent(jButtonStartTransaction)))
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));
    jPanel3.setToolTipText("Your Personal Seed. If sum of all 'Reciver' is 0, no Seed needed.");

    jTextFieldInput.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextFieldInputActionPerformed(evt);
        }
    });

    jButtonShowState.setText("Show State");
    jButtonShowState.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonShowStateActionPerformed(evt);
        }
    });

    jLableBalance.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
    jLableBalance.setText("**************************");

    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(EInputTypes.values()));
    jComboBox1.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jComboBox1ItemStateChanged(evt);
        }
    });

    jLabel15.setText("start index");

    jLabel16.setText("end index");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addComponent(jLableBalance)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonShowState))
                .addComponent(jTextFieldInput, javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(34, 34, 34)
                    .addComponent(jLabel15)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(19, 19, 19)
                    .addComponent(jLabel16)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(4, 4, 4)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonShowState)
                .addComponent(jLableBalance))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Log Screen"));

    jTextAreaLogScreen.setEditable(false);
    jTextAreaLogScreen.setColumns(20);
    jTextAreaLogScreen.setRows(5);
    jScrollPane3.setViewportView(jTextAreaLogScreen);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane3)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Info Screen"));

    jTextAreaInfoScreen.setEditable(false);
    jTextAreaInfoScreen.setColumns(20);
    jTextAreaInfoScreen.setRows(5);
    jScrollPane4.setViewportView(jTextAreaInfoScreen);

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
    );

    jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Tangle Listener"));

    jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(EBlockerInputs.values()));

    jTextFieldListener.setToolTipText("");
    jTextFieldListener.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextFieldListenerActionPerformed(evt);
        }
    });

    jButtonStartListener.setText("Start Listener");
    jButtonStartListener.setToolTipText("start to search after the specified has. Once the hash made a move on the tangle, blocking methodes can be applied");
    jButtonStartListener.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonStartListenerActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTextFieldListener)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonStartListener)))
            .addContainerGap())
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel6Layout.createSequentialGroup()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonStartListener))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldListener, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    jMenu1.setText("Connection Settings");

    jRadioButtonMenuItemSingle.setText("Single Connection");
    jRadioButtonMenuItemSingle.setToolTipText("All commands send to one full-node");
    jRadioButtonMenuItemSingle.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jRadioButtonMenuItemSingleItemStateChanged(evt);
        }
    });
    jMenu1.add(jRadioButtonMenuItemSingle);

    jRadioButtonMenuItemMultiple.setSelected(true);
    jRadioButtonMenuItemMultiple.setText("Multiple Connections");
    jRadioButtonMenuItemMultiple.setToolTipText("Creating multiple connections to varrious full-nodes. Reccomendet for spaming.");
    jMenu1.add(jRadioButtonMenuItemMultiple);

    jMenuBar1.add(jMenu1);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(10, 10, 10)))
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButtonStartTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartTransactionActionPerformed
        job = new Thread(() -> {
            con.transaction();
        }, "Transaction Thread");
        job.start();
    }//GEN-LAST:event_jButtonStartTransactionActionPerformed

    private void jTextFieldInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldInputActionPerformed

    private void jButtonShowStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowStateActionPerformed
        new Thread(() -> {
            con.loadInfos();
        }, "Load Info Thread").start();

    }//GEN-LAST:event_jButtonShowStateActionPerformed

    private void jButtonStartListenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartListenerActionPerformed
        String listen = getListentoHash();
        ticker.addSpy(listen, getBlockerType());
//        if (worker != null) {
//            worker.cancel(true);
//        }
//        worker = new Job() {
//            @Override
//            protected Void doInBackground() {
//                con.addListener(this::publish);
//                return null;
//            }
//        };
//        worker.execute();
    }//GEN-LAST:event_jButtonStartListenerActionPerformed

    private void jRadioButtonMenuItemSingleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItemSingleItemStateChanged
        con.reloadConnections();  // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButtonMenuItemSingleItemStateChanged

    private void jButtonInsertDonAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertDonAddressActionPerformed
        jTextFieldReceiver.setText("WJWFNLVTTFIFDFSBUWKBEFCHEEBTAXM9HVJJXJXV9DPVLSLPYLKU9CRFHESHEJZYIGWBNI9UYYWFXRJHDEXSGUZKKD");        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonInsertDonAddressActionPerformed

    private void jTextFieldReceiverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldReceiverActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldReceiverActionPerformed

    private void jTextFieldListenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldListenerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldListenerActionPerformed


    private void jButtonPrepareTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrepareTransactionActionPerformed
        new Thread(() -> {
            con.prepare();
        }, "Prepare Thread").start();
    }//GEN-LAST:event_jButtonPrepareTransactionActionPerformed

    private void AddManualHashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddManualHashButtonActionPerformed
        addManualHash();
    }//GEN-LAST:event_AddManualHashButtonActionPerformed

    private void manualHashTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_manualHashTextFieldKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addManualHash();
        }
    }//GEN-LAST:event_manualHashTextFieldKeyReleased

    private void jButtonClearHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearHistoryActionPerformed
        trunkBranchModel.removeAllElements();
        con.clearHistory();
    }//GEN-LAST:event_jButtonClearHistoryActionPerformed

    private void jButtonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPauseActionPerformed

        if (jButtonPause.getText().equals("Pause Spam")) {
            paused = true;
            jButtonPause.setText("Continue Spam");
            //job.wait();
        } else {
            //job.notify();
            paused = false;
            jButtonPause.setText("Pause Spam");
        }
    }//GEN-LAST:event_jButtonPauseActionPerformed

    private void BranchListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BranchListKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            trunkBranchModel.removeElementAt(BranchList.getSelectedIndex());
        }
    }//GEN-LAST:event_BranchListKeyReleased

    private void TrunkListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TrunkListKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            trunkBranchModel.removeElementAt(TrunkList.getSelectedIndex());
        }
    }//GEN-LAST:event_TrunkListKeyReleased

    private void jButtonStartSpamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartSpamActionPerformed
        job = new Thread(() -> {
            con.spam();
        }, "Spam Thread");
        job.start();
    }//GEN-LAST:event_jButtonStartSpamActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        System.err.println(jComboBox1.getSelectedItem());
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.err.println("closed");
    }//GEN-LAST:event_formWindowClosed
         
    private void addManualHash(){
    String hash = manualHashTextField.getText().replaceAll("\\s", "");
    String emptyAddress = "";
    BalanceRecorder newManualHash = new BalanceRecorder("Manual Hash: " + hash.substring(0,5), hash, emptyAddress);
        
    if(trunkBranchModel != null){
        trunkBranchModel.add(newManualHash);
    }
 }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddManualHashButton;
    private javax.swing.JList<String> BranchList;
    private javax.swing.JList<String> TrunkList;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClearHistory;
    private javax.swing.JButton jButtonInsertDonAddress;
    private javax.swing.JButton jButtonPause;
    private javax.swing.JButton jButtonPrepareTransaction;
    private javax.swing.JButton jButtonShowState;
    private javax.swing.JButton jButtonStartListener;
    private javax.swing.JButton jButtonStartSpam;
    private javax.swing.JButton jButtonStartTransaction;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLableBalance;
    private javax.swing.JList<String> jList3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemMultiple;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItemSingle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider10;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JSlider jSlider6;
    private javax.swing.JSlider jSlider7;
    private javax.swing.JSlider jSlider8;
    private javax.swing.JSlider jSlider9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JTextArea jTextAreaInfoScreen;
    private javax.swing.JTextArea jTextAreaLogScreen;
    private javax.swing.JTextField jTextFieldInput;
    private javax.swing.JTextField jTextFieldListener;
    private javax.swing.JTextField jTextFieldReceiver;
    private javax.swing.JLabel manualHash1Label;
    private javax.swing.JTextField manualHashTextField;
    // End of variables declaration//GEN-END:variables

    private class TrunkBanchListModel extends DefaultListModel {

        protected List<BalanceRecorder> balanceRecorders = new ArrayList<>();

        public void add(BalanceRecorder b) {
            balanceRecorders.add(b);
            super.addElement(b.getDisplayname());
        }

        public String getHashAt(int index) {
            if (index >= 0 && index < balanceRecorders.size()) {
                return balanceRecorders.get(index).getHash();
            } else {
                return "";
            }
        }
               
        @Override
        public void removeElementAt(int index) {
            balanceRecorders.remove(index);
            super.remove(index);
        }

        public BalanceRecorder getBalanceRecorderat(int index) {
            if (index >= 0 && index < balanceRecorders.size()) {
                return balanceRecorders.get(index);
            } else {
                return null;
            }
        }

    }

    private class BundleListModel extends DefaultListModel {

        protected List<Bundle> preparedBundles = new ArrayList<>();

        public void add(Bundle b) {
            preparedBundles.add(b);
            super.addElement("prepared bundle " + preparedBundles.size());

        }

//        public String getHashAt(int index) {
//            if (index >= 0 && index < preparedBundles.size()) {
//                return preparedBundles.get(index).getHash();
//            } else {
//                return "";
//            }
//        }
        public Bundle getBundleAt(int index) {
            if (index >= 0 && index < preparedBundles.size()) {
                return preparedBundles.get(index);
            } else {
                return null;
            }
        }

    }

    private class Job extends SwingWorker<Void, Publish> {

        private boolean shuttdown = false;

        @Override
        protected Void doInBackground() {
            while (!shuttdown) {
                try {
                    this.wait(5000);
                } catch (InterruptedException ex) {
                    System.out.println("asdfasdfasdfasdf");
                    Logger.getLogger(ControlerPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return null;
        }

        @Override
        protected void process(List<Publish> topublish) {
            topublish.forEach(pair -> {
                if (pair.getInfoLog() != null) {
                    jTextAreaInfoScreen.selectAll();
                    jTextAreaInfoScreen.replaceSelection("");
                    jTextAreaInfoScreen.append(pair.getInfoLog() + "\n");
                }
                if (pair.getBalanceRecorder() != null) {
                    trunkBranchModel.add(pair.getBalanceRecorder());
                    if (pair.getBalanceRecorder().getAlertWhenChangedByValueOf() > 0) {
                        ticker.addBalanceRecorder(pair.getBalanceRecorder());
                    }
                }
                if (pair.getBundle() != null) {
                    bundleListModel.add(pair.getBundle());
                }
                if (pair.getBalance() != null) {
                    jLableBalance.setText(pair.getBalance());
                }
                if (pair.getLog() != null) {
                    try {
                        jTextAreaLogScreen.append(">> " + pair.getLog() + "\n");
                    } catch (java.lang.IllegalStateException ex) {
                        System.out.println("Exception thrown " + ex.getMessage());
                    }
                }
            });
        }

        @Override
        protected void done() {

            System.out.println("jota.ControlerPanel.Job.done()");
//             throw new UnsupportedOperationException("why u done bitch."); 
            //publish(null);
        }

        public IPublish getPublisherMethod() {
            return this::publish;
        }

    }

    private class SharedListSelectionHandler implements ListSelectionListener {

        private ILogInfo lf;
        private JList j;

        SharedListSelectionHandler(ILogInfo lf, JList j) {
            this.lf = lf;
            this.j = j;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                BalanceRecorder b = ((TrunkBanchListModel) j.getModel()).getBalanceRecorderat(j.getSelectedIndex());
                if(b!=null){
                    lf.logInfo(b.toString());
                }else{
                    lf.logInfo("");
                }
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ControlerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ControlerPanel().setVisible(true);
        });
    }
}
