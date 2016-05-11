package com.framgia.e_learningsimple.constant;

import android.os.Environment;

/**
 * Created by ThuyIT on 20/04/2016.
 */
public class JsonKeyConstant {
    public static final String USER_SHARED_PREF = "USER_INFO";
    public static final String NAME_FIELD = "name";
    public static final String EMAIL_FILED = "email";
    public static final String AUTHO_TOKEN_FIELD = "auth_token";
    public static final String KEY_USER_ID = "userId";
    public static final String PAGE = "page";
    public static final String LESSON = "lesson";
    public static final String WORDS = "words";
    public static final String USER = "user";
    public static final String KEY_ID = "id";
    public static final String AVATAR = "avatar";
    public static final String MESSAGE = "message";
    public static final String CONTENT = "content";
    public static final String CREATE_AT = "created_at";
    public static final String KEY_USER_PASSWORD = "userPassword";
    public static final String CATEGORIES = "categories";
    public static final String PHOTO = "photo";
    public static final String LEARNED_WORDS = "learned_words";
    public static final String CATEGORY_ID = "category_id";
    public static final String OPTION = "option";
    public static final String ANSWERS = "answers";
    public static final String IS_CORRECT = "is_correct";

    public static final String DEFAULT_LESSON_NAME = "Training Project";
    public static final String KEY_LESSON_ID = "lessonId";
    public static final String KEY_WORDS_DATA = "wordsData";
    public static final String KEY_CATEGORY_NAME = "categoryName";
    public static final String KEY_CATEGORY_ID = "categoryId";
    public static final String KEY_USER_ANSWERS = "userAnwsersData";

    public static final String KEY_LESSON_NAME = "lessonName";
    public static final String KEY_CATEGORY_LIST = "categoryList";

    public static final String KEY_ALL_WORD = "all_word";
    public static final String KEY_LEARNED = "learned";
    public static final String KEY_NOT_LEARNED = "no_learn";
    public static final String DEFAULT_WORDLIST_FILE_NAME = "wordlist";
    public static final String DEFAULT_FILE_SAVED_PATH = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    ;
    public static final String SCORE_FORMAT = "%s/%s";
    public static final String SCORE = "%s %s";

    public static final String KEY_USER_AVATAR_URL = "userAvatarUrl";
    public static final String KEY_ACTIVITIES = "activities";
}
