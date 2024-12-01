package doan.doan_v1.Constant;

public interface Constant {

    interface DEVICE_TYPE {
        int KEY = 1;
        int MOU = 2;
        int SCREEN = 3;
        int CASE = 4;
    }

    interface DEVICE_TYPE_STR {
        String KEY = "KEY";
        String MOU = "MOU";
        String SCREEN = "SCREEN";
        String CASE = "CASE";
    }

    interface STATUS {
        int OK = 1;
        int ERROR = 2;
        int EMPTY = 3;
    }

    interface INCIDENT_STATUS {
        int UNPROCESSED = 1;
        int PROCESSING = 2;
        int PROCESSED = 3;
        int OVERDUE_UNPROCESSED = 4;
        int OVERDUE_PROCESSED = 5;
    }

    interface ROLE_ID {
        int ROLE_ADMIN = 1;
        int ROLE_LECTURE = 2;
        int ROLE_TECHNICIAN = 3;
    }
}
