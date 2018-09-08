package top.guaong.gobang.ai;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Set;

import top.guaong.gobang.bean.GameTree;

public class AI {
	private GameTree root;
	private int alpha = 0;
	private int beta = 0;
	
	public AI(){}
	
	public Point bestPoint(boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, int xp, int yp){
		GameTree root = buildGameTree(2, hadPoints, hadPointsColor, points, xp, yp);
		List<GameTree> children = root.getLeaf();
		int max = 0;
		Point maxPoint = null;
		for (GameTree child : children) {
			child = backtrack(child);
			if(child.getPrice() > max){
				max = child.getPrice();
				maxPoint = child.getBestPoint();
			}
		}
		return maxPoint;
		
	}
	
	private GameTree backtrack(GameTree parent){
		int maxPrice = 0;
		Point maxPoint = new Point();
		if(parent.getLeaf().size() != 0){
			List<GameTree> children = parent.getLeaf();
			for (GameTree child : children) {
				int price = backtrack(child).getPrice();
				if(price > maxPrice){
					maxPrice = price;
					maxPoint = child.getDate();
				}

			}
			parent.setPrice(maxPrice);
			parent.setBestPoint(maxPoint);

		}
		return parent;
	}
		
	/*
	 * 博弈树
	 */
	private GameTree buildGameTree(int depth, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, int xp, int yp){
		boolean[][] hadPointsCopy = new boolean[15][15]; 
		copyArrays(hadPoints, hadPointsCopy);
		Map<Point, Color> hadPointsColorCopy = new HashMap<>();
		copyMap(hadPointsColor, hadPointsColorCopy);
		Point p = new Point(xp, yp);
		root = new GameTree(p
				, new ArrayList<>()
				, evaluate(xp, yp, hadPointsCopy, hadPointsColorCopy, points, Color.black), p);
		buildLeaf(hadPointsCopy, 0, root, hadPointsColorCopy,depth, points);
		return root;
	}
	
	private void copyArrays(boolean[][] hadPoints, boolean[][] hadPointsCopy){
		for(int j = 0; j < hadPoints.length; j++){
			for(int i = 0; i < hadPoints.length; i++){
				hadPointsCopy[j][i] = hadPoints[j][i];
			}
		}
	}

	private void copyMap(Map<Point, Color> hadPointsColor, Map<Point, Color> hadPointsColorCopy){
		Set<Entry<Point, Color>> set = hadPointsColor.entrySet();
		for (Entry<Point, Color> entry : set) {
			hadPointsColorCopy.put(entry.getKey(), entry.getValue());
		}
	}
	
	private GameTree buildLeaf(boolean[][] hadPoints, int i, GameTree root, Map<Point, Color> hadPointsColor, int depth, Point[][] points){
		if(depth > 0){
			depth--;
			i++;
			if(i % 2 == 0){ //人
				for(int j = 0; j < 15; j++){
					for(int k = 0; k < 15; k++){
						int price = evaluate(j, k, hadPoints, hadPointsColor, points, Color.black);
						if(!hadPoints[j][k]){
							//将当前内容copy
							boolean[][] hadPointsCopy = new boolean[15][15]; 
							copyArrays(hadPoints, hadPointsCopy);
							Map<Point, Color> hadPointsColorCopy = new HashMap<>();
							copyMap(hadPointsColor, hadPointsColorCopy);
							//模拟放点
							hadPointsCopy[j][k] = true;
							hadPointsColorCopy.put(points[j][k], Color.black);
							//作为上个结点的叶子结点
							Point p = new Point(j,k);
							GameTree leaf = 
									new GameTree(p,new ArrayList<>(), price, p);
							//递归
							root.getLeaf().add(buildLeaf(hadPointsCopy, i, leaf, hadPointsColorCopy, depth, points));
						}
						
					}
				}
			}else{ //AI
				for(int j = 0; j < 15; j++){
					for(int k = 0; k < 15; k++){
						int price = evaluate(j, k, hadPoints, hadPointsColor, points, Color.white);
						if(!hadPoints[j][k]){
							//将当前内容copy
							boolean[][] hadPointsCopy = new boolean[15][15]; 
							copyArrays(hadPoints, hadPointsCopy);
							Map<Point, Color> hadPointsColorCopy = new HashMap<>();
							copyMap(hadPointsColor, hadPointsColorCopy);
							//模拟放点
							hadPointsCopy[j][k] = true;
							hadPointsColorCopy.put(points[j][k], Color.white);
							//作为上个结点的叶子结点
							Point p = new Point(j,k);
							GameTree leaf = 
									new GameTree(p,new ArrayList<>(), price, p);
							//递归
							root.getLeaf().add(buildLeaf(hadPointsCopy, i, leaf, hadPointsColorCopy, depth, points));
						}
					}
				}
			}
		}
		return root;
	}
	
	
	/*
	 * 评估
	 */	
	public int evaluate(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		return Math.max(Math.max(evaluateA(xp, yp, hadPoints, hadPointsColor, points, currentColor) 
				, evaluateB(xp, yp, hadPoints, hadPointsColor, points, currentColor)), 
						Math.max(evaluateC(xp, yp, hadPoints, hadPointsColor, points, currentColor)
				, evaluateD(xp, yp, hadPoints, hadPointsColor, points, currentColor)));
		
	}
	
