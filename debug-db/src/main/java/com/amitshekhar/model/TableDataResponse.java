/*
 *
 *  *    Copyright (C) 2016 Amit Shekhar
 *  *    Copyright (C) 2011 Android Open Source Project
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.amitshekhar.model;

import java.util.List;

/**
 * Created by amitshekhar on 04/02/17.
 */

public class TableDataResponse {

    public List<TableInfo> tableInfos;
    public boolean isSuccessful;
    public List<Object> rows;
    public String errorMessage;
    public boolean isEditable;
    public boolean isSelectQuery;

    public static class TableInfo {
        public String title;
        public boolean isPrimary;
    }

    public static class ColumnData {
        public String dataType;
        public Object value;
    }

}
