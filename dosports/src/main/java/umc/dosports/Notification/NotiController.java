package umc.dosports.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.dosports.Notification.model.DeleteNotiReq;
import umc.dosports.Notification.model.GetNotiRes;

import java.util.List;

@Controller
@RequestMapping("/notify")
public class NotiController {
    private final NotiService notiService;

    @Autowired
    public NotiController(NotiService notiService){
        this.notiService = notiService;
    }

    //알림 조회
    @GetMapping("")
    @ResponseBody
    public List<GetNotiRes> getNotifys(){
        //!!!!!JWT 수정 필요!!!!!!
        long userIdxByJWT = 1;

        return notiService.getNotis(userIdxByJWT);
    }

    //알림 읽음
    @GetMapping("/{notiIdx}")
    @ResponseBody
    public void patchNoti(@PathVariable("notiIdx") long notiIdx){
        notiService.patchNoti(notiIdx);
    }

    //알림 삭제
    @DeleteMapping("")
    @ResponseBody
    public void deleteNoti(DeleteNotiReq form){
        notiService.deleteNoti(form);
    }
}
