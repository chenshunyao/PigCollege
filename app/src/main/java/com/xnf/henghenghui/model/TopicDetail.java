package com.xnf.henghenghui.model;


/**
 * @author HuangWenwei
 *
 * @date 2014年10月16日
 */
public class TopicDetail extends Entity {
	
	private Tweet tweet;

	public Tweet getTweet() {
		return tweet;
	}
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
}
