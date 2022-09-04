import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

import io.github.coolcrabs.brachyura.util.Lazy;

public class Version implements Supplier<String> {
	private final Lazy<Properties> properties;

	private final String key;
	private String value;

	Version(String key, Lazy<Properties> properties) {
		this.properties = properties;
		this.key = key;
	}

	@Override
	public String get() {
		if (value == null) value = Objects.requireNonNull(properties.get().getProperty(key));
		return value;
	}
}
