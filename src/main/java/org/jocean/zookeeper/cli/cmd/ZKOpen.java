/**
 * 
 */
package org.jocean.zookeeper.cli.cmd;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.jocean.zookeeper.cli.ZKCliCommand;
import org.jocean.zookeeper.cli.ZKCliContext;

/**
 * @author Marvin.Ma
 *
 */
public class ZKOpen implements ZKCliCommand {

	public String getAction() {
		return "zkopen";
	}

	public String getHelp() {
		return "connect to zookeeper server"
				+ "\r\n\tUsage: zkopen [zk connect String]"
			;
	}

	@Override
	public String execute(final ZKCliContext ctx, String... args) throws Exception {
        CuratorFramework curator = ctx.getCuratorFramework();
        if ( null != curator ) {
            return "already connecting or connected ZooKeeperServer [" 
                    + curator.getZookeeperClient().getCurrentConnectionString() 
                    + "], exec closezk first";
        }
        
        if (args.length < 1) {
            return "missing zk connect String\n" + getHelp();
        }
        
        curator = CuratorFrameworkFactory.builder()
                .connectString(args[0])
                .retryPolicy(new RetryOneTime(1000))
                .build();
        curator.start();
        
        ctx.setCuratorFramework(curator);
        
        return "ok.";
	}
}
