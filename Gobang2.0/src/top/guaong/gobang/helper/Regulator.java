package top.guaong.gobang.helper;

import java.awt.Color;
import java.awt.Point;
import java.util.Map;

public class Regulator {
	
	public static Point getPosition(int x, int y){
		Point p = new Point();
		p.x = x / SizeConfig.BLOCK_SPACE - 1;
		p.y = y / SizeConfig.BLOCK_SPACE - 1;
		return p;
	}

	/**
	 * �жϵ��λ���Ƿ�Ϸ�
	 */
	public static boolean islegalPosition(int x, int y, boolean[][] hadPoints){
		if(0 > x % SizeConfig.BLOCK_SPACE 
				|| 0 > y % SizeConfig.BLOCK_SPACE 
				|| x % SizeConfig.BLOCK_SPACE > 10 
				|| y % SizeConfig.BLOCK_SPACE > 10){ //�����жϵ��λ���Ƿ�Ϸ�
			return false;
		}
		final Point p = getPosition(x, y);
		if(hadPoints[p.x][p.y]){ //�Ϸ�λ���Ƿ���������
			return false;
		}
		return true;
	}
	
	/**
	 * ����λ�õ��Ϸ�
	 */
	public static Point adjustPositon(Point p, Point[][] points){
		//�Ѿ��������λ���Ƿ�Ϸ����ж�
		return points[p.x][p.y];
	}
	
	
	public static boolean isWin(Point p, boolean[][] hadPoints, Map<Point, Color> hadPointsColor, Color currentColor, Point[][] points){
		int xp = p.x;
		int yp = p.y;
		if(winByA(xp, yp, hadPoints, hadPointsColor, currentColor, points)
				|| winByB(xp, yp, hadPoints, hadPointsColor, currentColor, points)
				|| winByC(xp, yp, hadPoints, hadPointsColor, currentColor, points)
				|| winByD(xp, yp, hadPoints, hadPointsColor, currentColor, points)){
			return true;
		}
		return false;
	}
	
	private static boolean winByA(int xp, int yp, boolean[][] hadPoints, Map<Point, Color> hadPointsColor, Color currentColor, Point[][] points){
		int count = 0;
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0 && yp - i >= 0){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp - i][yp - i]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp - i][yp - i]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14 && yp + j <= 14){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp + j][yp + j]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp + j][yp + j]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		if(count >= 4){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean winByB(int xp, int yp, boolean[][] hadPoints, Map<Point, Color> hadPointsColor, Color currentColor, Point[][] points){
		int count = 0;
		for(int i = 1; i <= 4; i++){
			if(yp - i >= 0){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp][yp - i]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp][yp - i]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(yp + j <= 14){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp][yp + j]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp][yp + j]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		if(count >= 4){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean winByC(int xp, int yp, boolean[][] hadPoints, Map<Point, Color> hadPointsColor, Color currentColor, Point[][] points){
		int count = 0;
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0 && yp + i <= 14){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp - i][yp + i]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp - i][yp + i]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14 && yp - j >= 0){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp + j][yp - j]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp + j][yp - j]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		if(count >= 4){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean winByD(int xp, int yp, boolean[][] hadPoints, Map<Point, Color> hadPointsColor, Color currentColor, Point[][] points){
		int count = 0;
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp - i][yp]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp - i][yp]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14){ //�ж��Ƿ񵽴�߽�
				if(hadPoints[xp + j][yp]){ //�жϵ�ǰλ���Ƿ�������
					if(hadPointsColor.get(points[xp + j][yp]) == currentColor){ //�ж���ɫ�Ƿ�һ��
						count++;
					}else{
						break;
					}
				}else{
					break;
				}
			}else{
				break;
			}
		}
		if(count >= 4){
			return true;
		}else{
			return false;
		}
	}
	
}
