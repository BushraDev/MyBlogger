package database;

public class BlogDbSchema
{

    public static final class UsersTable
    {

        public static final String NAME="users";

        public static final class Cols
        {

            public static final String ID="u_id";
            public static final String UUID="u_uuid";
            public static final String NAME="name";
            public static final String E_MAIL="e_mail";
            public static final String PASSWORD="password";
            public static final String BIRTH_DATE="birth_date";
            public static final String GENDER="gender";

        }

    }

    public static final class PostsTable
    {

        public static final String NAME="posts";

        public static final class Cols
        {

            public static final String ID="p_id";
            public static final String UUID="p_uuid";
            public static final String TITLE="title";
            public static final String CATEGORY="category";
            public static final String CONTENT="content";
            public static final String DATE="date";
            public static final String FAVORITE="favorite";
            public static final String U_ID="u_id";

        }

    }

}
