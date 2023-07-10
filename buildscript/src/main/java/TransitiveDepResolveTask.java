import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.util.Lazy;
import io.github.coolcrabs.brachyura.util.NetUtil;
import io.github.coolcrabs.brachyura.util.PathUtil;
import io.github.coolcrabs.brachyura.util.StreamUtil;
import io.github.coolcrabs.brachyura.util.Util;

public class TransitiveDepResolveTask {
	private final Lazy<Set<MavenId>> value = new Lazy<>(this::resolve);
	private final String repo;
	private final MavenId mavenId;
	private HashSet<MavenId> resolved = new HashSet<>();

	public TransitiveDepResolveTask(String repo, MavenId mavenId) {
		this.repo = repo;
		this.mavenId = mavenId;
	}

	private static Node nodeByName(NodeList nodes, String name) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName() == name) return node;
		}
		return null;
	}

	private Set<MavenId> resolve() {
		String fileName = mavenId.artifactId + "-" + mavenId.version;
		if (resolved.contains(mavenId)) return Collections.emptySet();
		resolved.add(mavenId);

		HashSet<MavenId> output = new HashSet<>();
		output.add(mavenId);

		ArrayList<Lazy<Set<MavenId>>> lazies = new ArrayList<>();
		try {
			Path cachedFilePath = PathUtil.cachePath().resolve(fileName + ".deps.txt");
			if (!Files.exists(cachedFilePath)) {
				URL url = new URL(
					(repo.endsWith("/") ? repo : repo + "/")
					+ mavenId.groupId.replace('.', '/')
					+ "/"
					+ mavenId.artifactId
					+ "/"
					+ mavenId.version
					+ "/"
					+ fileName + ".pom"
				);

				StringBuilder builder = new StringBuilder();
				try (InputStream inputStream = NetUtil.inputStream(url)) {
					Document pomDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
					Node dependenciesNode = nodeByName(pomDocument.getLastChild().getChildNodes(), "dependencies");
					if (dependenciesNode != null) {
						NodeList dependencyNodes = dependenciesNode.getChildNodes();
						for (int i = 0; i < dependencyNodes.getLength(); i++) {
							Node dependencyNode = dependencyNodes.item(i);
							if (dependencyNode.getNodeName().contains("#")) continue;
							NodeList childNodes = dependencyNode.getChildNodes();
							MavenId depMavenId = new MavenId(
								nodeByName(childNodes, "groupId").getTextContent(),
								nodeByName(childNodes, "artifactId").getTextContent(),
								nodeByName(childNodes, "version").getTextContent()
							);
							TransitiveDepResolveTask task = new TransitiveDepResolveTask(repo, depMavenId);
							task.resolved = resolved;
							lazies.add(task.value);

							if (i > 1) builder.append("\n");
							builder.append(depMavenId);
						}
					}
				}
				Files.write(cachedFilePath, builder.toString().getBytes(), StandardOpenOption.CREATE_NEW);
			} else {
				try (InputStream inputStream = Files.newInputStream(cachedFilePath)) {
					String[] deps = StreamUtil.readFullyAsString(inputStream).split("\n");
					for (String dep : deps) {
						if (dep.isEmpty()) continue;
						MavenId depMavenId = new MavenId(dep);
						TransitiveDepResolveTask task = new TransitiveDepResolveTask(repo, depMavenId);
						task.resolved = resolved;
						lazies.add(task.value);
					}
				}
			}
		} catch (Exception e) {
			throw Util.sneak(e);
		}

		Lazy.getParallel(lazies).forEach(output::addAll);

		return output;
	}

	public Set<MavenId> get() {
		return value.get();
	}
}
