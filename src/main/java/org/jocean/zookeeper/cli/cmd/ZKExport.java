/**
 *
 */
package org.jocean.zookeeper.cli.cmd;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.curator.framework.CuratorFramework;
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
public class ZKExport implements ZKCliCommand {

	@Override
    public String getAction() {
		return "zkexport";
	}

	@Override
    public String getHelp() {
		return "dump zk path and children's node content"
				+ "\r\n\tUsage: zkexport [path] [saved filename]"
			;
	}

	@Override
	public String execute(final ZKCliContext ctx, final String... args) throws Exception {
        final CuratorFramework curator = ctx.getCuratorFramework();
        if ( null == curator ) {
            return "not connect to zk, use zkopen first";
        }

        if (args.length < 1) {
            return "missing path arg\n" + getHelp();
        }

        final UnitDescription root = dumpNode(curator, args[0]);
        final Yaml yaml = new Yaml(new Constructor(UnitDescription.class));

        if (args.length < 2) {
            System.out.println( yaml.dump(root) );
        } else {
            final File savedFile = new File(args[1]);
            System.out.println( "exporting yaml to " + savedFile.getAbsolutePath() + " ...");
            try (final Writer writer = new FileWriter(savedFile)) {
                yaml.dump(root, writer);
            }
        }

        return "ok.";
	}

    private UnitDescription dumpNode(final CuratorFramework curator,
            final String path) throws Exception {
        final UnitDescription desc = dumpContent(curator, path);
        if (null!=desc) {
            final List<String> children = curator.getChildren().forPath(path);
            final List<UnitDescription>  descs = new ArrayList<>();
            for (final String child : children) {
                final UnitDescription childDesc = dumpNode(curator, path + "/" + child);
                if (null!=childDesc) {
                    descs.add(childDesc);
                } else {
                    System.out.println(path + "/" + child + " is EphemeralNode, not export.");
                }
            }
            if (!descs.isEmpty()) {
                desc.setChildren(descs.toArray(new UnitDescription[0]));
            }
        }
        return desc;
    }

    private UnitDescription dumpContent(final CuratorFramework curator,
            final String path) throws Exception {
        if (isEphemeralNode(curator, path) ) {
            return null;
        }
        final UnitDescription desc = new UnitDescription();
        desc.setName(FilenameUtils.getName(path));
        final byte[] content = curator.getData().forPath(path);
        if (null != content) {
            desc.setParameters(new String(content, Charsets.UTF_8));
        }
        return desc;
    }

    private static boolean isEphemeralNode(final CuratorFramework curator,
            final String path) throws Exception {
        return 0 != curator.checkExists().forPath(path).getEphemeralOwner();
    }
}
