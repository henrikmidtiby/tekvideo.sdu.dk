package dk.danthrane.twbs

enum Icon {
    ASTERISK,
    PLUS,
    EURO,
    EUR,
    MINUS,
    CLOUD,
    ENVELOPE,
    PENCIL,
    GLASS,
    MUSIC,
    SEARCH,
    HEART,
    STAR,
    STAR_EMPTY,
    USER,
    FILM,
    TH_LARGE,
    TH,
    TH_LIST,
    OK,
    REMOVE,
    ZOOM_IN,
    ZOOM_OUT,
    OFF,
    SIGNAL,
    COG,
    TRASH,
    HOME,
    FILE,
    TIME,
    ROAD,
    DOWNLOAD_ALT,
    DOWNLOAD,
    UPLOAD,
    INBOX,
    PLAY_CIRCLE,
    REPEAT,
    REFRESH,
    LIST_ALT,
    LOCK,
    FLAG,
    HEADPHONES,
    VOLUME_OFF,
    VOLUME_DOWN,
    VOLUME_UP,
    QRCODE,
    BARCODE,
    TAG,
    TAGS,
    BOOK,
    BOOKMARK,
    PRINT,
    CAMERA,
    FONT,
    BOLD,
    ITALIC,
    TEXT_HEIGHT,
    TEXT_WIDTH,
    ALIGN_LEFT,
    ALIGN_CENTER,
    ALIGN_RIGHT,
    ALIGN_JUSTIFY,
    LIST,
    INDENT_LEFT,
    INDENT_RIGHT,
    FACETIME_VIDEO,
    PICTURE,
    MAP_MARKER,
    ADJUST,
    TINT,
    EDIT,
    SHARE,
    CHECK,
    MOVE,
    STEP_BACKWARD,
    FAST_BACKWARD,
    BACKWARD,
    PLAY,
    PAUSE,
    STOP,
    FORWARD,
    FAST_FORWARD,
    STEP_FORWARD,
    EJECT,
    CHEVRON_LEFT,
    CHEVRON_RIGHT,
    PLUS_SIGN,
    MINUS_SIGN,
    REMOVE_SIGN,
    OK_SIGN,
    QUESTION_SIGN,
    INFO_SIGN,
    SCREENSHOT,
    REMOVE_CIRCLE,
    OK_CIRCLE,
    BAN_CIRCLE,
    ARROW_LEFT,
    ARROW_RIGHT,
    ARROW_UP,
    ARROW_DOWN,
    SHARE_ALT,
    RESIZE_FULL,
    RESIZE_SMALL,
    EXCLAMATION_SIGN,
    GIFT,
    LEAF,
    FIRE,
    EYE_OPEN,
    EYE_CLOSE,
    WARNING_SIGN,
    PLANE,
    CALENDAR,
    RANDOM,
    COMMENT,
    MAGNET,
    CHEVRON_UP,
    CHEVRON_DOWN,
    RETWEET,
    SHOPPING_CART,
    FOLDER_CLOSE,
    FOLDER_OPEN,
    RESIZE_VERTICAL,
    RESIZE_HORIZONTAL,
    HDD,
    BULLHORN,
    BELL,
    CERTIFICATE,
    THUMBS_UP,
    THUMBS_DOWN,
    HAND_RIGHT,
    HAND_LEFT,
    HAND_UP,
    HAND_DOWN,
    CIRCLE_ARROW_RIGHT,
    CIRCLE_ARROW_LEFT,
    CIRCLE_ARROW_UP,
    CIRCLE_ARROW_DOWN,
    GLOBE,
    WRENCH,
    TASKS,
    FILTER,
    BRIEFCASE,
    FULLSCREEN,
    DASHBOARD,
    PAPERCLIP,
    HEART_EMPTY,
    LINK,
    PHONE,
    PUSHPIN,
    USD,
    GBP,
    SORT,
    SORT_BY_ALPHABET,
    SORT_BY_ALPHABET_ALT,
    SORT_BY_ORDER,
    SORT_BY_ORDER_ALT,
    SORT_BY_ATTRIBUTES,
    SORT_BY_ATTRIBUTES_ALT,
    UNCHECKED,
    EXPAND,
    COLLAPSE_DOWN,
    COLLAPSE_UP,
    LOG_IN,
    FLASH,
    LOG_OUT,
    NEW_WINDOW,
    RECORD,
    SAVE,
    OPEN,
    SAVED,
    IMPORT,
    EXPORT,
    SEND,
    FLOPPY_DISK,
    FLOPPY_SAVED,
    FLOPPY_REMOVE,
    FLOPPY_SAVE,
    FLOPPY_OPEN,
    CREDIT_CARD,
    TRANSFER,
    CUTLERY,
    HEADER,
    COMPRESSED,
    EARPHONE,
    PHONE_ALT,
    TOWER,
    STATS,
    SD_VIDEO,
    HD_VIDEO,
    SUBTITLES,
    SOUND_STEREO,
    SOUND_DOLBY,
    SOUND_5_1,
    SOUND_6_1,
    SOUND_7_1,
    COPYRIGHT_MARK,
    REGISTRATION_MARK,
    CLOUD_DOWNLOAD,
    CLOUD_UPLOAD,
    TREE_CONIFER,
    TREE_DECIDUOUS,
    CD,
    SAVE_FILE,
    OPEN_FILE,
    LEVEL_UP,
    COPY,
    PASTE,
    ALERT,
    EQUALIZER,
    KING,
    QUEEN,
    PAWN,
    BISHOP,
    KNIGHT,
    BABY_FORMULA,
    TENT,
    BLACKBOARD,
    BED,
    APPLE,
    ERASE,
    HOURGLASS,
    LAMP,
    DUPLICATE,
    PIGGY_BANK,
    SCISSORS,
    BITCOIN,
    BTC,
    XBT,
    YEN,
    JPY,
    RUBLE,
    RUB,
    SCALE,
    ICE_LOLLY,
    ICE_LOLLY_TASTED,
    EDUCATION,
    OPTION_HORIZONTAL,
    OPTION_VERTICAL,
    MENU_HAMBURGER,
    MODAL_WINDOW,
    OIL,
    GRAIN,
    SUNGLASSES,
    TEXT_SIZE,
    TEXT_COLOR,
    TEXT_BACKGROUND,
    OBJECT_ALIGN_TOP,
    OBJECT_ALIGN_BOTTOM,
    OBJECT_ALIGN_HORIZONTAL,
    OBJECT_ALIGN_LEFT,
    OBJECT_ALIGN_VERTICAL,
    OBJECT_ALIGN_RIGHT,
    TRIANGLE_RIGHT,
    TRIANGLE_LEFT,
    TRIANGLE_BOTTOM,
    TRIANGLE_TOP,
    CONSOLE,
    SUPERSCRIPT,
    SUBSCRIPT,
    MENU_LEFT,
    MENU_RIGHT,
    MENU_DOWN,
    MENU_UP

    String getName() {
        name().toLowerCase().replaceAll("_", "-")
    }
}