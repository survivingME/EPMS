package com.qyn.project.service.impl;

import com.qyn.project.entity.Node;
import com.qyn.project.pojo.AjaxResult;
import com.qyn.project.util.Code;
import com.qyn.project.util.JedisUtil;
import com.qyn.project.util.PageBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MappingServiceImpl{

    public AjaxResult createMapping(Integer map, String name, String code) {
        AjaxResult res = new AjaxResult();
        if(checkLen(res, map, code) && checkExist(res, map, name, code)) {
            if(map == Code.REDIS_COMPANY) {
                JedisUtil.getJedis().hset(Code.REDIS_COMPANY_N2C_MAP, name, code);
                JedisUtil.getJedis().hset(Code.REDIS_COMPANY_C2N_MAP, code, name);
            } else {
                JedisUtil.getJedis().hset(Code.REDIS_PRODUCTION_N2C_MAP, name, code);
                JedisUtil.getJedis().hset(Code.REDIS_PRODUCTION_C2N_MAP, code, name);
            }
        }
        return res;
    }

    public PageBean<Node> getMappingList(Integer map, Integer pageNo, Integer pageSize) {
        PageBean<Node> pageBean = new PageBean<>(pageNo, pageSize);
        List<Node> list = new ArrayList<>();
        Map<String, String> resultMap;
        if(map == Code.REDIS_COMPANY) {
            resultMap = JedisUtil.getJedis().hgetAll(Code.REDIS_COMPANY_N2C_MAP);
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                list.add(new Node(entry.getKey(), entry.getValue()));
            }
        } else {
            resultMap = JedisUtil.getJedis().hgetAll(Code.REDIS_PRODUCTION_N2C_MAP);
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                list.add(new Node(entry.getKey(), entry.getValue()));
            }
        }
        pageBean.setTotalsize(resultMap.size());
        pageBean.setDatas(list);
        //TODO
        return pageBean;
    }

    private boolean checkLen(AjaxResult res, Integer map, String code) {
        if(map == Code.REDIS_COMPANY) {
            if(code.length() != Code.companyCodeLen) {
                res.setSuccess(false);
                res.setMessage("公司" + Code.MAPPING_LEN_ERROR_MES);
                res.setStatus(Code.MAPPING_LEN_ERROR);
                return false;
            }
        } else {
            if(code.length() != Code.nameCodeLen - 1) {
                res.setSuccess(false);
                res.setMessage("产品" + Code.MAPPING_LEN_ERROR_MES);
                res.setStatus(Code.MAPPING_LEN_ERROR);
                return false;
            }
        }
        return true;
    }

    private boolean checkExist(AjaxResult res, Integer map, String name, String code) {
        Boolean res1, res2;
        res.setSuccess(false);
        if(map == Code.REDIS_COMPANY) {
            res1 = JedisUtil.getJedis().hexists(Code.REDIS_COMPANY_N2C_MAP, name);
            res2 = JedisUtil.getJedis().hexists(Code.REDIS_COMPANY_C2N_MAP, code);
        } else {
            res1 = JedisUtil.getJedis().hexists(Code.REDIS_PRODUCTION_N2C_MAP, name);
            res2 = JedisUtil.getJedis().hexists(Code.REDIS_PRODUCTION_C2N_MAP, code);
        }
        if(res1 & res2) {
            res.setSuccess(true);
            res.setStatus(Code.MAPPING_OK);
            res.setMessage("映射创建" + Code.OK);
            return true;
        } else if(!res1) {
            res.setStatus(Code.MAPPING_KEY_EXIST);
            res.setMessage(Code.MAPPING_KEY_EXIST_MES);
            return false;
        } else {
            res.setStatus(Code.MAPPING_VALUE_EXIST);
            res.setMessage(Code.MAPPING_VALUE_EXIST_MES);
            return false;
        }
    }
}
