/*
 * Created on May 10, 2013 2:42:19 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package gui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;

/**
 * @author Phoenix Xu
 */
public class ExcelTab extends Tab {
    private static final String TAB_TITLE = "Excel_Tab";

    private OleClientSite       clientSite;

    private OleFrame            oleFrame;

    public ExcelTab(ExcelDownloader downloader) {
        super(downloader);
    }

    @Override
    protected void createPageContent() {
        this.oleFrame = new OleFrame(this.tabFolderPage, SWT.NONE);
    }

    @Override
    public String getTabText() {
        return ExcelDownloader.getResourceString(TAB_TITLE);
    }

    public void openExcel(File file) {
        try {
            this.clientSite = new OleClientSite(this.oleFrame, SWT.NONE, "Excel.Sheet", file);
        } catch (SWTException error) {
            this.disposeClient();
        }
        if (this.clientSite != null) {
            this.clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
        }
    }

    public boolean isOpened() {
        return this.clientSite != null;
    }

    void disposeClient() {
        if (this.clientSite != null) {
            this.clientSite.dispose();
        }
        this.clientSite = null;
    }
}
