import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;

public class Fmj {
	public static final String ID = "tropics";
	public static final String VERSION = "1.0.3";
	public static final String NAME = "Tropics";
	public static final String DESCRIPTION = "A mod about palm trees and coconuts!";
	public static final String[] AUTHORS = new String[] { "Maximum (Author)", "Octal (Texture Artist)" };

	public static class Contacts {
		public static final String SOURCES = "https://github.com/maximmods/Tropics";
		public static final String ISSUES = SOURCES + "/issues";
		public static final String DISCORD = "https://discord.gg/ZN6GAtbr3U";
	}

	public static final String LICENSE = "MIT";
	public static final String ICON = String.format("assets/%s/icon.png", ID);
	public static final String ENVIRONMENT = "*";

	public static final HashMap<String, String[]> ENTRYPOINTS = new HashMap<>();

	public static final String[] MIXINS = new String[] { String.format("%s.mixins.json", ID) };
	public static final String ACCESS_WIDENER = String.format("%s.accesswidener", ID);
	public static final HashMap<String, String> DEPENDS = new HashMap<>();

	private static HashMap<String, Object> classToJson(Class<?> klass) {
		HashMap<String, Object> map = new HashMap<>();
		for (Field field : klass.getDeclaredFields()) {
			String name = field.getName().toLowerCase(Locale.ENGLISH);
			StringBuffer fixedNameBuilder = new StringBuffer();

			boolean underscore = false;
			for (char letter : name.toCharArray()) {
				if (String.valueOf(letter).equals("_")) {
					underscore = true;
					continue;
				}

				if (!underscore) {
					fixedNameBuilder.append(letter);
				} else {
					fixedNameBuilder.append(String.valueOf(letter).toUpperCase(Locale.ENGLISH));
					underscore = false;
				}
			}

			try {
				map.put(fixedNameBuilder.toString(), field.get(null));
			} catch (Exception e) { }
		}
		return map;
	}

	public static HashMap<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<>();
		// https://discord.gg/h4Cspj9FPa
		map.put("schemaVersion", 1);
		map.putAll(classToJson(Fmj.class));
		map.put("contact", classToJson(Contacts.class));
		map.put("entrypoints", ENTRYPOINTS);
		return map;
	}
}
