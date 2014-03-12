/*
 * Created on May 10, 2013 2:42:19 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package gui;

import static utils.FileUtils.EXCEL_REPORT_PATH_NO_EXT;
import static utils.FileUtils.TEMP_EXCEL_PATH;
import static utils.Utils.concatenate;
import http.HttpDownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import model.Account;
import model.QueryParameter;
import model.QueryProfile;

import org.apache.http.HttpException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import excel.ExcelMerger;
import formatter.QueryProfileFormatter;

/**
 * @author Phoenix Xu
 */
public class QueryProfileTab extends Tab {

    private static Logger                      logger                       = LoggerFactory.getLogger(QueryProfileTab.class);

    private static final String                TAB_TITLE                    = "QueryProfile_Tab";

    private static final String                FILE_GROUP_TITLE             = "FileGroup";

    private static final String                PARAMETER_GROUP_TITLE        = "ParameterGroup";

    private static final String                EXCEL_FORMAT_GROUP_TITLE     = "ExcelFormatGroup";

    private static final String                EXCEL2003_BUTTON_TITLE       = "Excel2003Button";

    private static final String                EXCEL2007_BUTTON_TITLE       = "Excel2007Button";

    private static final String                DOWNLOAD_MODE_GROUP_TITLE    = "DownloadModeGroup";

    private static final String                SINGLE_THREAD_BUTTON_TITLE   = "SingleThreadButton";

    private static final String                MULTIPLE_THREAD_BUTTON_TITLE = "MultipleThreadButton";

    private static final String                FILE_AMOUNT_GROUP_TITLE      = "FileAmountGroup";

    private static final String                SINGLE_FILE_BUTTON_TITLE     = "SingleFileButton";

    private static final String                MULTIPLE_FILE_BUTTON_TITLE   = "MultipleFileButton";

    private static final String                OPEN_PROFILE_TITLE           = "OpenProfile";

    private static final String                SAVE_PROFILE_TITLE           = "SaveProfile";

    private static final String                DOWNLOAD_BUTTON_TITLE        = "DownloadButton";

    private static final String                QUERY_GROUP_TITLE            = "QueryGroup";

    private static final String                ADD_ITEM_TITLE               = "AddItem";

    private static final String                DELETE_ITEM_TITLE            = "DeleteItem";

    private static final String                CLEAR_ITEM_TITLE             = "ClearItem";

    private static final String                ACCOUNT_FIELD_NAME           = "AccountField";

    private static final String                PASSWORD_FIELD_NAME          = "PasswordField";

    private static final String                CITY_FIELD_NAME              = "CityField";

    private static final String                YEAR_FIELD_NAME              = "YearField";

    private static final String                INTERVAL_FIELD_NAME          = "IntervalField";

    private static final String                ERROR_MSG_EMPTY_FIELD        = "Error_EmptyField";

    private static final String                ERROR_MSG_SAVE_PROFILE       = "Error_SaveQueryProfile";

    private static final String                ERROR_MSG_OPEN_PROFILE       = "Error_OpenQueryProfile";

    private static final String                ERROR_MSG_EMPTY_PROFILE      = "Error_EmptyQueryProfile";

    private static final String                INFO_MSG_SAVE_PROFILE        = "Info_SaveQueryProfile";

    private static final String                INFO_MSG_DOWNLOAD_EXCEL      = "Info_DownloadExcelFile";

    private static final String                ERROR_MESSAGE_TITLE          = "ErrorMessage";

    private static final String                SAVE_MESSAGE_TITLE           = "SaveMessage";

    private static final String                DOWNLOAD_MESSAGE_TITLE       = "DownloadMessage";

    private static final String                FILE_CONTENT_TOOLTIP         = "FileContentTooltip";

    private static final String                CITY_FIELD_TEXT              = "Cities";

    private static final String                CITY_FIELD_VALUES            = "CityCode";

    private static final String                QUERY_RECORD_TIP             = "QueryRecordTip";

    private static final String                QUERY_RECORD_FORMAT          = "QueryRecordFormat";

