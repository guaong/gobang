package top.guaong.gobang.ui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import top.guaong.gobang.ai.AI;
import top.guaong.gobang.helper.ImageUtil;
import top.guaong.gobang.helper.Painter;
import top.guaong.gobang.helper.Regulator;
import top.guaong.gobang.helper.SizeConfig;

public class MainView extends Frame{
	
	private final String TITLE = "Gobang";
	/*用于记录当前点坐标*/
	private int currentPointX;
	private int currentPointY;
	
	private Point currentPoint;
	
	//15*15棋盘点位置偏移棋子半径位置,由于java画圆要左上角坐标
	private Point[][] points = new Point[15][15];
	//15*15棋盘点是否被放置过
	private boolean[][] hadPoints = new boolean[15][15];
	//已放置棋子颜色
	private Map<Point, Color> hadPointsColor = new HashMap<>();
	private Color currentColor = Color.BLACK;
	private boolean isAI = false;
	private boolean isStart = false;
	
	private Label current = new Label("五子棋", Label.CENTER);
	private Button btn = new Button("开始游戏");
	
	private AI ai = new AI();

	public MainView(){
		init();
	}
	
	private void init(){
		setSize(SizeConfig.WIDTH, SizeConfig.HEIGHT);
		setTitle(TITLE);
		setIconImage(ImageUtil.getImage("image/icon.png"));
		setResizable(false);
		setBackground(new Color(182, 142, 85));
		setLayout(null);
		current.setBounds(840, 100, 100, 50);
		current.setFont(new Font("Serif",Font.BOLD,20));
		btn.setBounds(840, 600, 100, 50);
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if("开始游戏".contains(e.getActionCommand())){
					isStart = true;
					btn.setLabel("重新开始");
				}else{
					playAgain();
				}
				
			}
		});
		add(btn);
		add(current);
		setVisible(true);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!isAI && isStart){
					currentPointX = e.getX();
					currentPointY = e.getY();
					if(Regulator.islegalPosition(currentPointX, currentPointY, hadPoints)){ //位置合法
						currentPoint = Regulator.getPosition(currentPointX, currentPointY);
						currentColor = Color.black;
						savePoint(currentPoint.x, currentPoint.y, currentColor);
						repaint();
						if(Regulator.isWin(currentPoint, hadPoints, hadPointsColor, currentColor, points)){
							final Object[] options ={"退出", "重来"};
							alert(true);
						}else{
							isAI = true;
						}
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		initPointsPosition();
		new Thread(new AIThread()).start();
	}
	
	/**
	 * 初始化存放点坐标数组和hadPoints数组
	 */
	private void initPointsPosition(){
		for(int j = 0; j < points.length; j++){
			for(int i = 0; i < points.length; i++){
				points[i][j] = new Point();
				points[i][j].x = i * SizeConfig.BLOCK_SPACE + 50;
				points[i][j].y = j * SizeConfig.BLOCK_SPACE + 50;
				hadPoints[i][j] = false;
			}
		}
	}

	/**
	 * 保存点
	 */
	private void savePoint(int x, int y, Color currentColor){
		hadPoints[x][y] = true;
		hadPointsColor.put(points[x][y], currentColor);
	}
	
	private void playAgain(){
		initPointsPosition();
		hadPointsColor.clear();
		isAI = false;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		//去锯齿
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Painter.drawChessBoard(g2);
		Painter.drawMenuBorder(g2);
		Painter.drawHave(hadPointsColor, g2);
	}
	
	@Override
	public void update(Graphics g) {
		//双缓冲用到的东西
		Image offScreenImage = null;
		if(offScreenImage == null){
			offScreenImage = this.createImage(1100,710);
		}
		Graphics gOff=offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}	
	
	private void alert(boolean b){
		int choose;
		if(b){
			final Object[] options ={"退出", "重来"};
			choose = JOptionPane.showOptionDialog(null
					, "你赢啦"
					, "win"
					,JOptionPane.YES_NO_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, null
					, options
					, options[0]);
		}else{
			final Object[] options ={"退出", "重来"};
			choose = JOptionPane.showOptionDialog(null
					, "你输啦"
					, "die"
					,JOptionPane.YES_NO_OPTION
					, JOptionPane.QUESTION_MESSAGE
					, null
					, options
					, options[0]);
		}
		if(choose == 0){
			System.exit(0);
		}else{
			playAgain();
		}
	}

	class AIThread implements Runnable{
		@Override
		public void run() {
			while(true){
				if(isAI){
					//ai走
					Point p = ai.bestPoint(hadPoints, hadPointsColor, points, currentPoint.x, currentPoint.y);
					savePoint(p.x, p.y, Color.white);
					repaint();
					if(Regulator.isWin(p, hadPoints, hadPointsColor, Color.white, points)){
						final Object[] options ={"退出", "重来"};
						alert(false);
					}
					isAI = false;
				}
			}
		}
	}
	
}
