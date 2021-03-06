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

		// 프레임 기본세팅
		this.setTitle("연습");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//사용할 컴포넌트 객체화
		ta = new JTextArea(20, 30);
		ta2 = new JTextArea(10, 20);
		
		sc = new JScrollPane(ta); // TextArea 객체를 ScrollPane 에 넣어줘서 적용!
		sc2 = new JScrollPane(ta2);
		
		panel = new JPanel();
		label = new JLabel("글자수 : " + count);

		btn = new JButton("Chagne");
		
		// 컴포넌트 속성 세팅
		sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 수평 스크롤 안쓰게함.
		sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		ta.setLineWrap(true); // 꽉차면 다음줄로 가게 해줌.

		panel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 글자수 나오는칸 오른쪽으로 가게 해줌.

		// 이벤트처리 연결

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
		label.setText("글자수 : " + count);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		count = ta.getText().length();
		label.setText("글자수 : " + count);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		count = ta.getText().length();
		label.setText("글자수 : " + count);
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
			//N_MES 로 이미 진행한 메소드 존재하는지 체크
			String pattern_N_MES = "^.*N_MES.*";
			if(fabUseCode.matches(pattern_N_MES)){
				isN_MES = true;
			}
			
			//getFabUseYN() 존재하는지 체크
			String pattern = "^.*getFabUseYN.*";
			System.out.println(fabUseCode.matches(pattern));
			if(fabUseCode.matches(pattern) == false){
				continue;
			}
						
			//주석 체크
			if(fabUseCode.trim().substring(0, 2).equals("//")
				|| fabUseCode.trim().substring(0, 2).equals("/*")
				|| fabUseCode.trim().substring(0, 2).equals("*/")
				|| fabUseCode.trim().substring(0, 1).equals("*")) {
				continue;
			}
			
			//System.out.println("if(getComTrx().getFabUseYN(\"mes_#12345\",doc))".matches(pattern));
			//System.out.println("if(getComTrx().getFabUseYn(\"mes_#12345\",doc))".matches(pattern));

			//FabUseYN 추출
			List<Integer> indexes = findIndexes(".getFabUseYN(",fabUseCode);
			
			String fabUseYNCode = "";
			
			if(indexes.size() > 0) {
				firstIndex = 0;
				lastIndex =0;
				
				for (int fabUseIndex : indexes) {
					// fabUseYNCode 추출
					firstIndex = fabUseIndex + 14;
					int lastIndex = fabUseCode.indexOf("\"", firstIndex); 
					fabUseYNCode = fabUseCode.substring(firstIndex, lastIndex);
					
					//_와 # 사이에 문자열 껴있는지 체크
					String pattern2 = ".*_.#.*";
					
					if(fabUseYNCode.matches(pattern2)){
						continue;
					}

					// 문자열 안껴있으면 출력
					// ta2.append(fabUseYNCode);
					// ta2.append("\n");
					
					fabCodeList.add(fabUseYNCode);
				}
			}
		}
		
		// 중복 제거 위해 Set List 순으로 변경후 출력
		Set<String> set = new HashSet<String>(fabCodeList);
		List<String> resultFabCodes = new ArrayList<String>(set);
		
		for (String fabUse : resultFabCodes) {
			ta2.append(fabUse);
			ta2.append("\n");
		}
		
		if(isN_MES) {ta2.append("N_MES 존재");}
		
		//ta2.setText(outList.get(1));
//		String words[] =text.split("\\s");
//		lb1.setText("단어 :" + words.length);
//		lb2.setText("문자 :" + text.length());
		
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