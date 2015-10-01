/**
 * 
 */
package org.jocean.zookeeper.cli;

import java.io.IOException;

/**
 * @author Marvin.Ma
 *
 */
public class Booter {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String[] extjars = JVMUtil.addAllJarsToClassPath( 
				System.getProperty("user.dir") + "/lib" );
		for ( String jarname : extjars ) {
			System.out.println("add path [" + jarname + "]");
		}
		
		Main.main(args);
	}

}
