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
	 * 判断点击位置是否合法
	 */
	public static boolean islegalPosition(int x, int y, boolean[][] hadPoints){
		if(0 > x % SizeConfig.BLOCK_SPACE 
				|| 0 > y % SizeConfig.BLOCK_SPACE 
				|| x % SizeConfig.BLOCK_SPACE > 10 
				|| y % SizeConfig.BLOCK_SPACE > 10){ //首先判断点击位置是否合法
			return false;
		}
		final Point p = getPosition(x, y);
		if(hadPoints[p.x][p.y]){ //合法位置是否已有棋子
			return false;
		}
		return true;
	}
	
	/**
	 * 调整位置到合法
	 */
	public static Point adjustPositon(Point p, Point[][] points){
		//已经做过点击位置是否合法的判断
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
			if(xp - i >= 0 && yp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp - i][yp - i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp - i]) == currentColor){ //判断颜色是否一致
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
			if(xp + j <= 14 && yp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp + j][yp + j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp + j]) == currentColor){ //判断颜色是否一致
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
			if(yp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp][yp - i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp][yp - i]) == currentColor){ //判断颜色是否一致
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
			if(yp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp][yp + j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp][yp + j]) == currentColor){ //判断颜色是否一致
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
			if(xp - i >= 0 && yp + i <= 14){ //判断是否到达边界
				if(hadPoints[xp - i][yp + i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp + i]) == currentColor){ //判断颜色是否一致
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
			if(xp + j <= 14 && yp - j >= 0){ //判断是否到达边界
				if(hadPoints[xp + j][yp - j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp - j]) == currentColor){ //判断颜色是否一致
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
			if(xp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp - i][yp]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp]) == currentColor){ //判断颜色是否一致
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
			if(xp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp + j][yp]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp]) == currentColor){ //判断颜色是否一致
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
