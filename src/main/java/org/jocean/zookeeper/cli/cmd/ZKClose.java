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
public class ZKClose implements ZKCliCommand {

	public String getAction() {
		return "zkclose";
	}

	public String getHelp() {
		return "disconnect from connected zookeeper server"
				+ "\r\n\tUsage: zkclose"
			;
	}

	@Override
	public String execute(final ZKCliContext ctx, final String... args) throws Exception {
        final CuratorFramework curator = ctx.getCuratorFramework();
        if ( null == curator ) {
            return "NOT connecting or connected ZooKeeperServer, ignore.";
        }

        curator.close();
        ctx.setCuratorFramework(null);
        return "ok.";
	}
}
