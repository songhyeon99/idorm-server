package idorm.idormServer.matchingInfo.domain;

import lombok.Getter;

public enum DormCategory {

    DORM1('1'),
    DORM2('2'),
    DORM3('3');

    @Getter
    Character type;

    DormCategory(Character type) {
        this.type = type;
    }

    public static DormCategory valueOf(Character type) {
        DormCategory dormCategory = null;
        switch (type) {
            case '1':
                dormCategory = DormCategory.DORM1;
                break;
            case '2':
                dormCategory = DormCategory.DORM2;
                break;
            case '3':
                dormCategory = DormCategory.DORM3;
                break;
            default:
                break;
        }
        return dormCategory;
    }
}