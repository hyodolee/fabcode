import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	private int firstIndex = 0;
	private int lastIndex = 0;
	
	private List<String> fabCodeList = new ArrayList<String>();
	
	boolean isN_MES = false;

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
		fabCodeList.clear();
		isN_MES = false;
		
		for (String fabUseCode : outList) {
			//N_MES �� �̹� ������ �޼ҵ� �����ϴ��� üũ
			String pattern_N_MES = "^.*N_MES.*";
			if(fabUseCode.matches(pattern_N_MES)){
				isN_MES = true;
			}
			
			//getFabUseYN() �����ϴ��� üũ
			String pattern = "^.*getFabUseYN.*";
			System.out.println(fabUseCode.matches(pattern));
			if(fabUseCode.matches(pattern) == false){
				continue;
			}
						
			//�ּ� üũ
			if(fabUseCode.trim().substring(0, 2).equals("//")
				|| fabUseCode.trim().substring(0, 2).equals("/*")
				|| fabUseCode.trim().substring(0, 2).equals("*/")
				|| fabUseCode.trim().substring(0, 1).equals("*")) {
				continue;
			}
			
			//System.out.println("if(getComTrx().getFabUseYN(\"mes_#12345\",doc))".matches(pattern));
			//System.out.println("if(getComTrx().getFabUseYn(\"mes_#12345\",doc))".matches(pattern));

			//FabUseYN ����
			List<Integer> indexes = findIndexes(".getFabUseYN(",fabUseCode);
			
			String fabUseYNCode = "";
			
			if(indexes.size() > 0) {
				firstIndex = 0;
				lastIndex =0;
				
				for (int fabUseIndex : indexes) {
					// fabUseYNCode ����
					firstIndex = fabUseIndex + 14;
					int lastIndex = fabUseCode.indexOf("\"", firstIndex); 
					fabUseYNCode = fabUseCode.substring(firstIndex, lastIndex);
					
					//_�� # ���̿� ���ڿ� ���ִ��� üũ
					String pattern2 = ".*_.#.*";
					
					if(fabUseYNCode.matches(pattern2)){
						continue;
					}

					// ���ڿ� �Ȳ������� ���
					// ta2.append(fabUseYNCode);
					// ta2.append("\n");
					
					fabCodeList.add(fabUseYNCode);
				}
			}
		}
		
		// �ߺ� ���� ���� Set List ������ ������ ���
		Set<String> set = new HashSet<String>(fabCodeList);
		List<String> resultFabCodes = new ArrayList<String>(set);
		
		for (String fabUse : resultFabCodes) {
			ta2.append(fabUse);
			ta2.append("\n");
		}
		
		if(isN_MES) {ta2.append("N_MES ����");}
		
		//ta2.setText(outList.get(1));
//		String words[] =text.split("\\s");
//		lb1.setText("�ܾ� :" + words.length);
//		lb2.setText("���� :" + text.length());
		
		//System.out.println(text);
	}
	
	public List<Integer> findIndexes(String word, String document) { 
		List<Integer> indexList = new ArrayList<Integer> (); 
		
		int index = document.indexOf(word); 
		
		while(index != -1) { 
			indexList.add(index); 
			index = document.indexOf(word, index+word.length()); 
		}
		
		return indexList; 
	}
}