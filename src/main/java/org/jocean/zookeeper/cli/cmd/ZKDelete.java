/**
 * 
 */
package org.jocean.zookeeper.cli.cmd;

import org.apache.curator.framework.CuratorFramework;
import org.jocean.zookeeper.cli.ZKCliCommand;
import org.jocean.zookeeper.cli.ZKCliContext;

/**
 * @author Marvin.Ma
 *
 */
public class ZKDelete implements ZKCliCommand {

	public String getAction() {
		return "zkdelete";
	}

	public String getHelp() {
		return "remove zk path and children"
				+ "\r\n\tUsage: zkdelete [path]"
			;
	}

	@Override
	public String execute(final ZKCliContext ctx, String... args) throws Exception {
        final CuratorFramework curator = ctx.getCuratorFramework();
        if ( null == curator ) {
            return "not connect to zk, use zkopen first";
        }
        
        if (args.length < 1) {
            return "missing path arg\n" + getHelp();
        }
        curator.delete()
            .deletingChildrenIfNeeded()
            .forPath(args[0]);
        
        return "ok.";
	}
}
