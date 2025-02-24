package com.liugh.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liugh.annotation.CurrentUser;
import com.liugh.annotation.ValidationParam;
import com.liugh.base.PublicResultConstant;
import com.liugh.config.ResponseHelper;
import com.liugh.config.ResponseModel;
import com.liugh.entity.Notice;
import com.liugh.entity.User;
import com.liugh.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 消息通知表 前端控制器
 * </p>
 *
 * @author liugh
 * @since 2018-07-27
 */
@RestController
@RequestMapping("/notice")
@ApiIgnore
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    /**
     * 获取自己的消息列表
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @GetMapping("/infoList")
    public ResponseModel<IPage<Notice>> findInfoList(@CurrentUser User user, @RequestParam(name = "pageIndex", defaultValue = "1", required = false) Integer pageIndex,
                                                     @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) throws Exception{

        return ResponseHelper.buildResponseModel(noticeService.page(new Page<>(pageIndex, pageSize), new QueryWrapper<Notice>().
                eq("mobile",user.getMobile()).orderByDesc("create_time")));
    }

    /**
     * 删除全部消息
     * @return
     * @throws Exception
     */
    @DeleteMapping
    public ResponseModel findInfoList(@CurrentUser User user) throws Exception{
        noticeService.deleteByNotices(user);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }

    /**
     * 消息改变为已读
     * @param requestJson
     * @return
     * @throws Exception
     */
    @PostMapping("/read")
    public ResponseModel read(@ValidationParam("noticeId,isRead")
                             @RequestBody JSONObject requestJson) throws Exception{
        noticeService.read(requestJson);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }
    /**
     * 未读消息总数
     * @return
     * @throws Exception
     */
    @GetMapping("/noReadCount")
    public ResponseModel getNoRead(@CurrentUser User user) throws Exception{
        return ResponseHelper.buildResponseModel(noticeService.list(new
                QueryWrapper<Notice>().eq("mobile",user.getMobile()).eq("is_read", 0)).size());
    }


}

