/**
 * 
 */
package org.jocean.zookeeper.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import jline.console.history.FileHistory;

import org.fusesource.jansi.AnsiConsole;
import org.jocean.cli.CliShell;
import org.jocean.cli.DefaultCommandRepository;
import org.jocean.cli.cmd.ExitCommand;
import org.jocean.cli.cmd.HelpCommand;


/**
 * @author Marvin.Ma
 *
 */
public class Main {

	public static final String HISTORY_FILENAME = "/.joceancli.history";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws IOException {
		
		AnsiConsole.systemInstall();
		
		final DefaultCommandRepository	repo = 
				new DefaultCommandRepository()
				.addCommand(new HelpCommand())
				.addCommand(new ExitCommand())
				;
		
        final ConsoleReader reader = new ConsoleReader();
        
        reader.setHistory(new FileHistory(new File(System.getProperty("user.home") + HISTORY_FILENAME)));
        reader.setBellEnabled(false);

        reader.addCompleter(new StringsCompleter(repo.getCommandActionAsArray()));
        
		final CliShell<ZKCliContext> shell = new CliShell<ZKCliContext>();
		
		final ZKCliContext ctx = new ZKCliContext();

		ctx.setCommandRepository(repo);
		
		shell.setCommandContext(ctx);
		
        String cmdline;
        final PrintWriter out = new PrintWriter(System.out);

        while ((cmdline = reader.readLine("zk> ")) != null) {
            String result = shell.execute(cmdline);
            if ( null != result ) {
				out.println(result);
				out.flush();
            }
        }
	}

}
