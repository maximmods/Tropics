import org.jetbrains.annotations.Nullable;

import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
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
	@Override
	public int getJavaVersion() {
		return 17;
	}

	@Override
	public @Nullable BrachyuraDecompiler decompiler() {
		return new FernflowerDecompiler(Maven.getMavenJarDep(QuiltMaven.URL, new MavenId("org.quiltmc:quiltflower:1.8.1")));
	}

	@Override
	public FabricLoader getLoader() {
		return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.14.9"));
	}

	@Override
	public VersionMeta createMcVersion() {
		return Minecraft.getVersion("1.19.2");
	}

	@Override
	public MappingTree createMappings() {
		return createMojmap();
	}

	@Override
	public void getModDependencies(ModDependencyCollector d) {
		for (String[] module : new String[][] {
			{"fabric-api-base", "0.4.10+e62f51a390"},
			{"fabric-resource-loader-v0", "0.6.0+6bee109e90"},
			{"fabric-registry-sync-v0", "0.9.23+23c4cfef90"},
			{"fabric-networking-api-v1", "1.2.3+5eb68ef290"},
			{"fabric-lifecycle-events-v1", "2.1.1+83a8659290"},
			{"fabric-item-groups-v0", "0.3.27+6bee109e90"},
			{"fabric-object-builder-api-v1", "4.0.10+7675279690"},
			{"fabric-content-registries-v0", "3.2.3+aeb40ebe90"},
			{"fabric-mining-level-api-v1", "2.1.13+33fbc73890"}
		}) {
			d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", module[0], module[1]), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		}

		for (String[] module : new String[][] {
			{"lib39-core"},
			{"lib39-lockpick"},
			{"lib39-dessicant"},
			{"lib39-crowbar"}
		}) {
			jij(d.addMaven("https://repo.sleeping.town/", new MavenId("com.unascribed", module[0], "1.1.10"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
		}

		d.addMaven("https://repo.sleeping.town/", new MavenId("com.unascribed:lucium:1.1"), ModDependencyFlag.RUNTIME);
	}
}
