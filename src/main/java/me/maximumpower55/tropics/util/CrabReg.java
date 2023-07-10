package me.maximumpower55.tropics.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public final class CrabReg {

	private final String namespace;
	private final List<Object> registered;

	public CrabReg(String namespace) {
		this.namespace = namespace;
		this.registered = Lists.newLinkedList();
	}

	public <V, T extends V> T delegateRegister(Registry<V> registry, String path, T value) {
		var v = Registry.register(registry, new ResourceLocation(namespace, path), value);
		registered.add(v);
		return v;
	}

	@SuppressWarnings("unchecked")
	public <T, C> void registerFields(Class<? super C> klass, Class<? super T> fieldType, BiConsumer<String, ? super T> consumer) {
		for (Field field : klass.getDeclaredFields()) {
			if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
				try {
					var obj = field.get(null);
					if (obj != null && fieldType.isAssignableFrom(obj.getClass())) {
						consumer.accept(field.getName(), (T) obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public <V> BiConsumer<String, ? super V> registerFunction(Registry<V> registry) {
		return (fieldName, value) -> delegateRegister(registry, fieldName.toLowerCase(Locale.ENGLISH), value);
	}

	public <T extends DeferringFunction> BiConsumer<String, ? super T> deferringFunction() {
		return (fieldName, value) -> value.register(this);
	}

	@SuppressWarnings("unchecked")
	public <T> void forEachRegistered(Class<? super T> registeredType, Consumer<? super T> consumer) {
		registered.forEach(obj -> {
			if (registeredType.isAssignableFrom(obj.getClass())) consumer.accept((T) obj);
		});
	}

	@FunctionalInterface
	public static interface DeferringFunction {
		void register(CrabReg reg);
	}

}
