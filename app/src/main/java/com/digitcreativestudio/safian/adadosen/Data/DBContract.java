/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.digitcreativestudio.safian.adadosen.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class DBContract {

    public static final String CONTENT_AUTHORITY = "com.digitcreativestudio.safian.adadosen";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_COLLEGER = "collegers";
    public static final String PATH_LECTURER = "lecturers";

    public static final class CollegerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COLLEGER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COLLEGER;

        public static final String TABLE_NAME = PATH_COLLEGER;

        public static final String COLUMN_NIM = "nim";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STATUS = "status";

        public static Uri buildCollegerUri(){
            return CONTENT_URI;
        }
    }

    public static final class LecturerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LECTURER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LECTURER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LECTURER;

        public static final String TABLE_NAME = PATH_LECTURER;

        public static final String COLUMN_NIP = "nip";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_MODIFIED_BY = "modified_by";
        public static final String COLUMN_LAST_MODIFY = "last_modify";

        public static Uri buildCollegerUri(){
            return CONTENT_URI;
        }

        public static Uri buildCollegerUriById(long id){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }
    }
}
