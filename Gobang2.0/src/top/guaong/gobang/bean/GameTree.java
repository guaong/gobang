package top.guaong.gobang.bean;


import java.awt.Point;
import java.util.List;

public class GameTree {

	private Point date;
	private int price;
	private List<GameTree> leaf;
	private Point bestPoint;
	
	public GameTree(Point date, List<GameTree> leaf, int price, Point bestPoint){
		this.date = date;
		this.leaf = leaf;
		this.price = price;
		this.bestPoint = bestPoint;
	}
	
	public GameTree(Point date, List<GameTree> leaf, int price){
		this.date = date;
		this.leaf = leaf;
		this.price = price;
	}
	
	public Point getDate(){
		return date;
	}
	
	public void setDate(Point date){
		this.date = date;
	}
	
	public List<GameTree> getLeaf(){
		return leaf;
	}
	
	public void setLeaf(List<GameTree> leaf){
		this.leaf = leaf;
	}
	
	public int getPrice(){
		return price;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public Point getBestPoint(){
		return bestPoint;
	}
	
	public void setBestPoint(Point bestPoint){
		this.bestPoint = bestPoint;
	}
	
	@Override
	public String toString() {
		return "root:\n\t["+date.x+","+date.y+"]price="+price+" "+leaf+"\n";
	}
	
}
