package org.eddie.crypto;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main extends Component
{
    private JTextArea inputStringTextArea;
    //    private JButton helpButton;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTextField key1_1;
    private JTextField key1_2;
    private JTextField key1_3;
    private JTextField key2_1;
    private JTextField key2_2;
    private JTextField key2_3;
    private JTextField key3_1;
    private JTextField key3_2;
    private JTextField key3_3;
    private JTextField invertedKey1_1;
    private JTextField invertedKey1_2;
    private JTextField invertedKey1_3;
    private JTextField invertedKey2_1;
    private JTextField invertedKey2_2;
    private JTextField invertedKey2_3;
    private JTextField invertedKey3_1;
    private JTextField invertedKey3_2;
    private JTextField invertedKey3_3;
    private JTextPane outputStringTextArea;

    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        }
        catch(UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }

        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Cryptography Example");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Main()
    {
        tabbedPane1.addChangeListener(e -> {
            Object sourceObj = e.getSource();

            if(JTabbedPane.class.isInstance(sourceObj))
            {
                JTabbedPane source = (JTabbedPane) sourceObj;

                int tabNumber = source.getSelectedIndex();

                handleTabSelected(tabNumber);
            }
        });
    }

    protected void handleTabSelected(int tabNumber)
    {
        Matrix keyMatrix = null;

        boolean failedToProcessKey = false;

        try
        {
            FractionFormat fractionFormat = new FractionFormat();

            Fraction[][] fractionArray = {{fractionFormat.parse(key1_1.getText()), fractionFormat.parse(key1_2.getText()), fractionFormat.parse(key1_3.getText())}, {fractionFormat.parse(key2_1.getText()), fractionFormat.parse(key2_2.getText()), fractionFormat.parse(key2_3.getText())}, {fractionFormat.parse(key3_1.getText()), fractionFormat.parse(key3_2.getText()), fractionFormat.parse(key3_3.getText())}};

            keyMatrix = new Matrix(fractionArray);
        }
        catch(Exception e)
        {
            System.out.println("Failed to parse matrix, exception: " + e.toString());

            failedToProcessKey = true;
        }

        switch(tabNumber)
        {
            case 0:
                break;

            case 1:
                break;

            case 2: // Inverted Key Tab
                if(!failedToProcessKey)
                {
                    if(keyMatrix.isInvertible())
                    {
                        Matrix invertedMatrix = keyMatrix.invert();

                        invertedKey1_1.setText(invertedMatrix.matrix.get(0).get(0).getNumerator() + " / " + invertedMatrix.matrix.get(0).get(0).getDenominator());
                        invertedKey1_2.setText(invertedMatrix.matrix.get(0).get(1).getNumerator() + " / " + invertedMatrix.matrix.get(0).get(1).getDenominator());
                        invertedKey1_3.setText(invertedMatrix.matrix.get(0).get(2).getNumerator() + " / " + invertedMatrix.matrix.get(0).get(2).getDenominator());

                        invertedKey2_1.setText(invertedMatrix.matrix.get(1).get(0).getNumerator() + " / " + invertedMatrix.matrix.get(1).get(0).getDenominator());
                        invertedKey2_2.setText(invertedMatrix.matrix.get(1).get(1).getNumerator() + " / " + invertedMatrix.matrix.get(1).get(1).getDenominator());
                        invertedKey2_3.setText(invertedMatrix.matrix.get(1).get(2).getNumerator() + " / " + invertedMatrix.matrix.get(1).get(2).getDenominator());

                        invertedKey3_1.setText(invertedMatrix.matrix.get(2).get(0).getNumerator() + " / " + invertedMatrix.matrix.get(2).get(0).getDenominator());
                        invertedKey3_2.setText(invertedMatrix.matrix.get(2).get(1).getNumerator() + " / " + invertedMatrix.matrix.get(2).get(1).getDenominator());
                        invertedKey3_3.setText(invertedMatrix.matrix.get(2).get(2).getNumerator() + " / " + invertedMatrix.matrix.get(2).get(2).getDenominator());
                    }
                    else
                    {
                        invertedKey1_1.setText("Matrix");
                        invertedKey1_2.setText("Not");
                        invertedKey1_3.setText("Invertible");
                    }
                }
                break;

            case 3: // Crypto Output Tab
                if(!failedToProcessKey)
                {
                    if(!keyMatrix.isInvertible())
                    {
                        outputStringTextArea.setText("Unable to invert key matrix.");

                        return;
                    }

                    StringBuilder stringBuilder = new StringBuilder(inputStringTextArea.getText());

                    int remainder = 3 - (stringBuilder.toString().length() % 3);

                    for(int i = 0; i < remainder; i++)
                    {
                        stringBuilder.append(" ");
                    }

                    String inputString = stringBuilder.toString();

                    int[] characterNumberArray = new int[inputString.length()];

                    int pos = 0;

                    for(char ch : inputString.toLowerCase().toCharArray())
                    {
                        if(ch == ' ')
                        {
                            characterNumberArray[pos] = 27;
                        }
                        else
                        {
                            characterNumberArray[pos] = ch - 'a' + 1;
                        }

                        pos++;
                    }

                    int columnCount = inputString.length() / 3;

                    int[][] stringIntMatrix = new int[3][columnCount];

                    pos = 0;

                    for(int column = 0; column < columnCount; column++)
                    {
                        for(int row = 0; row < 3; row++)
                        {
                            stringIntMatrix[row][column] = characterNumberArray[pos];

                            pos++;
                        }
                    }

                    Matrix stringMatrix = new Matrix(stringIntMatrix);

                    Matrix output = keyMatrix.multiplyByMatrix(stringMatrix);

                    outputStringTextArea.setText(output.toString());
                }
                break;

            default:
                System.out.println("No case for tab number " + tabNumber);
                break;
        }
    }
}
