package encrypt;

/*
 * 
 *      This file sometimes takes several tries to "render" when I run it.
 * 
 * 
 */




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Encrypt
{
    Font myFont;
    Boolean noKey;
    JScrollPane jsp;
    JTextField keyField;
    JFrame frame = new JFrame();
    JFileChooser jfc;
    JEditorPane jep;
        
    Color myPinkWhite;
    Color myWhite;
    Color pink;
    
    JLabel ti;
    JLabel spacer;
    JLabel spacer1; 
    JLabel encInstructions;    
    
    JButton encAndSave;
    JButton regSave;
    JButton openFile;
    JButton openRegFile;

    JPanel accessory;
    JPanel contentPanel;
    JPanel buttonPanel;
    JPanel textPanel;

    PrintWriter writeFileToEncrypt;
    PrintWriter writeFileToDecrypt;
    PrintWriter regFile;
    
    File fileToEncrypt;
    File fileToDecrypt;   
    File encryptedFile;
    File decryptedFile;
    
    String key;
    String toEncrypt_Filename;
    String toDecrypt_Filename;
    String currentDirectory;
    
    
    public Encrypt()
    {
        myFont = new Font("Segoe UI Symbol", Font.PLAIN, 16);
        pink = new Color(171, 204, 119);
        myWhite = new Color(244, 255, 242);
        myPinkWhite = new Color(243, 229, 235);
        
        makeJFileChooser_Accessory();
        makeFrame();
        makeTextInstructions();
        makeButtons();

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(200, 800));
        buttonPanel.setBackground(pink);
        addComponentsToButtonPanel(buttonPanel);       
        

        jep = new JEditorPane();        
        jep.setMargin(new Insets(30, 50, 0, 50));
        jep.setFont(myFont);

        textPanel = new JPanel(new BorderLayout());
        textPanel.add(ti, BorderLayout.NORTH);
        textPanel.add(jep, BorderLayout.CENTER);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(buttonPanel, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        jsp = new JScrollPane(contentPanel);        

        frame.add(jsp);
        frame.pack();  // .pack() learned in WSU CS 3230 
        
        noKey = true;
        jep.requestFocus();

    }

    private void makeJFileChooser_Accessory()
    {
        
        encInstructions = new JLabel();
        encInstructions.setText("Type in your encryption key below.");
        encInstructions.setOpaque(false);
        encInstructions.setPreferredSize(new Dimension(320, 30));

        keyField = new JTextField("");
        keyField.setText("");
        keyField.setBorder(new LineBorder(Color.BLACK));
        keyField.setPreferredSize(new Dimension(320, 30));
        keyField.setRequestFocusEnabled(true);
        keyField.requestFocusInWindow();
        accessory = new JPanel();
        accessory.setPreferredSize(new Dimension(330, 80));

        accessory.setLayout(new FlowLayout());
        accessory.add(encInstructions);
        accessory.add(keyField);
    }

    private void makeFrame()
    {
        frame.setSize(1100, 800);
        frame.setLocation(10, 5);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void makeTextInstructions()
    {
        ti = new JLabel("Add text below by either typing/pasting your own, or by opening a file.");
        ti.setFont(myFont);
        ti.setPreferredSize(new Dimension(900, 26));
        ti.setBackground(pink);
        ti.setOpaque(true);
    }
    private void setButtonSettings(JButton jb, String s, ActionListener al, Color c)
    {
        jb.setText(s);
        jb.addActionListener(al);
        jb.setBackground(c);
        jb.setPreferredSize(new Dimension(190, 60));
        jb.setMargin(new Insets(5, 0, 6, 0));
        jb.setFont(myFont);
        jb.setFocusPainted(false);        
    }
    private void makeButtons()
    {
        spacer1 = new JLabel();
        spacer1.setPreferredSize(new Dimension(190, 16));
        spacer1.setVisible(true);
        
        encAndSave = new JButton();
        setButtonSettings(encAndSave, "Save And Encrypt", (new Save_Listener()), myPinkWhite);        

        regSave = new JButton();
        setButtonSettings(regSave, "Save Without Encrypting", (new Save_Listener()), myWhite);
        
        openFile = new JButton();
        setButtonSettings(openFile, "Open Encrypted File", (new Open_Listener()), myPinkWhite);
        
        openRegFile = new JButton();
        setButtonSettings(openRegFile, "Open Non-Encrypted File", (new Open_Listener()), myWhite);
      
        spacer = new JLabel();
        spacer.setPreferredSize(new Dimension(190, 60));
        spacer.setVisible(true);

    }

    private void addComponentsToButtonPanel(JPanel jp)
    {
        jp.add(spacer1);
        jp.add(openFile);
        jp.add(openRegFile);
        jp.add(spacer);
        jp.add(encAndSave);
        jp.add(regSave);
    }
 

    private class Open_Listener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            jfc = new JFileChooser("Your Files");

            if (((JButton) ae.getSource()).equals(openRegFile))
            {
                jfc.showOpenDialog(jep);
                String regFilename = jfc.getCurrentDirectory() + "//" + jfc.getSelectedFile().getName();
                try
                {
                    File regFileFile = new File(regFilename);
                    DataInputStream regFile = new DataInputStream(new FileInputStream(regFileFile));
                    String text = "";
                    try
                    {
                        while (regFile.available() > 0)
                        {
                            text += (char) regFile.readByte();
                        }
                        jep.setText(text);

                    } catch (IOException ex)
                    {                       
                    }
                } catch (FileNotFoundException ex)
                {                   
                }
            } else
            {
                jfc.setAccessory(accessory);
                while (noKey)
                {
                    int choice = jfc.showDialog(frame, "Open Encrypted File");

                    if (choice == JFileChooser.CANCEL_OPTION || choice == JFileChooser.ABORT)
                    {
                        noKey = false;
                    } else if (choice == JFileChooser.APPROVE_OPTION
                            && (!(keyField.getText().isEmpty())))
                    {
                        key = keyField.getText();

                        toDecrypt_Filename = jfc.getCurrentDirectory() + "//" + jfc.getSelectedFile().getName();
                        fileToDecrypt = new File(toDecrypt_Filename);
                        decryptedFile = new File("decrypted.txt");

                        try
                        {
                            Encrypt.decrypt(fileToDecrypt, decryptedFile, key);
                            DataInputStream textFromDecryptedFile = new DataInputStream(new FileInputStream(decryptedFile));
                            String text = "";
                            while (textFromDecryptedFile.available() > 0)
                            {
                                text += (char) textFromDecryptedFile.readByte();
                                jep.setText(text);
                            }
                        } catch (IOException ex)
                        {
                        }

                        try //what is this for??
                        {
                            PrintWriter writeDecryptedFile = new PrintWriter(decryptedFile);
                            writeDecryptedFile.print("");
                        } catch (FileNotFoundException ex)
                        {
                        }
                        noKey = false;

                    } else
                    {
                        encInstructions.setText("You must first provide an encryption key.  Type it below.");
                        encInstructions.setForeground(Color.RED);
                        keyField.setBorder(new LineBorder(Color.RED));
                    }
                }

                noKey = true;
                encInstructions.setText("Type in your encryption key below.");
                encInstructions.setForeground(Color.BLACK);
                keyField.setBackground(Color.WHITE);
                keyField.setBorder(new LineBorder(Color.BLACK));
                keyField.setText("");
                jep.revalidate();
            }
        }
    }

    private class Save_Listener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            jfc = new JFileChooser("Your Files");

            if (((JButton) ae.getSource()).equals(regSave))
            {
                jfc.setSelectedFile(new File("Your_File_Name.txt"));
                if (jfc.showSaveDialog(jep) == JFileChooser.APPROVE_OPTION)
                {
                    String filename = jfc.getCurrentDirectory() + "//" + jfc.getSelectedFile().getName();
                    try
                    {
                        regFile = new PrintWriter(new File(filename));
                        regFile.write(jep.getText());
                        regFile.close();
                        
                    } catch (FileNotFoundException ex)
                    {                        
                    }
                }
                                
            } else
            {
                jfc.setAccessory(accessory);
                while (noKey)
                {
                    jfc.setSelectedFile(new File("Your_File_Name.txt"));

                    int choice = jfc.showDialog(frame, "Encrypt And Save");

                    if (choice == JFileChooser.CANCEL_OPTION || choice == JFileChooser.ABORT)
                    {
                        noKey = false;
                    } else if (choice == JFileChooser.APPROVE_OPTION
                            && (!(keyField.getText().isEmpty())))
                    {
                        key = keyField.getText();
                        toEncrypt_Filename = jfc.getCurrentDirectory() + "//" + jfc.getSelectedFile().getName();

                        fileToEncrypt = new File("toEncrypt.txt");
                        try
                        {
                            writeFileToEncrypt = new PrintWriter(fileToEncrypt);
                        } catch (FileNotFoundException ex)
                        {                           
                        }

                        writeFileToEncrypt.print(jep.getText());
                        writeFileToEncrypt.close();

                        encryptedFile = new File(toEncrypt_Filename);

                        try
                        {
                            Encrypt.encrypt(fileToEncrypt, encryptedFile, key);
                        } catch (IOException ex)
                        {
                        }

                        try
                        {
                            writeFileToEncrypt = new PrintWriter(fileToEncrypt);
                        } catch (FileNotFoundException ex)
                        {
                        }

                        writeFileToEncrypt.print("");
                        jep.setText("");

                        noKey = false;
                    } else
                    {
                        encInstructions.setText("You must first provide an encryption key.  Type it below.");
                        encInstructions.setForeground(Color.RED);
                        keyField.setBorder(new LineBorder(Color.RED));
                    }
                }

                noKey = true;
                encInstructions.setText("Type in your encryption key below.");
                encInstructions.setForeground(Color.BLACK);
                keyField.setBackground(Color.WHITE);
                keyField.setText("");
                keyField.setBorder(new LineBorder(Color.BLACK));

            }
        }
    }

    public static void encrypt(File toEnc, File enc, String key) throws IOException
    {
        DataInputStream toE = new DataInputStream(new FileInputStream(toEnc));
        DataOutputStream e = new DataOutputStream(new FileOutputStream(enc));

        char[] keyCh = key.toCharArray();

        int j = 0;
        while (toE.available() != 0)
        {
            e.writeInt(toE.readByte() + keyCh[j]);

            j++;
            if (j == keyCh.length)
            {
                j = 0;
            }
        }
        e.close();
    }

    public static void decrypt(File toDec, File dec, String key) throws IOException
    {
        DataInputStream toD = new DataInputStream(new FileInputStream(toDec));
        DataOutputStream d = new DataOutputStream(new FileOutputStream(dec));

        char[] keyCh = key.toCharArray();

        int j = 0;
        while (toD.available() > 0)
        {
            d.writeByte(toD.readInt() - keyCh[j]);

            j++;
            if (j == keyCh.length)
            {
                j = 0;
            }
        }
        d.close();
    }

    public static void main(String[] args) throws IOException
    {
        Encrypt hgN = new Encrypt();

    }
}
