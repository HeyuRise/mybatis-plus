package com.pcbwx.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.pcbwx.mybatis.SystemStart;
import com.pcbwx.mybatis.enums.DictionaryEnum;
import com.pcbwx.mybatis.enums.RedisKeyEnum;
import com.pcbwx.mybatis.model.Dictionary;
import com.pcbwx.mybatis.service.RedisService;
import com.pcbwx.mybatis.util.DataUtil;
import com.pcbwx.mybatis.util.JsonUtil;

@Service("redisService")
public class RedisServiceImpl implements RedisService {
	
	private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

	@Autowired
	private RedisTemplate<String, Object> template;

	@Override
	public void reloadDictionary(List<Dictionary> dictionarys) {
		ValueOperations<String, Object> operation = template.opsForValue();
		for (Dictionary record : dictionarys) {
			String key = SystemStart.MYSYSTEMCODE + ":" + RedisKeyEnum.DICTIONARY.getCode() + ":" + record.getType();
			if (record.getInnerId() != null) {
				key = key + ":" + record.getInnerId();
				operation.set(key, record);
				continue;
			}
			if (record.getInnerCode() != null) {
				key = key + ":" + record.getInnerCode();
				operation.set(key, record);
				continue;
			}
		}
		// #-------------------------------------------------------------
		Map<String, List<Dictionary>> dicCache = new HashMap<>();
		try {
			dicCache = DataUtil.list2mapList(dictionarys, Dictionary.class, "type");
			logger.info("字典类别缓存条数:" + dicCache.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ListOperations<String, Object> listOperation = template.opsForList();
		for (Map.Entry<String, List<Dictionary>> entry : dicCache.entrySet()) {
			String key = SystemStart.MYSYSTEMCODE + ":" + RedisKeyEnum.DICTIONARY.getCode() + ":" + entry.getKey();
			template.delete(key);
			List<Dictionary> dic = entry.getValue();
			for (Dictionary dictionary : dic) {
				listOperation.leftPush(key, dictionary);
			}
		}
		
	}

	@Override
	public Dictionary getDictionary(DictionaryEnum type, Integer innerId) {
		String key = SystemStart.MYSYSTEMCODE + ":" + RedisKeyEnum.DICTIONARY.getCode() + ":" + type.getCode() + ":"
				+ innerId;
		return (Dictionary) template.opsForValue().get(key);
	}

	@Override
	public Dictionary getDictionary(DictionaryEnum type, String innerCode) {
		String key = SystemStart.MYSYSTEMCODE + ":" + RedisKeyEnum.DICTIONARY.getCode() + ":" + type.getCode() + ":"
				+ innerCode;
		return (Dictionary) template.opsForValue().get(key);
	}

	@Override
	public List<Dictionary> getDictionarys(DictionaryEnum type) {
		ListOperations<String, Object> operation = template.opsForList();
		String key = SystemStart.MYSYSTEMCODE + ":" + RedisKeyEnum.DICTIONARY.getCode() + ":" + type.getCode();
		List<Object> list = operation.range(key, 0, -1);
		logger.info(JsonUtil.obj2json(list));
		List<Dictionary> dicList = new ArrayList<>();
		for (Object object : list) {
			dicList.add((Dictionary)object);
		}
		return dicList;
	}

}
