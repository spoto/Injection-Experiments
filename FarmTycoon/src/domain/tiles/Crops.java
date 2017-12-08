package domain.tiles;

import api.TileAction;
import domain.Product;

enum Crops implements TileAction {
	POTATO		(  2,  10, Product.POTATO),
	CARROT		(  3,  15, Product.CARROT),
	WHEAT		(  4,  20, Product.WHEAT),
	CORN		(  5,  30, Product.CORN),
	GRAPE		(  6,  40, Product.GRAPE),
	LETTUCE		(  7,  50, Product.LETTUCE),
	TOMATO		(  8,  60, Product.TOMATO),
	STRAWBERRY	(  9,  80, Product.STRAWBERRY),
	RASPBERRY	( 10, 100, Product.RASPBERRY),
	COCOA		( 11, 120, Product.COCOA),
	SOY		( 12, 150, Product.SOY);

	private final int growdays, seedprice;
	private final Product prod;

	public int getTime() 	{return growdays;}
	public int getCost() 	{return seedprice;}
	public Product getProduct()		{return prod;}

	private Crops(int growdays, int seedprice, domain.Product prod) {
		this.growdays = growdays;
		this.seedprice = seedprice;
		this.prod = prod;
	}
}