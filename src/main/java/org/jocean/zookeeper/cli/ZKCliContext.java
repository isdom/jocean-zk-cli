package org.jocean.zookeeper.cli;

import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.jocean.cli.AbstractCliContext;
import org.jocean.cli.CliContext;

public class ZKCliContext extends AbstractCliContext implements CliContext {

    @Override
    public <V> V getProperty(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <V> CliContext setProperty(String key, V obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    public CuratorFramework getCuratorFramework() {
        return this._curator;
    }

    public void setCuratorFramework(final CuratorFramework curator) {
        this._curator = curator;
    }
    
    private CuratorFramework _curator;
}
