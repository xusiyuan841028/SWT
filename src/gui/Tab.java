/*
 * Created on May 10, 2013 2:41:50 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

/**
 * @author Phoenix Xu
 */
public abstract class Tab {

    ExcelDownloader downloader;

    Display         display;

    Shell           shell;

    Composite       tabFolderPage;

    public String getTabText() {
        return "";
    }

    public Tab(ExcelDownloader downloader) {
        this.downloader = downloader;
    }

    public Composite createTabFolderPage(TabFolder tabFolder) {
        /* Cache the shell and display. */
        this.shell = tabFolder.getShell();
        this.display = this.shell.getDisplay();

        this.tabFolderPage = new Composite(tabFolder, SWT.NONE);
        this.tabFolderPage.setLayout(new FillLayout());
        this.tabFolderPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        this.createPageContent();

        return this.tabFolderPage;
    }

    protected abstract void createPageContent();
}
