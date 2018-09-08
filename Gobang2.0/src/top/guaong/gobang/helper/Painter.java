package top.guaong.gobang.helper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;
import java.util.Set;

public class Painter {

	/**
	 * ��������
	 */
	public static void drawChessBoard(Graphics g){
		g.setColor(Color.black);
		for(int i = 0; i < 15; i++){
			//����
			g.drawLine(SizeConfig.LEFT_TOP_X
					, SizeConfig.LEFT_TOP_Y + SizeConfig.BLOCK_SPACE*i
					, SizeConfig.RIGHT_TOP_X
					, SizeConfig.RIGHT_TOP_Y + SizeConfig.BLOCK_SPACE*i);
			//����
			g.drawLine(SizeConfig.LEFT_TOP_X + SizeConfig.BLOCK_SPACE*i
					, SizeConfig.LEFT_TOP_Y
					, SizeConfig.LEFT_BOTTOM_X + SizeConfig.BLOCK_SPACE*i
					, SizeConfig.LEFT_BOTTOM_Y);
		}
	}
	
	/**
	 * ���Ʋ˵���
	 */
	public static void drawMenuBorder(Graphics g){
		g.drawRect(SizeConfig.MENU_X
				, SizeConfig.MENU_Y
				, SizeConfig.MENU_WIDTH
				, SizeConfig.MENU_HEIGHT);
	}
	
	/**
	 * ��������
	 */
	public static void drawChessman(Color color, int x, int y, Graphics g){
		g.setColor(color);
		g.fillOval(x - SizeConfig.CHESSMAN_R
				, y - SizeConfig.CHESSMAN_R
				, SizeConfig.CHESSMAN_D
				, SizeConfig.CHESSMAN_D);
	}
	
	/**
	 * ������������
	 */
	public static void drawHave(Map<Point, Color> hadPointsColor, Graphics g){
		Set<Point> set = hadPointsColor.keySet();
		for (Point point : set) {
			drawChessman(hadPointsColor.get(point), point.x, point.y, g);
		}
	}
	
	
}
