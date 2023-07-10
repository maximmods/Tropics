import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.fabric.FabricContext;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.FabricModule;
import io.github.coolcrabs.brachyura.fabric.SimpleFabricProject;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag;
import io.github.coolcrabs.brachyura.ide.IdeModule;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.quilt.QuiltMaven;
import io.github.coolcrabs.brachyura.util.Lazy;
import io.github.coolcrabs.brachyura.util.PathUtil;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends SimpleFabricProject {
	private final Versions versions = new Versions(getProjectDir().resolve("buildscript").resolve("versions.properties"));
	private final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

	public Buildscript() {
		Fmj.ENTRYPOINTS.put("main", new String[] { String.format("me.maximumpower55.%s.Tropics", Fmj.ID) });
		Fmj.ENTRYPOINTS.put("client", new String[] { String.format("me.maximumpower55.%s.client.TropicsClient", Fmj.ID) });
		Fmj.ENTRYPOINTS.put("fabric-datagen", new String[] { String.format("me.maximumpower55.%s.datagen.TropicsDatagen", Fmj.ID) });

		Fmj.DEPENDS.put("fabricloader", ">=0.14.21");
		Fmj.DEPENDS.put("minecraft", ">=1.20 <1.21");
		Fmj.DEPENDS.put("fabric-api", ">=0.84.0");

		try {
			Path path = getResourcesDir().resolve("fabric.mod.json");
			if (Files.exists(path)) Files.delete(path);
			Files.write(path, GSON.toJson(Fmj.toMap()).getBytes(), StandardOpenOption.CREATE_NEW);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Path path = getResourcesDir().resolve("tropics.mixins.json");
			Map<Object, Object> map = GSON.fromJson(Files.readString(path), new TypeToken<Map<Object, Object>>(){}.getType());
			Set<String> mixins = new HashSet<>();
			Set<String> clientMixins = new HashSet<>();
			List<Pair<String, String>> paths = new LinkedList<>();
			paths.add(Pair.of("", getSrcDir().resolve(((String) map.get("package")).replace(".", "/")).toAbsolutePath().toString()));
			while (!paths.isEmpty()) {
				Pair<String, String> cur = paths.remove(0);
				for (File file : new File(cur.right()).listFiles()) {
					if (file.isDirectory()) {
						paths.add(Pair.of(file.getName() + ".",  file.getAbsolutePath()));
					} else {
						if (!cur.left().contains("client")) {
							mixins.add(cur.left() + file.getName().replace(".java", ""));
						} else {
							clientMixins.add(cur.left() + file.getName().replace(".java", ""));
						}
					}
				}
			}
			map.put("mixins", mixins);
			map.put("client", clientMixins);
			if (Files.exists(path)) Files.delete(path);
			Files.write(path, GSON.toJson(map).getBytes(), StandardOpenOption.CREATE_NEW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getJavaVersion() {
		return Integer.parseInt(versions.JAVA.get());
	}

	@Override
	public @Nullable BrachyuraDecompiler decompiler() {
		return new FernflowerDecompiler(Maven.getMavenJarDep(QuiltMaven.URL, new MavenId("org.quiltmc", "quiltflower", versions.QUILTFLOWER.get())));
	}

	@Override
	public FabricLoader getLoader() {
		return new FabricLoader(FabricMaven.URL, FabricMaven.loader(versions.FABRIC_LOADER.get()));
	}

	@Override
	public VersionMeta createMcVersion() {
		return Minecraft.getVersion(versions.MINECRAFT.get());
	}

	@Override
	public MappingTree createMappings() {
		return createMojmap();
	}

	@Override
	public void getModDependencies(ModDependencyCollector d) {
		jij(d.addMaven("https://jitpack.io/", new MavenId("com.github.LlamaLad7", "MixinExtras", versions.MIXIN_EXTRAS.get()), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));

		for (MavenId id : new TransitiveDepResolveTask(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-api", versions.FABRIC_API.get())).get()) {
			d.addMaven(FabricMaven.URL, id, ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		}
	}

	@Override
	public Path[] getResourceDirs() {
		return new Path[]{super.getResourceDirs()[0], getProjectDir().resolve("src").resolve("generated").resolve("resources")};
	}

	@Override
	protected FabricContext createContext() {
		return new SimpleFabricContext() {
			/*
			When mixin extras loads after mixin,
			MixinObfuscationProcessorInjection.getSupportedAnnotationTypes() does not contain the additional mixin types.
			This method override fixes this.
			*/
			@Override
			public List<Path> getCompileDependencies() {
				LinkedList<Path> paths = new LinkedList<>();

				for (Path p : super.getCompileDependencies()) {
					if (p.getFileName().toString().contains("MixinExtras")) {
						paths.addFirst(p);
					} else {
						paths.addLast(p);
					}
				}

				return paths;
			}
		};
	}

	@Override
	protected FabricModule createModule() {
		return new SimpleFabricModule(context.get()) {
			private String generateClassPathGroups() {
				Path[] resourceDirs = getResourceDirs();
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < resourceDirs.length; i++) {
					if (i != 0) builder.append(File.pathSeparator);
					builder.append(resourceDirs[i].toString());
				}
				return builder.toString();
			}

			@Override
			public IdeModule createIdeModule() {
				Path cwd = PathUtil.resolveAndCreateDir(getModuleRoot(), "run");
				Lazy<List<Path>> classpath = new Lazy<>(() -> {
					Path mappingsClasspath = writeMappings4FabricStuff().getParent().getParent();
					ArrayList<Path> r = new ArrayList<>(context.runtimeDependencies.get().size() + 1);
					for (JavaJarDependency dependency : context.runtimeDependencies.get()) {
						r.add(dependency.jar);
					}
					r.add(mappingsClasspath);
					return r;
				});
				return new IdeModule.IdeModuleBuilder()
					.name(getModuleName())
					.root(getModuleRoot())
					.javaVersion(getJavaVersion())
					.dependencies(context.ideDependencies)
					.sourcePaths(getSrcDirs())
					.resourcePaths(getResourceDirs())
					.dependencyModules(getModuleDependencies().stream().map(m -> m.ideModule.get()).collect(Collectors.toList()))
					.runConfigs(
						new IdeModule.RunConfigBuilder()
							.name("Minecraft Client")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotClient")
							.classpath(classpath)
							.resourcePaths(getResourceDirs())
							.vmArgs(() -> {
								List<String> vmArgs = this.ideVmArgs(true);
								vmArgs.add("-Dfabric.classPathGroups=" + generateClassPathGroups());
								return vmArgs;
							})
							.args(() -> this.ideArgs(true)),
						new IdeModule.RunConfigBuilder()
							.name("Minecraft Server")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotServer")
							.classpath(classpath)
							.resourcePaths(getResourceDirs())
							.vmArgs(() -> {
								List<String> vmArgs = this.ideVmArgs(false);
								vmArgs.add("-Dfabric.classPathGroups=" + generateClassPathGroups());
								return vmArgs;
							})
							.args(() -> this.ideArgs(false)),
						new IdeModule.RunConfigBuilder()
							.name("Data Generation")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotClient")
							.classpath(classpath)
							.resourcePaths(getResourceDirs())
							.vmArgs(() -> {
								List<String> vmArgs = this.ideVmArgs(true);
								vmArgs.add("-Dfabric-api.datagen");
								vmArgs.add("-Dfabric-api.datagen.output-dir=" + getProjectDir().resolve("src").resolve("generated").resolve("resources").toString());
								vmArgs.add("-Dfabric-api.datagen.strict-validation");
								return vmArgs;
							})
							.args(() -> this.ideArgs(true))

					)
				.build();
			}
		};
	}
}
