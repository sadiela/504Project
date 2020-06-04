import Checker.Checker;
import Crawler.Crawler;
import Serial.Serial;
import TextProcessor.TextProcessor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.List;

//Class to implement gray hint text in the textboxes

class JTextFieldHintUI extends BasicTextFieldUI implements FocusListener {
    private String hint;
    private Color hintColor;

    public JTextFieldHintUI(String hint, Color hintColor) {
        this.hint = hint;
        this.hintColor = hintColor;
    }

    private void repaint() {
        if (getComponent() != null) {
            getComponent().repaint();
        }
    }

    @Override
    protected void paintSafely(Graphics g) {
        // Render the default text field UI
        super.paintSafely(g);
        // Render the hint text
        JTextComponent component = getComponent();
        if (component.getText().length() == 0 && !component.hasFocus()) {
            g.setColor(hintColor);
            int padding = (component.getHeight() - component.getFont().getSize()) / 2;
            int inset = 3;
            g.drawString(hint, inset, component.getHeight() - padding - inset);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        repaint();
    }

    @Override
    public void installListeners() {
        super.installListeners();
        getComponent().addFocusListener(this);
    }

    @Override
    public void uninstallListeners() {
        super.uninstallListeners();
        getComponent().removeFocusListener(this);
    }
}

//Main Class containing GUI definition and associated functions

public class gui extends JFrame {

    //Class scope objects for usability

    Checker myChecker = new Checker();
    Crawler myCrawler = new Crawler();

    //Initialise the GUI elements
    JFrame frame = new JFrame("Language Checker GUI");
//    JPanel frame = new JPanel();
    JButton crawl_button = new JButton("Crawl 1 Gb");
    JButton gen_button = new JButton("Generate");
    JButton check_button = new JButton("Check");
    JButton load_button = new JButton("Load Data");
    JButton stop_button = new JButton("Stop Crawling");
    JTextField tarea1 = new JTextField("");
    JTextField tarea2 = new JTextField("");
    JTextField tarea3 = new JTextField("");
    JTextField tarea4 = new JTextField("");
    JProgressBar progressBar = new JProgressBar(0, 100);
    String LanguageOptions[] = {"English", "Spanish", "French", "Italian"};
    String DataBaseOptions[] = {"User Database", "Original Database"};
    JComboBox lang_drop_down = new JComboBox(LanguageOptions);
    JComboBox data_drop_down = new JComboBox(DataBaseOptions);

    JLabel label_1 = new JLabel("Database Preferences");
    JLabel label_2 = new JLabel("Crawler");
    JLabel label_3 = new JLabel("Checker");
    JLabel label_4 = new JLabel("Progress Bar");


    String selected_text = tarea1.getText();
    int x_val = 10, y_val = 10;

    //Function to create and instance of the GUI and instantiate its elements

    public void create_gui() {
        tarea1.setUI(new JTextFieldHintUI("Insert link here", Color.gray));
        tarea2.setUI(new JTextFieldHintUI("Enter a file path here", Color.gray));
        tarea3.setUI(new JTextFieldHintUI("Enter a test sentence here", Color.gray));
        tarea4.setUI(new JTextFieldHintUI("Suspicion", Color.gray));

        //Set the bounds for the various gui elements

        // Database
        label_1.setBounds(x_val, y_val, 300, 20);
        lang_drop_down.setBounds(x_val + 20, y_val + 30, 120, 20);
        data_drop_down.setBounds(x_val + 160, y_val + 30, 120, 20);
        load_button.setBounds(x_val + 100, y_val + 60, 100, 20);

        // Crawler
        label_2.setBounds(x_val, y_val + 90, 300, 20);
        tarea1.setBounds(x_val, y_val + 120, 300, 20);
        crawl_button.setBounds(x_val + 20, y_val + 150, 120, 20);
        stop_button.setBounds(x_val + 160, y_val + 150, 120, 20);


        // Progress Bar
        label_4.setBounds(x_val, y_val + 180, 300, 20);
        progressBar.setBounds(x_val, y_val + 210, 300, 20);



        // Checker
        label_3.setBounds(x_val, y_val + 240, 300, 20);
        tarea2.setBounds(x_val, y_val + 270, 300, 20);
        gen_button.setBounds(x_val + 100, y_val + 300, 100, 20);

        tarea3.setBounds(x_val, y_val + 330, 200, 20);
        tarea4.setBounds(x_val + 220, y_val + 330, 80, 20);
        tarea4.setEditable(false);
        check_button.setBounds(x_val + 100, y_val + 360, 100, 20);


        //Classes to enable background function calls to prevent GUI button freezes

        class back_crawler extends SwingWorker<List<Integer>, Integer> {

            //@Override
            protected List<Integer> doInBackground() throws Exception {

                String ar = tarea1.getText();
                try {
                    Set<String> arg = new HashSet<String>();
                    arg.add(ar);
                    progressBar.setValue(0);
                    myCrawler.unsetStop();
                    callCrawler(arg, lang_drop_down.getSelectedItem().toString());
                } catch (IllegalAccessException illegalAccessException) {
                    //illegalAccessException.printStackTrace();
                    System.out.println("illegalAccessException");
                } catch (InterruptedException interruptedException) {
                    //interruptedException.printStackTrace();
                    System.out.println("interruptedException");
                } catch (InstantiationException instantiationException) {
                    //instantiationException.printStackTrace();
                    System.out.println("instantiationException");
                } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    //instantiationException.printStackTrace();
                    System.out.println("indexOutOfBoundsException");
                    JOptionPane.showMessageDialog(frame, "Invalid URL! Did you forget the s in https or the / at the end of the url?");
                }
                return null;
            }

            //@Override
            protected void done() {
                JOptionPane.showMessageDialog(frame, "Crawled!");
            }
        }

