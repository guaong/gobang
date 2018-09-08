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

public class AICopy {
	private GameTree root;
	private int alpha = 0;
	private int beta = 0;
	
	public AICopy(){}
	
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
		int differenceCount = 0;
		int sameCount = 1;
		int nullCount = 0;
		int price = 0;
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0 && yp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp - i][yp - i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp - i]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		price = getPrice(differenceCount, sameCount, nullCount);
//		differenceCount = 0;
//		sameCount = 0;
//		nullCount = 0;
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14 && yp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp + j][yp + j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp + j]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		if(getPrice(differenceCount, sameCount, nullCount) > price){
//			price = getPrice(differenceCount, sameCount, nullCount);
//		}
		price = getPrice(differenceCount, sameCount, nullCount);
		return price;
	}
	
	private int evaluateB(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		int differenceCount = 0;
		int sameCount = 1;
		int nullCount = 0;
		int price = 0;
		for(int i = 1; i <= 4; i++){
			if(yp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp][yp - i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp][yp - i]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		price = getPrice(differenceCount, sameCount, nullCount);
//		differenceCount = 0;
//		sameCount = 0;
//		nullCount = 0;
		for(int j = 1; j <= 4; j++){
			if(yp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp][yp + j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp][yp + j]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		if(getPrice(differenceCount, sameCount, nullCount) > price){
//			price = getPrice(differenceCount, sameCount, nullCount);
//		}
		price = getPrice(differenceCount, sameCount, nullCount);
		return price;
	}

	private int evaluateC(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		int differenceCount = 0;
		int sameCount = 1;
		int nullCount = 0;
		int price = 0;
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0 && yp + i <= 14){ //判断是否到达边界
				if(hadPoints[xp - i][yp + i]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp + i]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		price = getPrice(differenceCount, sameCount, nullCount);
//		differenceCount = 0;
//		sameCount = 0;
//		nullCount = 0;
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14 && yp - j >= 0){ //判断是否到达边界
				if(hadPoints[xp + j][yp - j]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp - j]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		if(getPrice(differenceCount, sameCount, nullCount) > price){
//			price = getPrice(differenceCount, sameCount, nullCount);
//		}
		price = getPrice(differenceCount, sameCount, nullCount);
		return price;
	}
	
	private int evaluateD(int xp, int yp, boolean[][] hadPoints
			, Map<Point, Color> hadPointsColor, Point[][] points, Color currentColor){
		int differenceCount = 0;
		int sameCount = 1;
		int nullCount = 0;
		int price = 0;
		for(int i = 1; i <= 4; i++){
			if(xp - i >= 0){ //判断是否到达边界
				if(hadPoints[xp - i][yp]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp - i][yp]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		price = getPrice(differenceCount, sameCount, nullCount);
//		differenceCount = 0;
//		sameCount = 0;
//		nullCount = 0;
		for(int j = 1; j <= 4; j++){
			if(xp + j <= 14){ //判断是否到达边界
				if(hadPoints[xp + j][yp]){ //判断当前位置是否有棋子
					if(hadPointsColor.get(points[xp + j][yp]) == currentColor){ //判断颜色是否一致
						sameCount++;
						differenceCount--;
					}else{
						sameCount--;
						differenceCount++;
					}
				}else{
					nullCount++;
				}
			}else{
				break;
			}
		}
//		if(getPrice(differenceCount, sameCount, nullCount) > price){
//			price = getPrice(differenceCount, sameCount, nullCount);
//		}
		price = getPrice(differenceCount, sameCount, nullCount);
		return price;
	}
	
	private int getPrice(int diff, int same, int none){
		int price = 0;
		if(same == 4){
			price = 100000000;
		}else if(diff == 4){
			price = 10000000;
		}else if(same == 3 && none == 1){
			price = 1000000;
		}else if(diff == 3 && none == 1){
			price = 100000;
		}else if(same == 2 && none == 2){
			price = 10000;
		}else if(diff == 2 && none == 2){
			price = 1000;
		}else if(same == 1 && none == 3){
			price = 100;
		}else if(diff == 1 && none == 3){
			price = 10;
		}else{
			price = 1;
		}
		return price;
	}
}
