package settings;

/**
 * This enumerable type lists the various application-specific property types listed in the initial set of properties to
 * be loaded from the workspace properties <code>xml</code> file specified by the initialization parameters.
 *
 * @author Ritwik Banerjee
 * @see vilij.settings.InitializationParams
 */
public enum AppPropertyTypes{

    /* resource files and folders */
    DATA_RESOURCE_PATH,
    CSS_RESOURCE_PATH,
    ALGORITHMS_PATH,

    /* user interface icon file names */
    SCREENSHOT_ICON,

    /* tooltips for user interface buttons */
    SCREENSHOT_TOOLTIP,
    EDIT_TEXT,
    DONE_TEXT,
    SELECT_TEXT,

    /* error messages */
    RESOURCE_SUBDIR_NOT_FOUND,
    SCREENSHOT_ERROR_MESSAGE

    /* application-specific message titles */,
    SAVE_UNSAVED_WORK_TITLE,
    SCREENSHOT_ERROR_TITLE,
    LOAD_DATA_TITLE,
    INVALID_TEXT_ERROR_TITLE,
    DUPLICATE_ERROR_TITLE

    /* application-specific messages */,
    SAVE_UNSAVED_WORK,
    LOAD_DATA,
    INVALID_TEXT_ERROR_MESSAGE,
    DUPLICATE_ERROR_MESSAGE,
    NO_DATA_LOADED_IN_PLACEHOLDER,
    LOADED_DATA,

    /* application-specific parameters */
    DATA_FILE_EXT,
    DATA_FILE_EXT_DESC,
    TEXT_AREA,
    SCRNSHT_INITIAL,
    SCRNSHT_FILE_EXT,
    SCRNSHT_FILE_DESC,
    SPECIFIED_FILE,
    LEFT_PANE_TITLE,
    LEFT_PANE_TITLEFONT,
    LEFT_PANE_TITLESIZE,
    CHART_TITLE,
    BACK,

}