    private static final String                QUERY_FILE_HEADER            = ExcelDownloader.getResourceString(QUERY_RECORD_TIP)
                                                                                    + Text.DELIMITER
                                                                                    + Text.DELIMITER
                                                                                    + ExcelDownloader
                                                                                            .getResourceString(QUERY_RECORD_FORMAT)
                                                                                    + Text.DELIMITER + Text.DELIMITER;

    private static final String[]              FIELD_NAMES                  = new String[] {
            ExcelDownloader.getResourceString(ACCOUNT_FIELD_NAME), ExcelDownloader.getResourceString(PASSWORD_FIELD_NAME),
            ExcelDownloader.getResourceString(CITY_FIELD_NAME), ExcelDownloader.getResourceString(YEAR_FIELD_NAME),
            ExcelDownloader.getResourceString(INTERVAL_FIELD_NAME)         };

    private static final int                   ACCOUNT_COL                  = 0;

    private static final int                   PASSWORD_COL                 = 1;

    private static final int                   CITY_COL                     = 2;

    private static final int                   YEAR_COL                     = 3;

    private static final int                   INTERVAL_COL                 = 4;

    private static final int                   COL_AMOUNT                   = 5;

    private static final int[]                 COL_WIDTH                    = { 200, 100, 100, 80, 80 };

    private static int[]                       SASH_WIDTH                   = { 45, 55 };

    private static final String[]              CITIES                       = ExcelDownloader.getResourceString(CITY_FIELD_TEXT).split(
                                                                                    ExcelDownloader.VALUE_SEPARATOR);

    public static final String[]               CITYCODE                     = ExcelDownloader.getResourceString(CITY_FIELD_VALUES).split(
                                                                                    ExcelDownloader.VALUE_SEPARATOR);

    static {
        logger.debug(concatenate(CITIES));
        logger.debug(concatenate(CITYCODE));
    }

    private static final QueryProfileFormatter formatter                    = new QueryProfileFormatter();

    private SashForm                           sash;

    private Group                              fileGroup;

    private Text                               fileContent;

    private Group                              paramterGroup;

    private Button                             singleFileButton;

    private Button                             multipleFileButton;

    private Button                             excel2003Button;

    private Button                             excel2007Button;

    private Button                             singleThreadButton;

    private Button                             multipleThreadButton;

    private Button                             saveQueryProfileButton;

    private Button                             openQueryProfileButton;

    private Button                             downloadButton;

    private Group                              inputGroup;

    private ToolItem                           addItem;

    private ToolItem                           deleteItem;

    private ToolItem                           clearItem;

    /**
     * A table whose items represent the query profiles.
     */
    private Table                              table;

    private Vector<QueryProfile>               queryData                    = new Vector<QueryProfile>();

    TableItem                                  newItem, lastSelected;

    private TableEditor                        accountEditor;

    private Text                               accountText;

    private TableEditor                        passwordEditor;

    private Text                               passwordText;

    private TableEditor                        cityEditor;

    private CCombo                             cityCombo;

    private TableEditor                        yearEditor;

    private Spinner                            yearSpinner;

    private TableEditor                        intervalEditor;

    private Spinner                            intervalSpinner;

    private int                                index;

    private HashMap<String, Control>           fieldToControl               = new HashMap<String, Control>();

    private SelectionListener                  selectionListener            = new SelectionAdapter() {

                                                                                @Override
                                                                                public void widgetSelected(SelectionEvent e) {
                                                                                    // TODO
                                                                                }
                                                                            };

    private TraverseListener                   tableTraverseListener        = new TraverseListener() {

                                                                                public void keyTraversed(TraverseEvent e) {
                                                                                    if (e.detail == SWT.TRAVERSE_RETURN) {
                                                                                        e.doit = false;
                                                                                        QueryProfileTab.this.refreshEditors();
                                                                                    }
                                                                                }
                                                                            };

    private TraverseListener                   traverseListener             = new TraverseListener() {

                                                                                public void keyTraversed(TraverseEvent e) {
                                                                                    if (e.detail == SWT.TRAVERSE_ESCAPE) {
                                                                                        QueryProfileTab.this.disposeEditors();
                                                                                    }
                                                                                }
                                                                            };

    public QueryProfileTab(ExcelDownloader downloader) {
        super(downloader);
    }

