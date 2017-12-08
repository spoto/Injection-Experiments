package domain;

import java.util.Map;
import java.util.NoSuchElementException;

import exceptions.InventoryFullException;

/**
 * This class implements an inventory.
 * The inventory is based on a <Product,Integer> Map.
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public class Inventory extends java.util.AbstractMap<Product, Integer> {
	/**
	 * the amount of slots available by default.
	 */
	public static final int DEFAULTSPACE=5;
	/**
	 * the amount of slots a barn adds to this inventory
	 */
	public static final int BARNSPACE=25;

	private final Product[] keys = Product.values();
	private final int size = keys.length;
	/**
	 * the amount of barns available for this inventory.
	 */
	private int barncount;

	private final int[] vals = new int[keys.length];
	
	/**
	 * Create a new Inventory with a given amount of barns.
	 * @param barncount the amount of barns to use for this inventory.
	 */
	public Inventory(int barncount){
		this.barncount=barncount;
	}
	
	/**
	 * updates the amount of barns
	 * @param count the amount of barns to use for this inventory.
	 */
	public void setBarnCount(int count){
		this.barncount = count;
	}

	/**
	 * @return the amount of slots left in this inventory
	 */
	public int spaceLeft() {
		return getSize()-count();
	}
	
	/**
	 * @return the total amount of slots available for this inventory.
	 */
	public int getSize() {
		return DEFAULTSPACE + (BARNSPACE * barncount);
	}
	
	/**
	 * @return the amount of slots in use.
	 */
	public int count() {
		int count=0;
		for(int val:values())
			count+=val;
		return count;
	}
	
	/**
	 * Add one instance of a given Product to this inventory.
	 * @param key the Product to add.
	 * @return the new amount for this product.
	 * @throws InventoryFullException if ther is no space left in this inventory.
	 */
	public Integer add(Product key) throws InventoryFullException {
		return add(key,1);
	}
	/**
	 * Add a given amount of a given Product to this inventory.
	 * @param key the Product to add.
	 * @param amount the amount of this product to add.
	 * @return the new amount for this product.
	 * @throws InventoryFullException if ther is no space left in this inventory.
	 */
	public Integer add(Product key, int amount) throws InventoryFullException {
		if(amount > spaceLeft())
			throw new InventoryFullException();
		put(key, get(key)+amount);
		return get(key);
	}
	
	/**
	 * Remove a given amount of a given Product to this inventory.
	 * @param key the Product to remove.
	 * @param amount the amount of this product to remove.
	 * @return the new amount for this product.
	 */
	public Integer remove(Product key, int amount) {
		vals[key.ordinal()] -= amount;
		return get(key);
	}
	
	public void save() throws java.sql.SQLException{
		for (Entry<Product, Integer> e : entrySet())
			((InvItem) e).save();
	}

	public void load() throws java.sql.SQLException {
		InvItem.loadAll(InvItem.class);
	}

	public Integer put(Product key, Integer value) {
		int index = key.ordinal();
		int oldValue = vals[index];
		vals[index] = value;
		return oldValue;
	}

	public void clear() {
		java.util.Arrays.fill(vals, 0);
	}
	
	public java.util.Set<Product> keySet() {
		return new KeySet();
	}
	public java.util.Collection<Integer> values() {
		return new Values();
	}
	public java.util.Set<Map.Entry<Product, Integer>> entrySet() {
		return new EntrySet();
	}

	private class KeySet extends java.util.AbstractSet<Product> {
		public java.util.Iterator<Product> iterator() {
			return new InventoryIterator<Product>() {
				protected Product get(int i) {
					return keys[i];
				}
			};
		}
		public int size() {
			return size;
		}
	}

	private class Values extends java.util.AbstractCollection<Integer> {
		public java.util.Iterator<Integer> iterator() {
			return new InventoryIterator<Integer>(){
				protected Integer get(int i) {
					return vals[i];
				}
			};
		}
		public int size() {
			return size;
		}
	}

	private class EntrySet extends java.util.AbstractSet<Map.Entry<Product, Integer>> {
		public java.util.Iterator<Map.Entry<Product, Integer>> iterator() {
			return new InventoryIterator<Entry<Product, Integer>>() {
				public Entry<Product, Integer> get(int i) {
					return new InvItem(i);
				}
			};
		}
		public int size() {
			return size;
		}
	}

	private abstract class InventoryIterator<T> implements java.util.Iterator<T> {
		int index = 0;

		public boolean hasNext() {
			return index != size;
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return get(index++);
		}
		protected abstract T get(int i);
	}

	/**
	 * Class which represent a single entry in the inventory map.
	 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
	 */
	public class InvItem extends Savable implements Map.Entry<Product, Integer> {
		public InvItem(int index) {
			if (index >= size || index < 0)
				throw new NoSuchElementException();
			this.id = index;
		}

		public Product	getKey()	{ return keys[id]; }
		public Integer	getValue()	{ return vals[id]; }
		public String	toString()	{ return getKey() + "=" + getValue(); }

		public Integer setValue(Integer value) {
			Integer oldValue = vals[id];
			vals[id] = value;
			return oldValue;
		}
	}
}