    class back_builder extends SwingWorker<List<Integer>, Integer> {

        //@Override
        protected List<Integer> doInBackground() throws Exception {
            try {
                progressBar.setValue(0);
                String path;
                if (data_drop_down.getSelectedItem().toString().equals("User Database")) {
                    path = "src/UserDataBase/";
                } else {
                    path = "src/DataBase/";
                }

                buildChecker(lang_drop_down.getSelectedItem().toString(), path);
            } catch (java.nio.file.AccessDeniedException accessDeniedException) {
                JOptionPane.showMessageDialog(frame, "Some error has occurred.");
            }
            return null;
        }

        //@Override
        protected void done() {
            System.out.println("Done Loading data.");
            JOptionPane.showMessageDialog(frame, "Checker Rebuilt!");
        }
    }

    //Button

        crawl_button.addActionListener(new

    ActionListener() {
        //@Override
        public void actionPerformed (ActionEvent e){
            back_crawler c = new back_crawler();
            try {
                c.execute();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    });
        gen_button.addActionListener(new

    ActionListener() {
        //@Override
        public void actionPerformed (ActionEvent e){
            try {
                genFile(tarea2.getText());
                JOptionPane.showMessageDialog(frame, "New File Generated!");
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(frame, "Path not correct :(");
            }
        }
    });
        check_button.addActionListener(new

    ActionListener() {
        //@Override
        public void actionPerformed (ActionEvent e){
            String s = null;
            try {
                if (myChecker.loadStatus) {
                    s = Integer.toString(getSuspicion(tarea3.getText(), lang_drop_down.getSelectedItem().toString()));
                }
                if(!myChecker.loadStatus){
                    JOptionPane.showMessageDialog(frame, "The Checker has not yet finished loading!");
                }

            } catch (AccessDeniedException | NoSuchFileException accessDeniedException) {
                JOptionPane.showMessageDialog(frame, "Some Exception has occured.");
            }
            tarea4.setText(s);
        }
    });
        load_button.addActionListener(new

    ActionListener() {
        //@Override
        public void actionPerformed (ActionEvent e){
            back_builder b = new back_builder();
            try {
                b.execute();

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    });
        stop_button.addActionListener(new

    ActionListener() {
        //@Override
        public void actionPerformed (ActionEvent e){
            try {
                myCrawler.stop();
            } catch (Exception er) {
                JOptionPane.showMessageDialog(frame, "Something Went Wrong!");
                er.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "Crawling Stopped!");


        }
    });
        frame.add(crawl_button);
        frame.add(gen_button);
        frame.add(check_button);
        frame.add(load_button);
        frame.add(stop_button);
        frame.add(tarea1);
        frame.add(tarea2);
        frame.add(tarea3);
        frame.add(tarea4);
        frame.add(lang_drop_down);
        frame.add(data_drop_down);
        frame.add(label_1);
//        frame.add(new JSeparator());
        frame.add(label_2);
        frame.add(label_3);
        frame.add(label_4);
        frame.add(progressBar);

        frame.setSize(350,450);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private String callCrawler(Set<String> urls, String lang) throws IllegalAccessException, InterruptedException, InstantiationException, IndexOutOfBoundsException {
        myCrawler.unsetStop();

        String path = "src/UserDataBase/";
        int databaseSize = (int) Math.pow(10, 9); // 1 GB == 10^9 || 1 MB = 10 ^ 6
        Runnable crawl_runner = new Runnable() {
            @Override
            public void run() {
                while (myCrawler.getProgress() < 100 && (!myCrawler.getStop())) {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressBar.setValue((int) myCrawler.getProgress());
                }
                System.out.println("Crawling Done!");
            }
        };
        Thread crawl_progress_thread = new Thread(crawl_runner);
        crawl_progress_thread.start();
        myCrawler.scrape(urls, databaseSize, path, lang);
        crawl_progress_thread.interrupt();
        return "Done";
    }


    private Integer getSuspicion(String text, String lang) throws AccessDeniedException, NoSuchFileException {
        this_func:
        {
            if (!myChecker.loadStatus) {
                JOptionPane.showMessageDialog(frame, "The Checker has not yet finished loading!");
                break this_func;
            }
            TextProcessor textProcessor = new TextProcessor();
            String cleanedSentence = textProcessor.clean(text, lang);
            System.out.println(myChecker);
            return (int) (100.0 * myChecker.check(cleanedSentence));
        }
        return null;
    }


    private void genFile(String path) {
        try {
            Checker check = new Checker();
            check.checkFile(path);
        }  catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, "Path not correct :(");
        }
    }


    private String buildChecker(String lang, String path) throws AccessDeniedException, InterruptedException {
        myChecker = new Checker();
        System.out.println("Decompressing Database");
        Serial serial = new Serial("");
        String[] sentences = serial.decompress(path).split("\\. ");
        System.out.println(sentences.length);
        System.out.println("Rebuilding Checker");
        Runnable build_runner = new Runnable() {
            @Override
            public void run() {
                while (myChecker.getProgress() < 100) {
                    try{
                        Thread.sleep(5000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(myChecker.getProgress());
                    progressBar.setValue((int) myChecker.getProgress());
                    System.out.println((int) myChecker.getProgress());
                }
                System.out.println("Checker Rebuilt!");
            }
        };
        Thread build_progress_thread = new Thread(build_runner);
        build_progress_thread.start();
        myChecker = new Checker(sentences, lang);
        return "Done";
    }
}
