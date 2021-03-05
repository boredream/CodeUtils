package de.greenrobot.daogenerator;

/**
 * greenDao creator 优化版
 */
public class _GreenDaoMain {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1000, "com.boredream.herb.db");

		addHerb(schema);
		addHerbDetail(schema);

		new DaoGenerator().generateAll(schema, "temp/db/entities");
	}
	
	private static void addHerb(Schema schema) {
		Entity herb = schema.addEntity("Herb");
		herb.addStringProperty("type");
		herb.addStringProperty("path").primaryKey();
		herb.addStringProperty("letter");
		herb.addStringProperty("href");
		herb.addStringProperty("img");
	}

	private static void addHerbDetail(Schema schema) {
		Entity herb = schema.addEntity("HerbDetail");
		herb.addStringProperty("type");
		herb.addStringProperty("path").primaryKey();
		herb.addStringProperty("letter");
		herb.addStringProperty("href");
		herb.addStringProperty("img");
		herb.setSuperclass("Herb");
		herb.addStringProperty("detail");
	}


}
