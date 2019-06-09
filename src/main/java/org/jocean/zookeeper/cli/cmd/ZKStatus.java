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
public class ZKStatus implements ZKCliCommand {

	@Override
    public String getAction() {
		return "zkstatus";
	}

	@Override
    public String getHelp() {
		return "query zookeeper server's connecting status"
				+ "\r\n\tUsage: zkstatus"
			;
	}

	@Override
    public String execute(final ZKCliContext ctx, final String... args) throws Exception {
        final CuratorFramework curator = ctx.getCuratorFramework();
        return  (null == curator)
                ? "ZK Not connected."
                : "ZK Connected or Connecting" + curator.getZookeeperClient().getCurrentConnectionString();
	}
}
