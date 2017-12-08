package persistence.mappers;

import java.util.HashMap;
import java.util.Map;

import persistence.DBmap;

public class InvItem implements persistence.Mapper {

	public domain.Inventory.InvItem load(DBmap map) {
		domain.Inventory inv = domain.Game.getGame().getInv();
		domain.Inventory.InvItem item = inv.new InvItem(domain.Product.valueOf(
				map.getStr("Product")).ordinal());
		item.setValue(map.getInt("Amount"));
		return item;
	}

	public DBmap save(domain.Savable obj) {
		domain.Inventory.InvItem item = (domain.Inventory.InvItem) obj;
		return new DBmap(this,
				new String[]{"Product","Amount"},
				new Object[]{item.getKey().name(),item.getValue()});
	}

	@Override
	public Map<String, String> getFields() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("Product", "TEXT");
		fields.put("Amount", "INT");
		return fields;
	}

}
