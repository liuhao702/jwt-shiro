package code.controller;

import java.util.Arrays;

import code.entity.CesEntity;
import code.service.CesService;
import code.util.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * ${comments}
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-19 11:33:56
 */
@RestController
//@RequestMapping("generator/ces")
public class CesController {

    @Autowired
    private CesService cesService;

    @GetMapping("/re")
    @ResponseBody
    public String testReBuSshu(){
      return "测试热部署dd11";
    }


    @GetMapping("/index/{id}")
    @ResponseBody
    public String TestIndex(@PathVariable("id") Integer id){
        return "index"+id;
    }




    /**
     * 列表
     */
//    @RequestMapping("/list")
//    @RequiresPermissions("generator:ces:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = cesService.queryPage(params);
//        return R.ok().put("page", page);
//    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:ces:info")
    public R info(@PathVariable("id") Long id){
		CesEntity ces = cesService.getById(id);

        return R.ok().put("ces", ces);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:ces:save")
    public R save(@RequestBody CesEntity ces){
		cesService.save(ces);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:ces:update")
    public R update(@RequestBody CesEntity ces){
		cesService.updateById(ces);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:ces:delete")
    public R delete(@RequestBody Long[] ids){
		cesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }



}
