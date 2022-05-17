import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextArea extends JFrame implements KeyListener, ActionListener {

	private JTextArea ta;
	private JScrollPane sc;
	private JLabel label;
	private JPanel panel;
	
	private JTextArea ta2;
	private JScrollPane sc2;
	private JLabel label2;
	private JPanel panel2;
	
	private JButton btn;
	
	private int count = 0;

	public TextArea() {

		// ������ �⺻����
		this.setTitle("����");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//����� ������Ʈ ��üȭ
		ta = new JTextArea(20, 30);
		ta2 = new JTextArea(10, 20);
		
		sc = new JScrollPane(ta); // TextArea ��ü�� ScrollPane �� �־��༭ ����!
		sc2 = new JScrollPane(ta2);
		
		panel = new JPanel();
		label = new JLabel("���ڼ� : " + count);

		btn = new JButton("Chagne");
		
		// ������Ʈ �Ӽ� ����
		sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// ���� ��ũ�� �Ⱦ�����.
		sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		ta.setLineWrap(true); // ������ �����ٷ� ���� ����.

		panel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // ���ڼ� ������ĭ ���������� ���� ����.

		// �̺�Ʈó�� ����

		ta.addKeyListener(this);
		
		btn.addActionListener(this);

		this.add(sc, BorderLayout.NORTH);
		
		panel.add(btn);
		
		this.add(sc2);

		panel.add(label);

		this.add(panel, BorderLayout.SOUTH);

		this.pack();

		this.setVisible(true);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		count = ta.getText().length();
		label.setText("���ڼ� : " + count);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		count = ta.getText().length();
		label.setText("���ڼ� : " + count);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		count = ta.getText().length();
		label.setText("���ڼ� : " + count);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = ta.getText();
		
		String[] change_target = text.split("\\n");
		
		List<String> outList = Arrays.asList(change_target);
		
		ta2.setText("");
		
		for (String fabUseCode : outList) {
			//getFabUseYN() �����ϴ��� üũ
			String pattern = ".{0,9999}getFabUseYN.{0,9999}";
			System.out.println(fabUseCode.matches(pattern));
			if(fabUseCode.matches(pattern) == false){
				continue;
			}
			
			//_�� # ���̿� ���ڿ� ���ִ��� üũ
			String pattern2 = ".*_.#.*";
			System.out.println(fabUseCode.matches(pattern2));
			if(fabUseCode.matches(pattern2)){
				continue;
			}
			
			
			//�ߺ�üũ
			
			//�ּ� üũ
			// System.out.println(fabUseCode.trim().substring(0, 2).equals("//"));
			
			//System.out.println("if(getComTrx().getFabUseYN(\"mes_#12345\",doc))".matches(pattern));
			//System.out.println("if(getComTrx().getFabUseYn(\"mes_#12345\",doc))".matches(pattern));

			//FabUseYN ����
			int firstIndex = fabUseCode.indexOf(".getFabUseYN(") + 14;
			int lastIndex = fabUseCode.indexOf("\"", firstIndex); 
			ta2.append(fabUseCode.substring(firstIndex, lastIndex));
			ta2.append("\n");
		}
		
		//ta2.setText(outList.get(1));
//		String words[] =text.split("\\s");
//		lb1.setText("�ܾ� :" + words.length);
//		lb2.setText("���� :" + text.length());
		
		//System.out.println(text);
	}
}