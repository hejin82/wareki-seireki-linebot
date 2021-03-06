/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import com.example.bot.spring.echo.service.WarekiSeirekiConvertService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	private static List<String> excludeMessages = Arrays.asList(new String[]{"ヘルプ", "へるぷ", "help"});
	
	@Autowired
	private WarekiSeirekiConvertService service;
	
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        
        String respMessage = "";
        try {
        	// ユーザ入力テキスト
        	String reqMessage = event.getMessage().getText();
        	
        	if (excludeMessages.contains(reqMessage.toLowerCase())) {
        		// 除外メッセージ
        		return null;
        	}
        	
        	// 西暦、和暦変換サービス実行
    		respMessage = service.execute(reqMessage);
        } catch(Throwable t) {
        	t.printStackTrace();
        } finally {
        	// レスポンステキスト作成
	        if (StringUtils.isEmpty(respMessage)) {
	        	respMessage = "分かりません。\n（使い方はヘルプと話しかけて下さい）";
	        } else {
	        	respMessage += "年";
	        }
        }
        return new TextMessage(respMessage);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
