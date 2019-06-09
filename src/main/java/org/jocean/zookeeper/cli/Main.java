/**
 *
 */
package org.jocean.zookeeper.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.fusesource.jansi.AnsiConsole;
import org.jocean.cli.CliShell;
import org.jocean.cli.DefaultCommandRepository;
import org.jocean.cli.cmd.ExitCommand;
import org.jocean.cli.cmd.HelpCommand;
import org.jocean.cli.cmd.StopException;
import org.jocean.zookeeper.cli.cmd.ZKClose;
import org.jocean.zookeeper.cli.cmd.ZKDelete;
import org.jocean.zookeeper.cli.cmd.ZKExport;
import org.jocean.zookeeper.cli.cmd.ZKImport;
import org.jocean.zookeeper.cli.cmd.ZKList;
import org.jocean.zookeeper.cli.cmd.ZKOpen;
import org.jocean.zookeeper.cli.cmd.ZKStatus;

import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
import jline.console.history.FileHistory;
import jline.console.history.PersistentHistory;


/**
 * @author Marvin.Ma
 *
 */
public class Main {

	public static final String HISTORY_FILENAME = "/.jocean.zkcli.history";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws IOException {

		AnsiConsole.systemInstall();

		final DefaultCommandRepository	repo =
				new DefaultCommandRepository()
				.addCommand(new HelpCommand())
				.addCommand(new ExitCommand())
                .addCommand(new ZKOpen())
                .addCommand(new ZKClose())
                .addCommand(new ZKStatus())
                .addCommand(new ZKList())
                .addCommand(new ZKExport())
                .addCommand(new ZKImport())
                .addCommand(new ZKDelete())
				;

        final ConsoleReader reader = new ConsoleReader();
        final PersistentHistory history =
                new FileHistory(new File(System.getProperty("user.home") + HISTORY_FILENAME));
        reader.setHistory(history);
        reader.setBellEnabled(false);

        reader.addCompleter(new StringsCompleter(repo.getCommandActionAsArray()));

		final CliShell<ZKCliContext> shell = new CliShell<ZKCliContext>();

		final ZKCliContext ctx = new ZKCliContext();

		ctx.setCommandRepository(repo);

        String cmdline;
        final PrintWriter out = new PrintWriter(System.out);

        while ((cmdline = reader.readLine("zk> ")) != null) {
            try {
                final String result = shell.execute(ctx, cmdline);
                if ( null != result ) {
    				out.println(result);
    				out.flush();
                }
            } catch (final StopException e) {
                break;
            }
        }

        history.flush();
	}

}