    @Override
    public String getTabText() {
        return ExcelDownloader.getResourceString(TAB_TITLE);
    }

    @Override
    protected void createPageContent() {
        /* Create a two column page with a SashForm */
        this.sash = new SashForm(this.tabFolderPage, SWT.HORIZONTAL);

        /* Create the "file" and "input" columns */
        this.createFileGroup();
        this.createParamterGroup();

        this.sash.setWeights(SASH_WIDTH);
    }

    /**
     * Create the group for displaying the content of the query file.
     */
    private void createFileGroup() {
        this.fileGroup = new Group(this.sash, SWT.NONE);
        this.fileGroup.setText(ExcelDownloader.getResourceString(FILE_GROUP_TITLE));
        this.fileGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.fileGroup.setLayout(new GridLayout());

        this.fileContent = new Text(this.fileGroup, SWT.NONE | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.LEFT | SWT.BORDER
                | SWT.READ_ONLY);
        this.fileContent.setText(QUERY_FILE_HEADER);
        this.fileContent.setToolTipText(ExcelDownloader.getResourceString(FILE_CONTENT_TOOLTIP));
        this.fileContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
     * Create the group for inputting the query parameters.
     */
    private void createParamterGroup() {
        this.paramterGroup = new Group(this.sash, SWT.NONE);
        this.paramterGroup.setText(ExcelDownloader.getResourceString(PARAMETER_GROUP_TITLE));

        GridLayout layout = new GridLayout(2, true);
        layout.horizontalSpacing = 10;
        this.paramterGroup.setLayout(layout);

        this.createParamterWidgets();
        this.createMainButtons();
        
        this.createProfileTableButtons();
        this.createProfileTableWidget();
    }

    /**
     * Create widgets for setting parameters.
     */
    private void createParamterWidgets() {
        // Excel format
        Group excelGroup = new Group(this.paramterGroup, SWT.NONE);
        excelGroup.setText(ExcelDownloader.getResourceString(EXCEL_FORMAT_GROUP_TITLE));
        excelGroup.setLayout(new GridLayout());
        excelGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        // Excel 2003 Format button
        this.excel2003Button = new Button(excelGroup, SWT.RADIO);
        this.excel2003Button.setText(ExcelDownloader.getResourceString(EXCEL2003_BUTTON_TITLE));
        this.excel2003Button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.excel2003Button.setSelection(true);
        this.excel2003Button.addSelectionListener(this.selectionListener);
        // Excel 2007 Format button
        this.excel2007Button = new Button(excelGroup, SWT.RADIO);
        this.excel2007Button.setText(ExcelDownloader.getResourceString(EXCEL2007_BUTTON_TITLE));
        this.excel2007Button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.excel2007Button.addSelectionListener(this.selectionListener);

        // Download mode
        Group downloadGroup = new Group(this.paramterGroup, SWT.NONE);
        downloadGroup.setText(ExcelDownloader.getResourceString(DOWNLOAD_MODE_GROUP_TITLE));
        downloadGroup.setLayout(new GridLayout());
        downloadGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        // Single Thread button
        this.singleThreadButton = new Button(downloadGroup, SWT.RADIO);
        this.singleThreadButton.setText(ExcelDownloader.getResourceString(SINGLE_THREAD_BUTTON_TITLE));
        this.singleThreadButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.singleThreadButton.setSelection(true);
        this.singleThreadButton.addSelectionListener(this.selectionListener);
        // Multiple Thread button
        this.multipleThreadButton = new Button(downloadGroup, SWT.RADIO);
        this.multipleThreadButton.setText(ExcelDownloader.getResourceString(MULTIPLE_THREAD_BUTTON_TITLE));
        this.multipleThreadButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.multipleThreadButton.addSelectionListener(this.selectionListener);

        // File amount
        Group fileGroup = new Group(this.paramterGroup, SWT.NONE);
        fileGroup.setText(ExcelDownloader.getResourceString(FILE_AMOUNT_GROUP_TITLE));
        fileGroup.setLayout(new GridLayout());
        fileGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        // Single File button
        this.singleFileButton = new Button(fileGroup, SWT.RADIO);
        this.singleFileButton.setText(ExcelDownloader.getResourceString(SINGLE_FILE_BUTTON_TITLE));
        this.singleFileButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.singleFileButton.setSelection(true);
        this.singleFileButton.addSelectionListener(this.selectionListener);
        // Multiple File button
        this.multipleFileButton = new Button(fileGroup, SWT.RADIO);
        this.multipleFileButton.setText(ExcelDownloader.getResourceString(MULTIPLE_FILE_BUTTON_TITLE));
        this.multipleFileButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        this.multipleFileButton.addSelectionListener(this.selectionListener);
    }

    /**
     * Create the buttons for opening and saving the query profile file and starting downloading.
     */
    private void createMainButtons() {
        Group buttonGroup = new Group(this.paramterGroup, SWT.NONE);
        buttonGroup.setLayout(new GridLayout());
        buttonGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Button: Open Query Profile
        this.openQueryProfileButton = new Button(buttonGroup, SWT.NONE);
        this.openQueryProfileButton.setText(ExcelDownloader.getResourceString(OPEN_PROFILE_TITLE));
        this.openQueryProfileButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        this.openQueryProfileButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                // Open file dialog
                FileDialog fileDialog = new FileDialog(QueryProfileTab.this.shell, SWT.OPEN | SWT.CANCEL);
                fileDialog.setText(ExcelDownloader.getResourceString(OPEN_PROFILE_TITLE));
                String filePath = fileDialog.open();
                if (StringUtils.isEmpty(filePath)) {
                    return;
                }
                try {
                    if (new File(filePath).exists()) {
                        // Open the profile file & refresh table items.
                        QueryProfileTab.this.fileContent.setText(QueryProfileTab.this.openQueryProfileFile(filePath));
                        QueryProfileTab.this.refreshTableItems();
                    } else {
                        // The profile file is inexistent.
                        QueryProfileTab.this.openMessageBoxFromResource(ERROR_MESSAGE_TITLE, ERROR_MSG_EMPTY_PROFILE,
                                new String[] { filePath }, SWT.OK);
                    }
                } catch (IOException e) {
                    logger.error("Can't open file \"" + filePath + "\"!", e);
                    QueryProfileTab.this.openMessageBoxFromResource(ERROR_MESSAGE_TITLE, ERROR_MSG_OPEN_PROFILE, new String[] { filePath },
                            SWT.OK);
                }
            }
        });

        this.saveQueryProfileButton = new Button(buttonGroup, SWT.NONE);
        this.saveQueryProfileButton.setText(ExcelDownloader.getResourceString(SAVE_PROFILE_TITLE));
        this.saveQueryProfileButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        this.saveQueryProfileButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                // Open file dialog
                FileDialog fileDialog = new FileDialog(QueryProfileTab.this.shell, SWT.SAVE | SWT.CANCEL);
                fileDialog.setText(ExcelDownloader.getResourceString(SAVE_PROFILE_TITLE));
                String filePath = fileDialog.open();
                try {
                    QueryProfileTab.this.saveQueryProfileFile(filePath);
                    QueryProfileTab.this.openMessageBoxFromResource(SAVE_MESSAGE_TITLE, INFO_MSG_SAVE_PROFILE, new String[] { filePath },
                            SWT.OK);
                } catch (IOException e) {
                    logger.error("Can't save file \"" + filePath + "\"!", e);
                    QueryProfileTab.this.openMessageBoxFromResource(ERROR_MESSAGE_TITLE, ERROR_MSG_SAVE_PROFILE, new String[] { filePath },
                            SWT.OK);
                }
            }
        });

        this.downloadButton = new Button(buttonGroup, SWT.NONE);
        this.downloadButton.setText(ExcelDownloader.getResourceString(DOWNLOAD_BUTTON_TITLE));
        this.downloadButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        this.downloadButton.addSelectionListener(new SelectionAdapter() {

            /**
             * @param event
             */
            @Override
            public void widgetSelected(SelectionEvent event) {
                // ProgressBar progressBar = new ProgressBar(QueryProfileTab.this.tabFolderPage, SWT.HORIZONTAL |
                // SWT.SMOOTH
                // | SWT.INDETERMINATE);
                // progressBar.setMinimum(0);
                // progressBar.setMaximum(QueryProfileTab.this.queryData.size() * 10);
                // progressBar.setBounds(100, 100, 200, 20);
                //int progress = 0;
                for (QueryProfile profile : QueryProfileTab.this.queryData) {
                    // progressBar.setSelection(progress * 10);
                    try {
                        HttpDownloader downloader = new HttpDownloader(profile);
                        downloader.downloadExcel();
                        downloader.close();
                    } catch (HttpException e) {
                        logger.error("Can't download excel files!", e);
                    }
                    //progress++;
                }
                // progressBar.dispose();
                // Close opening Excel report file.
                QueryProfileTab.this.downloader.getExcelTab().disposeClient();
                ExcelMerger merger = new ExcelMerger(TEMP_EXCEL_PATH, EXCEL_REPORT_PATH_NO_EXT, QueryProfileTab.this.getExcelFormat());
                merger.mergeExcel();
                QueryProfileTab.this.openMessageBoxFromResource(DOWNLOAD_MESSAGE_TITLE, INFO_MSG_DOWNLOAD_EXCEL,
                        new String[] { TEMP_EXCEL_PATH }, SWT.OK);
                ExcelTab tab = QueryProfileTab.this.downloader.getExcelTab();
                tab.openExcel(merger.getDestinationExcel());
                if (tab.isOpened()) {
                    ((TabFolder) QueryProfileTab.this.downloader.getExcelTab().tabFolderPage.getParent()).setSelection(1);
                }
            }
        });
    }

    /**
     * Get Excel format from the input widget.
     * @return
     */
    private ExcelFormat getExcelFormat() {
        if (this.excel2003Button.getSelection()) {
            return ExcelFormat.Excel2003;
        }
        if (this.excel2007Button.getSelection()) {
            return ExcelFormat.Excel2007;
        }
        return ExcelFormat.Excel2003;
    }

    /**
     * Open a message box.
     * 
     * @param title The title of the message box
     * @param message The content of the message box
     * @param style The style of the message box
     */
    private void openMessageBox(String title, String message, int style) {
        MessageBox msgDialog = new MessageBox(this.shell, style);
        msgDialog.setMessage(message);
        msgDialog.setText(title);
        msgDialog.open();
    }

    /**
     * Open a message box, and the settings of the message box are retrieved from the resource file.
     * 
     * @param titleField The field name of the message box title
     * @param messageField The field name of the message box content
     * @param style The style of the message box
     */
    private void openMessageBoxFromResource(String titleField, String messageField, String[] messageVars, int style) {
        this.openMessageBox(ExcelDownloader.getResourceString(titleField), ExcelDownloader.getResourceString(messageField, messageVars),
                style);
    }

    private void createProfileTableButtons() {
        this.inputGroup = new Group(this.paramterGroup, SWT.NONE);
        this.inputGroup.setText(ExcelDownloader.getResourceString(QUERY_GROUP_TITLE));
        this.inputGroup.setLayout(new GridLayout());
        this.inputGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        ToolBar toolBar = new ToolBar(this.inputGroup, SWT.FLAT);
        toolBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        this.addItem = new ToolItem(toolBar, SWT.PUSH);
        this.addItem.setText(ExcelDownloader.getResourceString(ADD_ITEM_TITLE));
        this.addItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                if (!QueryProfileTab.this.refreshEditors()) {
                    return;
                }
                QueryProfile defaultQuery = new QueryProfile();
                TableItem item = new TableItem(QueryProfileTab.this.table, 0);
                item.setText(QueryProfileTab.this.getQueryDataTableText(defaultQuery));
                QueryProfileTab.this.queryData.addElement(defaultQuery);
            }
        });

        new ToolItem(toolBar, SWT.SEPARATOR);

        this.deleteItem = new ToolItem(toolBar, SWT.PUSH);
        this.deleteItem.setText(ExcelDownloader.getResourceString(DELETE_ITEM_TITLE));
        this.deleteItem.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                QueryProfileTab.this.resetEditors();
                int[] selected = QueryProfileTab.this.table.getSelectionIndices();
                QueryProfileTab.this.table.remove(selected);
                // Delete query data from end to start.
                for (int i = selected.length - 1; i >= 0; i--) {
                    QueryProfileTab.this.queryData.remove(selected[i]);
                }
                QueryProfileTab.this.resetFileContent();
            }
        });

        new ToolItem(toolBar, SWT.SEPARATOR);
        this.clearItem = new ToolItem(toolBar, SWT.PUSH);
        this.clearItem.setText(ExcelDownloader.getResourceString(CLEAR_ITEM_TITLE));
        this.clearItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                QueryProfileTab.this.clearProfileTable();
            }
        });
        toolBar.pack();
    }

    private void createProfileTableWidget() {
        /* Create query data table */
        this.table = new Table(this.inputGroup, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
                | SWT.HIDE_SELECTION);
        this.table.setLinesVisible(true);
        this.table.setHeaderVisible(true);
        FontData def[] = this.display.getSystemFont().getFontData();
        this.table.setFont(new Font(this.display, def[0].getName(), 10, SWT.NONE));
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gridData.heightHint = 150;
        this.table.setLayoutData(gridData);
        this.table.addTraverseListener(this.tableTraverseListener);

        /* Add columns to the table */
        for (int i = 0; i < FIELD_NAMES.length; i++) {
            TableColumn column = new TableColumn(this.table, SWT.NONE);
            column.setText(FIELD_NAMES[i]);
            column.setWidth(COL_WIDTH[i]);
        }

        /* Add TableEditors */
        this.accountEditor = new TableEditor(this.table);
        this.passwordEditor = new TableEditor(this.table);
        this.cityEditor = new TableEditor(this.table);
        this.yearEditor = new TableEditor(this.table);
        this.intervalEditor = new TableEditor(this.table);

        this.table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                QueryProfileTab.this.refreshEditors();
                QueryProfileTab.this.index = QueryProfileTab.this.table.getSelectionIndex();
                logger.debug("Selected line:  " + QueryProfileTab.this.index);
                // No existed line is selected.
                if (QueryProfileTab.this.index == -1) {
                    return;
                }

                TableItem oldItem = QueryProfileTab.this.accountEditor.getItem();
                QueryProfileTab.this.newItem = QueryProfileTab.this.table.getItem(QueryProfileTab.this.index);
                if (QueryProfileTab.this.newItem == oldItem || QueryProfileTab.this.newItem != QueryProfileTab.this.lastSelected) {
                    QueryProfileTab.this.lastSelected = QueryProfileTab.this.newItem;
                    return;
                }
                QueryProfileTab.this.table.showSelection();

                QueryProfileTab.this.accountText = new Text(QueryProfileTab.this.table, SWT.SINGLE);
                QueryProfileTab.this.fieldToControl.put(QueryProfileTab.FIELD_NAMES[ACCOUNT_COL], QueryProfileTab.this.accountText);
                QueryProfileTab.this.createTextEditor(QueryProfileTab.this.accountText, QueryProfileTab.this.accountEditor, ACCOUNT_COL);

                QueryProfileTab.this.passwordText = new Text(QueryProfileTab.this.table, SWT.SINGLE);
                QueryProfileTab.this.fieldToControl.put(QueryProfileTab.FIELD_NAMES[PASSWORD_COL], QueryProfileTab.this.passwordText);
                QueryProfileTab.this.createTextEditor(QueryProfileTab.this.passwordText, QueryProfileTab.this.passwordEditor, PASSWORD_COL);

                QueryProfileTab.this.cityCombo = new CCombo(QueryProfileTab.this.table, SWT.READ_ONLY);
                QueryProfileTab.this.fieldToControl.put(QueryProfileTab.FIELD_NAMES[CITY_COL], QueryProfileTab.this.cityCombo);
                QueryProfileTab.this.createComboEditor(QueryProfileTab.this.cityCombo, QueryProfileTab.this.cityEditor, CITIES, CITY_COL);

                QueryProfileTab.this.yearSpinner = new Spinner(QueryProfileTab.this.table, SWT.NONE | SWT.WRAP);
                QueryProfileTab.this.fieldToControl.put(QueryProfileTab.FIELD_NAMES[YEAR_COL], QueryProfileTab.this.yearSpinner);
                QueryProfileTab.this.createSpinnerEditor(QueryProfileTab.this.yearSpinner, QueryProfileTab.this.yearEditor,
                        QueryParameter.LAST_YEAR, QueryParameter.FIRST_YEAR, YEAR_COL);

                QueryProfileTab.this.intervalSpinner = new Spinner(QueryProfileTab.this.table, SWT.NONE | SWT.WRAP);
                QueryProfileTab.this.fieldToControl.put(QueryProfileTab.FIELD_NAMES[INTERVAL_COL], QueryProfileTab.this.intervalSpinner);
                QueryProfileTab.this.createSpinnerEditor(QueryProfileTab.this.intervalSpinner, QueryProfileTab.this.intervalEditor,
                        QueryParameter.INTERVAL_MAXIMUM, QueryParameter.INTERVAL_MINIMUM, INTERVAL_COL);
            }
        });
    }

    /**
     * Creates the TableEditor with a Text in the given column of the table.
     */
    private void createTextEditor(Text text, TableEditor textEditor, int column) {
        text.setFont(this.table.getFont());
        text.setText(this.newItem.getText(column));
        text.selectAll();
        text.addTraverseListener(this.traverseListener);

        textEditor.horizontalAlignment = SWT.LEFT;
        textEditor.grabHorizontal = true;
        textEditor.setEditor(text, this.newItem, column);
    }

    private void createSpinnerEditor(Spinner spinner, TableEditor editor, int maximum, int minimium, int column) {
        spinner.setFont(this.table.getFont());
        spinner.setMaximum(maximum);
        spinner.setMinimum(minimium);
        spinner.setSelection(Integer.valueOf(this.newItem.getText(column)));
        spinner.addTraverseListener(this.traverseListener);

        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.setEditor(spinner, this.newItem, column);
    }

    /**
     * Creates the TableEditor with a CCombo.
     */
    private void createComboEditor(CCombo combo, TableEditor editor, String[] items, int column) {
        combo.setFont(this.table.getFont());
        combo.setItems(items);
        combo.setText(this.newItem.getText(column));
        combo.addTraverseListener(this.traverseListener);

        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = COL_WIDTH[column];
        editor.setEditor(combo, this.newItem, column);
    }

    private String[] getQueryDataTableText(QueryProfile query) {
        return new String[] { query.account.accountNum, query.account.password, this.getCityText(query.account.city),
                String.valueOf(query.parameter.year), String.valueOf(query.parameter.interval) };
    }

    private QueryProfile getQueryData() {
        String cityCode = StringUtils.isEmpty(this.cityCombo.getText()) ? "" : this.getCityCode(this.cityCombo.getText());
        return new QueryProfile(new Account(this.accountText.getText().trim(), this.passwordText.getText().trim(), cityCode),
                new QueryParameter(this.yearSpinner.getSelection(), this.intervalSpinner.getSelection()));
    }

    private String getCityCode(String cityText) {
        return CITYCODE[Arrays.asList(CITIES).indexOf(cityText)];
    }

    private String getCityText(String cityCode) {
    	int cityIndex = Arrays.asList(CITYCODE).indexOf(cityCode);
    	cityIndex = cityIndex >= 0 ? cityIndex : 0;
        return CITIES[cityIndex];
    }
    
    private void clearProfileTable(){
        this.queryData.clear();
        this.resetEditors();
        this.table.removeAll();
    }

    private boolean refreshEditors() {
        return this.refreshEditors(false);
    }

    private void refreshTableItems() {
        List<QueryProfile> queryProfiles = QueryProfileTab.formatter.getQueryProfilesFromRecord(this.fileContent.getText());

        this.table.removeAll();
        this.queryData.removeAllElements();

        for (int i = 0; i < queryProfiles.size(); i++) {
            TableItem item = new TableItem(this.table, i);
            item.setText(this.getQueryDataTableText(queryProfiles.get(i)));
            this.queryData.addElement(queryProfiles.get(i));
        }
    }

    /**
     * Takes information from TableEditors and stores it.
     * 
     * @param tab
     * @return
     */
    private boolean refreshEditors(boolean tab) {
        TableItem oldItem = this.accountEditor.getItem();
        if (oldItem != null) {
            int row = this.table.indexOf(oldItem);
            QueryProfile data = this.getQueryData();
            List<Integer> emptyFieldIndices = this.checkData();
            if (emptyFieldIndices.size() > 0) {
                StringBuilder sBuilder = new StringBuilder();
                for (int index : emptyFieldIndices) {
                    sBuilder.append(FIELD_NAMES[index]).append(", ");
                }
                sBuilder.delete(sBuilder.length() - 2, sBuilder.length());
                this.openMessageBoxFromResource(ERROR_MESSAGE_TITLE, ERROR_MSG_EMPTY_FIELD, new String[] { sBuilder.toString() }, SWT.OK);
                this.fieldToControl.get(FIELD_NAMES[emptyFieldIndices.get(0)]).setFocus();
                return false;
            }
            this.queryData.setElementAt(data, row);
            this.setItemText(oldItem, data);
            if (!tab) {
                this.disposeEditors();
            }
        }
        this.resetFileContent();
        return true;
    }

    private void resetEditors() {
        TableItem oldItem = this.accountEditor.getItem();
        if (oldItem != null) {
            this.disposeEditors();
        }
        this.resetFileContent();
    }

    /**
     * Dispose editor widgets.
     */
    private void disposeEditors() {
        this.fieldToControl.clear();

        this.accountEditor.setEditor(null, null, -1);
        this.accountText.dispose();

        this.passwordEditor.setEditor(null, null, -1);
        this.passwordText.dispose();

        this.cityEditor.setEditor(null, null, -1);
        this.cityCombo.dispose();

        this.yearEditor.setEditor(null, null, -1);
        this.yearSpinner.dispose();

        this.intervalEditor.setEditor(null, null, -1);
        this.intervalSpinner.dispose();
    }

    private void resetFileContent() {
        StringBuilder sBuilder = new StringBuilder(QUERY_FILE_HEADER);

        for (QueryProfile query : this.queryData) {
            sBuilder.append(QueryProfileTab.formatter.getQueryProfileRecord(query));
            sBuilder.append(Text.DELIMITER);
        }
        this.fileContent.setText(sBuilder.toString());
    }

    private void setItemText(TableItem item, QueryProfile data) {
        String[] dataText = this.getQueryDataTableText(data);
        for (int i = 0; i < COL_AMOUNT; i++) {
            item.setText(i, dataText[i]);
        }
    }

    /**
     * Check the query data listed in the parameter table, and return a list contains all index number of the columns
     * which contains invalid data.
     * 
     * @return A list contains all index number of the columns which contains invalid data.
     */
    private List<Integer> checkData() {
        List<Integer> emptyFieldIndices = new ArrayList<Integer>();
        String[] dataText = this.getQueryDataTableText(this.getQueryData());
        for (int i = 0; i < COL_AMOUNT; i++) {
            if (dataText[i] == null || dataText[i].isEmpty()) {
                emptyFieldIndices.add(i);
            }
        }
        return emptyFieldIndices;
    }

    /**
     * Save the content of the query profile into a file.
     * 
     * @param path The absolute path of the query profile file.
     * @return The java.io.File object
     * @throws IOException
     */
    private File saveQueryProfileFile(String path) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(this.fileContent.getText());
        writer.close();
        return file;
    }

    private String openQueryProfileFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sBuilder.append(line);
            sBuilder.append(Text.DELIMITER);
        }
        reader.close();
        return sBuilder.toString();
    }

    /**
     * Excel Format
     * @author Phoenix Xu
     */
    public enum ExcelFormat {
        Excel2003(".xls"),
        Excel2007(".xlsx");

        private final String suffix;

        private ExcelFormat(String suffix) {
            this.suffix = suffix;
        }

        /**
         * Get the file name with file extent name.
         * @param fileName
         * @return
         */
        public String getFileNameWithExt(String fileName) {
            return fileName + this.suffix;
        }

        /**
         * Get the file extent name.
         * @return
         */
        public String getSuffix() {
            return this.suffix;
        }
    }
}
