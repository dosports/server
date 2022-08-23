package umc.dosports.Notification;

import umc.dosports.Notification.model.DeleteNotiReq;
import umc.dosports.Notification.model.GetNotiRes;

import java.util.List;

public class NotiService {
    private final NotiRepository notiRepository;

    public NotiService(NotiRepository notiRepository){
        this.notiRepository = notiRepository;
    }

    //알림 조회
    public List<GetNotiRes> getNotis(long userIdxByJWT){
        return notiRepository.getNotis(userIdxByJWT);
    }

    //알림 읽음
    public void patchNoti(long notiIdx){
        notiRepository.patchNoti(notiIdx);
    }

    //알림 삭제
    public void deleteNoti(DeleteNotiReq form){
        notiRepository.deleteNoti(form);
    }
}
