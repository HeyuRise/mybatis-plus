/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pcbwx.mybatis;

import com.pcbwx.mybatis.bean.MyResponse;
import com.pcbwx.mybatis.enums.DictionaryEnum;
import com.pcbwx.mybatis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
	
	private Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	
	@Autowired
	private MyResponse<Object> response;
	
	@Autowired
	private RedisService redisService;

	@GetMapping("/redis")
	public Object welcome() {
		return redisService.getDictionarys(DictionaryEnum.PAY_METHOD);
	}
	
}