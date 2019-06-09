/**
 *
 */
package org.jocean.zookeeper.cli.cmd;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.jocean.zookeeper.cli.ZKCliCommand;
import org.jocean.zookeeper.cli.ZKCliContext;

import com.google.common.base.Charsets;

/**
 * @author Marvin.Ma
 *
 */
public class ZKList implements ZKCliCommand {

	@Override
    public String getAction() {
		return "zklist";
	}

	@Override
    public String getHelp() {
		return "list zk path and children's node content"
				+ "\r\n\tUsage: zklist [path]"
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

        dumpNode(curator, args[0], "");
        return "ok.";
	}

    private void dumpNode(final CuratorFramework curator,
            final String path, final String prefix) throws Exception {
        dumpContent(curator, path, prefix);
        final List<String> children = curator.getChildren().forPath(path);
        for (final String child : children) {
            dumpNode(curator, path + "/" + child, prefix + "\t");
        }
    }

    private void dumpContent(final CuratorFramework curator,
            final String path, final String prefix) throws Exception {
        System.out.println(prefix + "path:" + path);
        final byte[] content = curator.getData().forPath(path);
        System.out.println( null != content
                ? prefix + new String(content, Charsets.UTF_8)
                : prefix +"<EMPTY>");
    }
}
