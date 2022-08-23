package umc.dosports.Notification;

import umc.dosports.Notification.model.DeleteNotiReq;
import umc.dosports.Notification.model.GetNotiRes;

import java.util.List;

public interface NotiRepository {
    //알림 조회
    List<GetNotiRes> getNotis(long userIdxByJWT);

    //알림 읽음
    void patchNoti(long notiIdx);

    //알림 삭제
    void deleteNoti(DeleteNotiReq form);
}