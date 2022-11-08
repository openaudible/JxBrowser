import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.swt.BrowserView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * This example demonstrates how to initialize Chromium, create a browser instance
 * (equivalent of the Chromium tab), embed an SWT BrowserView widget into SWT
 * shell to display content of the loaded web page, load the required web page.
 */
public final class JxBrowserTest {

    public static void main(String[] args) {
        if (System.getProperty("jxbrowser.license.key")==null)
        {
            Properties p = new Properties();
            try {
                p.load(new FileReader("creds.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.setProperty("jxbrowser.license.key", p.getProperty("jxbrowser.license.key"));
        }
        // Initialize Chromium.
        // Engine engine = Engine.newInstance(HARDWARE_ACCELERATED);
        File dir = new File("bin_" + System.getProperty("os.arch"));
        dir.mkdirs();

        Engine engine = Engine.newInstance(
                EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                        .chromiumDir(dir.toPath())
                        .build());

        // Create a Browser instance.
        Browser browser = engine.newBrowser();

        // browser.userAgent("");
        // Load the required web page.
        browser.navigation().loadUrl("https://html5test.com");

        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("JxBrowser SWT");
        shell.setLayout(new FillLayout());

        // Create and embed SWT BrowserView widget to display web content.
        BrowserView view = BrowserView.newInstance(shell, browser);
        shell.addListener(SWT.Dispose, (e) -> {
            view.dispose();
            System.out.println("disposed view");
        });

        view.setSize(1280, 800);

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        // Shutdown Chromium and release allocated resources.
        engine.close();

        display.dispose();
    }
}
