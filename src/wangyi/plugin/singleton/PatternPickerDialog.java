package wangyi.plugin.singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * author WangYi
 * created on 2017/1/14.
 * 选择单例实现模式
 */
public class PatternPickerDialog extends JDialog {
    private JPanel mContentPane;
    private JButton mButtonOK;
    private JButton mButtonCancel;
    private JList mListPattern;

    private OnPatternSelectedListener mListener;

    public PatternPickerDialog() {
        setContentPane(mContentPane);
        setModal(true);
        getRootPane().setDefaultButton(mButtonOK);
        setTitle("Choose a Pattern");

        mButtonOK.addActionListener(e -> onOK());
        mButtonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        mContentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        mListPattern.setSelectedIndex(0);
        mListPattern.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mListPattern.addKeyListener(new KeyListener() {
            int preIndex = -1;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                preIndex = mListPattern.getSelectedIndex();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int selectedIndex = mListPattern.getSelectedIndex();
                if (preIndex == selectedIndex) {
                    if (selectedIndex == 0) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            mListPattern.setSelectedIndex(4);
                        }
                    } else if (selectedIndex == 4) {
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            mListPattern.setSelectedIndex(0);
                        }
                    }
                }
            }
        });
        setBounds(new Rectangle(300, 200));
        setLocationRelativeTo(null);
    }

    private void onOK() {
        dispose();
        if (mListener != null) {
            mListener.onPatternSelected(mListPattern.getSelectedIndex());
        }
    }

    private void onCancel() {
        dispose();
    }

    public void setOnPatternSelectedListener(OnPatternSelectedListener mListener) {
        this.mListener = mListener;
    }
}
