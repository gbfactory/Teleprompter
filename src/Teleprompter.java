import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Teleprompter extends JPanel implements KeyListener {

    private static boolean isEditing = true;
    private static boolean isRunning = false;

    private static int spostamento = 1;

    private static JTextArea taText;
    private static JScrollPane spTele;
    private static JPanel pnlControlli;
    private static JButton btnPaste, btnStart, btnReset, btnAvanti, btnIndietro, btnSpeed, btnText, btnClear;

    private static Timer timer;

    Teleprompter() {

        timer = new Timer(50, e -> scrollBar(spostamento));

        taText = new JTextArea();
        taText.setFont(new Font("Verdana", Font.BOLD, 70));
        taText.setBackground(Color.BLACK);
        taText.setForeground(Color.WHITE);
        taText.setCaretColor(Color.GRAY);
        taText.setLineWrap(true);
        taText.setWrapStyleWord(true);

        spTele = new JScrollPane(taText);
        spTele.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spTele.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());

        add(spTele, BorderLayout.CENTER);

        // Controlli
        pnlControlli = new JPanel();
        pnlControlli.setLayout(new GridLayout(8, 1));

        btnStart = new JButton(new ImageIcon(this.getClass().getResource("start.png")));
        btnStart.addKeyListener(this);
        btnStart.addActionListener(e -> {
            toggleEdit();
            togglePrompter();
        });

        btnReset = new JButton(new ImageIcon(this.getClass().getResource("restart.png")));
        btnReset.addKeyListener(this);
        btnReset.addActionListener(e -> resetPrompter());

        btnAvanti = new JButton(new ImageIcon(this.getClass().getResource("doublearrowinv.png")));
        btnAvanti.addActionListener(e -> {
            scrollBar(-70);
        });

        btnIndietro = new JButton(new ImageIcon(this.getClass().getResource("doublearrow.png")));
        btnIndietro.addActionListener(e -> {
            scrollBar(70);
        });

        btnSpeed = new JButton(new ImageIcon(this.getClass().getResource("speed.png")));
        btnSpeed.addActionListener(e -> {
            JLabel lblNum = new JLabel(spostamento + "");
            lblNum.setHorizontalAlignment(JLabel.CENTER);
            lblNum.setFont(new Font("Verdana", Font.BOLD, 20));

            JButton btnAdd = new JButton("+");
            btnAdd.setFont(new Font("Verdana", Font.BOLD, 30));
            btnAdd.addActionListener(e1 -> {
                int num = Integer.parseInt(lblNum.getText());
                num++;
                lblNum.setText(num + "");
            });

            JButton btnRemove = new JButton("-");
            btnRemove.setFont(new Font("Verdana", Font.BOLD, 30));
            btnRemove.addActionListener(e1 -> {
                int num = Integer.parseInt(lblNum.getText());
                if (num > 1) {
                    num--;
                    lblNum.setText(num + "");
                }
            });

            JPanel panel = new JPanel(new GridLayout(1, 3));

            panel.add(btnRemove);
            panel.add(lblNum);
            panel.add(btnAdd);

            int result = JOptionPane.showConfirmDialog(null, panel, "Velocità", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                int num = Integer.parseInt(lblNum.getText());
                if (num > 0) {
                    spostamento = num;
                }
            }
        });

        btnText = new JButton(new ImageIcon(this.getClass().getResource("text.png")));

        btnPaste = new JButton(new ImageIcon(this.getClass().getResource("paste.png")));
        btnPaste.addActionListener(e -> {
            Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
            try {
                if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    String text = (String)t.getTransferData(DataFlavor.stringFlavor);
                    taText.setText(text);
                }
            } catch (UnsupportedFlavorException | IOException ignored) {
            }
        });

        btnClear = new JButton(new ImageIcon(this.getClass().getResource("rubber.png")));
        btnClear.addActionListener(e -> taText.setText(""));

        pnlControlli.add(btnStart);
        pnlControlli.add(btnReset);
        pnlControlli.add(btnAvanti);
        pnlControlli.add(btnIndietro);
        pnlControlli.add(btnSpeed);
        pnlControlli.add(btnText);
        pnlControlli.add(btnPaste);
        pnlControlli.add(btnClear);

        add(pnlControlli, BorderLayout.LINE_START);
    }

    private void resetPrompter() {
        JScrollBar vertical = spTele.getVerticalScrollBar();
        vertical.getModel().setValue(vertical.getMinimum());
    }

    private void scrollBar(int x) {
        JScrollBar vertical = spTele.getVerticalScrollBar();
        int posizione = vertical.getModel().getValue();
        vertical.getModel().setValue(posizione + x);
    }

    private void toggleEdit() {
        if (isEditing) {
            taText.setEditable(false);
            isEditing = false;
        } else {
            taText.setEditable(true);
            isEditing = true;
        }
    }

    private void togglePrompter() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
            btnStart.setIcon(new ImageIcon(this.getClass().getResource("stop.png")));
        } else {
            timer.stop();
            isRunning = false;
            btnStart.setIcon(new ImageIcon(this.getClass().getResource("start.png")));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && !isEditing) {
            togglePrompter();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