	private int evaluateA(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		String link = "1";
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0 && yp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp - i][yp - i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp - i]) == currentColor){ //判断颜色是否一致
						link += "1";
					}else{
						link += "2";
					}
				}else{
					link += "0";
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14 && yp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp + j][yp + j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp + j]) == currentColor){ //判断颜色是否一致
						link = "1" + link;
					}else{
						link = "2" + link;
					}
				}else{
					link = "0" + link;
				}
			}else{
				break;
			}
		}
		return getPrice(link);
	}
	
	private int evaluateB(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		String link = "1";
		for(int i = 1; i <= 4; i++){
			if(yp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp][yp - i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp][yp - i]) == currentColor){ //判断颜色是否一致
						link += "1";
					}else{
						link += "2";
					}
				}else{
					link += "0";
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(yp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp][yp + j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp][yp + j]) == currentColor){ //判断颜色是否一致
						link = "1" + link;
					}else{
						link = "2" + link;
					}
				}else{
					link = "0" + link;
				}
			}else{
				break;
			}
		}
		return getPrice(link);
	}

	private int evaluateC(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		String link = "1";
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0 && yp + i <= 14){ //判断是否到达边界
				if(hadPoints[xp - i][yp + i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp + i]) == currentColor){ //判断颜色是否一致
						link += "1";
					}else{
						link += "2";
					}
				}else{
					link += "0";
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14 && yp - j >= 0){ //判断是否到达边界
				if(hadPoints[xp + j][yp - j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp - j]) == currentColor){ //判断颜色是否一致
						link = "1" + link;
					}else{
						link = "2" + link;
					}
				}else{
					link = "0" + link;
				}
			}else{
				break;
			}
		}
		return getPrice(link);
	}
	
	private int evaluateD(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		String link = "1";
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp - i][yp]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp]) == currentColor){ //判断颜色是否一致
						link += "1";
					}else{
						link += "2";
					}
				}else{
					link += "0";
				}
			}else{
				break;
			}
		}
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp + j][yp]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp]) == currentColor){ //判断颜色是否一致
						link = "1" + link;
					}else{
						link = "2" + link;
					}
				}else{
					link = "0" + link;
				}
			}else{
				break;
			}
		}
		return getPrice(link);
	}
	
	private int getPrice(String link){
		int price = 0;
		String bestLink = "";
		String[] links = link.split("2");
		for(int i = 0; i < links.length; i++){
			if(bestLink.length() < links[i].length()){
				bestLink = links[i];
			}
		}
		if(link.indexOf("22222") != -1){
			price = 100000000;
		}else if(bestLink.indexOf("11111") != -1){
			price = 10000000;
		}else if(link.indexOf("022220") != -1){
			price = 5000000;
		}else if(bestLink.indexOf("011110") != -1){
			price = 3000000;
		}else if((link.indexOf("22202") != -1) || (bestLink.indexOf("0011100") != -1)){
			price = 5000;
		}else if((bestLink.indexOf("11101") != -1) || (bestLink.indexOf("0011100") != -1)){
			price = 3000;
		}else if(link.indexOf("22022") != -1){
			price = 2800;
		}else if(bestLink.indexOf("11011") != -1){
			price = 2600;
		}else if(link.indexOf("22220") != -1){
			price = 2400;
		}else if(bestLink.indexOf("11110") != -1){
			price = 2200;
		}else if(link.indexOf("020220") != -1){
			price = 1000;
		}else if(bestLink.indexOf("010110") != -1){
			price = 800;
		}else if(link.indexOf("00022000") != -1){
			price = 700;
		}else if(bestLink.indexOf("00011000") != -1){
			price = 650;
		}else if(link.indexOf("20022") != -1){
			price = 600;
		}else if(bestLink.indexOf("10011") != -1){
			price = 500;
		}else if(link.indexOf("20202") != -1){
			price = 450;
		}else if(bestLink.indexOf("10101") != -1){
			price = 400;
		}else if(link.indexOf("22200") != -1){
			price = 350;
		}else if(bestLink.indexOf("11100") != -1){
			price = 300;
		}else if(bestLink.indexOf("0010100") != -1){
			price = 250;
		}else if(bestLink.indexOf("010010") != -1){
			price = 200;
		}else if(bestLink.indexOf("11000") != -1){
			price = 150;
		}else{
			price = 20;
		}
		return price;
	}
}
