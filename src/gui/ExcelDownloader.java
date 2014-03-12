/*
 * Created on May 8, 2013 1:41:35 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package gui;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import utils.Utils;

/**
 * @author Phoenix Xu
 */
public class ExcelDownloader {

    private static ResourceBundle resourceBundle  = ResourceBundle.getBundle("exceldownloader");

    public static final String    VALUE_SEPARATOR = ",";

    private TabFolder             tabFolder;

    private Tab[]                 tabs;

    /**
     * Creates an instance of a ExcelDownloader embedded inside the supplied parent Composite.
     * 
     * @param parent the container of the example
     */
    public ExcelDownloader(Composite parent) {
        this.tabFolder = new TabFolder(parent, SWT.NONE);

        this.tabs = new Tab[] { new QueryProfileTab(this), new ExcelTab(this) };
        for (Tab tab : this.tabs) {
            TabItem item = new TabItem(this.tabFolder, SWT.NONE);
            item.setText(tab.getTabText());
            item.setControl(tab.createTabFolderPage(this.tabFolder));
        }
    }

    public Tab[] getTabs() {
        return this.tabs;
    }

    /**
     * Grabs input focus.
     */
    public void setFocus() {
        this.tabFolder.setFocus();
    }

    /**
     * Disposes of all resources associated with a particular instance of the ExcelDownloader.
     */
    public void dispose() {
        this.tabFolder = null;
    }

    /**
     * Invokes as a standalone program.
     * 
     * @param args
     */
    public static void main(String[] args) {

        final Display display = new Display();
        // main shell
        final Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        new ExcelDownloader(shell);
        shell.setText(getResourceString("window.title"));
        shell.addShellListener(new ShellAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ShellAdapter#shellClosed(org.eclipse.swt.events.ShellEvent)
             */
            @Override
            public void shellClosed(ShellEvent e) {
                Shell[] shells = display.getShells();
                // Close all shells.
                for (Shell shell2 : shells) {
                    if (shell2 != shell) {
                        shell2.close();
                    }
                }
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Gets a string from the resource bundle. We don't want to crash because of a missing String. Returns the key if
     * not found.
     */
    public static String getResourceString(String key) {
        return Utils.getResourceString(resourceBundle, key);
    }

    /**
     * Gets a string from the resource bundle and binds it with the given arguments. If the key is not found, return the
     * key.
     */
    public static String getResourceString(String key, Object[] args) {
        return Utils.getResourceString(resourceBundle, key, args);
    }

    public QueryProfileTab getQueryTab() {
        for (Tab tab : this.tabs) {
            if (tab instanceof QueryProfileTab) {
                return (QueryProfileTab) tab;
            }
        }
        return null;
    }

    public ExcelTab getExcelTab() {
        for (Tab tab : this.tabs) {
            if (tab instanceof ExcelTab) {
                return (ExcelTab) tab;
            }
        }
        return null;
    }
}
