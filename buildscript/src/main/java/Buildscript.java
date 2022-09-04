import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.fabric.FabricContext;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.SimpleFabricProject;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.quilt.QuiltMaven;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends SimpleFabricProject {
	private Versions versions = new Versions(getProjectDir().resolve("buildscript").resolve("versions.properties"));

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

		for (String[] module : new String[][] {
			{"fabric-api-base", "0.4.10+e62f51a390"},
			{"fabric-resource-loader-v0", "0.6.0+6bee109e90"},
			{"fabric-registry-sync-v0", "0.9.23+23c4cfef90"},
			{"fabric-networking-api-v1", "1.2.3+5eb68ef290"},
			{"fabric-lifecycle-events-v1", "2.1.1+83a8659290"},
			{"fabric-item-groups-v0", "0.3.27+6bee109e90"},
			{"fabric-rendering-v1", "1.10.16+aeb40ebe90"},
			{"fabric-models-v0", "0.3.17+aeb40ebe90"},
			{"fabric-object-builder-api-v1", "4.0.10+7675279690"},
			{"fabric-content-registries-v0", "3.2.3+aeb40ebe90"},
			{"fabric-mining-level-api-v1", "2.1.13+33fbc73890"}
		}) {
			d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", module[0], module[1]), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		}

		for (String[] module : new String[][] {
			{"lib39-core"},
			{"lib39-ripple"},
			{"lib39-dessicant"},
			{"lib39-crowbar"}
		}) {
			jij(d.addMaven("https://repo.sleeping.town/", new MavenId("com.unascribed", module[0], versions.LIB39.get()), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
		}

		d.addMaven("https://repo.sleeping.town/", new MavenId("com.unascribed", "lucium", versions.LUCIUM.get()), ModDependencyFlag.RUNTIME);

		// Kahur
		d.addMaven("https://api.modrinth.com/maven/", new MavenId("maven.modrinth", "kahur", versions.KAHUR.get()), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		for (String[] module : new String[][] {
			{"lib39-lockpick"},
			{"lib39-recoil"},
			{"lib39-fractal"},
			{"lib39-util"}
		}) {
			d.addMaven("https://repo.sleeping.town/", new MavenId("com.unascribed", module[0], versions.LIB39.get()), ModDependencyFlag.RUNTIME);
		}
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
}
