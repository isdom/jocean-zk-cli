/**
 * 
 */
package org.jocean.zookeeper.cli.cmd;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.jocean.j2se.unit.model.UnitDescription;
import org.jocean.zookeeper.cli.ZKCliCommand;
import org.jocean.zookeeper.cli.ZKCliContext;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.common.base.Charsets;

/**
 * @author Marvin.Ma
 *
 */
public class ZKImport implements ZKCliCommand {

    public String getAction() {
		return "zkimport";
	}

	public String getHelp() {
		return "dump zk path and children's node content"
				+ "\r\n\tUsage: zkimport [import filename] [to path]"
			;
	}

	@Override
	public String execute(final ZKCliContext ctx, String... args) throws Exception {
        final CuratorFramework curator = ctx.getCuratorFramework();
        if ( null == curator ) {
            return "not connect to zk, use zkopen first";
        }
        
        if (args.length < 2) {
            return "missing import filename or path arg\n" + getHelp();
        }
        
        final UnitDescription root = loadYaml(args[0]);
        if (null!=root) {
            importNode(curator, args[1], root);
        }
        return "ok.";
	}

    private UnitDescription loadYaml(final String filename) throws Exception {
        try (final Reader reader = new FileReader(new File(filename))) {
            final Yaml yaml = new Yaml(new Constructor(UnitDescription.class));
            return (UnitDescription)yaml.load(reader);
        }
    }

    private void importNode(final CuratorFramework curator, 
            final String path, final UnitDescription desc) throws Exception {
        final String createdPath = importContent(curator, path, desc);
        for (UnitDescription child : desc.getChildren()) {
            importNode(curator, createdPath, child);
        }
    }

    private String importContent(final CuratorFramework curator, 
            final String path, final UnitDescription desc) throws Exception {
        return curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path + "/" + desc.getName(), 
                        genBytes(desc.getParameters()));
    }

    private static final byte[] EMPTY_BYTES = new byte[0];

    private static byte[] genBytes(final String parameters) {
        if (null!=parameters) {
            return parameters.getBytes(Charsets.UTF_8);
        } else {
            return EMPTY_BYTES;
        }
    }
}
