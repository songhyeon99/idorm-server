package idorm.idormServer.report.domain;

import idorm.idormServer.exception.CustomException;
import lombok.Getter;

import static idorm.idormServer.exception.ExceptionCode.REPORT_TYPE_CHARACTER_INVALID;
import static idorm.idormServer.exception.ExceptionCode.SERVER_ERROR;

public enum ReportType {

    MEMBER('M'),
    POST('P'),
    COMMENT('C');

    @Getter
    Character type;

    ReportType(Character type) {
        this.type = type;
    }

    public static ReportType validateType(String type) {
        try {
            return ReportType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new CustomException(REPORT_TYPE_CHARACTER_INVALID);
        }
    }

    public static ReportType valueOf(Character type) {
        ReportType reportType = null;
        switch (type) {
            case 'M':
                reportType = ReportType.MEMBER;
                break;
            case 'P':
                reportType = ReportType.POST;
                break;
            case 'C':
                reportType = ReportType.COMMENT;
                break;
            default:
                throw new CustomException(SERVER_ERROR);
        }
        return reportType;
    }
}
