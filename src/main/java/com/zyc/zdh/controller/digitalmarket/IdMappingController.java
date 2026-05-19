package com.zyc.zdh.controller.digitalmarket;

import com.zyc.zdh.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 智能营销-ID转换服务
 */
@Controller
public class IdMappingController extends BaseController {

    /**
     * id mapping
     * @return
     */
    @RequestMapping(value = "/id_mapping_detail", method = RequestMethod.GET)
    public String id_mapping_detail() {

        return "digitalmarket/id_mapping_detail";
    }
}
